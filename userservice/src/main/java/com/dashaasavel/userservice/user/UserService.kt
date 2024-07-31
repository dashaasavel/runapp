package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.rabbit.UserDeletionNotificator
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO
import org.springframework.transaction.support.TransactionTemplate

class UserService(
    private val userDAO: UserDAO,
    private val userToRolesDAO: UserToRolesDAO,
    private val rolesDAO: RolesDAO,
    private val notificator: UserDeletionNotificator,
    private val transactionTemplate: TransactionTemplate
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

    fun updateConfirmed(userId: Int, confirmed: Boolean) {
        userDAO.updateConfirmed(userId, confirmed)
    }

    fun getUser(id: Int): User? {
        val user = userDAO.getUser(id) ?: return null
        val roles = getUserRoles(user.id!!)
        return user.apply { this.roles = roles }
    }

    fun isUserExists(username: String) = userDAO.isUserExists(username)

    fun isUserExists(userId: Int) = userDAO.isUserExists(userId)

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
            userDAO.deleteUser(userId)
            notificator.notify(userId)
        }
    }

    fun deleteUser(username: String) {
        getUser(username)?.id?.let {
            deleteUser(it)
        }
    }
}