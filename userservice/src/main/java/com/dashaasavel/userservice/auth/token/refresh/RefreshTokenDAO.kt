package com.dashaasavel.userservice.auth.token.refresh

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.ResultSet

class RefreshTokenDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val tableName = "refresh_tokens"

    fun saveToken(refreshToken: RefreshToken) {
        val sql = "insert into $tableName(userId, username, token, expDate) values(?,?,?,?)"
        jdbcTemplate.update(sql, refreshToken.userId, refreshToken.username, refreshToken.token, refreshToken.expDate)
    }

    fun findByToken(token: String): RefreshToken? {
        val sql = "select (id, userId, username, token, expDate) from $tableName where token=?"
        return jdbcTemplate.query(sql, RefreshTokenResultSetExtractor, token)
    }

    fun deleteByToken(token: String) {
        val sql = "delete from $tableName where token=?"
        jdbcTemplate.update(sql, token)
    }

    fun deleteByUserId(userId: Int) {
        val sql = "delete from $tableName where userId=?"
        jdbcTemplate.update(sql, userId)
    }
}

object RefreshTokenResultSetExtractor : ResultSetExtractor<RefreshToken> {
    override fun extractData(rs: ResultSet): RefreshToken? {
        return if (rs.next()) {
            return RefreshToken().apply {
                this.id = rs.getInt("id")
                this.userId = rs.getInt("userId")
                this.username = rs.getString("username")
                this.token = rs.getString("token")
                this.expDate = rs.getTimestamp("expDate")
            }
        } else null
    }
}