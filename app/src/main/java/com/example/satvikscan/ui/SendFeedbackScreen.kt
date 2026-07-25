package com.example.satvikscan.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

data class FeedbackTypeOption(
    val id: String,
    val title: String,
    val iconRes: Int
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SendFeedbackScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Theme Colors
    val backgroundColor = Color(0xFFF8F9FA)
    val primaryGreen = Color(0xFF1B3A2D)
    val primaryText = Color(0xFF1B1C1C)
    val secondaryText = Color(0xFF5F6368)
    val labelText = Color(0xFF707973)
    val cardBorderColor = Color(0xFFE5E7EB)
    val placeholderColor = Color(0xFF9CA3AF)

    val feedbackOptions = listOf(
        FeedbackTypeOption("bug", "Bug Report", R.drawable.ic_chemical),
        FeedbackTypeOption("feature", "Feature Request", R.drawable.star),
        FeedbackTypeOption("diet", "Diet Update", R.drawable.leaf_svg),
        FeedbackTypeOption("other", "Other", R.drawable.feedback_icon)
    )

    var selectedType by remember { mutableStateOf(feedbackOptions.first()) }
    var feedbackText by remember { mutableStateOf("") }

    val maxCharLimit = 500

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        // ─── 1. Top Header Bar ───
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = primaryText,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Send Feedback",
                fontSize = 20.sp,
                color = primaryText,
                textAlign = TextAlign.Center
            )
        }

        // ─── Main Scrollable Form ───
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // ─── 2. Headline Section ───
            Text(
                text = "We'd love to hear from\nyou!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Your feedback helps us improve SatvikScan for everyone. Let us know how we can serve your dietary journey better.",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = secondaryText
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ─── 3. Category Label ───
            Text(
                text = "FEEDBACK TYPE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = labelText,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ─── 4. Feedback Chips ───
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                feedbackOptions.forEach { option ->
                    val isSelected = option == selectedType
                    FeedbackChip(
                        option = option,
                        isSelected = isSelected,
                        selectedColor = primaryGreen,
                        unselectedBorderColor = cardBorderColor,
                        onSelect = { selectedType = option }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ─── 5. Text Input Box (Max 500 chars) ───
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, cardBorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        if (feedbackText.isEmpty()) {
                            Text(
                                text = "Tell us how we can improve your experience...",
                                fontSize = 14.sp,
                                color = placeholderColor,
                                lineHeight = 20.sp
                            )
                        }
                        BasicTextField(
                            value = feedbackText,
                            onValueChange = { input ->
                                if (input.length <= maxCharLimit) {
                                    feedbackText = input
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = primaryText,
                                lineHeight = 20.sp
                            ),
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Character counter
                    Text(
                        text = "${feedbackText.length}/$maxCharLimit",
                        fontSize = 11.sp,
                        color = labelText,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // ─── 6. Bottom Submit Button ───
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    if (feedbackText.trim().isEmpty()) {
                        Toast.makeText(context, "Please enter your feedback first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val recipient = "support.satvikscan@gmail.com"
                    val subject = "[SatvikScan Feedback] - ${selectedType.title}"
                    val mailtoUrl = "mailto:$recipient" +
                            "?subject=${Uri.encode(subject)}" +
                            "&body=${Uri.encode(feedbackText)}"

                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(mailtoUrl)).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                        putExtra(Intent.EXTRA_SUBJECT, subject)
                        putExtra(Intent.EXTRA_TEXT, feedbackText)
                    }
                    try {
                        context.startActivity(intent)
                        onBackClick()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open email app", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Send Feedback",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedbackChip(
    option: FeedbackTypeOption,
    isSelected: Boolean,
    selectedColor: Color,
    unselectedBorderColor: Color,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(50),
        color = if (isSelected) selectedColor else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, unselectedBorderColor),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(option.iconRes),
                contentDescription = option.title,
                tint = if (isSelected) Color.White else Color(0xFF1B1C1C),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = option.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else Color(0xFF1B1C1C)
            )
        }
    }
}
