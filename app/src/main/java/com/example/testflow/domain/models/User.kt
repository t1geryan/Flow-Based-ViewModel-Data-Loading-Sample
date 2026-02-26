package com.example.testflow.domain.models

typealias UserId = Int

data class User(
    val id: UserId,
    val name: String,
)

data class UsersList(
    val users: List<User>,
) : List<User> by users {

    constructor(vararg users: User) : this(users.toList())
}
