package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.rowmappers.ExistsRowExtractor
import org.springframework.jdbc.core.JdbcTemplate
import com.dashaasavel.userservice.rowmappers.UserResultSetExtractor
import com.dashaasavel.userservice.rowmappers.UsersRowMapper
import java.sql.PreparedStatement

class UserDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val tableName = "users"

    fun getUser(userId: Long): UserDTO {
        val sql = "select id,username,password,confirmed from $tableName where id=?"
        return jdbcTemplate.query(sql, UserResultSetExtractor, userId)!!
    }

    fun getUserByUsername(username: String): UserDTO {
        val sql = "select id,username,password,confirmed from $tableName where username=?"
        PreparedStatement.RETURN_GENERATED_KEYS
        return jdbcTemplate.query(sql, UserResultSetExtractor, username)!!
    }

    fun addUser(dto: UserDTO): Int {
        val sql = "insert into $tableName(username,password,confirmed) values(?,?, ?)"
        return jdbcTemplate.update(sql, dto.username, dto.password, dto.confirmed)
    }

    fun getAllUsers(): List<UserDTO> {
        val sql = "select id,username,confirmed from $tableName"
        return jdbcTemplate.query(sql, UsersRowMapper)
    }

    fun isUserExists(username: String): Boolean {
        val sql = "select 1 from $tableName where username=?"
        return jdbcTemplate.query(sql, ExistsRowExtractor, username)!!
    }

    fun setConfirmed(userId: Int, confirmed: Boolean) {
        val sql = "update $tableName set confirmed=? where id=?"
        jdbcTemplate.update(sql, confirmed, userId)
    }
}