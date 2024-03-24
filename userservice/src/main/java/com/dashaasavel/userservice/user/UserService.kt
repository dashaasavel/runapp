package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO

class UserService(
    private val userDAO: UserDAO,
    private val userToRolesDAO: UserToRolesDAO,
    private val rolesDAO: RolesDAO
) {
    fun addUser(userDTO: UserDTO) {
        userDAO.addUser(userDTO)
        val userId = userDAO.getUserByUsername(userDTO.username!!).id
        for (role in userDTO.roles!!) {
            val roleId = rolesDAO.getIdByRole(role) // TODO caching
            userToRolesDAO.addRoleToUser(userId, roleId)
        }
    }

    fun setConfirmed(userId: Int, confirmed: Boolean) {
        userDAO.setConfirmed(userId, confirmed)
    }

    fun getUser(id: Long): UserDTO {
        return userDAO.getUser(id)
    }

    fun getAllUsers(): List<UserDTO> = userDAO.getAllUsers()

    fun isUserExists(username: String) = userDAO.isUserExists(username)

    fun getUserByUsername(username: String): UserDTO {
        val userDTO = userDAO.getUserByUsername(username)
        userDTO.roles = userToRolesDAO.getUserRoles(userDTO.id).map { rolesDAO.getRoleById(it) }
        return userDTO
    }
}