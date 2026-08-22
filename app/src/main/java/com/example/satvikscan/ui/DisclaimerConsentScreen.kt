package com.example.satvikscan.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
fun DisclaimerConsentScreen(
    onUnderstandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDataDisclaimerChecked by remember { mutableStateOf(false) }
    var isTermsChecked by remember { mutableStateOf(false) }

    val isButtonEnabled = isDataDisclaimerChecked && isTermsChecked

    val emeraldGreen = Color(0xFF00C853)
    val darkCharcoal = Color(0xFF0F172A)
    val bodyGrey = Color(0xFF475569)
    val inactiveIndicator = Color(0xFFE5E7EB)
    val cardBgColor = Color.White
    val cardBorderColor = Color(0xFFE2E8F0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ==========================================
        // 1. TOP HERO IMAGE (spash3image.png)
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .padding(top = 16.dp, start = 20.dp, end = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.spash3image),
                contentDescription = "Scan Result Mockup Preview",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ==========================================
        // 2. CONTENT AREA (Indicator, Title, Checkbox Cards)
        // ==========================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.15f)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 3-Segment Progress Indicator (Step 3 Active)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                // Bar 1 (Inactive)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(inactiveIndicator)
                )
                // Bar 2 (Inactive)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(inactiveIndicator)
                )
                // Bar 3 (Active Green)
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(emeraldGreen)
                )
            }

            // Headline Text: "Please Read & Acknowledge"
            Text(
                text = "Please Read & Acknowledge",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = darkCharcoal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // CHECKBOX CARD 1: Public Database & Manual Scan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { isDataDisclaimerChecked = !isDataDisclaimerChecked },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDataDisclaimerChecked) Color(0xFFF6FEF9) else cardBgColor
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.2.dp,
                    if (isDataDisclaimerChecked) emeraldGreen.copy(alpha = 0.7f) else cardBorderColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isDataDisclaimerChecked,
                                onCheckedChange = { isDataDisclaimerChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = emeraldGreen,
                                    checkmarkColor = Color.White,
                                    uncheckedColor = Color(0xFF94A3B8)
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Public Database & Manual Scan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = darkCharcoal
                            )
                        }

                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("Barcode data is sourced from Open Food Facts (a community database). For 100% accuracy, please verify via our ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                append("Capure & Discover")
                            }
                            append(" or physical packaging.")
                        },
                        fontSize = 12.sp,
                        color = bodyGrey,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(start = 34.dp, end = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // CHECKBOX CARD 2: Dietary Responsibility & Terms
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { isTermsChecked = !isTermsChecked },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTermsChecked) Color(0xFFF6FEF9) else cardBgColor
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.2.dp,
                    if (isTermsChecked) emeraldGreen.copy(alpha = 0.7f) else cardBorderColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isTermsChecked,
                                onCheckedChange = { isTermsChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = emeraldGreen,
                                    checkmarkColor = Color.White,
                                    uncheckedColor = Color(0xFF94A3B8)
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Dietary Responsibility & Terms",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = darkCharcoal
                            )
                        }

                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("You agree to the ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                append("Terms of Service & Privacy Policy")
                            }
                            append(". SatvikScan is for guidance only – final dietary decisions are your personal responsibility. The app is not liable for manufacturer recipe changes or religious differences.")
                        },
                        fontSize = 12.sp,
                        color = bodyGrey,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(start = 34.dp, end = 4.dp)
                    )
                }
            }
        }

        // ==========================================
        // 3. FIXED BOTTOM CTA BUTTON: "I UNDERSTAND"
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp)
        ) {
            Button(
                onClick = onUnderstandClick,
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .then(
                        if (isButtonEnabled) {
                            Modifier.shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = emeraldGreen.copy(alpha = 0.5f),
                                ambientColor = emeraldGreen.copy(alpha = 0.3f)
                            )
                        } else Modifier
                    ),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = emeraldGreen,
                    disabledContainerColor = Color(0xFFE2E8F0)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "I UNDERSTAND",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isButtonEnabled) Color.White else Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DisclaimerConsentScreenPreview() {
    DisclaimerConsentScreen(onUnderstandClick = {})
}
