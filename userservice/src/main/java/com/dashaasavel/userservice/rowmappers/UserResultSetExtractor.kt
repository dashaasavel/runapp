package com.dashaasavel.userservice.rowmappers

import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDTO
import com.dashaasavel.userservice.user.User
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet


object UserResultSetExtractor: ResultSetExtractor<User> {
    override fun extractData(rs: ResultSet): User? {
        return if (rs.next()) {
            val id = rs.getInt("id")
            val username = rs.getString("username")
            val password = rs.getString("password")
            val confirmed = rs.getBoolean("confirmed")
            User().apply {
                this.id = id
                this.username = username
                this.password = password
                this.confirmed = confirmed
            }
        } else null
    }
}

object ConfirmationTokenExtractor: ResultSetExtractor<ConfirmationTokenDTO> {
    override fun extractData(rs: ResultSet): ConfirmationTokenDTO {
        rs.next()
        return ConfirmationTokenDTO().apply {
            this.token = rs.getString("token")
            this.creationDate = rs.getTimestamp("creationDate").toLocalDateTime()
            this.confirmationDate = rs.getTimestamp("confirmationDate")?.toLocalDateTime()
            this.expirationDate = rs.getTimestamp("expirationDate").toLocalDateTime()
        }
    }
}

object UsersRowMapper: RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User {
        val username = rs.getString("username")
        val id = rs.getInt("id")
        val confirmed = rs.getBoolean("confirmed")
        return User().apply {
            this.id = id
            this.username = username
            this.confirmed = confirmed
        }
    }
}

object ExistsRowExtractor: ResultSetExtractor<Boolean> {
    override fun extractData(rs: ResultSet): Boolean {
        return rs.next()
    }
}

object IntIdResultSetExtractor: ResultSetExtractor<Int> {
    override fun extractData(rs: ResultSet): Int? {
        return if (rs.next()) {
            rs.getInt("id")
        } else null
    }
}