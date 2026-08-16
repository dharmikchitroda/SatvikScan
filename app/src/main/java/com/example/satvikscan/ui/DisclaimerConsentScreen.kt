package com.example.satvikscan.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
    val darkCharcoal = Color(0xFF1E293B)
    val bodyGrey = Color(0xFF475569)
    val cardBgColor = Color(0xFFFAFBFC)
    val cardBorderColor = Color(0xFFE2E8F0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==========================================
            // 1. TOP HERO IMAGE (spash3image.png)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.spash3image),
                    contentDescription = "Scan Result Mockup Preview",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 2. CHECKBOX CARD 1: Public Database & Manual Scan
            // ==========================================
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
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    // Header Row: Checkbox + Title + Info Icon
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
                                    checkedColor = Color(0xFF1E293B),
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

                    Spacer(modifier = Modifier.height(6.dp))

                    // Description text with bold highlights
                    Text(
                        text = buildAnnotatedString {
                            append("Barcode data is sourced from Open Food Facts (a community database). For 100% accuracy, please verify via our ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                append("Capture & Discover")
                            }
                            append(" or physical packaging.")
                        },
                        fontSize = 12.5.sp,
                        color = bodyGrey,
                        lineHeight = 17.5.sp,
                        modifier = Modifier.padding(start = 34.dp, end = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 3. CHECKBOX CARD 2: Dietary Responsibility & Terms
            // ==========================================
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
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    // Header Row: Checkbox + Title + Info Icon
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
                                    checkedColor = Color(0xFF1E293B),
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

                    Spacer(modifier = Modifier.height(6.dp))

                    // Description text with bold highlights
                    Text(
                        text = buildAnnotatedString {
                            append("You agree to the ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                append("Terms of Service & Privacy Policy")
                            }
                            append(". SatvikScan is for guidance only – final dietary decisions are your personal responsibility. The app is not liable for manufacturer recipe changes or religious differences.")
                        },
                        fontSize = 12.5.sp,
                        color = bodyGrey,
                        lineHeight = 17.5.sp,
                        modifier = Modifier.padding(start = 34.dp, end = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ==========================================
        // 4. BOTTOM ACTION CTA BUTTON: "I UNDERSTAND"
        // ==========================================
        Button(
            onClick = onUnderstandClick,
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = emeraldGreen,
                disabledContainerColor = Color(0xFFE2E8F0)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = if (isButtonEnabled) 4.dp else 0.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "I UNDERSTAND",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isButtonEnabled) Color.White else Color(0xFF94A3B8)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DisclaimerConsentScreenPreview() {
    DisclaimerConsentScreen(onUnderstandClick = {})
}
