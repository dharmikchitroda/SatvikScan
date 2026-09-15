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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.viewmodel.SatvikViewModel

// Data model for language options
data class LanguageItem(
    val code: String,
    val badgeText: String,
    val badgeColor: Color,
    val title: String,
    val subtitle: String,
    val nativeGreeting: String
)

@Composable
fun LanguageSelectionScreen(
    viewModel: SatvikViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLanguage by viewModel.appLanguage.collectAsState()
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    val languages = listOf(
        LanguageItem(
            code = "en",
            badgeText = "EN",
            badgeColor = Color(0xFF1565C0),
            title = "English",
            subtitle = "Default language",
            nativeGreeting = "Welcome"
        ),
        LanguageItem(
            code = "hi",
            badgeText = "हि",
            badgeColor = Color(0xFF2E7D32),
            title = "हिन्दी",
            subtitle = "Hindi",
            nativeGreeting = "नमस्ते"
        ),
        LanguageItem(
            code = "gu",
            badgeText = "ગુ",
            badgeColor = Color(0xFF1B3A2D),
            title = "ગુજરાતી",
            subtitle = "Gujarati",
            nativeGreeting = "જય જિનેન્દ્ર / જય સ્વામિનારાયણ"
        )
    )

    val backgroundColor = Color(0xFFFAF9F9)
    val brandGreen = Color(0xFF1B3A2D)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = brandGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "App Language",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = brandGreen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable content area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Subtitle
            Text(
                text = "Choose your preferred language for the app",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3 Distinct Language Option Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                languages.forEach { lang ->
                    val isSelected = selectedLanguage == lang.code

                    LanguageOptionCard(
                        item = lang,
                        isSelected = isSelected,
                        onClick = {
                            selectedLanguage = lang.code
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Action Section (Matching SelectDietScreen styling)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (selectedLanguage != currentLanguage) {
                        viewModel.updateLanguage(selectedLanguage)
                        val langLabel = when (selectedLanguage) {
                            "gu" -> "ગુજરાતી"
                            "hi" -> "हिन्दी"
                            else -> "English"
                        }
                        Toast.makeText(context, "Language set to $langLabel ✓", Toast.LENGTH_SHORT).show()
                    }
                    onContinueClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = brandGreen,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "YOU CAN CHANGE YOUR LANGUAGE ANYTIME",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun LanguageOptionCard(
    item: LanguageItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val brandGreen = Color(0xFF1B3A2D)
    val cardBg = if (isSelected) Color(0xFFF2F9F5) else Color.White
    val borderColor = if (isSelected) brandGreen else Color(0xFFE5E7EB)
    val borderThickness = if (isSelected) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                width = borderThickness,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Badge Icon
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        if (isSelected) Color(0xFFBEF3CE) else Color(0xFFF5F3F3),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.badgeText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) brandGreen else item.badgeColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title, Subtitle, and Greeting
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) brandGreen else Color(0xFF1F1F1F)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF666666)
                )
            }

            // Green Radio Button
            SatvikGreenRadioButton(
                selected = isSelected,
                selectedColor = brandGreen,
                unselectedColor = Color(0xFFD1D5DB)
            )
        }
    }
}

@Composable
fun SatvikGreenRadioButton(
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 2.dp,
                color = if (selected) selectedColor else unselectedColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(selectedColor, shape = CircleShape)
            )
        }
    }
}

