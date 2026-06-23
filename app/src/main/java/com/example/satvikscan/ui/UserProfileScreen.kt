package com.example.satvikscan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.satvikscan.R
import com.example.satvikscan.routname
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel

@Composable
fun UserProfileScreen(
    paddingValues: PaddingValues,
    viewModel: Satvik_ViewModel,
    navController: NavHostController
) {
//    val isDarkMode by viewModel.isDarkMode.collectAsState()
    var isDarkMode = false
    val dietName by viewModel.dietName.collectAsState()



    val CardBg = Color(0xFFF5F3F3)           // Main Card / List item Background (#f5f3f3)
    val RowIconColor = Color(0xFF333333)     // Dark Gray/Black for main setting icons





    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color(0xFF121212) else Color(0xFFFAF9F9))
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        // Custom  Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isDarkMode) {
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFADFDD4), Color(0xFF121212))
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFEAF4EF), Color(0xFFFAF9F9))
                        )
                    }
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (isDarkMode) Color.White else Color(0xFF2D6A4F)  ,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Settings",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color(0xFF2D6A4F)
            )
        }

        // Main content inside Column (non-scrollable layout)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 2. DIET PREFERENCES section
            SectionHeader(title = "DIET PREFERENCES", isDarkMode = isDarkMode, headerColor = Color(0xFF7E8B85))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(routname().WelcomeScreen)
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF2E2420) else Color(0xFFFCEEE4)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rounded Square Background for Diet Leaf Icon
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                if (isDarkMode) Color(0xFF4D382B) else Color(0xFFFFF2EC),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.swaminarayan_tilak),
                            contentDescription = "Diet preferences",
                            tint = Color(0xFFD35400),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ACTIVE DIET",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD35400),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (dietName) {
                                "Swaminarayan" -> "Swaminarayan (Strict)"
                                "Jain" -> "Jain (Strict)"
                                else -> "$dietName Diet"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color(0xFFFFCCAA) else Color(0xFF5D4037)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (dietName) {
                                "Swaminarayan" -> "Strict Satvik guidelines enabled."
                                "Jain" -> "No root vegetables or honey."
                                else -> "Custom ingredient exclusions enabled."
                            },
                            fontSize = 13.sp,
                            color = if (isDarkMode) Color(0xFFCCB3A3) else Color(0xFF7E726B)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Go",
                        tint = if (isDarkMode) Color(0xFFFFCCAA) else Color(0xFFD35400),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. APP SETTINGS section
            SectionHeader(title = "APP SETTINGS", isDarkMode = isDarkMode, headerColor = Color(0xFF7E8B85))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E1E1E) else CardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column {
                    // Dark Mode Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    if (isDarkMode) Color(0xFF2C2C2C) else Color.White,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.moon_svg),
                                contentDescription = "Dark Mode",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Dark Mode",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color(0xFF1F1F1F),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { /* Toggle dark mode */ },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor =  Color(0xFF2D6A4F),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f)
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = if (isDarkMode) Color(0xFF2A2A2A) else Color(0xFFE5E3E3)
                    )

                    // Language Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Language details could be added */ }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    if (isDarkMode) Color(0xFF2C2C2C) else Color.White,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.langues_png),
                                contentDescription = "Language",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Language",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color(0xFF1F1F1F),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "English",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. SUPPORT & ABOUT section
            SectionHeader(title = "SUPPORT & ABOUT", isDarkMode = isDarkMode, headerColor = Color(0xFF7E8B85))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E1E1E) else CardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column {
                    // Send Feedback
                    SupportRow(
                        icon = painterResource(R.drawable.feedback_icon),
                        title = "Send Feedback",
                        isDarkMode = isDarkMode,
                        iconColor = RowIconColor,
                        onClick = { /* Handle feedback */ }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = if (isDarkMode) Color(0xFF2A2A2A) else Color(0xFFE5E3E3)
                    )

                    // Privacy Policy
                    SupportRow(
                        icon = painterResource(R.drawable.policy_icon),
                        title = "Privacy Policy",
                        isDarkMode = isDarkMode,
                        iconColor = RowIconColor,
                        onClick = { /* Handle privacy policy */ }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = if (isDarkMode) Color(0xFF2A2A2A) else Color(0xFFE5E3E3)
                    )

                    // About SatvikScan
                    SupportRow(
                        icon = painterResource(R.drawable.info_icon),
                        title = "About SatvikScan",
                        isDarkMode = isDarkMode,
                        iconColor = RowIconColor,
                        onClick = { /* Handle About screen */ }
                    )
                }
            }

            // Pushes version and badge down to the bottom of the screen dynamically
            Spacer(modifier = Modifier.weight(1f))

            // 5. Version info footer
            Text(
                text = "v1.0.0",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Made for Jain & Swaminarayan pill badge
            Box(
                modifier = Modifier
                    .background(
                        if (isDarkMode) Color(0xFF382920) else Color(0xFFFFF2EC),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MADE FOR JAIN & SWAMINARAYAN",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD35400),
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String, isDarkMode: Boolean, headerColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, bottom = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.Gray else headerColor,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun SupportRow(
    icon: Painter,
    title: String,
    isDarkMode: Boolean,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    if (isDarkMode) Color(0xFF2C2C2C) else Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color(0xFF1F1F1F),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}