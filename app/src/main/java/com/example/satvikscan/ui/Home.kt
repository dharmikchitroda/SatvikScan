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
import androidx.compose.ui.res.painterResource
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
    val title: String,
    val body: String,
    val bgColor: Color,
    val accentColor: Color
)

fun getQuickTips(dietName: String): List<QuickTip> {
    val commonTips = listOf(
        QuickTip(
            emoji = "🔍",
            title = "Labels Padhna Zaroori Hai",
            body = "Ingredients list me highest quantity wala item sabse pehle hota hai. E-codes dhyan se check karo!",
            bgColor = Color(0xFFE8F5E9),
            accentColor = Color(0xFF2E7D32)
        ),
        QuickTip(
            emoji = "📦",
            title = "Barcode Scan Better Hai",
            body = "Packaged goods ke liye barcode scan photo scan se zyada accurate results deta hai.",
            bgColor = Color(0xFFE3F2FD),
            accentColor = Color(0xFF1565C0)
        ),
        QuickTip(
            emoji = "⚠️",
            title = "\"Natural Flavors\" Alert",
            body = "\"Natural flavors\" mein kabhi kabhi animal-derived sources hote hain. Hamesha verify karo!",
            bgColor = Color(0xFFFFF8E1),
            accentColor = Color(0xFFF57F17)
        ),
        QuickTip(
            emoji = "🌿",
            title = "Satvik Food Kya Hai?",
            body = "Fresh, whole aur minimally processed foods hamesha safest choice hoti hain.",
            bgColor = Color(0xFFE8F5E9),
            accentColor = Color(0xFF388E3C)
        )
    )

    val dietSpecific = when {
        dietName.contains("Jain", ignoreCase = true) -> listOf(
            QuickTip(
                emoji = "🥕",
                title = "Root Vegetables Alert",
                body = "Aalu, Pyaz, Lehsun, Gajar — Jain diet mein avoid kiye jaate hain. Ingredients mein dhundo!",
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFC62828)
            ),
            QuickTip(
                emoji = "🍯",
                title = "Honey in Products",
                body = "Kaafi energy bars aur sweets mein honey hoti hai. Jain tradition mein yeh Mahavigai hai.",
                bgColor = Color(0xFFFFF3E0),
                accentColor = Color(0xFFE65100)
            ),
            QuickTip(
                emoji = "🧀",
                title = "Cheese ka Source",
                body = "Kuch cheese animal-derived rennet se banti hai. Source verify karo consuming se pehle.",
                bgColor = Color(0xFFF3E5F5),
                accentColor = Color(0xFF6A1B9A)
            ),
            QuickTip(
                emoji = "🌾",
                title = "E-Codes Dhyan Rakhna",
                body = "E120 (Carmine), E441 (Gelatin), E901 (Beeswax) — sab animal-derived hain. Avoid karo!",
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFB71C1C)
            )
        )
        dietName.contains("Swaminarayan", ignoreCase = true) -> listOf(
            QuickTip(
                emoji = "🧅",
                title = "Pyaz & Lehsun Check Karo",
                body = "Allium family strictly avoid hai. Spice blends mein garlic powder ya onion extract dhundo.",
                bgColor = Color(0xFFFCE4EC),
                accentColor = Color(0xFFC62828)
            ),
            QuickTip(
                emoji = "🍺",
                title = "Food mein Alcohol?",
                body = "Kuch sauces, vinegars aur bakery items mein alcohol hota hai. Scan karo pehle!",
                bgColor = Color(0xFFFFF3E0),
                accentColor = Color(0xFFBF360C)
            ),
            QuickTip(
                emoji = "🌾",
                title = "Hing ki Purity",
                body = "Commercial Hing mein aksar wheat flour milta hai. Use karne se pehle purity verify karo.",
                bgColor = Color(0xFFE8EAF6),
                accentColor = Color(0xFF283593)
            ),
            QuickTip(
                emoji = "🥦",
                title = "Shikshapatri Guideline",
                body = "Meat, eggs, alcohol aur narcotics — Shikshapatri ke Verse 15 & 18 ke anusar avoid karo.",
                bgColor = Color(0xFFE8F5E9),
                accentColor = Color(0xFF1B5E20)
            )
        )
        else -> listOf(
            QuickTip(
                emoji = "🥦",
                title = "Plant-Based Raho",
                body = "Packaged foods mein dairy aur meat derivatives ke plant-based alternatives dhundo.",
                bgColor = Color(0xFFE8F5E9),
                accentColor = Color(0xFF1B5E20)
            ),
            QuickTip(
                emoji = "💊",
                title = "Avoid E-Codes",
                body = "E120 (Carmine), E441 (Gelatin), E901 (Beeswax) — sab animal-derived. Watch out!",
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
            alpha = 0.15f
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
                // Capture & Discover Card (Photo Scan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(148.dp)
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
                        painter = painterResource(id = R.drawable.capture_discover_banner),
                        contentDescription = "Capture & Discover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Scan & Discover Card (Barcode Scan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(148.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onScanBarcodescan() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.scan_discover_banner),
                        contentDescription = "Scan & Discover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Quick Tips Section ──
            QuickTipsSection(dietName = dietName)

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom text
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
                text = "💡 Did You Know?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Swipe →",
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
                text = tip.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = tip.accentColor,
                lineHeight = 17.sp
            )
            Text(
                text = tip.body,
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

    val sliderImages = listOf(
        R.drawable.banner,
        R.drawable.banner_2,
        R.drawable.banner_3
    )

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
fun FooterText() {
    // Bottom Tagline Section
    Text(
        text = "Purity in Every Bite\nThat's SatvikScan! \uD83C\uDF43",
        fontSize = 34.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFFA2A2A2),
        textAlign = TextAlign.Center,
        lineHeight = 42.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    )
}


