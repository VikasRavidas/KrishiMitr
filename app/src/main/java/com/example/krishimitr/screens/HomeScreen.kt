package com.example.krishimitr.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.krishimitr.R
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var backPressCount by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    BackHandler(enabled = true, onBack = {
        backPressCount++

        if (backPressCount == 1) {
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
        } else {
            (context as? Activity)?.finish()
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {

            QuoteCarousel(
                quotes = listOf(
                    "“It is only the farmer who faithfully plants seeds in the Spring, who reaps a harvest in the Autumn.”\n— B. C. Forbes",
                    "“There are only three things that can kill a farmer: lightning, rolling over in a tractor, and old age.”\n— Bill Bryson",
                    "“I believe in Jai Jawan Jai Kisan - a country where the farmer is unhappy is on its path towards destruction.”\n— Gurpreet Ghuggi"
                )
            )


            PhotoActions()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PhotoActions() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .fillMaxWidth()
       .height(290.dp),


        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose an action",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PhotoActionButton(
                    icon = Icons.Default.Camera,
                    label = "Take Photo",
                    onClick = {
                        // Define the action when "Import Photo" is clicked
                        println("Take Photo clicked")
                    }
                )

                PhotoActionButton(
                    icon = Icons.Default.Image,
                    label = "Import Photo",
                    onClick = {
                        // Define the action when "Import Photo" is clicked
                        println("Import Photo clicked")
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(140.dp)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color(0xFF4CAF50))

            )
            .padding(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF81C784), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuoteCarousel(quotes: List<String>) {
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(9000) // Change quote every 9 seconds
            currentIndex = (currentIndex + 1) % quotes.size
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(Color(0xFF528447))
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quotes[currentIndex],
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
