package com.example.krishimitr.room

import android.util.Log

class UserRepository(private val userDao: UserDao) {

    suspend fun getUser(userId: String?): User? {
        return userDao.getUserById(userId).also {
            Log.d("UserRepository", "Fetched user: $it")
        }
    }

    suspend fun insertUser(user: User) {
        userDao.insert(user)
        Log.d("UserRepository", "Inserted user: $user")
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
        Log.d("UserRepository", "Updated user: $user")
    }

    // Prepopulate user if they don't exist
    suspend fun prepopulateUser(userId: String, name: String, email: String, mobileNumber: String) {
        val existingUser = getUser(userId)
        if (existingUser == null) {
            val newUser = User(userId, name, email, mobileNumber)
            insertUser(newUser)
            Log.d("UserRepository", "Prepopulated new user: $newUser")
        } else {
            Log.d("UserRepository", "User already exists: $existingUser")
        }
    }
}
