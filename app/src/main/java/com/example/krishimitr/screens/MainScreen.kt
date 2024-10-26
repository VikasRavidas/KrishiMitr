package com.example.krishimitr.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleSignInClient = getClient(context, DEFAULT_SIGN_IN)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val nestedNavController = rememberNavController()
    val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var userName by remember { mutableStateOf("Loading...") }
    var userEmail by remember { mutableStateOf("Loading...") }
    var userPhotoUrl by remember { mutableStateOf<String?>(null) }
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Fetch user details
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        currentUser?.let {
            userName = it.displayName ?: "Anonymous"
            userEmail = it.email ?: "No Email"
            userPhotoUrl = it.photoUrl?.toString()
                ?: "https://th.bing.com/th/id/R.7cc8de4020756a6b9d8a6aeb4441d188?rik=1ChCG5Z9Fc6FTQ&riu=http%3a%2f%2fgetdrawings.com%2ffree-icon%2ffacebook-avatar-icon-57.png&ehk=KTLTz0tf%2bVA8%2fB5pCKnDw%2fX%2fm1GcCpXkqxSR%2bM2aEg4%3d&risl=&pid=ImgRaw&r=0"

            val userId = currentUser.uid
            val userDocument = firestore.collection("users").document(userId)

            userDocument.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userName = document.getString("username") ?: "Anonymous"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching user data: ", exception)
                }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(159.dp)
                            .padding(12.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(BorderStroke(3.dp, Color.White), CircleShape)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(userPhotoUrl)
                                        .apply { crossfade(true); size(Size.ORIGINAL) }
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Column {
                                Text(
                                    text = userName,
                                    style = TextStyle(color = Color.White, fontSize = 17.sp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = userEmail,
                                    style = TextStyle(
                                        color = Color.White, fontSize = 14.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                )
                            }
                        }
                    }
                    Text("Account", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold), modifier = Modifier.padding(16.dp))
                    NavigationDrawerItem("Manage Account", Icons.Default.ManageAccounts, nestedNavController, "manage_account",drawerState,scope)
                    NavigationDrawerItem("Location", Icons.Default.LocationOn, navController, "location",drawerState,scope)
                    NavigationDrawerItem("Theme", Icons.Default.Palette, navController, "theme",drawerState,scope)

                    Text("Support", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold), modifier = Modifier.padding(16.dp))
                    NavigationDrawerItem("Contact Us", Icons.Default.LocalPhone, navController, "contact_us",drawerState,scope)
                    NavigationDrawerItem("Help Us", Icons.Default.Help, navController, "help",drawerState,scope)

                    Text("About", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold), modifier = Modifier.padding(16.dp))
                    NavigationDrawerItem("Privacy Policy", Icons.Default.PrivacyTip, navController, "privacy_policy",drawerState,scope)

                    NavigationDrawerItem("Logout", Icons.Default.Logout, navController, "logout",drawerState,scope) {
                        Firebase.auth.signOut()
                        googleSignInClient.signOut().addOnCompleteListener {
                            Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                if (currentRoute == Screen.Home.route) {
                    CenterAlignedTopAppBar(
                        colors = topAppBarColors(containerColor = Color.White),
                        title = { Text("KrishiMitr", style = TextStyle(color = Color.Black, fontSize = 20.sp)) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu", modifier = Modifier.size(30.dp), tint = Color.Black)
                            }
                        },
                        actions = {
                            IconButton(onClick = { Toast.makeText(context, "No new notifications", Toast.LENGTH_SHORT).show() }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", modifier = Modifier.size(30.dp), tint = Color.Black)
                            }
                        }
                    )
                }
                if(currentRoute==Screen.ManageAccount.route){
                    CenterAlignedTopAppBar(
                        colors = topAppBarColors(containerColor = Color.White),
                        title = { Text("Manage Account",color=Color.Black) },
                        navigationIcon = {
                            IconButton(onClick = { nestedNavController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        }

                    )
                }
            },
            bottomBar = { BottomNavigationBar(navController = nestedNavController, currentRoute = currentRoute) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NestedNavHost(navController = nestedNavController)
            }
        }
    }
}

@Composable
fun NavigationDrawerItem(
    label: String,
    icon: ImageVector,
    navController: NavController,
    route: String = "",
    drawerState: DrawerState,
    scope: CoroutineScope,
    onClick: () -> Unit = {
        scope.launch {
            drawerState.close() // Close the drawer
        }
        navController.navigate(route)
    }
) {
    NavigationDrawerItem(
        label = { Text(label, style = TextStyle(color = Color.Black)) },
        icon = { Icon(icon, contentDescription = null, tint = Color.Black) },
        selected = false,
        onClick = onClick,
        shape = RectangleShape,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.Gray,
            unselectedContainerColor = Color.White
        )
    )
}



@Composable
fun NestedNavHost(navController: NavController) {
    val context = LocalContext.current
//    val questionDao= QuizDatabase.getSQLDatabase(context).questionDao()
//    val repository= SqlRepository(questionDao)
//    val factory= QuestionViewModelFactory(repository)
//    val viewModel: SQLQuestionViewModel = viewModel(factory=factory)

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Contact.route) {
            ContactScreen(navController = navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController = navController)
        }
        composable(Screen.ManageAccount.route) {
            ManageAccountScreen(navController = navController)
        }
    }
}