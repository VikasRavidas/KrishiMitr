package com.example.krishimitr.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val mobileNumber: String
)

