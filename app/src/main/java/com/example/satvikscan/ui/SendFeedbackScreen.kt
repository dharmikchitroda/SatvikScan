package com.example.satvikscan.ui

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    val backgroundColor = Color(0xFFFAFAFA)
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
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

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
                text = stringResource(R.string.feedback_title),
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
                text = stringResource(R.string.feedback_heading),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.feedback_desc),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = secondaryText
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ─── 3. Category Label ───
            Text(
                text = stringResource(R.string.feedback_type_label),
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
                shape = RoundedCornerShape(16.dp),
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
                                text = stringResource(R.string.feedback_placeholder),
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

            // ─── 6. Attach Screenshot Section (Optional) ───
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.feedback_attach_label),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = labelText,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedImageUri == null) {
                // Empty state: clickable card to pick an image
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, cardBorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(primaryGreen.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.gallery),
                                contentDescription = null,
                                tint = primaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.feedback_attach_btn),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(R.string.feedback_attach_placeholder),
                                fontSize = 12.sp,
                                color = secondaryText,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            } else {
                // Image Selected Preview State
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, cardBorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF3F4F6))
                        ) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Attached Screenshot",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Delete / Remove Button (Top Right)
                            IconButton(
                                onClick = { selectedImageUri = null },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(32.dp)
                                    .background(Color.Black.copy(alpha = 0.60f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.feedback_attach_remove),
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✓ ${stringResource(R.string.feedback_attach_attached)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryGreen
                            )

                            Text(
                                text = stringResource(R.string.feedback_attach_change),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryGreen,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        imagePickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // ─── 7. Bottom Submit Button ───
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

                    // Helper to create email intent with attachment & prefilled details
                    fun createEmailIntent(targetPackage: String?): Intent {
                        return if (selectedImageUri != null) {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "image/*"
                                if (targetPackage != null) {
                                    setPackage(targetPackage)
                                } else {
                                    selector = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                                }
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                                putExtra(Intent.EXTRA_SUBJECT, subject)
                                putExtra(Intent.EXTRA_TEXT, feedbackText)
                                putExtra(Intent.EXTRA_STREAM, selectedImageUri)
                                clipData = ClipData.newRawUri("Attachment", selectedImageUri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                        } else {
                            if (targetPackage != null) {
                                Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$recipient")
                                    setPackage(targetPackage)
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                                    putExtra(Intent.EXTRA_SUBJECT, subject)
                                    putExtra(Intent.EXTRA_TEXT, feedbackText)
                                }
                            } else {
                                val mailtoUrl = "mailto:$recipient" +
                                        "?subject=${Uri.encode(subject)}" +
                                        "&body=${Uri.encode(feedbackText)}"

                                Intent(Intent.ACTION_SENDTO, Uri.parse(mailtoUrl)).apply {
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                                    putExtra(Intent.EXTRA_SUBJECT, subject)
                                    putExtra(Intent.EXTRA_TEXT, feedbackText)
                                }
                            }
                        }
                    }

                    // Grant URI permission explicitly for Gmail
                    selectedImageUri?.let { uri ->
                        try {
                            context.grantUriPermission(
                                "com.google.android.gm",
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        } catch (_: Exception) {}
                    }

                    // 1. Try opening Gmail directly
                    try {
                        val directGmailIntent = createEmailIntent("com.google.android.gm")
                        context.startActivity(directGmailIntent)
                        onBackClick()
                    } catch (e: Exception) {
                        // 2. Fallback: If Gmail is not installed, open default email client
                        try {
                            val fallbackIntent = createEmailIntent(null)
                            context.startActivity(fallbackIntent)
                            onBackClick()
                        } catch (e2: Exception) {
                            Toast.makeText(context, "Could not open email app", Toast.LENGTH_SHORT).show()
                        }
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
                        text = stringResource(R.string.feedback_btn_submit),
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
