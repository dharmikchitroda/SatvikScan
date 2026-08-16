package com.example.satvikscan.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

@Composable
fun ReligiousOnboardingScreen(
    onLetsBeginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emeraldGreen = Color(0xFF00C853)
    val darkCharcoal = Color(0xFF0F172A)
    val slateGrey = Color(0xFF64748B)
    val inactiveIndicator = Color(0xFFE5E7EB)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ==========================================
        // 1. TOP HERO IMAGE WITH SMOOTH GRADIENT FADE
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.15f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.spashiamge2),
                contentDescription = "Religious Onboarding Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Smooth White Gradient Overlay at Bottom of Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.7f),
                                Color.White
                            )
                        )
                    )
            )
        }

        // ==========================================
        // 2. CONTENT SECTION (Tagline, Step Indicator, Headline, CTA)
        // ==========================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.95f)
                .padding(horizontal = 24.dp, vertical = 16.dp),
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
                    text = "Simplify your diet",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = slateGrey
                )
            }

            // 3-Segment Progress Indicator (Step 1 Active)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bar 1 (Active)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(emeraldGreen)
                )
                // Bar 2 (Inactive)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(inactiveIndicator)
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

            // Headline with Color Highlights
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = darkCharcoal)) {
                        append("Protect Your\n")
                    }
                    withStyle(SpanStyle(color = emeraldGreen)) {
                        append("Religious ")
                    }
                    withStyle(SpanStyle(color = darkCharcoal)) {
                        append("Values\n")
                    }
                    withStyle(SpanStyle(color = darkCharcoal)) {
                        append("Eat ")
                    }
                    withStyle(SpanStyle(color = emeraldGreen)) {
                        append("Pure Satvik")
                    }
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            // Subtitle Description
            Text(
                text = "Scan packaged foods instantly based on\nSwaminarayan, Jain & Pure Satvik dietary principles.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = slateGrey,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            // Full-Width CTA Button: "Let's Begin"
            Button(
                onClick = onLetsBeginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = emeraldGreen
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 1.dp
                )
            ) {
                Text(
                    text = "Let’s Begin",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReligiousOnboardingScreenPreview() {
    ReligiousOnboardingScreen(onLetsBeginClick = {})
}
