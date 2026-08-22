package com.example.satvikscan.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.satvikscan.R

@Composable
fun ScanDetectOnboardingScreen(
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val emeraldGreen = Color(0xFF00C853)
    val darkCharcoal = Color(0xFF0F172A)
    val slateGrey = Color(0xFF64748B)
    val inactiveIndicator = Color(0xFFE5E7EB)

    // Animated WebP / GIF Loader setup using Coil
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ==========================================
        // 1. TOP HERO AREA (Animated WebP Video/Graphic)
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.15f)
                .padding(top = 24.dp, start = 20.dp, end = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Yahan aapka WebP video / animation play hoga
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.food_scan_anim) // res/drawable me aapka webp file name
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Scan Animation",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ==========================================
        // 2. CONTENT AREA (Tagline, Step Indicator, Headline, Description)
        // ==========================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.85f)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Brand Tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SatvikScan : ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = emeraldGreen
                )
                Text(
                    text = "Instant Verification",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = slateGrey
                )
            }

            // 3-Segment Progress Indicator (Step 2 Active)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bar 1 (Inactive)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(inactiveIndicator)
                )
                // Bar 2 (Active Green)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(emeraldGreen)
                )
                // Bar 3 (Inactive)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(inactiveIndicator)
                )
            }

            // Dynamic Headline (Fixed min height container to prevent shift)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = darkCharcoal)) {
                            append("Scan & Detect\n")
                        }
                        withStyle(SpanStyle(color = emeraldGreen)) {
                            append("Hidden Non-Satvik\nItems")
                        }
                    },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }

            // Subtitle Description (Fixed min height container to prevent shift)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Instantly uncover onion, garlic, animal fats, and\ncomplex E-codes by scanning the barcode or\ningredient label.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = slateGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        // ==========================================
        // 3. FIXED BOTTOM CTA BUTTON: "Next ->"
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp)
        ) {
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = emeraldGreen.copy(alpha = 0.5f),
                        ambientColor = emeraldGreen.copy(alpha = 0.3f)
                    ),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = emeraldGreen
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "Next ->",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


