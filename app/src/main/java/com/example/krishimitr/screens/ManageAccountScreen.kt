package com.example.krishimitr.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.krishimitr.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import coil.size.Size
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAccountScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val firestore = FirebaseFirestore.getInstance()

    // State variables to store user details
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var isGoogleLogin by remember { mutableStateOf(false) }

    // Fetch user details on launch
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        currentUser?.let {
            email = it.email ?: "No Email"
            nickname = it.displayName ?: "No Name"
            isGoogleLogin = it.providerData.any { provider -> provider.providerId == "google.com" }

            // If needed, fetch additional details from Firestore
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nickname = document.getString("username") ?: nickname
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching user data", exception)
                }
        }
    }


        // Main layout container with grey background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0)) // Light grey background color // Adds padding to content to avoid overlapping the BottomBar
                .padding(16.dp)
        ) {
            // User details section in a separate box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small) // White background for box
                    .border(BorderStroke(1.dp, Color.LightGray), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp) // Padding inside the box
            ) {
                Column {
                    Text(
                        text = "Your details",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black// Replace with your desired color
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("Nickname") },
//                        enabled = !isGoogleLogin, // Disable if logged in with Google
                        textStyle = TextStyle(color= Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
//                        enabled = false, // Email should be non-editable in general
                        textStyle = TextStyle(color= Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (!isGoogleLogin) {

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
//                        enabled = !isGoogleLogin // Disable button if Google login
                    ) {
                        Text("Save details")
                    }
                }
            }

            // Change password section in a separate box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small) // White background for box
                    .border(BorderStroke(1.dp, Color.LightGray), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp) // Padding inside the box
            ) {
                Column {
                    Text(
                        text = "Change password",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black// Replace with your desired color
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New password") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Toggle Password Visibility"
                            )
                        },
                        textStyle = TextStyle(color= Color.Black),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = { Text("Repeat new password") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Toggle Password Visibility"
                            )
                        },
                        textStyle = TextStyle(color= Color.Black),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Logic to update password using Firebase Auth
                            val currentUser = auth.currentUser
                            if (newPassword == repeatPassword && newPassword.isNotBlank()) {
                                currentUser?.let {
                                    it.updatePassword(newPassword)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Password update failed", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save password")
                    }
                }
            }
        }
    }

