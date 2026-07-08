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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.satvikscan.R
import com.example.satvikscan.ui.viewmodel.Satvik_ViewModel
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

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
    viewModel: Satvik_ViewModel,
    modifier: Modifier = Modifier,
    dietName: String,
    onBackClick: () -> Unit,
    onScanAgainClick: () -> Unit,
    ScanIngredient: () -> Unit
) {

    // collect both state
    val CurrentUiState by viewModel.productUiState.collectAsState()
    val ingredientList by viewModel.ingredients.collectAsState()

    when (val state = CurrentUiState) {
        is Satvik_ViewModel.ProductUiState.Loading -> {
            LoadingView()
        }

        is Satvik_ViewModel.ProductUiState.ProductNotFound -> {
            ProductNotFoundScreen(
                onBackClick = onBackClick,
                onScanIngredientLabelClick = ScanIngredient // OCR mode - scan ingredient label directly
            )
        }

        is Satvik_ViewModel.ProductUiState.Error -> {

            if (state.message == "NO_INTERNET") {

                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingView()
                    NoInternetDialog(
                        onRetry = { viewModel.retryLastScan(onNoBarcodeAndHasInternet = onScanAgainClick) },
                        onDismiss = { onBackClick() }
                    )
                }
            } else {
                ErrorView(state.message)
            }
        }

        is Satvik_ViewModel.ProductUiState.Success -> {

            val isSafe = ingredientList.isEmpty() // if list is blanck so it is safe


            Column(
                modifier = modifier
                    .background(Color(0xFFFAFAFA)), // Ultra-clean premium light grey/off-white background
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
                            .clickable { onBackClick() })
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

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // avoid Status Block
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                color = if (isSafe) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                            )
                            .border(
                                width = 3.dp,
                                color = if (isSafe) Color(0xFF2E7D32) else Color(0xFFC62828), // Green border if Safe, Red border if Avoid
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {  // get image from string url
                        AsyncImage(
                            model = state.product.product?.imageUrl,
                            placeholder = painterResource(
                                id = if (isSafe) R.drawable.safe_foodpacket else R.drawable.avoid_foodpacket
                            ), // till load image show this
                            error = painterResource(
                                id = if (isSafe) R.drawable.safe_foodpacket else R.drawable.avoid_foodpacket
                            ), // error fallback
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop, // crops image
                            modifier = Modifier.padding(7.dp).fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = state.product.product?.productName ?: "Unknown Product",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (isSafe) "SATVIK" else "AVOID",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSafe) Color(0xFF2E7D32) else Color(0xFFC62828),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isSafe) "Product is safe to consume" else "Contains restricted ingredients",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF555555)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    //  Diet Context Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .background(
                                Color(0xFFF9F5F0), shape = RoundedCornerShape(16.dp)
                            ) // Cozy beige-brown background
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color(0xFFFFE0B2), shape = CircleShape
                                ), // Warm peach circle
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
                                            Color(0xFFFFEAEA), shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${ingredientList.size} FOUND", // size of list
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC62828)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // dynamic list of ingredients
                            ingredientList.forEachIndexed { index, ingredient ->
                                val iconRes = when {
                                    ingredient.name.contains(
                                        "Garlic", ignoreCase = true
                                    ) -> R.drawable.ic_garlic

                                    ingredient.name.contains(
                                        "Potato", ignoreCase = true
                                    ) -> R.drawable.ic_potato

                                    ingredient.name.contains(
                                        "Carrot", ignoreCase = true
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
                                if (index < ingredientList.lastIndex) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                } //--> if last index not take a space
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Context Guidelines Note
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(14.dp)
                            ) { // this is make variable store string

                                val annotatedString = buildAnnotatedString {
                                    append("According to ")
                                    withStyle(
                                        SpanStyle(
                                            color = Color(0xFF8D6E63), fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(dietName)
                                    }
                                    append(" guidelines, these items are avoided to maintain purity and mindful eating.")
                                }
                                Text(
                                    text = annotatedString, // this is make actual line
                                    fontSize = 12.sp, color = Color(0xFF616161), lineHeight = 18.sp
                                )
                            }
                        }
                    }

                } // End of scrollable Column

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFAFAFA))
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp, top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                    Spacer(modifier = Modifier.height(24.dp))

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
                }
            }
        }

        else -> {
            // Idle state me blank box ya loading dikha sakte hain
        }
    }


}


@Composable
fun ErrorView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color(0xFFC62828),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color(0xFFC62828),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

// IngredientItem box
@Composable
private fun IngredientItem(
    name: String, reason: String, iconRes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFFFF9F9), shape = RoundedCornerShape(14.dp)
            ) // Soft red background tint
            .border(BorderStroke(1.dp, Color(0xFFFFF0F0)), shape = RoundedCornerShape(14.dp))
            .padding(14.dp), verticalAlignment = Alignment.CenterVertically
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
                text = reason, fontSize = 12.sp, color = Color(0xFF757575)
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

@Composable
fun NoInternetDialog(
    onRetry: () -> Unit ,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF9F6)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top-right close button (X)
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF6D4C41),
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .clickable { onDismiss() }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Cloud Off Icon Container (Circle)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFFF1EDE9), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cloud_off),
                            contentDescription = "Offline",
                            tint = Color(0xFF7D5233),
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title
                    Text(
                        text = "Connection Lost",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7D5233),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Subtitle
                    Text(
                        text = "It seems you're offline. Please check your internet connection and try again.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6D6D6D),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Try Again Button
                    Button(
                        onClick = onRetry,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7D5233),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "Try Again",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


