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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

@Composable
fun Splash3(
    onUnderstandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDataDisclaimerChecked by remember { mutableStateOf(false) }
    var isTermsChecked by remember { mutableStateOf(false) }

    // bothe are Enable so that btn Clickable or not
    val isButtonEnabled = isDataDisclaimerChecked && isTermsChecked

    val primaryGreen = Color(0xFF00C853)   
    val satvikGreen = Color(0xFF1B3A2D)   
    val darkCharcoal = Color(0xFF0F172A)
    val slateGrey = Color(0xFF64748B)
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.15f)
                .padding(top = 24.dp, start = 20.dp, end = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.spash3image),
                contentDescription = "Scan Result Mockup Preview",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

         
        // content section
         
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
                        text = "Read & Acknowledge",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = slateGrey
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3-Segment Progress Indicator (Step 3 Active)
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
                            .background(satvikGreen)
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

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = darkCharcoal)) {
                            append("Please ")
                        }
                        withStyle(SpanStyle(color = primaryGreen)) {
                            append("Read ")
                        }
                        withStyle(SpanStyle(color = darkCharcoal)) {
                            append("& ")
                        }
                        withStyle(SpanStyle(color = primaryGreen)) {
                            append("Acknowledge")
                        }
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

            // CheckBox Cards Container
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // CHECKBOX CARD 1
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { isDataDisclaimerChecked = !isDataDisclaimerChecked },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDataDisclaimerChecked) Color(0xFFF6FEF9) else cardBgColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.2.dp,
                        if (isDataDisclaimerChecked) satvikGreen.copy(alpha = 0.7f) else cardBorderColor
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
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
                                        checkedColor = satvikGreen,
                                        checkmarkColor = Color.White,
                                        uncheckedColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Our Verified List & 100% Accuracy Goal",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkCharcoal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("Currently, results combine public food data (Open Food Facts) with our own ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = satvikGreen)) {
                                    append("Satvik verified list")
                                }
                                append(". We are manually checking products daily, and with your support, we will reach ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                    append("100% accuracy")
                                }
                                append("! For complete certainty, please verify via our ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                    append("Capture & Discover")
                                }
                                append(" feature or physical packaging.")
                            },
                            fontSize = 11.5.sp,
                            color = bodyGrey,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(start = 32.dp, end = 4.dp)
                        )
                    }
                }

                // CHECKBOX CARD 2
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { isTermsChecked = !isTermsChecked },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isTermsChecked) Color(0xFFF6FEF9) else cardBgColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.2.dp,
                        if (isTermsChecked) satvikGreen.copy(alpha = 0.7f) else cardBorderColor
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
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
                                        checkedColor = satvikGreen,
                                        checkmarkColor = Color.White,
                                        uncheckedColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Helpful Guidance & Terms",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkCharcoal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("I agree to the ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = darkCharcoal)) {
                                    append("Terms of Service & Privacy Policy")
                                }
                                append(". SatvikScan is made to help and guide you. Because brands sometimes change ingredients without notice, the final eating decision is your personal choice.")
                            },
                            fontSize = 11.5.sp,
                            color = bodyGrey,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(start = 32.dp, end = 4.dp)
                        )
                    }
                }
            }
        }
    }

         
        // I UNDERSTAND Btn 
         
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
            ){
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
