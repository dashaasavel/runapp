package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.role.Roles

class User {
    var id: Int? = null
    var username: String? = null
    var password: String? = null
    var confirmed: Boolean? = null
    var roles: List<Roles>? = null
}