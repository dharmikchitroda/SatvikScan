package com.example.satvikscan.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.satvikscan.R
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomExclusionsScreen(
    viewModel: Satvik_ViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val customExclusions by viewModel.customAvoidList.collectAsState()

    val commonAllergens = listOf(
        "Peanuts",
        "Gluten",
        "Dairy",
        "Soy",
        "Mustard",
        "Onion"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        //  Custom Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1B3A2D)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Custom Exclusions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A2D),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            // Empty spacer to balance back button
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Main scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Search or manually add ingredients you want to restrict from your personal sanctuary.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Search & Add Bar

            /* here two option to save ingredint in local
            1-> custom green button
            2-> inbuilt key-board done btn
              */
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search ingredients...", color = Color(0xFF9E9E9E)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF757575)
                    )
                },
                trailingIcon = {
                    Button(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                //added in sharedprefernce
                                viewModel.addCustomAvoidIngredient(searchQuery)
                                // after add make blanc search bar
                                searchQuery = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3A2D)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Add", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                singleLine = true,

                // done btn set in keyborad instead of next
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {

                    if (searchQuery.isNotBlank()) {
                        // added in sharedprefernce
                        viewModel.addCustomAvoidIngredient(searchQuery)
                        searchQuery = ""
                    }
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1B3A2D),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFEEEEEE),
                    unfocusedContainerColor = Color(0xFFEEEEEE)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Common Allergens header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "COMMON ALLERGENS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D6E63),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Tap to exclude",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF757575)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Allergens 2-column Grid (olumn inside multiple row )
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                for (i in commonAllergens.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (j in i..i + 1) {
                            // safe check
                            if (j < commonAllergens.size) {

                                val allergen = commonAllergens[j]
                                val isSelected = customExclusions.contains(allergen.lowercase())
                                val allergenIcon = when (allergen.lowercase()) {
                                    "peanuts" -> R.drawable.peanut
                                    "gluten" -> R.drawable.gluten
                                    "dairy" -> R.drawable.dairy
                                    "soy" -> R.drawable.soy
                                    "mustard" -> R.drawable.mustard
                                    "onion" -> R.drawable.onion
                                    else -> R.drawable.download__1_
                                }
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(120.dp)
                                        .clickable {
                                            if (isSelected) {
                                                // select kia gya ahin by user so, isSelected = true and second vahi clik remove it
                                                viewModel.removeCustomAvoidIngredient(allergen)
                                            } else {
                                                // first time click nahi kia hoga is liye add bhi nahi hua hoga - firt click add this
                                                viewModel.addCustomAvoidIngredient(allergen)
                                            }
                                        },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) Color(0xFFFFEBEE) else Color(
                                            0xFFFAFAFA
                                        )
                                    ),
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFC62828) else Color(
                                            0xFFEEEEEE
                                        )
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        // Top-Right Indicator (+ or Check)
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 10.dp, end = 10.dp)
                                                .size(22.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) Color(0xFFC62828) else Color(
                                                        0xFF9E9E9E
                                                    ),
                                                    shape = CircleShape
                                                )
                                                .align(Alignment.TopEnd),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                                                contentDescription = if (isSelected) "Selected" else "Add",
                                                tint = if (isSelected) Color(0xFFC62828) else Color(
                                                    0xFF757575
                                                ),
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }

                                        // Central Content: Image & Text
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(bottom = 12.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .background(Color.White, shape = CircleShape)
                                                    .border(1.dp, Color(0xFFE0E0E0), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(id = allergenIcon),
                                                    contentDescription = allergen,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = allergen,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (isSelected) Color(0xFFC62828) else Color(
                                                    0xFF212121
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            } else {
                                // index total 5 remain 1 so without thi uccupy full width so use 1f= half blanck space contain
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            //Selected to Avoid Divider & Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                Text(
                    text = "SELECTED TO AVOID",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    letterSpacing = 1.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Exclusion chips flowing layout
            if (customExclusions.isEmpty()) {
                Text(
                    text = "No ingredients selected yet.",
                    fontSize = 13.sp,
                    color = Color(0xFF9E9E9E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    customExclusions.forEach { exclusion ->
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFCE4EC), shape = RoundedCornerShape(20.dp))
                                .border(1.dp, Color(0xFFF8BBD0), shape = RoundedCornerShape(20.dp))
                                .clickable { viewModel.removeCustomAvoidIngredient(exclusion) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = exclusion.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                                    color = Color(0xFFC2185B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color(0xFFC2185B), shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Bottom Purity Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Shield Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFECEFF1), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        ShieldIcon(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF1B3A2D)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your purity standards are key.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Adding specific additives ensures every scan respects your Satvik journey.",
                        fontSize = 13.sp,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // 6. Action Button Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3A2D))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save Preferences",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sync Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LeafIcon(
                    modifier = Modifier.size(12.dp),
                    color = Color(0xFF8D6E63)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Syncs across your devices automatically",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}

@Composable
fun LeafIcon(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.85f)
            cubicTo(
                size.width * 0.15f, size.height * 0.35f,
                size.width * 0.45f, size.height * 0.15f,
                size.width * 0.85f, size.height * 0.15f
            )
            cubicTo(
                size.width * 0.85f, size.height * 0.55f,
                size.width * 0.65f, size.height * 0.85f,
                size.width * 0.15f, size.height * 0.85f
            )
            moveTo(size.width * 0.15f, size.height * 0.85f)
            lineTo(size.width * 0.05f, size.height * 0.95f)
            // Center vein
            moveTo(size.width * 0.15f, size.height * 0.85f)
            lineTo(size.width * 0.65f, size.height * 0.35f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun ShieldIcon(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            val w = size.width
            val h = size.height
            moveTo(w * 0.15f, h * 0.2f)
            quadraticTo(w * 0.5f, h * 0.1f, w * 0.85f, h * 0.2f)
            lineTo(w * 0.85f, h * 0.55f)
            quadraticTo(w * 0.85f, h * 0.85f, w * 0.5f, h * 0.95f)
            quadraticTo(w * 0.15f, h * 0.85f, w * 0.15f, h * 0.55f)
            close()
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        val checkPath = Path().apply {
            val w = size.width
            val h = size.height
            moveTo(w * 0.35f, h * 0.5f)
            lineTo(w * 0.47f, h * 0.62f)
            lineTo(w * 0.68f, h * 0.4f)
        }
        drawPath(
            path = checkPath,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}
