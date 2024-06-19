package com.dashaasavel.userservice.role

import com.dashaasavel.userservice.rowmappers.IntListRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class UserToRolesDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val tableName = "users_to_roles"

    fun addRoleToUser(userId: Int, roleId: Int) {
        val sql = "insert into $tableName (userId, roleId) values(?,?)"
        jdbcTemplate.update(sql, userId, roleId)
    }

    fun getUserRoles(userId: Int): List<Int> {
        val sql = "select roleId as id from $tableName where userId=?"
        return jdbcTemplate.query(sql, IntListRowMapper, userId)
    }
}