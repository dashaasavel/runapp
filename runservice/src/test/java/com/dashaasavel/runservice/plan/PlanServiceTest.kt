package com.dashaasavel.runservice.plan

import com.dashaasavel.runapplib.grpc.error.CommonError
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.runservice.plan.utils.MINIMUM_WEEKS
import com.dashaasavel.runservice.training.Trainings
import com.dashaasavel.runservice.training.TrainingsDAO
import com.dashaasavel.grpcmessages.utils.CompetitionRunType
import com.mongodb.assertions.Assertions.assertNotNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlanServiceTest {
    private val userId = 1

    private val planInfoDAO: PlanInfoDAO = mock()
    private val trainingsDAO: TrainingsDAO = mock()
    private val planService = PlanService(trainingsDAO, planInfoDAO, mock())

    @Test
    fun `when type is a half marathon or 10 km then createPlan method should throw an exception (unsupported now)`() {
        val marathonDate = LocalDate.now().plusWeeks(MINIMUM_WEEKS.toLong())
        val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)

        assertThrows<CreatingPlanException>(CreatingPlanError.COMPETITION_TYPE_NOT_SUPPORTED_NOW) {
            planService.createPlan(userId, CompetitionRunType.HALF_MARATHON, marathonDate, daysOfWeek, 6)
        }

        assertThrows<CreatingPlanException>(CreatingPlanError.COMPETITION_TYPE_NOT_SUPPORTED_NOW) {
            planService.createPlan(userId, CompetitionRunType.TEN_KILOMETERS, marathonDate, daysOfWeek, 6)
        }
    }

    @Test
    fun `when plan is already exists in db then createPlan method should throw an exception`() {
        whenever(planInfoDAO.isPlanExists(1, CompetitionRunType.MARATHON)) doReturn true

        val marathonDate = LocalDate.now().plusWeeks(MINIMUM_WEEKS.toLong())
        val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)

        assertThrows<CreatingPlanException>(CreatingPlanError.PLAN_ALREADY_EXISTS) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }
    }

    @Test
    fun `when plan with 3,4,5 trainings a week then createPlan method should not throw an exception`() {
        val marathonDate = LocalDate.now().plusWeeks(MINIMUM_WEEKS.toLong()).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val daysOfWeek = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY)

        assertDoesNotThrow {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }

        daysOfWeek += DayOfWeek.TUESDAY
        assertDoesNotThrow {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }

        daysOfWeek += DayOfWeek.FRIDAY
        assertDoesNotThrow {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }
    }


    @Test
    fun `when plan with 3 less or 5 more trainings a week then createPlan method should throw an exception (unsupported)`() {
        val marathonDate = LocalDate.now().plusWeeks(20)

        val daysOfWeek = mutableListOf(DayOfWeek.MONDAY)
        creationUnsupportedTimesAWeekShouldThrowAnException(marathonDate, daysOfWeek)


        daysOfWeek += DayOfWeek.TUESDAY
        creationUnsupportedTimesAWeekShouldThrowAnException(marathonDate, daysOfWeek)

        daysOfWeek += listOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        creationUnsupportedTimesAWeekShouldThrowAnException(marathonDate, daysOfWeek)

        daysOfWeek += DayOfWeek.SUNDAY
        creationUnsupportedTimesAWeekShouldThrowAnException(marathonDate, daysOfWeek)
    }

    private fun creationUnsupportedTimesAWeekShouldThrowAnException(marathonDate: LocalDate, daysOfWeek: List<DayOfWeek>) {
        assertThrows<CreatingPlanException>(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK) {
            planService.createPlan(userId, CompetitionRunType.MARATHON, marathonDate, daysOfWeek, 6)
        }
    }

    @Test
    fun `when plan not exists in repo then getPlan method should return null`() {
        val runType = CompetitionRunType.MARATHON
        whenever(planInfoDAO.getPlanInfo(userId, runType)) doReturn null
        assertNull(planService.getPlanFromRepo(userId, runType))
    }

    @Test
    fun `when plan exists in repo then getPlan method should return plan`() {
        val runType = CompetitionRunType.MARATHON
        val daysOfWeek = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        val planInfo = PlanInfo(null, "trainingsId", userId, runType, LocalDate.now(), daysOfWeek, 5)

        whenever(planInfoDAO.getPlanInfo(userId, runType)) doReturn planInfo
        whenever(trainingsDAO.findById(planInfo.trainingsId!!)) doReturn Trainings()

        assertNotNull(planService.getPlanFromRepo(userId, runType))
    }

    @Test
    fun `when plan does not exists in local cache then savePlan method throws an exception`() {
        assertThrows<IllegalStateException> { planService.savePlan(userId) }
    }
}

inline fun <reified T : StatusRuntimeException> assertThrows(error: CommonError, executable: () -> Unit) {
    val trailers = assertThrows<T> {
        executable.invoke()
    }.trailers!!
    assertEquals(error.getName(), trailers[GrpcMetadataUtils.ERROR_METADATA_KEY])
}