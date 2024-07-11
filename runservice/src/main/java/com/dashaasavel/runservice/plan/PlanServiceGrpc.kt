package com.dashaasavel.runservice.plan

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.runservice.api.Runservice
import com.dashaasavel.runservice.api.Runservice.CreatePlan.Response
import com.dashaasavel.runservice.utils.toGrpc
import com.dashaasavel.runservice.utils.toLocalEnum
import com.dashaasavel.userserviceapi.utils.DateUtils.convertToDayOfWeek
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import java.time.LocalDate

@GrpcService
class PlanServiceGrpc(
    private val planService: PlanService
) : com.dashaasavel.runservice.api.PlanServiceGrpc.PlanServiceImplBase() {
    override fun createPlan(
        request: Runservice.CreatePlan.Request,
        responseObserver: StreamObserver<Response>
    ) {
        val planInfo = request.planInfo
        val userId = planInfo.userId
        val type = planInfo.type.toLocalEnum()
        val date = planInfo.date
        val localDate = LocalDate.of(date.year, date.month, date.day)
        val timesAWeek = planInfo.daysOfWeekList.map { convertToDayOfWeek(it) }
        val longRunDistance = planInfo.longRunDistance

        responseObserver.reply {
            val trainings = planService.createPlan(userId, type, localDate, timesAWeek, longRunDistance)
            val grpcTrainings = trainings.map { it.toGrpc() }
            Response.newBuilder().addAllTrainings(grpcTrainings).build()
        }
    }

    override fun savePlan(
        request: Runservice.SavePlan.Request,
        responseObserver: StreamObserver<Empty>
    ) {
        val planIdentifier = request.planIdentifier
        val userId = planIdentifier.userId
        val type = planIdentifier.type.toLocalEnum()

        responseObserver.reply {
            planService.savePlan(userId, type)
            Empty.getDefaultInstance()
        }
    }

    override fun getPlan(
        request: Runservice.GetPlan.Request,
        responseObserver: StreamObserver<Runservice.GetPlan.Response>
    ) {
        val planIdentifier = request.planIdentifier
        val userId = planIdentifier.userId
        val type = planIdentifier.type.toLocalEnum()

        responseObserver.reply {
            val plan = planService.getPlanFromRepo(userId, type)
            val planInfo = plan.info.toGrpc()
            val trainings = plan.trainings!!.map { it.toGrpc() }
            val grpcPlan = Runservice.Plan.newBuilder().apply {
                this.planInfo = planInfo
                this.addAllTrainings(trainings)
            }
            Runservice.GetPlan.Response.newBuilder().setPlan(grpcPlan).build()
        }
    }

    override fun deletePlan(
        request: Runservice.DeletePlan.Request,
        responseObserver: StreamObserver<Empty>
    ) {
        val planIdentifier = request.planIdentifier
        val type = planIdentifier.type.toLocalEnum()
        val userId = planIdentifier.userId

        responseObserver.reply {
            planService.deletePlan(userId, type)
            Empty.getDefaultInstance()
        }
    }
}