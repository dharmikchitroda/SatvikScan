package com.example.satvikscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
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

// ==========================================
// 1. Core Screen Composable
// ==========================================

enum class IngredientType {
    ROOT_VEGETABLE,
    NON_VEG_ORIGIN,
    OTHER
}

data class IngredientData(
    val name: String,
    val reason: String,
    val type: IngredientType
)

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    dietName: String = "Swaminarayan",
    ingredients: List<IngredientData> = listOf(
        IngredientData("Garlic", "Restricted Root Vegetable", IngredientType.ROOT_VEGETABLE),
        IngredientData("Gelatin", "Non-vegetarian Origin", IngredientType.NON_VEG_ORIGIN)
    ),
    onBackClick: () -> Unit = {},
    onScanAgainClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // Ultra-clean premium light grey/off-white background
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Custom Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                CustomBackArrow()
            }
            Text(
                text = "Result",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D), // Custom dark green matching main app theme
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 40.dp), // Offsets the back button size to ensure absolute centering
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 1.2 AVOID Status Block ---
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFFFEAEA), shape = RoundedCornerShape(20.dp)), // Premium rounded light red card
            contentAlignment = Alignment.Center
        ) {
            CustomAvoidIcon()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AVOID",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFC62828), // Deep warnings red
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Contains restricted ingredients",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF555555) // Slate grey for subtitles
        )

        Spacer(modifier = Modifier.height(28.dp))

        // --- 1.3 Diet Context Row ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(Color(0xFFF9F5F0), shape = RoundedCornerShape(16.dp)) // Cozy beige-brown background
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFE0B2), shape = CircleShape), // Warm peach circle
                contentAlignment = Alignment.Center
            ) {
                CustomContextIcon()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "CURRENT CONTEXT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D6E63), // Brown context tag
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Diet: $dietName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037) // Deep brown title
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 1.4 Detected Ingredients Card ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detected Ingredients",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )

                    // Found Badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFEAEA), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${ingredients.size} FOUND",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Render dynamic list of ingredients
                ingredients.forEachIndexed { index, ingredient ->
                    IngredientItem(
                        name = ingredient.name,
                        reason = ingredient.reason,
                        icon = {
                            when (ingredient.type) {
                                IngredientType.ROOT_VEGETABLE -> CustomForkKnifeIcon()
                                IngredientType.NON_VEG_ORIGIN -> CustomGelatinIcon()
                                IngredientType.OTHER -> CustomForkKnifeIcon()
                            }
                        }
                    )
                    if (index < ingredients.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Context Guidelines Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    val annotatedString = buildAnnotatedString {
                        append("According to ")
                        withStyle(style = SpanStyle(color = Color(0xFF8D6E63), fontWeight = FontWeight.Bold)) {
                            append(dietName)
                        }
                        append(" guidelines, these items are avoided to maintain purity and mindful eating.")
                    }
                    Text(
                        text = annotatedString,
                        fontSize = 12.sp,
                        color = Color(0xFF616161),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // --- 1.5 Action Buttons Section ---
        Button(
            onClick = onScanAgainClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3A2D)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_camera_alt_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Scan Again",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 1.6 Disclaimer Footer Note ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier
                    .size(14.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "ANALYSIS BASED ON COMMON DATABASE.\nALWAYS DOUBLE-CHECK LABELS.",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E9E9E),
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                lineHeight = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// ==========================================
// 2. Sub-Composables
// ==========================================

@Composable
private fun IngredientItem(
    name: String,
    reason: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F9), shape = RoundedCornerShape(14.dp)) // Soft red background tint
            .border(BorderStroke(1.dp, Color(0xFFFFF0F0)), shape = RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red Icon container
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFFFEAEA), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7A1C1C) // Deep dark warnings red
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = reason,
                fontSize = 12.sp,
                color = Color(0xFF757575)
            )
        }

        // Warning sign
        CustomWarningTriangle()
    }
}

// ==========================================
// 3. Custom Canvas Vector Graphics (Beautiful & Independent of external assets)
// ==========================================

