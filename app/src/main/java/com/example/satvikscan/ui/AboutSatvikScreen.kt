package com.example.satvikscan.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

@OptIn(ExperimentalLayoutApi::class)
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
            .background(Color(0xFFFDFDFD)) // Soft off-white surface
            .statusBarsPadding()
    ) {
        //  Top Bar (Centered Title) 
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
                    tint = Color(0xFF1B3A2D)
                )
            }

            Text(
                text = stringResource(R.string.about_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D),
                textAlign = TextAlign.Center
            )
        }

        //  Main Scrollable Content 
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //  1. Header Section 
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFBEF3CE), shape = RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: change orignal logo
                Icon(
                    painter = painterResource(id = R.drawable.leaf_svg),
                    contentDescription = "SatvikScan Logo",
                    tint = Color(0xFF1B3A2D),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // App Name (SatvikScan)
            Text(
                text = stringResource(R.string.about_app_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tagline
            Text(
                text = stringResource(R.string.about_tagline),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1B3A2D),
                textAlign = TextAlign.Center
            )

            // Sanskrit Quote
            Text(
                text = stringResource(R.string.about_sanskrit_quote),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF404943),
                modifier = Modifier.padding(top = 8.dp)
            )


            Spacer(modifier = Modifier.height(24.dp))

            //  2. Mission Card (Single Surface) 
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF1B3A2D), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Mission",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(R.string.about_mission_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B3A2D)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mission Text
                    Text(
                        text = stringResource(R.string.about_mission_desc),
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = Color(0xFF1B1C1C)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Focus Note
                    val focusLabel = stringResource(R.string.about_focus_label)
                    val focusDesc = stringResource(R.string.about_focus_desc)
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF404943)
                                )
                            ) {
                                append(focusLabel)
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF404943)
                                )
                            ) {
                                append(focusDesc)
                            }
                        },
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  3. Satvik Food Info Cards (Vertical List) 
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.about_what_is_satvik),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3A2D)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Item 1: Sattva
                SatvikInfoCard(
                    R.drawable.leaf_svg,
                    heading = stringResource(R.string.about_sattva_title),
                    description = stringResource(R.string.about_sattva_desc)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Item 2: No Onion & Garlic
                SatvikInfoCard(
                    iconRes = R.drawable.ic_onion,
                    heading = stringResource(R.string.about_no_onion_garlic_title),
                    description = stringResource(R.string.about_no_onion_garlic_desc)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Item 3: Hidden Non-Veg Free
                SatvikInfoCard(
                    iconRes = R.drawable.ic_chemical,
                    heading = stringResource(R.string.about_hidden_nonveg_title),
                    description = stringResource(R.string.about_hidden_nonveg_desc)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  4. Diets Supported Section 
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.about_diets_supported),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3A2D)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DietGridCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.custom_icon,
                        text = stringResource(R.string.diet_custom_title)
                    )
                    DietGridCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.jain_logo,
                        text = stringResource(R.string.diet_jain_title)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DietGridCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.swaminarayan_tilak,
                        text = stringResource(R.string.diet_swami_title)
                    )
                    DietGridCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_carrot_1,
                        text = stringResource(R.string.about_diet_vegan)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  5. Key Features Grid (2x2) 
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.about_key_features),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3A2D)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KeyFeatureCell(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.baseline_qr_code_scanner_24,
                        label = stringResource(R.string.about_feature_instant_scanner)
                    )
                    KeyFeatureCell(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_chemical,
                        label = stringResource(R.string.about_feature_e_number)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KeyFeatureCell(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_diet_guidelines,
                        label = stringResource(R.string.about_feature_diet_gyan)
                    )
                    KeyFeatureCell(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_safe_tick,
                        label = stringResource(R.string.about_feature_privacy)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            //  6. Footer & Actions 
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.about_version),
                    fontSize = 11.sp,
                    color = Color(0xFF404943)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.about_made_with),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B3A2D)
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Love",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = stringResource(R.string.about_made_with_suffix),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B3A2D)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.about_privacy_policy),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF404943),
                        modifier = Modifier.clickable { onPrivacyPolicyClick() }
                    )

                    Text(
                        text = "|",
                        fontSize = 12.sp,
                        color = Color(0xFFD1D5DB)
                    )

                    Text(
                        text = stringResource(R.string.about_contact_support),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF404943),
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:support.satvikscan@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "SatvikScan Feedback & Support")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // fallback
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rate Us Button (fillMaxWidth, height 56.dp, backgroundColor #1b3a2d, full rounded)
                Button(
                    onClick = {
                        val playStoreIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=${context.packageName}")
                        )
                        try {
                            context.startActivity(playStoreIntent)
                        } catch (e: Exception) {
                            val webIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                            )
                            context.startActivity(webIntent)
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3A2D)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.about_rate_us),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Bottom Margin (16.dp gesture bar avoidance)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// 3. Satvik Food Info Cards (Vertical List)
@Composable
private fun SatvikInfoCard(
    iconRes: Int,
    heading: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEFFDF5))
            .border(1.dp, Color(0xFFD1FAE5), RoundedCornerShape(20.dp))
    ) {
        // Left Curved Accent Strip / Bar
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(68.dp)
                .align(Alignment.CenterStart)
                .background(
                    Color(0xFF38684B),
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = heading,
                tint = Color(0xFF1B3A2D),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = heading,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B1C1C)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color(0xFF404943),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// 4. Diet Grid Card (2x2 Grid with Icon Circle & Checkmark)
@Composable
private fun DietGridCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE8F7EA),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD0E7D8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = text,
                    tint = Color(0xFF1B3A2D),
                    modifier = Modifier.size(18.dp)
                )


            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color(0xFF1B3A2D),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// 5. Key Features Grid Cell (2x2)
@Composable
private fun KeyFeatureCell(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = Color(0xFF1B3A2D),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D),
                textAlign = TextAlign.Center
            )
        }
    }
}
