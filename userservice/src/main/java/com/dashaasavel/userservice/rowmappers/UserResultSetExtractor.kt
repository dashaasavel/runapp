package com.dashaasavel.userservice.rowmappers

import com.dashaasavel.userservice.user.User
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.ResultSet


object UserResultSetExtractor : ResultSetExtractor<User> {
    override fun extractData(rs: ResultSet): User? {
        return if (rs.next()) {
            val id = rs.getInt("id")
            val firstName = rs.getString("firstName")
            val username = rs.getString("username")
            val password = rs.getString("password")
            User().apply {
                this.id = id
                this.firstName = firstName
                this.username = username
                this.password = password
            }
        } else null
    }
}

object ExistsRowExtractor : ResultSetExtractor<Boolean> {
    override fun extractData(rs: ResultSet): Boolean {
        return rs.next()
    }
}

object IntIdResultSetExtractor : ResultSetExtractor<Int> {
    override fun extractData(rs: ResultSet): Int? {
        return if (rs.next()) {
            rs.getInt("id")
        } else null
    }
}