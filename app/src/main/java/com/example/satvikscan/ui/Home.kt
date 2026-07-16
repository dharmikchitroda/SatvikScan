package com.example.satvikscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R
import com.example.satvikscan.ui.viewmodel.ScanMode


@Composable
fun HomeScreenUI(
    paddingValues: PaddingValues,
    currentMode: ScanMode,
    onScanPhotoclick: () -> Unit,
    onScanBarcodescan: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F9))
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroud_photo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.05f
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Original Scan Your Food header
            ScanBox()

            Spacer(modifier = Modifier.height(20.dp))

            // Two cards in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Capture & Discover Card (Photo Scan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(125.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onScanPhotoclick() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.capture_discover_banner),
                        contentDescription = "Capture & Discover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Scan & Discover Card (Barcode Scan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(125.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onScanBarcodescan() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.scan_discover_banner),
                        contentDescription = "Scan & Discover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enter Barcode Number manually Card
            ManualBarcodeCard()

            Spacer(modifier = Modifier.weight(1f))

            // Bottom text
            FooterText()
        }
    }
}

@Composable
fun ScanBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.scan__1_),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Scan Your Food",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Instant purity check for your dietary needs.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun FooterText() {
    // Bottom Tagline Section
    Text(
        text = "Purity in Every Bite\nThat's SatvikScan! \uD83C\uDF43",
        fontSize = 34.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFFB0B0B0),
        textAlign = TextAlign.Center,
        lineHeight = 42.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    )
}

@Composable
fun ManualBarcodeCard(
    modifier: Modifier = Modifier,
    onCheckClick: (String) -> Unit = {}
) {
    var barcodeInput by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFF1B3A2D).copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Row: Icon + Title/Subtitle + Keyboard Icon button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Green Barcode Icon Container
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = Color(0xFF1B3A2D).copy(alpha = 0.08f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.barcode_scan__1_),
                            contentDescription = null,
                            tint = Color(0xFF1B3A2D),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Text titles
                    Column {
                        Text(
                            text = "Enter Barcode Number",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B3A2D)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Type barcode number manually",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Keyboard icon button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_24),
                        contentDescription = "Manual Type",
                        tint = Color(0xFF1B3A2D),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Bottom Row: Custom Text Field & Check Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text Field Container
                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B3A2D).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (barcodeInput.isEmpty()) {
                        Text(
                            text = "e.g. 8901234567890",
                            color = Color.Gray.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    BasicTextField(
                        value = barcodeInput,
                        onValueChange = { input ->
                            // Allow only numeric input
                            if (input.all { it.isDigit() }) {
                                barcodeInput = input
                            }
                        },
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // Check Button
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .height(48.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF4C8C64), Color(0xFF1B3A2D))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onCheckClick(barcodeInput) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Check",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

