package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.role.Roles

class UserDTO {
    var id: Int = 0
    var username: String? = null
    var password: String? = null
    var confirmed: Boolean? = null
    var roles: List<Roles>? = null
}