package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.training.CompetitionRunType
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.ResultSet
import java.time.DayOfWeek

object PlanInfoResultSet : ResultSetExtractor<PlanInfo> {
    override fun extractData(rs: ResultSet): PlanInfo? {
        return if (rs.next()) {
            val id = rs.getInt("id")
            val trainingsId = rs.getString("trainingsId")
            val userId = rs.getInt("userId")
            val competitionRunType = CompetitionRunType.valueOf(rs.getString("competitionRunType"))
            val competitionDate = rs.getDate("competitionDate").toLocalDate()
            val daysOfWeek = (rs.getArray("daysOfWeek").array as Array<String>).map { DayOfWeek.valueOf(it) }
            val longRunDistance = rs.getInt("longRunDistance")
            PlanInfo(id, trainingsId, userId, competitionRunType, competitionDate, daysOfWeek, longRunDistance)
        } else null
    }
}

object ExistsRowExtractor : ResultSetExtractor<Boolean> {
    override fun extractData(rs: ResultSet): Boolean {
        return rs.next()
    }
}