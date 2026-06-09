package com.example.satvikscan.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R

// main 3 type show
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
    modifier: () -> Unit = Modifier,
    dietName: () -> Unit = "Swaminarayan",
    ingredients: () -> Unit = listOf(
        IngredientData("Garlic", "Restricted Root Vegetable", IngredientType.ROOT_VEGETABLE),
        IngredientData("Gelatin", "Non-vegetarian Origin", IngredientType.NON_VEG_ORIGIN)
    ),
    onBackClick: () -> Unit,
    onScanAgainClick: () -> Unit
) {
    Column(
        modifier = modifier
            .invoke()
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
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBackClick() }
            )
            Text(
                text = "Result",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary, // main app theme
                modifier = Modifier
                    .weight(1f)  // contain remaining space
                    .padding(end = 32.dp), // ensure center (compensated back button size)
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // avoid Status Block
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFFFEAEA), shape = RoundedCornerShape(20.dp)), // light red card
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.restricted_svgrepo_com),
                contentDescription = "Avoid",
                tint = Color(0xFFC62828),
                modifier = Modifier.size(64.dp)
            )
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
            color = Color(0xFF555555) // Slate grey
        )

        Spacer(modifier = Modifier.height(28.dp))

        //  Diet Context Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    Color(0xFFF9F5F0),
                    shape = RoundedCornerShape(16.dp)
                ) // Cozy beige-brown background
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFE0B2), shape = CircleShape), // Warm peach circle
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_diet_guidelines),
                    contentDescription = "Context",
                    tint = Color(0xFF5D4037),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "CURRENT CONTEXT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D6E63), // Brown
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Diet: $dietName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary // Deep brown title
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //  Detected Ingredients Card
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

                    // Found box
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFFFEAEA),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${ingredients.size} FOUND", // size of list
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // dynamic list of ingredients
                ingredients.forEachIndexed { index, ingredient ->
                    val iconRes = when {
                        ingredient.name.contains(
                            "Garlic",
                            ignoreCase = true
                        ) -> R.drawable.ic_garlic

                        ingredient.name.contains(
                            "Potato",
                            ignoreCase = true
                        ) -> R.drawable.ic_potato

                        ingredient.name.contains(
                            "Carrot",
                            ignoreCase = true
                        ) -> R.drawable.ic_carrot

                        ingredient.type == IngredientType.ROOT_VEGETABLE -> R.drawable.ic_root_vegetable
                        ingredient.type == IngredientType.NON_VEG_ORIGIN -> R.drawable.ic_meat
                        else -> R.drawable.ic_chemical
                    }
                    IngredientItem(
                        name = ingredient.name,
                        reason = ingredient.reason,
                        iconRes = iconRes       // icon we decided accroding when statement
                    )
                    if (index < ingredients.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    } //--> if last index not take a space
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Context Guidelines Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) { // this is make variable store string

                    val annotatedString = buildAnnotatedString {
                        append("According to ")
                        withStyle(
                            SpanStyle(color = Color(0xFF8D6E63), fontWeight = FontWeight.Bold)
                        ) {
                            append(dietName)
                        }
                        append(" guidelines, these items are avoided to maintain purity and mindful eating.")
                    }
                    Text(
                        text = annotatedString, // this is make actual line
                        fontSize = 12.sp,
                        color = Color(0xFF616161),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        //  Action Buttons Section
        Button(
            onClick = onScanAgainClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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

        //  Disclaimer Footer Note
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
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

// IngredientItem box
@Composable
private fun IngredientItem(
    name: String,
    reason: String,
    iconRes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFFFF9F9),
                shape = RoundedCornerShape(14.dp)
            ) // Soft red background tint
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
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color(0xFFC62828),
                modifier = Modifier.size(24.dp)
            )
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

        // Warning icon
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning",
            tint = Color(0xFFC62828),
            modifier = Modifier.size(24.dp)
        )
    }
}


