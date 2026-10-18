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
import androidx.compose.ui.res.stringResource
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
                text = stringResource(R.string.privacy_title),
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
                                text = stringResource(R.string.privacy_card1_title),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.privacy_card1_desc),
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
                            text = stringResource(R.string.privacy_legal_title),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.privacy_legal_subtitle),
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        val bullets = listOf(
                            stringResource(R.string.privacy_bullet_1),
                            stringResource(R.string.privacy_bullet_2),
                            stringResource(R.string.privacy_bullet_3),
                            stringResource(R.string.privacy_bullet_4),
                            stringResource(R.string.privacy_bullet_5),
                            stringResource(R.string.privacy_bullet_6)
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
                            text = stringResource(R.string.privacy_data_storage_title),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.lock_icon),
                            title = stringResource(R.string.privacy_feature_no_tracking_title),
                            subtitle = stringResource(R.string.privacy_feature_no_tracking_desc),
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.db_icon),
                            title = stringResource(R.string.privacy_feature_device_storage_title),
                            subtitle = stringResource(R.string.privacy_feature_device_storage_desc),
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.ic_cloud_off),
                            title = stringResource(R.string.privacy_feature_no_cloud_title),
                            subtitle = stringResource(R.string.privacy_feature_no_cloud_desc),
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
                            text = stringResource(R.string.privacy_camera_title),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.baseline_camera_alt_24),
                            title = stringResource(R.string.privacy_feature_camera_scan_title),
                            subtitle = stringResource(R.string.privacy_feature_camera_scan_desc),
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.no_image_ic),
                            title = stringResource(R.string.privacy_feature_no_record_title),
                            subtitle = stringResource(R.string.privacy_feature_no_record_desc),
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
                            text = stringResource(R.string.privacy_network_title),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.db_icon),
                            title = stringResource(R.string.privacy_feature_off_api_title),
                            subtitle = stringResource(R.string.privacy_feature_off_api_desc),
                            primaryTextColor = primaryText,
                            secondaryTextColor = secondaryText
                        )
                        HorizontalDivider(color = dividerColor, thickness = 1.dp)
                        PolicyFeatureRow(
                            icon = painterResource(R.drawable.key_ic),
                            title = stringResource(R.string.privacy_feature_no_id_title),
                            subtitle = stringResource(R.string.privacy_feature_no_id_desc),
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
                                text = stringResource(R.string.privacy_verified_badge),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.privacy_version_date),
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
