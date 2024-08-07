package com.dashaasavel.userservice.user

import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.userservice.auth.UserRegistrationException
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDAO
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDTO
import com.dashaasavel.userservice.rabbit.UserDeletionSender
import com.dashaasavel.userservice.rabbit.WelcomeMessageSender
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

class UserService(
    private val userDAO: UserDAO,
    private val userToRolesDAO: UserToRolesDAO,
    private val rolesDAO: RolesDAO,
    private val confirmationTokenDAO: ConfirmationTokenDAO,
    private val transactionTemplate: TransactionTemplate,
    private val messageSender: UserDeletionSender,
    private val welcomeMessageSender: WelcomeMessageSender,
) {
    /**
     * @return userId
     */
    fun saveUser(user: User): Int {
        return transactionTemplate.execute {
            val userId = userDAO.insertUser(user)
            for (role in user.roles!!) {
                val roleId = rolesDAO.getIdByRole(role) ?: error("Role ${role.name} was not found in db")
                userToRolesDAO.addRoleToUser(userId, roleId)
            }
            userId
        }!!
    }

    fun getUser(id: Int): User? {
        val user = userDAO.getUser(id) ?: return null
        val roles = getUserRoles(user.id!!)
        return user.apply { this.roles = roles }
    }

    fun isUserExists(username: String) = userDAO.isUserExists(username)

    fun getUser(username: String): User? {
        val userDTO = userDAO.getUser(username) ?: return null
        userDTO.roles = getUserRoles(userDTO.id!!)
        return userDTO
    }

    private fun getUserRoles(userId: Int): List<Roles> {
        return userToRolesDAO.getUserRoles(userId).map { rolesDAO.getRoleById(it) }
    }

    fun deleteUser(userId: Int) {
        transactionTemplate.executeWithoutResult {
            userToRolesDAO.deleteUserRoles(userId)
            confirmationTokenDAO.deleteUserTokens(userId)
            val user = userDAO.deleteUser(userId) ?: return@executeWithoutResult
            messageSender.sendUserDeletion(user.firstName!!, user.username!!, userId) // удалить это из транзакции
        }
    }

    fun deleteUser(username: String) {
        userDAO.getUser(username)?.id?.let {
            deleteUser(it)
        }
    }

    fun confirmUser(token: String) {
        val currentTime = LocalDateTime.now()
        val daoToken = checkAndGetToken(token)
        if (daoToken.confirmationDate != null) return
        transactionTemplate.executeWithoutResult {
            val user = userDAO.updateConfirmed(daoToken.userId!!, true)!!
            confirmToken(token, currentTime)
            welcomeMessageSender.sendWelcomeMessage(user.firstName!!, user.username!!) // убрать отсюда!
        }
    }

    private fun checkAndGetToken(token: String): ConfirmationTokenDTO {
        val userId = confirmationTokenDAO.getUserIdByToken(token) ?: throw UserRegistrationException(
            UserRegistrationError.TOKEN_NOT_FOUND
        )
        val lastConfirmationToken = confirmationTokenDAO.getLastConfirmationTokenByUserId(userId)!!
        val lastToken = lastConfirmationToken.token
        if (lastToken != token) {
            throw UserRegistrationException(UserRegistrationError.NEED_TO_CONFIRM_THE_LATEST_TOKEN)
        }
        return lastConfirmationToken
    }

    private fun confirmToken(token: String, confirmedTime: LocalDateTime) {
        confirmationTokenDAO.setConfirmed(token, confirmedTime)
    }
}