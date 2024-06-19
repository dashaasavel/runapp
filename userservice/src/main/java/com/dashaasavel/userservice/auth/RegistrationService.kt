package com.dashaasavel.userservice.auth

import com.dashaasavel.userservice.ProfilesHelper
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailSender
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.User
import com.dashaasavel.userservice.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import com.dashaasavel.userservice.api.Userservice.UserRegistrationResponseError

open class RegistrationService(
    private val userService: UserService,
    private val mailService: MailSender,
    private val profilesHelper: ProfilesHelper,
    private val confirmationTokenService: ConfirmationTokenService,
    private val encoder: PasswordEncoder
) {
    /**
     * userId or null, if
     */
    @Transactional
    open fun registerUser(username: String, password: String, roles: List<Roles>) {
        if (userService.isUserExists(username)) {
            val user = userService.getUserByUsername(username)!!
            if (user.confirmed!!) {
                throw UserRegistrationException(UserRegistrationResponseError.USER_EXISTS_AND_CONFIRMED.name)
            }
            val token = confirmationTokenService.getLastConfirmationToken(user.id!!)
            if (token.expirationDate!!.isAfter(LocalDateTime.now())) {
                throw UserRegistrationException(UserRegistrationResponseError.NEED_TO_CONFIRM_ACCOUNT.name)
            } else {
                createAndSendToken(username, user.id!!)
                throw UserRegistrationException(UserRegistrationResponseError.NEW_TOKEN_WAS_SENT.name)
            }

        }
        createUserAndSendToken(username, password, roles)
    }

    @Transactional
    open fun confirmAccount(token: String) {
        val currentTime = LocalDateTime.now()
        val userId = confirmationTokenService.checkTokenAndGetUserId(token)
        userService.updateConfirmed(userId, true)
        confirmationTokenService.confirmToken(token, currentTime)
    }

    private fun createUserAndSendToken(username: String, password: String, roles: List<Roles>): Int? {
        val encodedPassword = encoder.encode(password)
        val user = User().apply {
            this.username = username
            this.password = encodedPassword
            this.roles = roles
        }
        user.confirmed = !profilesHelper.isMailConfirmationEnabled()

        val userId = userService.addUser(user)

        if (profilesHelper.isMailConfirmationEnabled()) {
            createAndSendToken(username, userId!!)
        }
        return userId
    }

    private fun createAndSendToken(username: String, userId: Int) {
        val token = confirmationTokenService.createAndSaveConfirmationToken(userId)
        mailService.sendToConfirm(username, token)
    }
}