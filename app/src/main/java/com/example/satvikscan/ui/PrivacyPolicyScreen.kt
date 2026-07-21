package com.example.satvikscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0xFFF2F2F2)
    val cardBgColor = Color(0xFFFFFFFF)
    val borderColor = Color(0xFFECEFF1)
    val primaryTextColor = Color(0xFF1E293B)
    val secondaryTextColor = Color(0xFF64748B)
    val accentIconColor = Color(0xFF0284C7)
    val greenBannerBg = Color(0xFFE8F5E9)
    val greenBannerBorder = Color(0xFFA5D6A7)
    val greenBannerText = Color(0xFF1B5E20)
    val greenBannerSubtitle = Color(0xFF2E7D32)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = primaryTextColor
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Privacy & Legal Policy",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )
        }

        // Main Scrollable Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Top Green Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = greenBannerBg),
                    border = BorderStroke(1.dp, greenBannerBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFC8E6C9), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // TODO: Replace with real shield icon
                            Icon(
                                painter = painterResource(R.drawable.policy_icon),
                                contentDescription = "Privacy Shield",
                                tint = greenBannerText,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "100% On-Device Privacy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = greenBannerText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your scans and diet preferences are processed locally. No personal data ever leaves your phone.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = greenBannerSubtitle,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // 2. Legal and Data Source Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    border = BorderStroke(1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Legal and data source",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Read these limits before relying on scan results.",
                            fontSize = 13.sp,
                            color = secondaryTextColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val bullets = listOf(
                            "Result based on Open Food Facts data for accurate ingredient analysis.",
                            "Data may be incomplete or inaccurate for certain regional products.",
                            "Data source: Open Food Facts (OFF) community-driven database.",
                            "Results are for reference; always verify critical allergies or strict vows directly on product packaging.",
                            "No official affiliation with Open Food Facts or its contributors.",
                            "The application is provided as-is without guarantee of 100% product coverage."
                        )

                        bullets.forEach { bullet ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = secondaryTextColor
                                )
                                Text(
                                    text = bullet,
                                    fontSize = 13.sp,
                                    color = primaryTextColor,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Data Collection & Storage Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    border = BorderStroke(1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Data Collection & Storage",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real Zero Personal Tracking icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.policy_icon),
                            iconColor = accentIconColor,
                            title = "Zero Personal Tracking",
                            subtitle = "No account required, no personal data collected.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real On-Device Storage (Room DB) icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.info_icon),
                            iconColor = accentIconColor,
                            title = "On-Device Storage (Room DB)",
                            subtitle = "Scan history and custom exclusions stay on your phone.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real No Cloud Uploads icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.ic_cloud_off),
                            iconColor = accentIconColor,
                            title = "No Cloud Uploads",
                            subtitle = "Your diet preferences and scan history never sent to cloud servers.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )
                    }
                }
            }

            // 4. Camera Permissions Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    border = BorderStroke(1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Camera Permissions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real Real-Time Label Scanning icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.baseline_camera_alt_24),
                            iconColor = accentIconColor,
                            title = "Real-Time Label Scanning",
                            subtitle = "Camera used exclusively for live barcodes, ingredients OCR scanning via Google ML Kit.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real No Image Recording icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.baseline_qr_code_scanner_24),
                            iconColor = accentIconColor,
                            title = "No Image Recording",
                            subtitle = "Frames are processed in real-time memory and instantly discarded.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )
                    }
                }
            }

            // 5. Network & API Transparency Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    border = BorderStroke(1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Network & API Transparency",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real Open Food Facts Barcode API icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.barcode_scan),
                            iconColor = accentIconColor,
                            title = "Open Food Facts Barcode API",
                            subtitle = "Only the numerical barcode strings are queried to fetch product details.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // TODO: Replace with real No Tracking Identifiers icon
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.policy_icon),
                            iconColor = accentIconColor,
                            title = "No Tracking Identifiers",
                            subtitle = "No device ID or user tracking headers sent with network requests.",
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor
                        )
                    }
                }
            }

            // 6. Footer Badge & Version info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = greenBannerBg,
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // TODO: Replace with real footer shield icon
                            Icon(
                                painter = painterResource(R.drawable.policy_icon),
                                contentDescription = null,
                                tint = greenBannerSubtitle,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "100% UPHOLD SATVIKSCAN PRIVACY STANDARDS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = greenBannerSubtitle,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "v1.0.0 • Last updated: July 2026",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun PolicyFeatureRow(
    icon: Painter,
    iconColor: Color,
    title: String,
    subtitle: String,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    color = iconColor.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = secondaryTextColor,
                lineHeight = 16.sp
            )
        }
    }
}
