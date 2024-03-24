package com.dashaasavel.userservice.role

import com.dashaasavel.userservice.rowmappers.IntIdResultSetExtractor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor

class RolesDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val tableName = "user_roles"

    fun getIdByRole(role: Roles): Int {
        val sql = "select id from $tableName where role=?"
        return jdbcTemplate.query(sql, IntIdResultSetExtractor, role.name)!!
    }

    fun getRoleById(roleId: Int): Roles {
        val sql = "select role from $tableName where id=?"
        val s = jdbcTemplate.query(sql,
            ResultSetExtractor {
                it.next()
                it.getString("role")
            }, roleId
        )?: error("role with id $roleId not found")

        return Roles.valueOf(s)
    }
}