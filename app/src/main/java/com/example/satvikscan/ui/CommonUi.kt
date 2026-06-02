package com.example.satvikscan.ui

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.satvikscan.R
import com.example.satvikscan.routname
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import kotlin.getValue

@Composable
fun CommonUi(modifier: Modifier = Modifier.fillMaxSize()) {

    val navconroller = rememberNavController()
    val viewModel: Satvik_ViewModel = viewModel() // made object by android

    Scaffold(
        topBar = { Topbar() },
        bottomBar = {
            BottomBar(
                moveScanscreen = {
                    navconroller.navigate(routname().ScanScreen){
                        popUpTo(navconroller.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                moveHisscreen = {
                    navconroller.navigate(routname().HisScreen){
                        popUpTo(navconroller.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                moveProfilescreen = {
                    navconroller.navigate(routname().ProfileScreen){
                        popUpTo(navconroller.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }

    ) { paddingValues ->

        NavHost(navconroller, startDestination = routname().ScanScreen) {
            composable(routname().ScanScreen) {
                ScanScreenUI(paddingValues,{
                    navconroller.navigate(routname().CameraScreen)
                })
            }
            composable(routname().HisScreen) {
                HistoryScreen(paddingValues)
            }
            composable(routname().ProfileScreen) {
                UserProfileScreen(paddingValues)
            }
            composable(routname().CameraScreen) {
                BarcodeScannercameraScreen(viewModel)

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar() {
    TopAppBar(
        title = {
            Text(
                "SatvikScan", fontWeight = FontWeight.Bold,
                    color = Color.White
            )
        },
        actions = {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Swaminarayan", // baad mein DataStore se aayega
                    fontSize = 11.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )

    )
}

@Composable
fun BottomBar(
    moveScanscreen: () -> Unit,
    moveHisscreen: () -> Unit,
    moveProfilescreen: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            // History
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    moveHisscreen()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_history_24),
                    contentDescription = "History",
                    tint = Color.Gray
                )
                Text(text = "History", fontSize = 11.sp, color = Color.Gray)
            }

            // Scan
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        moveScanscreen()
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.scan),
                        contentDescription = "Scan",
                        tint = Color.Black,
                        modifier = Modifier.size(38.dp)
                    )
                }
                Text(
                    text = "Scan",
                    fontSize = 11.sp,
                    color = Color(0xFF1B3A2D),
                    fontWeight = FontWeight.Bold
                )
            }

            // Profile
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    moveProfilescreen()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_supervised_user_circle_24),
                    contentDescription = "Profile",
                    tint = Color.Gray
                )
                Text(text = "Profile", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}