package com.example.krishimitr.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.* // Material 2 imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.* // Material 3 imports
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.constraintlayout.compose.Visibility
import androidx.navigation.NavController
import com.example.krishimitr.R
import com.example.krishimitr.Screen
import com.example.krishimitr.ui.theme.LoginTheme
import com.example.krishimitr.ui.theme.Purple40
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun WaveHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Adjust height as necessary for the wave
            .background(Color.White)
    ) {
        // Column to stack Canvas and Text vertically
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom // Aligns children at the bottom
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth() // This will fill the entire Box
                    .weight(1f) // This gives the canvas weight, filling space above the text
            ) {
                val width = size.width
                val height = size.height

                // Create the path for the wave
                val path = Path().apply {
                    moveTo(0f, height * 0.6f) // Start wave
                    quadraticBezierTo(
                        width * 0.25f, height * 0.4f, // Control point 1
                        width * 0.5f, height * 0.6f  // Mid point of wave
                    )
                    quadraticBezierTo(
                        width * 0.75f, height * 0.8f, // Control point 2
                        width, height * 0.6f  // End point
                    )
                    lineTo(width, 0f) // Top-right corner
                    lineTo(0f, 0f) // Top-left corner
                    close()
                }

                drawPath(
                    path = path,
                    color = Color(0xFF267B37) // Green wave color
                )
            }
            // Aligns text at the bottom of the Box, below the canvas
            Text(
                text = "KrishiMitr",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()  // Makes the text take full width
                    .padding(16.dp)   // Adds padding around the text
                    .wrapContentWidth(Alignment.CenterHorizontally)  // Centers the text horizontally
            )
        }
    }
}

private const val TAG = "EmailPassword"
@Composable
fun LoginScreen(navController: NavController,mGoogleSignInClient: GoogleSignInClient) {

    LoginTheme {


        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        val auth = Firebase.auth
        val context = LocalContext.current
        var isRememberMeChecked by remember { mutableStateOf(false) }

        val signInLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = getSignedInAccountFromIntent(result.data)
                    task.addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            try {
                                val account = signInTask.getResult(ApiException::class.java)!!
                                val idToken = account.idToken
                                val credential = GoogleAuthProvider.getCredential(idToken, null)
                                Firebase.auth.signInWithCredential(credential)
                                    .addOnCompleteListener { authTask ->
                                        if (authTask.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Authentication Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                              navController.navigate(Screen.Main.route){
                                                popUpTo(0){
                                                    inclusive= true
                                               }
                                               }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Authentication Failed: ${authTask.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } catch (e: ApiException) {
                                Toast.makeText(
                                    context,
                                    "Google Sign-In Failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Google Sign-In Task Failed: ${signInTask.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        )




        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)

        ) {


            WaveHeader()

            // Rest of the login form below the wave
            //Spacer(modifier = , Modifier.height(20.dp)) // Spacing between wave and login form


            Image(
                painter =
                painterResource(id = R.drawable.image),
                contentDescription = "Top wave",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            // Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Log in to your account",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)  // Centers the text horizontally
            )
            // Login form
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                OutlinedTextField(
                    value = email, // Bind with state

                    onValueChange = {
                        if (it.length <= 50) {
                            email = it
                        } else {
                            Toast.makeText(
                                context,
                                "Maximum  characters reached",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    label = { Text("Email or Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        // autoCorrect = true,
                        imeAction = ImeAction.Next,

                        ),
                    maxLines = 1, // Limit to a single line
                    singleLine = true // Ensures no vertical expansion
                    //modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // TextField for Password
                OutlinedTextField(
                    value = password, // Bind with state
                    onValueChange = {
                        if (it.length <= 30) {
                            password = it
                        }
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },

//                modifier = Modifier.fillMaxWidth(),
//                visualTransformation = PasswordVisualTransformation() // Hide password
//                ,
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = "Visibility Toggle"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    maxLines = 1, // Limit to a single line
                    singleLine = true // Ensures no vertical expansion
                )


                //Spacer(modifier = Modifier.height(8.dp))

                // Row for Remember Me and Forgot Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isRememberMeChecked,
                            onCheckedChange = {
                                isRememberMeChecked = it
                            })
                        Text("Remember Me")
                    }




                    Text(
                        text = "Forgot Password?",
                        style = TextStyle(color = Color.Blue),
                        modifier = Modifier.clickable {

                            if (email.isNotEmpty()) {
                                auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Password reset email sent!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error sending password reset email",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter your email",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    )

                }

                //Spacer(modifier = Modifier.height(4.dp))

                // Sign In Button
                Button(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            Log.d(TAG, "signInWithEmail:null")
                            Toast.makeText(
                                navController.context,
                                "Enter the credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "signInWithEmail:success")
                                    Toast.makeText(
                                        navController.context,
                                        "Logging in",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                navController.navigate(Screen.Main.route)
                                {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }

                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        navController.context,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    //  updateUI(null)
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green
                ) {
                    Text("Sign In", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()

                Box(   //..................Google Sign In ...........................................//
                    Modifier.clickable {
                        signInLauncher.launch(mGoogleSignInClient.signInIntent)
                    }
                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.padding(2.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.google_icon),
                                contentDescription = "Google Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )

                            Text(
                                text = "Sign In with Google",
                                modifier = Modifier.padding(10.dp, 0.dp),
                                color = Purple40,
                                fontWeight = FontWeight.Bold
                            )

                        }


                    }


                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(42.dp), // Add padding around the card
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    // Add some elevation for a shadow effect
                    colors = CardDefaults.cardColors(Color.White),
                    shape = MaterialTheme.shapes.medium // Use medium shape for rounded corners
                    ,
                    onClick = {

                    }

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), // Padding inside the card
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account?",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Adjusted spacer width

                        Text(
                            text = "Sign Up",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary, // Use primary color for emphasis
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold // Make it bold for emphasis
                            )
                        )

                    }
                }
            }
        }
    }

}