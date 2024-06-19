package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.plan.training.CompetitionRunType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Date
import java.sql.ResultSet
import java.sql.Statement
import java.time.DayOfWeek

class PlanInfoDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    companion object {
        const val tableName = "plan_info"
    }

    fun getPlanId(userId: Int, competitionRunType: CompetitionRunType): Int {
        val query = "select planId from $tableName where userId=? and competition_run_type=?"
        return jdbcTemplate.query(query, IntPlanIdResultSetExtractor, userId, competitionRunType.name)
    }

    // OK
    fun getPlanInfo(trainingListId: String): PlanInfo? {
        val query = "select * from $tableName where trainingListId=?"
        return jdbcTemplate.query(query, PlanInfoResultSet, trainingListId)
//        return jdbcTemplate.queryForObject(query, PlanInfoRowMapper, trainingListId)
    }

    fun getPlanInfo(userId: Int, competitionRunType: CompetitionRunType): PlanInfo? {
        val query = "select * from $tableName where userId=? and competitionRunType=?"
        return jdbcTemplate.query(query, PlanInfoResultSet, userId, competitionRunType)
//        return jdbcTemplate.queryForObject(query, PlanInfoRowMapper, trainingListId)
    }

    fun getAllPlanIdsByUserId(userId: Int): List<Int> {
        val query = "select planId from $tableName where userId=?"
        return jdbcTemplate.query(query, IntListRowMapper, userId)
    }

    fun isPlanExists(userId: Int, competitionRunType: CompetitionRunType): Boolean {
        val query = "select 1 from $tableName where userId=? and competition_run_type=?"
        return jdbcTemplate.query(query, ExistsRowExtractor, userId, competitionRunType.name)
    }

    // OK
    fun insertPlan(planInfo: PlanInfo): Int {
        val query =
            "insert into $tableName(trainingListId, userId, competitionRunType, competitionDate, daysOfWeek, longRunDistance) values(?, ?, ?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
            val ps = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, planInfo.trainingListId)
            ps.setInt(2, planInfo.userId)
            ps.setString(3, planInfo.competitionRunType.name)
            ps.setDate(4, Date.valueOf(planInfo.competitionDate))
            ps.setArray(5, it.createArrayOf("varchar", planInfo.daysOfWeek.toTypedArray()))
            ps.setInt(6, planInfo.longRunDistance)
            ps
        }, keyHolder)
        return keyHolder.keys?.get("id") as Int
    }

    fun deletePlan(planId: String) {
        val query = "delete * from $tableName where planId=?"
        jdbcTemplate.update(query, planId)
    }

    fun deletePlan(userId: Int, competitionRunType: CompetitionRunType) {
        val query = "delete * from $tableName where userId=? and competitionRunType=?"
        jdbcTemplate.update(query, userId, competitionRunType)
    }
}

object ExistsRowExtractor : ResultSetExtractor<Boolean> {
    override fun extractData(rs: ResultSet): Boolean {
        return rs.next()
    }
}

object IntListRowMapper : RowMapper<Int> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Int {
        return rs.getInt("planId")
    }
}

object IntPlanIdResultSetExtractor : ResultSetExtractor<Int> {
    override fun extractData(rs: ResultSet): Int {
        rs.next()
        return rs.getInt("planId")
    }
}

//object PlanInfoRowMapper : RowMapper<PlanInfo> {
//    override fun mapRow(rs: ResultSet, rowNum: Int): PlanInfo? {
//        if (rs.next()) {
//            val id = rs.getInt("id")
//            val trainingListId = rs.getString("trainingListId")
//            val userId = rs.getInt("userId")
//            val competitionRunType = CompetitionRunType.valueOf(rs.getString("competitionRunType"))
//            val competitionDate = rs.getDate("competitionDate").toLocalDate()
//            val daysOfWeek = (rs.getArray("daysOfWeek").array as Array<String>).map { DayOfWeek.valueOf(it) }
//            val longRunDistance = rs.getInt("longRunDistance")
//            return PlanInfo(id, trainingListId, userId, competitionRunType, competitionDate, daysOfWeek, longRunDistance)
//        }
//        return null
//    }
//}

object PlanInfoResultSet: ResultSetExtractor<PlanInfo> {
    override fun extractData(rs: ResultSet): PlanInfo? {
        return if (rs.next()) {
            val id = rs.getInt("id")
            val trainingListId = rs.getString("trainingListId")
            val userId = rs.getInt("userId")
            val competitionRunType = CompetitionRunType.valueOf(rs.getString("competitionRunType"))
            val competitionDate = rs.getDate("competitionDate").toLocalDate()
            val daysOfWeek = (rs.getArray("daysOfWeek").array as Array<String>).map { DayOfWeek.valueOf(it) }
            val longRunDistance = rs.getInt("longRunDistance")
            PlanInfo(id, trainingListId, userId, competitionRunType, competitionDate, daysOfWeek, longRunDistance)
        } else null
    }
}