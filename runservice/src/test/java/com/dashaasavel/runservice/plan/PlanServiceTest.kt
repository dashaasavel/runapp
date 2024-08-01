package com.dashaasavel.runservice.plan

import com.dashaasavel.runapplib.grpc.error.CommonError
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.test.assertEquals

class PlanServiceTest {
    private val userId = 1
    private val planInfoDAO: PlanInfoDAO = mock()
    private val planService = PlanService(mock(), planInfoDAO, mock())

    @Test
    fun `types half marathon and 10 km throws exception (unsupported now)`() {
        val marathonDate = LocalDate.now().plusWeeks(20)
        val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)

        assertThrows<CreatingPlanException>(CreatingPlanError.COMPETITION_TYPE_NOT_SUPPORTED_NOW) {
            planService.createPlan(userId, CompetitionRunType.HALF_MARATHON, marathonDate, daysOfWeek, 6)
        }

        assertThrows<CreatingPlanException>(CreatingPlanError.COMPETITION_TYPE_NOT_SUPPORTED_NOW) {
            planService.createPlan(userId, CompetitionRunType.TEN_KILOMETERS, marathonDate, daysOfWeek, 6)
        }
    }

    @Test
    fun `create plan when plan already exists (throw exception)`() {
        whenever(planInfoDAO.isPlanExists(1, CompetitionRunType.MARATHON)) doReturn true

        val marathonDate = LocalDate.now().plusWeeks(20)
        val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)

        assertThrows<CreatingPlanException>(CreatingPlanError.PLAN_ALREADY_EXISTS) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }
    }

    @Test
    fun `create plan with training times a week less 3 or more than 5 is unsupported`() {
        val marathonDate = LocalDate.now().plusWeeks(20)

        val daysOfWeek = mutableListOf(DayOfWeek.MONDAY)
        assertThrows<CreatingPlanException>(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }

        daysOfWeek += DayOfWeek.TUESDAY
        assertThrows<CreatingPlanException>(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }

        daysOfWeek += listOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        assertThrows<CreatingPlanException>(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }

        daysOfWeek += DayOfWeek.SUNDAY
        assertThrows<CreatingPlanException>(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }
    }
}

inline fun <reified T : StatusRuntimeException> assertThrows(error: CommonError, executable: () -> Unit) {
    val trailers = assertThrows<T> {
        executable.invoke()
    }.trailers!!
    assertEquals(error.getName(), trailers[GrpcMetadataUtils.ERROR_METADATA_KEY])
}