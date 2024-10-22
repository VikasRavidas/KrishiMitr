package com.example.krishimitr.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// Example ViewModel or Activity/Fragment




class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _updateStatus = MutableLiveData<Result<Boolean>>()
    val updateStatus: LiveData<Result<Boolean>> get() = _updateStatus

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                Log.d("UserViewModel", "Fetching user with ID: $userId")
                val userFromDb = userRepository.getUser(userId)
                if (userFromDb != null) {
                    Log.d("UserViewModel", "User fetched from DB: $userFromDb")
                    _user.value = userFromDb
                } else {
                    Log.d("UserViewModel", "User not found in DB, attempting to fetch from Firebase.")
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val newUserName = firebaseUser.displayName ?: "No Name"
                        val newUserEmail = firebaseUser.email ?: "No Email"
                        val newMobileNumber = "Set number" // Set a default or blank value

                        // Prepopulate user in the database
                        userRepository.prepopulateUser(userId, newUserName, newUserEmail, newMobileNumber)

                        // Fetch the newly inserted user
                        _user.value = userRepository.getUser(userId)
                        Log.d("UserViewModel", "User prepopulated and fetched: ${_user.value}")
                    } else {
                        Log.d("UserViewModel", "No authenticated user found.")
                    }
                }
            } else {
                Log.d("UserViewModel", "No authenticated user ID found.")
            }
        }
    }


    fun updateUserDetails(newName: String, newMobileNumber: String) {
        viewModelScope.launch {
            try {
                val userId = firebaseAuth.currentUser?.uid
                if (userId != null) {
                    val user = userRepository.getUser(userId)
                    if (user != null) {
                        val updatedUser = user.copy(name = newName, mobileNumber = newMobileNumber)
                        userRepository.updateUser(updatedUser)
                        _updateStatus.value = Result.success(true)
                        Log.d("UserViewModel", "User updated successfully: $updatedUser")
                    } else {
                        Log.d("UserViewModel", "User not found when attempting to update.")
                    }
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error updating user: ${e.message}")
                _updateStatus.value = Result.failure(e)
            }
        }
    }
}