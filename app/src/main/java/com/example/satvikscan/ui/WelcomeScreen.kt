package com.example.satvikscan.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R


@Composable

fun WelcomeScreen(
    onContinueClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDiet by remember { mutableStateOf("Swaminarayan") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // light grey/off-white background
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper Column for Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_diet_guidelines),
                    contentDescription = "SatvikScan Logo",
                    tint = Color(0xFF1B3A2D), // SatvikGreen
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SATVIKSCAN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B3A2D),
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Title Header
            Text(
                text = "Welcome to\nSatvikScan",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F1F1F),
                textAlign = TextAlign.Center,
                lineHeight = 44.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "Choose your dietary path to get started.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Select Your Diet label
            Text(
                text = "SELECT YOUR DIET",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7E8B85), // Lighter green/gray
                letterSpacing = 0.8.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Diet Selection Options List
            Column(
                verticalArrangement = Arrangement.spacedBy(17.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Swaminarayan Option
                DietOptionCard(
                    title = "Swaminarayan",
                    subtitle = "No onion, garlic, or eggs.",
                    iconRes = R.drawable.swaminarayan_tilak,
                    iconBgColor = Color(0xFFEFEBE9),
                    iconTint = Color(0xFF5D4037),
                    onClick = { selectedDiet = "Swaminarayan" },
                    selected = selectedDiet == "Swaminarayan"
                )

                // 2. Jain Option
                DietOptionCard(
                    title = "Jain",
                    subtitle = "No root vegetables or honey.",
                    iconRes = R.drawable.ic_jain,
                    iconBgColor = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF2E7D32),
                    selected = selectedDiet == "Jain",
                    onClick = { selectedDiet = "Jain" }
                )

                // 3. Custom Diet Option
                DietOptionCard(
                    title = "Custom Diet",
                    subtitle = "Choose specific ingredients you want to avoid.",
                    iconRes = R.drawable.ic_custom_diet,
                    iconBgColor = Color(0xFFECEFF1),
                    iconTint = Color(0xFF455A64),
                    selected = selectedDiet == "Custom",
                    onClick = {
                        selectedDiet = "Custom"
                        onContinueClick("Custom")
                    }
                )
            }
        }

        // Bottom Action Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Button(
                onClick = { onContinueClick(selectedDiet) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3A2D))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "YOU CAN CHANGE YOUR DIET SETTINGS LATER",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun DietOptionCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    iconBgColor: Color,
    iconTint: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Custom borders and backgrounds based on selected state
    val backgroundColor = if (selected) Color(0xFFFDF7F5) else Color.White
    val borderColor = if (selected) Color(0xFF8D6E63) else Color(0xFFE0E0E0)
    val borderThickness = if (selected) 2.dp else 1.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                borderThickness,
                borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666)
                )
            }

            // Radio Button
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF8D6E63),
                    unselectedColor = Color(0xFFBDBDBD)
                )
            )
        }
    }
}
