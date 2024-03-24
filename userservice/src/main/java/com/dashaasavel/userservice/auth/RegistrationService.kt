package com.dashaasavel.userservice.auth

import com.dashaasavel.userservice.ProfilesHelper
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailSender
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.UserDTO
import com.dashaasavel.userservice.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

open class RegistrationService(
    private val userService: UserService,
    private val mailService: MailSender,
    private val profilesHelper: ProfilesHelper,
    private val confirmationTokenService: ConfirmationTokenService,
    private val encoder: PasswordEncoder
) {
    @Transactional
    open fun registerAccount(username: String, password: String, roles: List<Roles>) {
        if (userService.isUserExists(username)) {
            // если аккаунт подтвержден -- тогда ниче не делаем
            val user = userService.getUserByUsername(username)
            if (user.confirmed!!) {
                return
            }
            val token = confirmationTokenService.getLastConfirmationToken(user.id)
            if (token.expirationDate!!.isAfter(LocalDateTime.now())) {
                throw IllegalStateException("Токен не истек")
            }
            sendTokenToAlreadyExistedUser(username, user.id)
        } else {
            createUserAndSendToken(username, password, roles)
        }
    }

    @Transactional
    open fun confirmAccount(token: String) {
        val currentTime = LocalDateTime.now()
        val userId = confirmationTokenService.checkTokenAndGetUserId(token)
        userService.setConfirmed(userId, true)
        confirmationTokenService.confirmToken(token, currentTime)
    }

    private fun sendTokenToAlreadyExistedUser(username: String, userId: Int) {
        val token = confirmationTokenService.createAndSaveConfirmationToken(userId)
        mailService.sendToConfirm(username, token)
    }

    private fun createUserAndSendToken(username: String, password: String, roles: List<Roles>) {
        val encodedPassword = encoder.encode(password)
        val user = UserDTO().apply {
            this.username = username
            this.password = encodedPassword
            this.confirmed = false
            this.roles = roles
        }
        if (!profilesHelper.isMailConfirmationEnabled()) {
            user.confirmed = true
        }

        userService.addUser(user)

        if (profilesHelper.isMailConfirmationEnabled()) {
            val userId = userService.getUserByUsername(username).id
            val token = confirmationTokenService.createAndSaveConfirmationToken(userId)
            mailService.sendToConfirm(username, token)
        }
    }
}