@Composable
fun CustomBackArrow(modifier: Modifier = Modifier, color: Color = Color(0xFF1B3A2D)) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        // Head paths
        val path = Path().apply {
            moveTo(w * 0.65f, h * 0.25f)
            lineTo(w * 0.35f, h * 0.5f)
            lineTo(w * 0.65f, h * 0.75f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Shaft
        drawLine(
            color = color,
            start = Offset(w * 0.35f, h * 0.5f),
            end = Offset(w * 0.75f, h * 0.5f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun CustomAvoidIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(48.dp)) {
        val r = size.width / 2
        val center = Offset(size.width / 2, size.height / 2)
        val strokeWidth = 5.dp.toPx()

        // 1. Circle Outline
        drawCircle(
            color = Color(0xFFC62828),
            radius = r - strokeWidth / 2,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        // 2. Diagonal Block line (45 degrees angle)
        val angle = 45f
        val cosVal = kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
        val sinVal = kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
        val offsetOffset = r - strokeWidth / 2

        drawLine(
            color = Color(0xFFC62828),
            start = Offset(center.x - offsetOffset * cosVal, center.y - offsetOffset * sinVal),
            end = Offset(center.x + offsetOffset * cosVal, center.y + offsetOffset * sinVal),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun CustomWarningTriangle(modifier: Modifier = Modifier, color: Color = Color(0xFFC62828)) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.15f)
            lineTo(w * 0.12f, h * 0.85f)
            lineTo(w * 0.88f, h * 0.85f)
            close()
        }
        // Fill triangle base
        drawPath(
            path = path,
            color = color,
            style = Fill
        )

        // Exclamation Mark inside triangle
        // Dot at bottom
        drawCircle(
            color = Color.White,
            radius = w * 0.05f,
            center = Offset(w * 0.5f, h * 0.74f)
        )
        // Upper Line
        drawLine(
            color = Color.White,
            start = Offset(w * 0.5f, h * 0.38f),
            end = Offset(w * 0.5f, h * 0.62f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun CustomContextIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val brownColor = Color(0xFF5D4037)

        // Profile shape outline
        val headPath = Path().apply {
            moveTo(w * 0.35f, h * 0.85f)
            lineTo(w * 0.35f, h * 0.65f)
            cubicTo(w * 0.15f, h * 0.6f, w * 0.15f, h * 0.25f, w * 0.45f, h * 0.2f)
            cubicTo(w * 0.65f, h * 0.2f, w * 0.75f, h * 0.3f, w * 0.75f, h * 0.45f)
            lineTo(w * 0.72f, h * 0.48f)
            lineTo(w * 0.68f, h * 0.5f)
            quadraticTo(w * 0.63f, h * 0.58f, w * 0.58f, h * 0.65f)
            lineTo(w * 0.58f, h * 0.85f)
            close()
        }
        drawPath(path = headPath, color = brownColor, style = Fill)

        // Inside setting contrast circle
        drawCircle(
            color = Color(0xFFFFE0B2),
            radius = w * 0.12f,
            center = Offset(Offset(w * 0.46f, h * 0.42f).x, Offset(w * 0.46f, h * 0.42f).y)
        )
        // Center pin
        drawCircle(
            color = brownColor,
            radius = w * 0.05f,
            center = Offset(Offset(w * 0.46f, h * 0.42f).x, Offset(w * 0.46f, h * 0.42f).y)
        )
    }
}

@Composable
fun CustomForkKnifeIcon(modifier: Modifier = Modifier, color: Color = Color(0xFFC62828)) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        // 1. Draw Fork (Left Side)
        // Tines (prongs)
        drawLine(color, Offset(w * 0.3f, h * 0.2f), Offset(w * 0.3f, h * 0.45f), 1.8.dp.toPx(), StrokeCap.Round)
        drawLine(color, Offset(w * 0.22f, h * 0.2f), Offset(w * 0.22f, h * 0.4f), 1.8.dp.toPx(), StrokeCap.Round)
        drawLine(color, Offset(w * 0.38f, h * 0.2f), Offset(w * 0.38f, h * 0.4f), 1.8.dp.toPx(), StrokeCap.Round)
        // Fork Horizontal Bridge
        drawLine(color, Offset(w * 0.22f, h * 0.4f), Offset(w * 0.38f, h * 0.4f), 1.8.dp.toPx(), StrokeCap.Round)
        // Handle
        drawLine(color, Offset(w * 0.3f, h * 0.4f), Offset(w * 0.3f, h * 0.8f), 2.2.dp.toPx(), StrokeCap.Round)

        // 2. Draw Knife (Right Side)
        val knifePath = Path().apply {
            moveTo(w * 0.65f, h * 0.2f)
            lineTo(w * 0.72f, h * 0.2f)
            quadraticTo(w * 0.72f, h * 0.5f, w * 0.65f, h * 0.55f)
            close()
        }
        drawPath(knifePath, color)
        // Handle
        drawLine(color, Offset(w * 0.65f, h * 0.5f), Offset(w * 0.65f, h * 0.8f), 2.2.dp.toPx(), StrokeCap.Round)
    }
}

@Composable
fun CustomGelatinIcon(modifier: Modifier = Modifier, color: Color = Color(0xFFC62828)) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        // Jar/Box Body
        val rectPath = Path().apply {
            moveTo(w * 0.28f, h * 0.3f)
            lineTo(w * 0.72f, h * 0.3f)
            lineTo(w * 0.72f, h * 0.78f)
            lineTo(w * 0.28f, h * 0.78f)
            close()
        }
        drawPath(rectPath, color, style = Stroke(1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Lid
        drawLine(color, Offset(w * 0.35f, h * 0.23f), Offset(w * 0.65f, h * 0.23f), 3.dp.toPx(), StrokeCap.Round)

        // Horizontal line in the middle representing the label
        drawLine(color, Offset(w * 0.38f, h * 0.54f), Offset(w * 0.62f, h * 0.54f), 1.8.dp.toPx(), StrokeCap.Round)
    }
}

// ==========================================
// 4. Android Studio Preview
// ==========================================

//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ResultScreenPreview() {
    ResultScreen()
}