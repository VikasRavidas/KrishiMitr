package com.example.krishimitr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.krishimitr.login.LoginScreen
import com.example.krishimitr.login.SignUpScreen
import com.example.krishimitr.screens.ContactScreen
import com.example.krishimitr.screens.HistoryScreen
import com.example.krishimitr.screens.MainScreen
import com.example.krishimitr.screens.ProfileScreen
import com.example.krishimitr.screens.ManageAccountScreen
import com.example.krishimitr.ui.theme.KrishiMitrTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
//import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient

class MainActivity : ComponentActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        val auth: FirebaseAuth?
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val gso = Builder(DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = getClient(this, gso)

        setContent {
            KrishiMitrTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if(currentUser!=null){
                        Screen.Main.route}
                            else{
                        Screen.Login.route
                    }
                ) {
                    composable(route = Screen.Login.route) {
                        LoginScreen(navController, mGoogleSignInClient)

                    }
                    composable(route = Screen.Main.route) {
                        MainScreen(navController = navController)
                    }
                    composable(route = Screen.Profile.route) {
                        ProfileScreen(navController = navController)
                    }
                    composable(route = Screen.History.route) {
                        HistoryScreen(navController = navController)
                    }
                    composable(route = Screen.Contact.route) {
                        ContactScreen(navController = navController)
                    }
                    composable(route = Screen.SignUp.route) {
                        SignUpScreen(navController = navController)
                    }
                    composable(route = Screen.ManageAccount.route) {
                        ManageAccountScreen(navController = navController)
                    }
                }
            }
        }
    }
}


