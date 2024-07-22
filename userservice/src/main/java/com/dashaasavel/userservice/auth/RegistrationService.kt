package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.userservice.profiles.ProfilesHelper
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailSender
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.User
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
    /**
     * userId or null, if
     */
    @Transactional
    open fun registerUser(username: String, password: String, roles: List<Roles>): Int {
        if (userService.isUserExists(username)) {
            val user = userService.getUser(username)!!
            if (user.confirmed!!) {
                throw UserRegistrationException(
                    UserRegistrationError.USER_EXISTS_AND_CONFIRMED
                )
            }
            val token = confirmationTokenService.getLastConfirmationToken(user.id!!)
            if (token.expirationDate!!.isAfter(LocalDateTime.now())) {
                throw UserRegistrationException(
                    UserRegistrationError.NEED_TO_CONFIRM_ACCOUNT
                )
            } else {
                createAndSendToken(username, user.id!!)
                throw UserRegistrationException(
                    UserRegistrationError.NEW_TOKEN_WAS_SENT
                )
            }

        }
        return createUserAndSendToken(username, password, roles)
    }

    @Transactional
    open fun confirmAccount(token: String) {
        val currentTime = LocalDateTime.now()
        val userId = confirmationTokenService.checkTokenAndGetUserId(token)
        userService.updateConfirmed(userId, true)
        confirmationTokenService.confirmToken(token, currentTime)
    }

    private fun createUserAndSendToken(username: String, password: String, roles: List<Roles>): Int {
        val encodedPassword = encoder.encode(password)
        val user = User().apply {
            this.username = username
            this.password = encodedPassword
            this.roles = roles
        }
        user.confirmed = !profilesHelper.isMailConfirmationEnabled()

        val userId = userService.saveUser(user)

        if (profilesHelper.isMailConfirmationEnabled()) {
            createAndSendToken(username, userId)
        }
        return userId
    }

    private fun createAndSendToken(username: String, userId: Int) {
        val token = confirmationTokenService.createAndSaveConfirmationToken(userId)
        mailService.sendToConfirm(username, token)
    }
}