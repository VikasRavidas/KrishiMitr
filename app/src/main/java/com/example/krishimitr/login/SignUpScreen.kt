package com.example.krishimitr.login


//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.krishimitr.R
import com.example.krishimitr.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var mobilenumber by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var numbersnotvalid by remember { mutableStateOf(true) }
    val context = LocalView.current.context
    if(password.isEmpty()){
        Log.d(TAG,"Password is empty")
    }
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
Column (
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
){

    Image(
        painter =
        painterResource(id = R.drawable.wave),
        contentDescription = "Top wave",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer { scaleX = 1f;scaleY = 1f }
    )
    Text(
        text = "KrishiMitr",
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()  // Makes the text take full width
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(top = 10.dp)// Centers the text horizontally
    )
    Image(
        painter =
        painterResource(id = R.drawable.image),
        contentDescription = "Top wave",
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .graphicsLayer { scaleX = 1f;scaleY = 1f }
    )
    Text(
        text = "Sign Up to your account",
        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
        modifier = Modifier
            .fillMaxWidth()  // Makes the text take full width
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(top = 10.dp)// Centers the text horizontally
    )
    OutlinedTextField(
        value = username,
        onValueChange = {
            username = it
        },
        label = { Text("Username", color = Color.Black) },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null,tint = Color.Black) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = RoundedCornerShape(20.dp),
        textStyle = TextStyle(color= Color.Black),
        modifier = Modifier
            .padding(top = 10.dp)
            .width(340.dp)
    )
    OutlinedTextField(
        value = email,
        onValueChange = {
            email = it
            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        },
        label = { Text("Email", color = Color.Black) },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null,tint = Color.Black) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = !isEmailValid,
        shape = RoundedCornerShape(20.dp),
        textStyle = TextStyle(color= Color.Black),
        modifier = Modifier
            .padding(top = 10.dp)
            .width(340.dp)
    )
    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
        },
        label = { Text("Password", color = Color.Black) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null,tint = Color.Black) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(20.dp),
        textStyle = TextStyle(color= Color.Black),

        modifier = Modifier
            .padding(top = 10.dp)
            .width(340.dp)
    )
    OutlinedTextField(
        value = mobilenumber,
        onValueChange = {
            mobilenumber = it
            numbersnotvalid = it.length != 10
        },
        label = { Text("Mobile Number", color = Color.Black) },
        leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null,tint = Color.Black) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(20.dp),
//        isError = numbersnotvalid,
        textStyle = TextStyle(color= Color.Black),
        modifier = Modifier
            .padding(top = 10.dp)
            .width(340.dp)
    )
    Button(onClick ={
    if (email.isEmpty() || password.isEmpty() || mobilenumber.isEmpty() || username.isEmpty()) {
        // Display error message to the user

            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()

    } else if(!isEmailValid){
        Toast.makeText(context, "Enter the valid email", Toast.LENGTH_SHORT).show()
    }else if(numbersnotvalid){
        Toast.makeText(context, "Enter the valid mobile number", Toast.LENGTH_SHORT).show()
    }else
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val userId = user?.uid

                    // Store additional user data in Firestore
                    if (userId != null) {
                        val userData = hashMapOf(
                            "username" to username,
                            "mobileNumber" to mobilenumber
                        )
                        firestore.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "UserData added successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding user data", e)
                            }
                    }
                    // ... navigate to next screen or update UI ...
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    // ... display error message ...
                }
            }
    }},
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
     modifier = Modifier
        .padding(top = 20.dp)
        .height(45.dp)
        .width(200.dp)

    ){
        Text("Sign Up")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=30.dp), // Padding inside the card
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account?",
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.width(8.dp)) // Adjusted spacer width

        Text(
            text = "Sign In",
            style = TextStyle(
                color = Color(0xFF4CAF50), // Use primary color for emphasis
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold // Make it bold for emphasis
            ) ,
            modifier = Modifier.clickable {
                Log.d(TAG, "Sign Up")
                navController.navigate(Screen.Login.route)
            }
        )

    }








}


}