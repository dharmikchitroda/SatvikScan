package com.example.satvikscan.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

// Data model for a tip card
data class QuickTip(
    val emoji: String,
    val titleRes: Int,
    val bodyRes: Int,
    val bgColor: Color,
    val accentColor: Color
)

fun getQuickTips(dietName: String): List<QuickTip> {
    val commonTips = listOf(
        QuickTip(
            emoji = "🔍",
            titleRes = R.string.tip_read_labels_title,
            bodyRes = R.string.tip_read_labels_body,
            bgColor = Color(0xFFE8F5E9),
            accentColor = Color(0xFF2E7D32)
        ),
        QuickTip(
            emoji = "📦",
            titleRes = R.string.tip_barcode_accuracy_title,
            bodyRes = R.string.tip_barcode_accuracy_body,
            bgColor = Color(0xFFE3F2FD),
            accentColor = Color(0xFF1565C0)
        ),
        QuickTip(
            emoji = "⚠️",
            titleRes = R.string.tip_natural_flavors_title,
            bodyRes = R.string.tip_natural_flavors_body,
            bgColor = Color(0xFFFFF8E1),
            accentColor = Color(0xFFF57F17)
        ),
        QuickTip(
            emoji = "🌿",
            titleRes = R.string.tip_satvik_food_title,
            bodyRes = R.string.tip_satvik_food_body,
            bgColor = Color(0xFFE8F5E9),
            accentColor = Color(0xFF388E3C)
        )
    )

    val dietSpecific = when {
        dietName.contains("Jain", ignoreCase = true) -> listOf(
            QuickTip(
                emoji = "🥕",
                titleRes = R.string.tip_jain_roots_title,
                bodyRes = R.string.tip_jain_roots_body,
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFC62828)
            ),
            QuickTip(
                emoji = "🍯",
                titleRes = R.string.tip_jain_honey_title,
                bodyRes = R.string.tip_jain_honey_body,
                bgColor = Color(0xFFFFF3E0),
                accentColor = Color(0xFFE65100)
            ),
            QuickTip(
                emoji = "🧀",
                titleRes = R.string.tip_jain_cheese_title,
                bodyRes = R.string.tip_jain_cheese_body,
                bgColor = Color(0xFFF3E5F5),
                accentColor = Color(0xFF6A1B9A)
            ),
            QuickTip(
                emoji = "🌾",
                titleRes = R.string.tip_jain_ecodes_title,
                bodyRes = R.string.tip_jain_ecodes_body,
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFB71C1C)
            )
        )
        dietName.contains("Swaminarayan", ignoreCase = true) -> listOf(
            QuickTip(
                emoji = "🍺",
                titleRes = R.string.tip_swami_alcohol_title,
                bodyRes = R.string.tip_swami_alcohol_body,
                bgColor = Color(0xFFFFF3E0),
                accentColor = Color(0xFFBF360C)
            ),
            QuickTip(
                emoji = "🧅",
                titleRes = R.string.tip_swami_onion_garlic_title,
                bodyRes = R.string.tip_swami_onion_garlic_body,
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFC62828)
            ),
            QuickTip(
                emoji = "🌾",
                titleRes = R.string.tip_swami_hing_title,
                bodyRes = R.string.tip_swami_hing_body,
                bgColor = Color(0xFFE8EAF6),
                accentColor = Color(0xFF283593)
            ),
            QuickTip(
                emoji = "🥦",
                titleRes = R.string.tip_swami_shikshapatri_title,
                bodyRes = R.string.tip_swami_shikshapatri_body,
                bgColor = Color(0xFFE8F5E9),
                accentColor = Color(0xFF1B5E20)
            )
        )
        else -> listOf(
            QuickTip(
                emoji = "🥦",
                titleRes = R.string.tip_plant_based_title,
                bodyRes = R.string.tip_plant_based_body,
                bgColor = Color(0xFFE8F5E9),
                accentColor = Color(0xFF1B5E20)
            ),
            QuickTip(
                emoji = "💊",
                titleRes = R.string.tip_harmful_ecodes_title,
                bodyRes = R.string.tip_harmful_ecodes_body,
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFB71C1C)
            )
        )
    }

    return dietSpecific + commonTips
}



