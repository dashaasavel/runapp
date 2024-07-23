package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO

class UserService(
    private val userDAO: UserDAO,
    private val userToRolesDAO: UserToRolesDAO,
    private val rolesDAO: RolesDAO
) {
    /**
     * @return userId or null
     */
    fun saveUser(user: User): Int {
        val userId = userDAO.insertUser(user)
        for (role in user.roles!!) {
            val roleId = rolesDAO.getIdByRole(role) ?: error("Role ${role.name} was not found in db")
            userToRolesDAO.addRoleToUser(userId, roleId)
        }
        return userId
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

    fun deleteUserById(userId: Int) {
        userToRolesDAO.deleteUserRoles(userId)
        userDAO.deleteUser(userId)
    }

    fun deleteUserByUsername(username: String) {
        getUser(username)?.id?.let {
            userToRolesDAO.deleteUserRoles(it)
            userDAO.deleteUser(it)
        }
    }
}