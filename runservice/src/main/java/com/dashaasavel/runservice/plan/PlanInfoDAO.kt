package com.dashaasavel.runservice.plan

import com.dashaasavel.grpcmessages.utils.CompetitionRunType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Date
import java.sql.Statement

class PlanInfoDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    companion object {
        const val tableName = "plan_info"
    }

    fun getPlanInfo(userId: Int, competitionRunType: CompetitionRunType): PlanInfo? {
        val query = "select * from $tableName where userId=? and competitionRunType=?"
        return jdbcTemplate.query(query, PlanInfoResultSet, userId, competitionRunType.name)
    }

    fun isPlanExists(userId: Int, competitionRunType: CompetitionRunType): Boolean {
        val query = "select 1 from $tableName where userId=? and competitionRunType=?"
        return jdbcTemplate.query(query, ExistsRowExtractor, userId, competitionRunType.name)
    }

    fun insertPlan(planInfo: PlanInfo): Int {
        val query =
            "insert into $tableName(trainingsId, userId, competitionRunType, competitionDate, daysOfWeek, longRunDistance) values(?, ?, ?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
            val ps = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, planInfo.trainingsId)
            ps.setInt(2, planInfo.userId)
            ps.setString(3, planInfo.competitionRunType.name)
            ps.setDate(4, Date.valueOf(planInfo.competitionDate))
            ps.setArray(5, it.createArrayOf("varchar", planInfo.daysOfWeek.toTypedArray()))
            ps.setInt(6, planInfo.longRunDistance)
            ps
        }, keyHolder)
        return keyHolder.keys?.get("id") as Int
    }

    /**
     * @return trainingsId
     */
    fun deletePlan(userId: Int, competitionRunType: CompetitionRunType): String? {
        val query = "delete from $tableName where userId=? and competitionRunType=?"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
            val ps = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ps.setInt(1, userId)
            ps.setString(2, competitionRunType.name)
            ps
        }, keyHolder)
        return keyHolder.keys?.get("trainingsId") as String?
    }

    fun deleteAllPlans(userId: Int): List<String> {
        val query = "delete from $tableName where userId=?"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
            val ps = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ps.setInt(1, userId)
            ps
        }, keyHolder)
        return keyHolder.keyList.map { it["trainingsId"] as String }
    }
}