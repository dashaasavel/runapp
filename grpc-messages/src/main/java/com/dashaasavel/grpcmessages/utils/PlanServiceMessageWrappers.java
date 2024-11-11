package com.dashaasavel.grpcmessages.utils;

import com.dashaasavel.runservice.api.Runservice;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class PlanServiceMessageWrappers {
    public static Runservice.PlanIdentifier planIdentifier(int userId, CompetitionRunType type) {
        return Runservice.PlanIdentifier.newBuilder()
                .setUserId(userId)
                .setType(convertToGrpc(type))
                .build();
    }

    public static Runservice.PlanInfo planInfo(int userId, CompetitionRunType type, LocalDate competitionDate, List<DayOfWeek> trainingDays, int longRunDistance) {
        var convertedTrainingsDays = trainingDays.stream().map(DateUtils::convertToGrpcDayOfWeek).toList();
        return Runservice.PlanInfo.newBuilder()
                .setIdentifier(planIdentifier(userId, type))
                .setDate(DateUtils.convertToGrpcDate(competitionDate))
                .addAllDaysOfWeek(convertedTrainingsDays)
                .setLongRunDistance(longRunDistance)
                .build();
    }

    public static Runservice.Training training(int trainingNumber, int weekNumber, TrainingType type, int distance, LocalDate date) {
        var trainingBody = trainingBody(trainingNumber, weekNumber, type, date);
        return Runservice.Training.newBuilder().setBody(trainingBody).setDistance(distance).build();
    }

    public static Runservice.Training speedTraining(int trainingNumber, int weekNumber, TrainingType type, int distance, LocalDate date, int repetition, int distanceInMeters) {
        var trainingBody = trainingBody(trainingNumber, weekNumber, type, date);
        var speedRunningInfo = speedRunningInfo(repetition, distanceInMeters);
        return Runservice.Training.newBuilder().setBody(trainingBody).setDistance(distance).setSpeedRunningInfo(speedRunningInfo).build();
    }

    public static Runservice.Training gymTraining(int trainingNumber, int weekNumber, TrainingType type, LocalDate date) {
        var trainingBody = trainingBody(trainingNumber, weekNumber, type, date);
        return Runservice.Training.newBuilder().setBody(trainingBody).build();
    }

    private static Runservice.TrainingBody trainingBody(int trainingNumber, int weekNumber, TrainingType type, LocalDate date) {
        return Runservice.TrainingBody.newBuilder()
                .setTrainingNumber(trainingNumber)
                .setWeekNumber(weekNumber)
                .setType(convertToGrpc(type))
                .setDate(DateUtils.convertToGrpcDate(date))
                .build();
    }

    public static Runservice.SpeedRunningInfo speedRunningInfo(int repetition, int distanceInMeters) {
        return Runservice.SpeedRunningInfo.newBuilder().setRepetition(repetition).setDistanceInMeters(distanceInMeters).build();
    }

    private static Runservice.CompetitionRunType convertToGrpc(CompetitionRunType type) {
        return Runservice.CompetitionRunType.valueOf(type.name());
    }

    private static Runservice.TrainingType convertToGrpc(TrainingType type) {
        return Runservice.TrainingType.valueOf(type.name());
    }
}
