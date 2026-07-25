package com.example.satvikscan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topBarTextColor  = Color(0xFF1B1C1C)
    val cardSurface      = Color(0xFFFFFFFF)
    val primaryText      = Color(0xFF1B1C1C)
    val secondaryText    = Color(0xFF5F6368)
    val dividerColor     = Color(0xFFF1F3F4)
    val badgeBgColor     = Color(0xFFDCFCE7)
    val badgeTextColor   = Color(0xFF15803D)
    val versionTextColor = Color(0xFF9CA3AF)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .statusBarsPadding()
    ) {
        //  Top App Bar 
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = topBarTextColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Privacy & Legal Policy",
                fontSize = 20.sp,
                color = topBarTextColor,
                textAlign = TextAlign.Center
            )
        }

        //  Main Content List 
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Privacy Card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color(0xFFDCFCE7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.lock_icon),
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "100% On-Device Privacy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your scans and diet preferences are processed locally. No personal data ever leaves your phone.",
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = secondaryText
                            )
                        }
                    }
                }
            }

            //  Legal and Data Source Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Legal and data source",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Read these limits before relying on scan results.",
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        val bullets = listOf(
                            "Result based on Open Food Facts data for accurate ingredient analysis.",
                            "Data may be incomplete or inaccurate for certain regional products.",
                            "Data source: Open Food Facts (OFF) community-driven database.",
                            "Results are for reference; always verify critical allergies or strict vows directly on product packaging.",
                            "No official affiliation with Open Food Facts or its contributors.",
                            "The application is provided as-is without guarantee of 100% product coverage."
                        )
                        bullets.forEachIndexed { index, bullet ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 6.dp)
                                        .size(6.dp)
                                        .background(secondaryText, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = bullet,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = secondaryText
                                )
                            }
                            if (index < bullets.lastIndex) {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            //  Data Collection & Storage Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Data Collection & Storage",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.lock_icon),
                            title = "Zero Personal Tracking",
                            subtitle = "No account required, no personal ID collected.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.db_icon),
                            title = "On-Device Storage ",
                            subtitle = "Scan history and custom exclusions stay on your phone.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.ic_cloud_off),
                            title = "No Cloud Uploads",
                            subtitle = "Your dietary choices and scans are never sent to remote servers.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                    }
                }
            }

            //  Camera Permissions Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Camera Permissions",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.baseline_camera_alt_24),
                            title = "Real-Time Label Scanning",
                            subtitle = "Camera used exclusively for live barcode & ingredient OCR reading via Google ML Kit.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.no_image_ic),
                            title = "No Image Recording",
                            subtitle = "Frames are processed in real-time memory and instantly discarded.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                    }
                }
            }

            //  Network & API Transparency Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Network & API Transparency",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.openfc_icon),
                            title = "Open Food Facts Barcode API",
                            subtitle = "Only the anonymous numerical barcode string is queried to fetch product details.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.key_ic),
                            title = "No Tracking Identifiers",
                            subtitle = "No device ID or user tracking token is sent with network requests.",
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                    }
                }
            }

            //  Footer: Verified Badge & Version
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(badgeBgColor, RoundedCornerShape(50))
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(badgeTextColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "VERIFIED SATVIKSCAN PRIVACY STANDARD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "v1.0.0 • Last updated: July 2026",
                        fontSize = 11.sp,
                        color = versionTextColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun PolicyFeatureRow(
    icon: Painter,
    title: String,
    subtitle: String,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = icon,
            contentDescription = title,
            tint = primaryTextColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = secondaryTextColor
            )
        }
    }
}
