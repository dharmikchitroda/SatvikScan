package com.example.satvikscan.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import com.example.satvikscan.R
import com.example.satvikscan.routname
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import com.example.satvikscan.ui.viewmodel.ScanMode
import kotlinx.coroutines.launch

@Composable
fun CommonUi(
    modifier: Modifier = Modifier.fillMaxSize()
) {

    val viewModel: Satvik_ViewModel = viewModel()
    val navController = rememberNavController() // return navhostcontroller object

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    // for this both top & bottom bar show or hide
    val showBars = currentRoute == routname.HomeScreen
    // only topbar show or hide
    val showTopbar = showBars


    // which screen open when user start the app first time and another time
    val startDestination = remember {
        if (viewModel.hasSelectedDiet()) routname.HomeScreen else routname.WelcomeScreen
    }

    // dietName show in topbar
    val dietName by viewModel.dietName.collectAsState()

    // current cameramode
    val currentCameramode by viewModel.Scanmode.collectAsState()

    // Hoisted search state for History top bar
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    val scanHistory by viewModel.scanHistory.collectAsState()
    val hasHistory = scanHistory.isNotEmpty()
    val focusManager = LocalFocusManager.current

    // Pager state: 0 = History, 1 = Home, 2 = Profile
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage

    // Reset search state when leaving History tab
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page != 0) {
                isSearchActive = false
                searchQuery = ""
            }
        }
    }

    Scaffold(modifier = modifier,
        topBar = {
            if (showTopbar) {
                Topbar {
                    when(currentPage) {
                        0 -> {
                            HistoryTopBar(
                                isSearchActive = isSearchActive,
                                searchQuery = searchQuery,
                                onSearchActiveChange = { isSearchActive = it },
                                onSearchQueryChange = { searchQuery = it },
                                onDeleteClick = { showClearHistoryDialog = true },
                                hasHistory = hasHistory
                            )
                        }
                        1 -> {
                            homeScreentopbar(dietName)
                        }
                        2 -> {
                            ProfileScreentopbar()
                        }
                    }
                }
            }

    }, bottomBar = {
        if (showBars) {
            BottomBar(
                currentPage = currentPage,
                moveScanscreen = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(1)
                    }
                    if (currentCameramode != ScanMode.BARCODE) viewModel.ChnageScanMode(ScanMode.BARCODE)
                    viewModel.startScanning()
                    navController.navigate(routname.CameraScreen)
                },
                moveHisscreen = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                moveProfilescreen = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                }
            )
        }
    }) { paddingValues ->

        NavHost(
            navController, startDestination = startDestination, modifier = Modifier.fillMaxSize()
        ) {

            composable(routname.HomeScreen) {
                // BackHandler: if user is on History or Profile page, go back to Home tab
                BackHandler(enabled = currentPage != 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> {
                            HistoryScreen(
                                paddingValues = paddingValues,
                                onStartScanningClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(1)
                                    }
                                },
                                viewModel = viewModel,
                                isSearchActive = isSearchActive,
                                searchQuery = searchQuery,
                                onSearchActiveChange = { isSearchActive = it },
                                onSearchQueryChange = { searchQuery = it },
                                showClearHistoryDialog = showClearHistoryDialog,
                                onShowClearHistoryDialogChange = { showClearHistoryDialog = it }
                            )
                        }
                        1 -> {
                            HomeScreenUI(
                                paddingValues,
                                onScanPhotoclick = {
                                    if (currentCameramode != ScanMode.OCR) viewModel.ChnageScanMode(ScanMode.OCR)
                                    viewModel.startScanning()
                                    navController.navigate(routname.CameraScreen)
                                },
                                onScanBarcodescan = {
                                    if (currentCameramode != ScanMode.BARCODE) viewModel.ChnageScanMode(ScanMode.BARCODE)
                                    viewModel.startScanning()
                                    navController.navigate(routname.CameraScreen)
                                }
                            )
                        }
                        2 -> {
                            UserProfileScreen(
                                paddingValues = paddingValues,
                                viewModel = viewModel,
                                onBackClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(1)
                                    }
                                },
                                onChangeDietClick = { navController.navigate(routname.SelectDietScreen) },
                                onCustomExclusionsClick = { navController.navigate(routname.CustomExclusionsScreen) },
                                onDietGyanClick = { dietType ->
                                    navController.navigate("${routname.DietGyanScreen}/$dietType")
                                }
                            )
                        }
                    }
                }
            }
            composable(routname.CameraScreen) {
                val uiState = viewModel.productUiState.collectAsState().value
                CameraScanScreen(
                    viewModel = viewModel,
                    cameramode = currentCameramode,
                    onBackClick = {
                        viewModel.startScanning()
                        navController.popBackStack()
                    }
                )
                LaunchedEffect(uiState) {
                    if (uiState != Satvik_ViewModel.ProductUiState.Idle &&
                        !(uiState is Satvik_ViewModel.ProductUiState.Error && uiState.message == "NO_INTERNET")
                    ) {
                        navController.navigate(
                            routname.ResultScreen
                        ) {
                            // remove the last screen from the stack
                            popUpTo(routname.CameraScreen) { inclusive = true }
                        }
                    }
                }

            }
            composable(routname.ResultScreen) {

                ResultScreen(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    onBackClick = {
                        viewModel.startScanning() // reset old state, restart camerax barcode fetch
                        navController.popBackStack()
                    },
                    onScanAgainClick = {
                        viewModel.startScanning()
                        navController.navigate(routname.CameraScreen) {
                            viewModel.startScanning() // reset old state
                            popUpTo(routname.HomeScreen)
                        }
                    },
                    dietName = dietName,
                    ScanIngredient = {
                        if (currentCameramode != ScanMode.OCR) viewModel.ChnageScanMode(ScanMode.OCR)
                        viewModel.startScanning()
                        navController.navigate(routname.CameraScreen) {
                            popUpTo(routname.ResultScreen) { inclusive = true }
                        }
                    }
                )

            }
            composable(routname.WelcomeScreen) {
                WelcomeScreen(
                    onContinueClick = { selectedDiet ->
                        // save Diet
                        viewModel.updateDiet(selectedDiet)

                        if (selectedDiet == "Custom") {
                            navController.navigate(routname.CustomExclusionsScreen)
                        } else {
                            navController.navigate(routname.HomeScreen) {
                                popUpTo(routname.WelcomeScreen) { inclusive = true }
                            }
                        }
                    })
            }

            composable(routname.CustomExclusionsScreen) {
                CustomExclusionsScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    })
            }

            composable(routname.SelectDietScreen) {
                SelectDietScreen(
                    viewModel = viewModel,
                    onContinueClick = { selectedDiet ->
                        navController.popBackStack()
                    },
                    onCustomClick = {
                        navController.navigate(routname.CustomExclusionsScreen) {
                            popUpTo(routname.SelectDietScreen) { inclusive = true }
                        }
                    })
            }

            composable("${routname.DietGyanScreen}/{dietType}") { backStackEntry ->
                val dietType = backStackEntry.arguments?.getString("dietType") ?: "Jain"
                DietGyanScreen(
                    dietType = dietType,
                    onBackClick = { navController.popBackStack() }
                )
            }

        }

    }
}




