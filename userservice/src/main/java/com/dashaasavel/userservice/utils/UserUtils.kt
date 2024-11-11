package com.dashaasavel.userservice.utils

import com.dashaasavel.userservice.api.Userservice

fun com.dashaasavel.userservice.user.User.toGrpcUser(): Userservice.User = Userservice.User.newBuilder().also {
    it.id = this.id!!
    it.username = this.username
}.build()