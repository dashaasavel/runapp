package com.dashaasavel.userservice

import jakarta.annotation.PostConstruct
import org.springframework.core.env.Environment

class ProfilesHelper(
    env: Environment
) {
    private val activeProfiles = env.activeProfiles.asList()

    fun isMailConfirmationEnabled(): Boolean = activeProfiles.contains(Profiles.MAIL.name)

    @PostConstruct
    fun postConstruct() {
        println("ACTIVE PROFILES: $activeProfiles")
    }
}