package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.UserAlreadyExistsException
import com.dashaasavel.userservice.rabbit.UserDeletionSender
import com.dashaasavel.userservice.rabbit.WelcomeMessageSender
import org.springframework.dao.DuplicateKeyException
import org.springframework.transaction.support.TransactionTemplate

class UserService(
    private val userDAO: UserDAO,
    private val transactionTemplate: TransactionTemplate,
    private val messageSender: UserDeletionSender,
    private val welcomeMessageSender: WelcomeMessageSender,
) {
    /**
     * @return userId
     */
    fun saveUser(user: User): Int {
        try {
            return userDAO.insertUser(user)
        } catch (e: DuplicateKeyException) {
            throw UserAlreadyExistsException()
        }
    }

    fun getUser(id: Int): User? {
        return userDAO.getUser(id)
    }

    fun getUser(username: String): User? {
        return userDAO.getUser(username)
    }

    fun deleteUser(userId: Int) {
        transactionTemplate.executeWithoutResult {
            val user = userDAO.deleteUser(userId) ?: return@executeWithoutResult
            messageSender.sendUserDeletion(user.firstName!!, user.username!!, userId) // удалить это из транзакции
        }
    }

    fun deleteUser(username: String) {
        userDAO.getUser(username)?.id?.let {
            deleteUser(it)
        }
    }
}