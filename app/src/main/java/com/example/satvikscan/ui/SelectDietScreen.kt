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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.example.satvikscan.R
import com.example.satvikscan.viewmodel.SatvikViewModel

@Composable
fun SelectDietScreen(
    viewModel: SatvikViewModel,
    onContinueClick: (String) -> Unit,
    onCustomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentDiet by viewModel.dietName.collectAsState()
    var selectedDiet by remember { mutableStateOf(currentDiet) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 35.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Green Leaf Logo Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFBEF3CE), shape = RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.leaf_svg),
                    contentDescription = "SatvikScan Logo",
                    tint = Color(0xFF1B3A2D),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = stringResource(R.string.select_diet_title),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F1F1F),
                lineHeight = 40.sp,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.select_diet_subtitle),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // 3 Diet Options list
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Jain Option
                SelectDietOptionCard(
                    title = stringResource(R.string.diet_jain_title),
                    subtitle = stringResource(R.string.diet_jain_desc),
                    iconRes = R.drawable.jain_logo,
                    iconBgColor = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF2E7D32),
                    selected = selectedDiet == "Jain",
                    onClick = { selectedDiet = "Jain" }
                )

                // 2. Swaminarayan Option
                SelectDietOptionCard(
                    title = stringResource(R.string.diet_swami_title),
                    subtitle = stringResource(R.string.diet_swami_desc),
                    iconRes = R.drawable.swaminarayan_tilak,
                    iconBgColor = Color(0xFFEFEBE9),
                    iconTint = Color(0xFF5D4037),
                    selected = selectedDiet == "Swaminarayan",
                    onClick = { selectedDiet = "Swaminarayan" }
                )

                // 3. Custom Option
                SelectDietOptionCard(
                    title = stringResource(R.string.diet_custom_title),
                    subtitle = stringResource(R.string.diet_custom_desc),
                    iconRes = R.drawable.custom_icon,
                    iconBgColor = Color(0xFFECEFF1),
                    iconTint = Color(0xFF455A64),
                    selected = selectedDiet == "Custom",
                    onClick = {
                        selectedDiet = "Custom"  // Only select — don't navigate yet
                    }
                )

                // Features Banner Image
                Image(
                    painter = painterResource(id = R.drawable.diet_features_banner),
                    contentDescription = "Diet Features Info",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        , alpha = 0.75f

                )
            }
        }

        // Action Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Button(
                onClick = {
                    viewModel.updateDiet(selectedDiet)
                    if (selectedDiet == "Custom") {
                        onCustomClick()       // → Navigate to Custom Exclusions Screen
                    } else {
                        val localizedDietName = when (selectedDiet) {
                            "Swaminarayan" -> context.getString(R.string.diet_swami_title)
                            "Jain" -> context.getString(R.string.diet_jain_title)
                            else -> context.getString(R.string.diet_custom_title)
                        }
                        Toast.makeText(
                            context,
                            context.getString(R.string.select_diet_toast_saved, localizedDietName),
                            Toast.LENGTH_SHORT
                        ).show()
                        onContinueClick(selectedDiet)  // → Navigate to Home
                    }
                },
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
                        text = stringResource(R.string.select_diet_btn_continue),
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
                text = stringResource(R.string.select_diet_footer),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun SelectDietOptionCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    iconBgColor: Color,
    iconTint: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

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
