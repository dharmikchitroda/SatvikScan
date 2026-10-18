package com.example.satvikscan.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

@Composable
fun Splash1(
    onLetsBeginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF00C853)   
    val satvikGreen = Color(0xFF1B3A2D)    
    val darkCharcoal = Color(0xFF0F172A)
    val slateGrey = Color(0xFF64748B)
    val inactiveIndicator = Color(0xFFE5E7EB)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
         
        // Main Image
         
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

            // Smooth Gradient Overlay at Bottom of Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFF7F7F7).copy(alpha = 0.7f),
                                Color(0xFFF7F7F7)
                            )
                        )
                    )
            )
        }

         
        // Content Section
         
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.85f)
                .padding(horizontal = 24.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                        color = primaryGreen
                    )
                    Text(
                        text = "Simplify your diet",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = slateGrey
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Segment Progress Indicator (Step 1 Active)
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
                            .background(satvikGreen)
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
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Headline with Color Highlights
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = darkCharcoal)) {
                                append("Protect Your ")
                            }
                            withStyle(SpanStyle(color = primaryGreen)) {
                                append("Religious\n")
                            }
                            withStyle(SpanStyle(color = darkCharcoal)) {
                                append("Values\n")
                            }
                            withStyle(SpanStyle(color = darkCharcoal)) {
                                append("Eat ")
                            }
                            withStyle(SpanStyle(color = primaryGreen)) {
                                append("Pure Satvik")
                            }
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )
                }

                // Subtitle Description
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Scan food packets instantly based on\nSwaminarayan, Jain & Custom Satvik dietary principles.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = slateGrey,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }

         
        // Let's Begin Btn
         
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp)
        ) {
            Button(
                onClick = onLetsBeginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = satvikGreen.copy(alpha = 0.5f),
                        ambientColor = satvikGreen.copy(alpha = 0.3f)
                    ),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = satvikGreen
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
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