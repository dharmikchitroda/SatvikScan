package com.example.satvikscan.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

// ── Color tokens (matching PrivacyPolicyScreen) ──────────────────────────────
private val Background     = Color(0xFFFAFAFA)
private val CardSurface    = Color(0xFFFFFFFF)
private val PrimaryText    = Color(0xFF1B1C1C)
private val SecondaryText  = Color(0xFF5F6368)
private val MutedText      = Color(0xFF9CA3AF)
private val DividerColor   = Color(0xFFF1F3F4)
private val AccentGreen    = Color(0xFF1B3A2D)
private val LightGreen     = Color(0xFFDCFCE7)

@Composable
fun AboutSatvikScreen(
    onBackClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ) {
        // ── 1. Top App Bar ───────────────────────────────────────────────────
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
                    tint = PrimaryText,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(R.string.about_title),
                fontSize = 20.sp,
                color = PrimaryText,
                textAlign = TextAlign.Center
            )
        }

        // ── Main Scrollable Content ──────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── 2. App Identity ──────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App Logo
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "SatvikScan Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(88.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // App Name
                    Text(
                        text = stringResource(R.string.about_app_name),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Tagline
                    Text(
                        text = stringResource(R.string.about_tagline),
                        fontSize = 13.sp,
                        color = SecondaryText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtitle / App description
                    Text(
                        text = stringResource(R.string.about_subtitle),
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = SecondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // ── 3. Why SatvikScan? (Mission) ────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about_mission_title),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.about_mission_desc),
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = SecondaryText
                        )
                    }
                }
            }

            // ── 4. How It Works ──────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about_how_it_works),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Step 1: Scan
                        AboutStepRow(
                            stepNumber = "1",
                            title = stringResource(R.string.about_step_scan_title),
                            description = stringResource(R.string.about_step_scan_desc)
                        )
                        HorizontalDivider(
                            color = DividerColor,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        // Step 2: Check
                        AboutStepRow(
                            stepNumber = "2",
                            title = stringResource(R.string.about_step_check_title),
                            description = stringResource(R.string.about_step_check_desc)
                        )
                        HorizontalDivider(
                            color = DividerColor,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        // Step 3: Understand
                        AboutStepRow(
                            stepNumber = "3",
                            title = stringResource(R.string.about_step_understand_title),
                            description = stringResource(R.string.about_step_understand_desc)
                        )
                    }
                }
            }

            // ── 5. Supported Diets ───────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about_diets_supported),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        AboutDietRow(
                            iconRes = R.drawable.jain_logo,
                            title = stringResource(R.string.about_diet_jain),
                            description = stringResource(R.string.about_diet_jain_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutDietRow(
                            iconRes = R.drawable.swaminarayan_tilak,
                            title = stringResource(R.string.about_diet_swami),
                            description = stringResource(R.string.about_diet_swami_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutDietRow(
                            iconRes = R.drawable.ic_carrot_1,
                            title = stringResource(R.string.about_diet_vegan),
                            description = stringResource(R.string.about_diet_vegan_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutDietRow(
                            iconRes = R.drawable.custom_icon,
                            title = stringResource(R.string.about_diet_custom),
                            description = stringResource(R.string.about_diet_custom_desc)
                        )
                    }
                }
            }

            // ── 6. Key Features ──────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about_key_features),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        AboutFeatureRow(
                            iconRes = R.drawable.baseline_qr_code_scanner_24,
                            title = stringResource(R.string.about_feature_barcode),
                            description = stringResource(R.string.about_feature_barcode_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutFeatureRow(
                            iconRes = R.drawable.baseline_camera_alt_24,
                            title = stringResource(R.string.about_feature_capture_discover),
                            description = stringResource(R.string.about_feature_capture_discover_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutFeatureRow(
                            iconRes = R.drawable.ic_chemical,
                            title = stringResource(R.string.about_feature_e_number),
                            description = stringResource(R.string.about_feature_e_number_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutFeatureRow(
                            iconRes = R.drawable.ic_diet_guidelines,
                            title = stringResource(R.string.about_feature_diet_guidelines),
                            description = stringResource(R.string.about_feature_diet_guidelines_desc)
                        )
                        HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        AboutFeatureRow(
                            iconRes = R.drawable.ic_language,
                            title = stringResource(R.string.about_feature_multilingual),
                            description = stringResource(R.string.about_feature_multilingual_desc)
                        )
                    }
                }
            }

            // ── 7. Footer ───────────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App name
                    Text(
                        text = stringResource(R.string.about_app_name),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Version
                    Text(
                        text = stringResource(R.string.about_version),
                        fontSize = 11.sp,
                        color = MutedText
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Privacy Policy | Contact Support
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.about_privacy_policy),
                            fontSize = 12.sp,
                            color = SecondaryText,
                            modifier = Modifier.clickable { onPrivacyPolicyClick() }
                        )
                        Text(
                            text = "|",
                            fontSize = 12.sp,
                            color = DividerColor
                        )
                        Text(
                            text = stringResource(R.string.about_contact_support),
                            fontSize = 12.sp,
                            color = SecondaryText,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:support.satvikscan@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "SatvikScan Feedback & Support")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    // fallback
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Made with ♥
                    Text(
                        text = stringResource(R.string.about_footer),
                        fontSize = 11.sp,
                        color = MutedText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// ── Helper Composables ───────────────────────────────────────────────────────

/**
 * A single "How It Works" step row with numbered circle, title and description.
 */
@Composable
private fun AboutStepRow(
    stepNumber: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Step number circle
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(LightGreen, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGreen
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = SecondaryText
            )
        }
    }
}

/**
 * A single diet row with icon, title and short description.
 */
@Composable
private fun AboutDietRow(
    iconRes: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = AccentGreen,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                color = SecondaryText
            )
        }
    }
}

/**
 * A single feature row with icon, title and short description.
 * Matches PrivacyPolicyScreen's PolicyFeatureRow pattern.
 */
@Composable
private fun AboutFeatureRow(
    iconRes: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = PrimaryText,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = SecondaryText
            )
        }
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AboutSatvikScreenPreview() {
    AboutSatvikScreen()
}
