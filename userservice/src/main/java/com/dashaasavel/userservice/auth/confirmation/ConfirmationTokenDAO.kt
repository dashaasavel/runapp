package com.dashaasavel.userservice.auth.confirmation

import com.dashaasavel.userservice.rowmappers.ConfirmationTokenExtractor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.Timestamp
import java.time.LocalDateTime

class ConfirmationTokenDAO(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val tableName = "conf_tokens"

    fun addToken(dto: ConfirmationTokenDTO): Int {
        val sql = "insert into $tableName(userId, token, creationDate, expirationDate) values(?,?,?,?)"
        return jdbcTemplate.update(sql, dto.userId, dto.token, dto.creationDate, dto.expirationDate)
    }

    fun getLastConfirmationTokenByUserId(userId: Int): ConfirmationTokenDTO {
        val sql = "select token,creationDate,confirmationDate,expirationDate from $tableName where userId=? order by id desc limit 1"
        return jdbcTemplate.query(sql, ConfirmationTokenExtractor, userId)!!
    }

    fun getUserIdByToken(token: String): Int {
        val sql = "select userId from $tableName where token=?"
        return jdbcTemplate.query(sql, ResultSetExtractor {
            it.next()
            it.getInt("userId")
        }, token)!!
    }

    fun setConfirmed(token: String, confirmationDateTime: LocalDateTime) {
        val sql = "update $tableName set confirmationDate=? where token=?"
        jdbcTemplate.update(sql, Timestamp.valueOf(confirmationDateTime), token)
    }
}