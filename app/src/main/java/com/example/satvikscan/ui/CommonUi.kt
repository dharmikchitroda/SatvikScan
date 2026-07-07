package com.example.satvikscan.ui

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.satvikscan.R
import com.example.satvikscan.routname
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import com.example.satvikscan.ui.viewmodel.ScanMode

@Composable
fun CommonUi(
    modifier: Modifier = Modifier.fillMaxSize(),

    ) {

    val viewModel: Satvik_ViewModel = viewModel()
    val navconroller = rememberNavController() // return navhostcontroller object

    val navBackStackEntry by navconroller.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    // for this both top&bottom bar show or hide
    val showBars =
        currentRoute != routname().CameraScreen && currentRoute != routname().ResultScreen && currentRoute != routname().WelcomeScreen && currentRoute != routname().CustomExclusionsScreen
    // only topbar show or hide
    val showTopbar =
        showBars && currentRoute != routname().HistoryScreen && currentRoute != routname().ProfileScreen


    // which screen open when user start the app first time and another time
    val startDestination = remember {
        if (viewModel.hasSelectedDiet()) routname().ScanScreen else routname().WelcomeScreen
    }

    // dietName show in topbar
    val dietName by viewModel.dietName.collectAsState()

    // current cameramode
    val currentCameramode by viewModel.Scanmode.collectAsState()

    Scaffold(modifier = modifier, topBar = { if (showTopbar) Topbar(dietName) }, bottomBar = {
        if (showBars) {
            BottomBar(moveScanscreen = {
                navconroller.navigate(routname().ScanScreen) {
                    popUpTo(navconroller.graph.startDestinationId)
                    launchSingleTop = true
                }
            }, moveHisscreen = {
                navconroller.navigate(routname().HistoryScreen) {
                    popUpTo(navconroller.graph.startDestinationId)
                    launchSingleTop = true
                }
            }, moveProfilescreen = {
                navconroller.navigate(routname().ProfileScreen) {
                    popUpTo(navconroller.graph.startDestinationId)
                    launchSingleTop = true
                }
            })
        }
    }) { paddingValues ->

        NavHost(
            navconroller, startDestination = startDestination, modifier = Modifier.fillMaxSize()
        ) {

            composable(routname().ScanScreen) {
                HomeScreenUI(
                    paddingValues,
                    currentMode = currentCameramode,
                    onScanPhotoclick = {
                        if (currentCameramode != ScanMode.OCR) viewModel.ChnageScanMode(ScanMode.OCR)
                        viewModel.startScanning()
                        navconroller.navigate(routname().CameraScreen)
                    },
                    onScanBarcodescan = {
                        if (currentCameramode != ScanMode.BARCODE) viewModel.ChnageScanMode(ScanMode.BARCODE)
                        viewModel.startScanning()
                        navconroller.navigate(routname().CameraScreen)
                    }
                )
            }
            composable(routname().HistoryScreen) {
                HistoryScreen(
                    paddingValues = paddingValues, onStartScanningClick = {
                        navconroller.navigate(routname().ScanScreen) {
                            popUpTo(navconroller.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }, viewModel = viewModel
                )
            }
            composable(routname().ProfileScreen) {
                UserProfileScreen(
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    onBackClick = { navconroller.popBackStack() },
                    onChangeDietClick = { navconroller.navigate(routname().WelcomeScreen) }
                )
            }
            composable(routname().CameraScreen) {
                val uiState = viewModel.productUiState.collectAsState().value
                CameraScanScreen(
                    viewModel = viewModel,
                    cameramode = currentCameramode,
                    onBackClick = {
                        viewModel.startScanning()
                        navconroller.popBackStack()
                    }
                )
                LaunchedEffect(uiState) {
                    if (uiState != Satvik_ViewModel.ProductUiState.Idle &&
                        !(uiState is Satvik_ViewModel.ProductUiState.Error && uiState.message == "NO_INTERNET")
                    ) {
                        navconroller.navigate(
                            routname().ResultScreen
                        ) {
                            // remove the last screen from the stack
                            popUpTo(routname().CameraScreen) { inclusive = true }
                        }
                    }
                }

            }
            composable(routname().ResultScreen) {

                ResultScreen(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    onBackClick = {
                        viewModel.startScanning() // reset old state , this main use the restart the camerax fatch barcode in one time one
                        navconroller.popBackStack()
                    },
                    onScanAgainClick = {
                        viewModel.startScanning()
                        navconroller.navigate(routname().CameraScreen) {
                            viewModel.startScanning() // reset old state
                            popUpTo(routname().ScanScreen)
                        }
                    },
                    dietName = dietName,
                    ScanIngredient = {
                        if (currentCameramode != ScanMode.OCR) viewModel.ChnageScanMode(ScanMode.OCR)
                        viewModel.startScanning()
                        navconroller.navigate(routname().CameraScreen) {
                            popUpTo(routname().ResultScreen) { inclusive = true }
                        }
                    }
                )

            }
            composable(routname().WelcomeScreen) {
                WelcomeScreen(
                    onContinueClick = { selectedDiet ->
                        // save Diet
                        viewModel.updateDiet(selectedDiet)

                        if (selectedDiet == "Custom") {
                            navconroller.navigate(routname().CustomExclusionsScreen)
                        } else {
                            navconroller.navigate(routname().ScanScreen) {
                                popUpTo(routname().WelcomeScreen) { inclusive = true }
                            }
                        }
                    })
            }

            composable(routname().CustomExclusionsScreen) {
                CustomExclusionsScreen(
                    viewModel = viewModel, onBackClick = {
                        navconroller.navigate(routname().ScanScreen) {
                            popUpTo(routname().WelcomeScreen) { inclusive = true }
                        }
                    })
            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(dietName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFBEF3CE), Color(0xFFFAFAFA))
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                "SatvikScan",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B3A2D),
                textAlign = TextAlign.Center
            )


            Box(
                modifier = Modifier
                    .background(
                        color = Color.White, shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = dietName,
                    fontSize = 11.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
fun BottomBar(
    moveScanscreen: () -> Unit, moveHisscreen: () -> Unit, moveProfilescreen: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.White, tonalElevation = 0.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            // History
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                    moveHisscreen()
                }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_history_24),
                    contentDescription = "History",
                    tint = Color.Gray
                )
                Text(text = "History", fontSize = 11.sp, color = Color.Gray)
            }

            // Scan
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                    moveScanscreen()
                }) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary, shape = CircleShape
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.scan),
                        contentDescription = "Scan",
                        tint = Color.White,
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
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                    moveProfilescreen()
                }) {
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