@Composable
fun Topbar(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFBEF3CE), Color(0xFFFAFAFA))
                )
            )
            .statusBarsPadding()
            .height(46.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        content()
    }
}


@Composable
fun BottomBar(
    currentPage: Int,
    moveScanscreen: () -> Unit,
    moveHisscreen: () -> Unit,
    moveProfilescreen: () -> Unit
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    moveHisscreen()
                }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_history_24),
                    contentDescription = "History",
                    tint = if (currentPage == 0) Color(0xFF1B3A2D) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "History",
                    fontSize = 11.sp,
                    color = if (currentPage == 0) Color(0xFF1B3A2D) else Color.Gray
                )
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
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
                            ), shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.barcode_scan__1_),
                        contentDescription = "Scan",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
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
                    painter = painterResource(R.drawable.user),
                    contentDescription = "Profile",
                    tint = if (currentPage == 2) Color(0xFF1B3A2D) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Profile",
                    fontSize = 11.sp,
                    color = if (currentPage == 2) Color(0xFF1B3A2D) else Color.Gray
                )
            }
        }
    }
}


@Composable
fun HistoryTopBar(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchActiveChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    hasHistory: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    if (isSearchActive) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChange(it) },
                placeholder = { Text("Search products...", color = Color.Gray) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF1B3A2D)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )

            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Text",
                        tint = Color(0xFF1B3A2D)
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "History",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B3A2D),
                textAlign = TextAlign.Center
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (hasHistory) {
                    IconButton(onClick = { onSearchActiveChange(true) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF1B3A2D),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear History",
                            tint = Color(0xFF1B3A2D),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun homeScreentopbar(dietName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // SatvikScan Logo with leaf icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                "SatvikScan",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B3A2D)
            )
        }

        // Premium Diet Capsule
        val dietIconRes = when {
            dietName.contains("Swaminarayan", ignoreCase = true) -> R.drawable.swaminarayan_tilak
            dietName.contains("Jain", ignoreCase = true) -> R.drawable.jain_logo
            dietName.contains("Custom", ignoreCase = true) -> R.drawable.custom_icon
            else -> R.drawable.leaf_svg
        }
        val dietIconTint = when {
            dietName.contains("Swaminarayan", ignoreCase = true) -> Color(0xFF5D4037)
            dietName.contains("Jain", ignoreCase = true) -> Color(0xFF2E7D32)
            dietName.contains("Custom", ignoreCase = true) -> Color(0xFF455A64)
            else -> Color(0xFF2E7D32)
        }

        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFF1B3A2D).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = dietIconRes),
                    contentDescription = null,
                    tint = dietIconTint,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = dietName,
                    fontSize = 12.sp,
                    color = Color(0xFF1B3A2D),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

@Composable
fun ProfileScreentopbar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1B3A2D)
        )
    }
}