@Composable
fun HomeScreenUI(
    paddingValues: PaddingValues,
    onScanPhotoclick: () -> Unit,
    onScanBarcodescan: () -> Unit,
    dietName: String = ""
) {
    val currentLang = LocalConfiguration.current.locales[0]?.language ?: "en"

    val scanBannerRes = when (currentLang) {
        "gu" -> R.drawable.scan_gu
        "hi" -> R.drawable.scan_hi
        else -> R.drawable.scan_discover_banner
    }

    val captureBannerRes = when (currentLang) {
        "gu" -> R.drawable.capture_gu
        "hi" -> R.drawable.capture_hi
        else -> R.drawable.capture_discover_banner
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F9))
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroud_photo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.18f
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            //  Image Slider
            ImageSlider()

            Spacer(modifier = Modifier.height(20.dp))

            // Two cards in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Scan & Discover Card (Barcode Scan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            onScanBarcodescan()
                        }
                ) {
                    Image(
                        painter = painterResource(id = scanBannerRes),
                        contentDescription = "Scan & Discover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Capture & Discover Card (Photo Scan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onScanPhotoclick() }
                ) {
                    Image(
                        painter = painterResource(id = captureBannerRes),
                        contentDescription = "Capture & Discover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Quick Tips Section ──
            QuickTipsSection(dietName = dietName)

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom text — full width clean lines
            FooterText()
        }
    }
}


// ─── Quick Tips Section ───────────────────────────────────────────────────────

@Composable
fun QuickTipsSection(dietName: String) {
    val tips = getQuickTips(dietName)

    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_did_you_know),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.home_swipe_hint),
                fontSize = 11.sp,
                color = Color(0xFF1B3A2D).copy(alpha = 0.45f),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal scrollable tip cards
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tips) { tip ->
                TipCard(tip = tip)
            }
        }
    }
}

@Composable
fun TipCard(tip: QuickTip) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(tip.bgColor)
            .border(
                width = 1.dp,
                color = tip.accentColor.copy(alpha = 0.18f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            // Emoji badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(tip.accentColor.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = tip.emoji, fontSize = 20.sp)
            }

            Text(
                text = stringResource(tip.titleRes),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = tip.accentColor,
                lineHeight = 17.sp
            )
            Text(
                text = stringResource(tip.bodyRes),
                fontSize = 11.5.sp,
                color = Color(0xFF424242),
                lineHeight = 16.sp
            )
        }
    }
}


//  Image Slider 
@Composable
fun ImageSlider() {
    val currentLang = LocalConfiguration.current.locales[0]?.language ?: "en"

    val sliderImages = when (currentLang) {
        "gu" -> listOf(
            R.drawable.guj1,
            R.drawable.guj2,
            R.drawable.guj3
        )
        "hi" -> listOf(
            R.drawable.hi1,
            R.drawable.hi2,
            R.drawable.hi3
        )
        else -> listOf(
            R.drawable.banner,
            R.drawable.banner_2,
            R.drawable.banner_3
        )
    }

    val pagerState = rememberPagerState(pageCount = { sliderImages.size })

    // Auto-scroll every 5 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000L)
            val nextPage = (pagerState.currentPage + 1) % sliderImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 12.dp
        ) { page ->
            // Calculate page offset for animation
            val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            ).absoluteValue

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Scale effect: current page is full size, neighbours are slightly smaller
                        val scale = 1f - (pageOffset * 0.1f).coerceIn(0f, 0.1f)
                        scaleX = scale
                        scaleY = scale
                        // Fade effect
                        alpha = 1f - (pageOffset * 0.3f).coerceIn(0f, 0.3f)
                    }
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = sliderImages[page]),
                    contentDescription = "Slider image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Dot Indicators — overlaid on bottom of image
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(sliderImages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 7.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}

@Composable
fun FooterText(modifier: Modifier = Modifier) {
    // Bottom Tagline Section — two separate full-width text lines aligned to start
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.home_footer_tagline_1),
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFA2A2A2),
            textAlign = TextAlign.Start,
            lineHeight = 40.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.home_footer_tagline_2),
            fontSize = 35.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFA2A2A2),
            textAlign = TextAlign.Start,
            lineHeight = 40.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
