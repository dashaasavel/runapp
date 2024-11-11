package com.dashaasavel.grpcmessages.utils;

public enum CompetitionRunType {
    MARATHON(42), HALF_MARATHON(21), TEN_KILOMETERS(10);
    public final int distance;

    CompetitionRunType(int distance) {
        this.distance = distance;
    }
}
