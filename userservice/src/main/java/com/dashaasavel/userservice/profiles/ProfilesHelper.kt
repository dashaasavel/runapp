package com.dashaasavel.userservice.profiles

import com.dashaasavel.runapplib.logger
import jakarta.annotation.PostConstruct
import org.springframework.core.env.Environment

class ProfilesHelper(
    env: Environment
) {
    private val logger = logger()

    private val activeProfiles = env.activeProfiles.asList()

    fun isMailConfirmationEnabled(): Boolean = activeProfiles.contains(Profiles.MAIL.name)

    @PostConstruct
    fun postConstruct() {
        logger.info("Active profiles: {}", activeProfiles)
    }
}