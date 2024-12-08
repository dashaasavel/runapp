package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.rowmappers.UserResultSetExtractor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class UserDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val tableName = "users"

    fun getUser(userId: Int): User? {
        val sql = "select id,firstName,username,password from $tableName where id=?"
        return jdbcTemplate.query(sql, UserResultSetExtractor, userId)
    }

    fun getUser(username: String): User? {
        val sql = "select id,firstName,username,password from $tableName where username=?"
        return jdbcTemplate.query(sql, UserResultSetExtractor, username)
    }

    fun deleteUser(userId: Int): User? {
        val sql = "delete from $tableName where id=? returning *"
        return jdbcTemplate.query(sql, UserResultSetExtractor, userId)
    }

    fun insertUser(dto: User): Int {
        val query = "insert into $tableName(firstName, username, password) values(?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
            val ps = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, dto.firstName)
            ps.setString(2, dto.username)
            ps.setString(3, dto.password)
            ps
        }, keyHolder)

        return keyHolder.keys?.get("id") as Int
    }
}