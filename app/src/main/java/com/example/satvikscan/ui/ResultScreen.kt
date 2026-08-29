package com.example.satvikscan.ui

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.satvikscan.R
import com.example.satvikscan.ui.viewmodel.SatvikViewModel

// main 3 type show
enum class IngredientType {
    ROOT_VEGETABLE,
    NON_VEG_ORIGIN,
    OTHER
}

data class IngredientData(
    val name: String,
    val reason: String,
    val type: IngredientType,
    val scientificReason: String = ""
)

@Composable
fun ResultScreen(
    viewModel: SatvikViewModel,
    modifier: Modifier = Modifier,
    dietName: String,
    onBackClick: () -> Unit,
    onScanAgainClick: () -> Unit,
    scanIngredient: () -> Unit
) {

    // collect both state
    val currentUiState by viewModel.productUiState.collectAsState()
    val ingredientList by viewModel.ingredients.collectAsState()

    // Remember last non-Idle state so exit transition doesn't flash black when state resets to Idle
    var lastActiveState by remember { mutableStateOf<SatvikViewModel.ProductUiState?>(null) }
    if (currentUiState !is SatvikViewModel.ProductUiState.Idle) {
        lastActiveState = currentUiState
    }

    // System back button bhi onBackClick use kare (state reset + navigate)
    BackHandler { onBackClick() }

    when (val state = lastActiveState ?: currentUiState) {
        is SatvikViewModel.ProductUiState.Loading -> {
            LoadingView()
        }

        is SatvikViewModel.ProductUiState.ProductNotFound -> {
            ProductNotFoundScreen(
                onBackClick = onBackClick,
                onScanIngredientLabelClick = scanIngredient // OCR mode - scan ingredient label directly
            )
        }

        is SatvikViewModel.ProductUiState.Error -> {

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

        is SatvikViewModel.ProductUiState.Success -> {

            val isSafe = ingredientList.isEmpty() // if list is blank so it is safe

            // State to track which ingredient was tapped by user
            var selectedIngredient by remember { mutableStateOf<IngredientData?>(null) }

            // Show the "Why Avoid?" Dialog when an ingredient is tapped
            selectedIngredient?.let { ing ->
                WhyAvoidDialog(
                    ingredient = ing,
                    dietName = dietName,
                    onDismiss = { selectedIngredient = null }
                )
            }

            Column(
                modifier = modifier
                    .background(Color(0xFFFAFAFA)), // Ultra-clean premium light grey/off-white background
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Custom Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = "Result",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary, // main app theme
                        modifier = Modifier
                            .weight(1f)  // contain remaining space
                            .padding(end = 48.dp), // compensate IconButton width for exact center alignment
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
                            modifier = Modifier
                                .padding(7.dp)
                                .fillMaxSize()
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
                    if (!state.product.code.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Barcode: ${state.product.code}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF757575),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
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

                    // Dynamic Diet icon and color mapping
                    val contextIconRes = when (dietName) {
                        "Swaminarayan" -> R.drawable.swaminarayan_tilak
                        "Jain" -> R.drawable.jain_logo
                        else -> R.drawable.custom_icon
                    }
                    val contextIconTint = when (dietName) {
                        "Swaminarayan" -> Color(0xFFD35400)
                        "Jain" -> Color(0xFF2E7D32)
                        else -> Color(0xFF1B3A2D)
                    }
                    val contextBgColor = when (dietName) {
                        "Swaminarayan" -> Color(0xFFFFF2EC)
                        "Jain" -> Color(0xFFE8F5E9)
                        else -> Color(0xFFE2EFE9)
                    }

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
                                    contextBgColor, shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = contextIconRes),
                                contentDescription = "Context",
                                tint = contextIconTint,
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

                    if (isSafe) {
                        // ✅ Safe product: "All Clear" section dikhao, ingredients card nahi
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Green checkmark circle
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            Color(0xFF2E7D32),
                                            shape = RoundedCornerShape(50.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_safe_tick1),
                                        contentDescription = "Safe",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Text(
                                    text = "No Restricted Ingredients Found",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1B5E20),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "This product contains no ingredients that conflict with your $dietName diet guidelines.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF4CAF50),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 19.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    } else {
                        // Avoid product: Detected Ingredients Card dikhao
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
                                            text = "${ingredientList.size} FOUND",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFC62828)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Dynamic list of ingredients
                                ingredientList.forEachIndexed { index, ingredient ->
                                    val iconRes = when {
                                        ingredient.name.contains(
                                            "Garlic", ignoreCase = true
                                        ) -> R.drawable.ic_garlic

                                        ingredient.name.contains(
                                            "Onion", ignoreCase = true
                                        ) -> R.drawable.ic_onion

                                        ingredient.name.contains(
                                            "Potato", ignoreCase = true
                                        ) -> R.drawable.ic_potato

                                        ingredient.name.contains(
                                            "Carrot", ignoreCase = true
                                        ) -> R.drawable.ic_carrot_1

                                        ingredient.name.contains(
                                            "Egg", ignoreCase = true
                                        ) -> R.drawable.ic_egg

                                        ingredient.name.contains(
                                            "Poppy", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Khus", ignoreCase = true
                                        ) -> R.drawable.ic_poppy_seeds

                                        ingredient.name.contains(
                                            "Vinegar", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Sirka", ignoreCase = true
                                        ) -> R.drawable.ic_vinegar

                                        ingredient.name.contains(
                                            "Turmeric", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Haldi", ignoreCase = true
                                        ) -> R.drawable.ic_turmeric

                                        ingredient.name.contains(
                                            "Honey", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Shehad", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Madh", ignoreCase = true
                                        ) -> R.drawable.ic_honey

                                        ingredient.name.contains(
                                            "Cheese", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Paneer", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Cottage Cheese", ignoreCase = true
                                        ) -> R.drawable.ic_cheese

                                        ingredient.name.contains(
                                            "Chocolate", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Cocoa", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Cacao", ignoreCase = true
                                        ) -> R.drawable.ic_chocolate

                                        ingredient.name.contains(
                                            "Hing", ignoreCase = true
                                        ) || ingredient.name.contains(
                                            "Asafoetida", ignoreCase = true
                                        ) -> R.drawable.ic_powder

                                        ingredient.type == IngredientType.ROOT_VEGETABLE -> R.drawable.ic_root_vegetable
                                        ingredient.type == IngredientType.NON_VEG_ORIGIN -> R.drawable.ic_meat

                                        // Check for alcohol and intoxicants to display the general restricted icon
                                        ingredient.name.contains("alcohol", ignoreCase = true) ||
                                                ingredient.name.contains(
                                                    "wine",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "beer",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "rum",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "whisky",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "whiskey",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "brandy",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "vodka",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "gin",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "spirit",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "ethanol",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "cannabis",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "bhang",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "marijuana",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "ganja",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "opium",
                                                    ignoreCase = true
                                                ) ||
                                                ingredient.name.contains(
                                                    "hashish",
                                                    ignoreCase = true
                                                ) -> R.drawable.restricted_svgrepo_com

                                        else -> R.drawable.ic_chemical
                                    }
                                    IngredientItem(
                                        name = ingredient.name,
                                        reason = ingredient.reason,
                                        iconRes = iconRes,
                                        onClick = { selectedIngredient = ingredient }
                                    )
                                    if (index < ingredientList.lastIndex) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(28.dp))

                                // Context Guidelines Note
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFFF5F5F5),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(14.dp)
                                ) {
                                    val annotatedString = remember(dietName) {
                                        buildAnnotatedString {
                                            append("According to ")
                                            withStyle(
                                                SpanStyle(
                                                    color = Color(0xFF8D6E63),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ) {
                                                append(dietName)
                                            }
                                            append(" guidelines, these items are avoided to maintain purity and mindful eating.")
                                        }
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
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                } // End of scrollable Column

                // Bottom Fixed Action Buttons & Disclaimer Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFAFAFA))
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp, top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Action Buttons Section
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

                    // Disclaimer Footer Note
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
            } // End of outer screen Column
        } // End of Success block

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
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.food_scan_anim)
                .decoderFactory(
                    if (Build.VERSION.SDK_INT >= 28) {
                        ImageDecoderDecoder.Factory()
                    } else {
                        GifDecoder.Factory()
                    }
                )
                .build(),
            contentDescription = "Scanning Loader",
            modifier = Modifier.size(240.dp)
        )
    }
}

// IngredientItem box — now clickable to show "Why Avoid?" dialog
@Composable
private fun IngredientItem(
    name: String,
    reason: String,
    iconRes: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFFFF9F9), shape = RoundedCornerShape(14.dp)
            )
            .border(BorderStroke(1.dp, Color(0xFFFFF0F0)), shape = RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red Icon container
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFFFEAEA), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color(0xFFC62828),
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7A1C1C)
            )
        }

        // Tap hint — question mark icon
        Icon(
            painter = painterResource(id = R.drawable.question_mark),
            contentDescription = "Why Avoid?",
            tint = Color(0xFFC62828).copy(alpha = 0.7f),
            modifier = Modifier.size(26.dp)
        )
    }
}

// Why Avoid? dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhyAvoidDialog(
    ingredient: IngredientData,
    dietName: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF9F9F9),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 28.dp)
        ) {
            //  Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Why Avoid?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A1A)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFF0F0F0), shape = CircleShape)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF555555),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  Ingredient Card (white box, red left border)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE8E8E8), shape = RoundedCornerShape(12.dp))
            ) {
                // Red left accent bar — fills full height of card
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFC62828))
                )
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "INGREDIENT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ingredient.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            //  STATUS row
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // circle icon
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .border(2.dp, Color(0xFFC62828), CircleShape)
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFC62828), CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "STATUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFC62828),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Non-Permissible for $dietName",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  Two info cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Religious Guidance card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF0FDF4), shape = RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.leaf_svg),
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "RELIGIOUS GUIDANCE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF15803D),
                            letterSpacing = 0.5.sp,
                            lineHeight = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = ingredient.reason
                            .removePrefix("Avoided: ")
                            .removePrefix("Caution: "),
                        fontSize = 11.5.sp,
                        color = Color(0xFF1F2937),
                        lineHeight = 16.sp
                    )
                }

                // Industry Origin card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chemical),
                            contentDescription = null,
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "INDUSTRY ORIGIN",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF333333),
                            letterSpacing = 0.5.sp,
                            lineHeight = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (ingredient.scientificReason.isNotBlank())
                            ingredient.scientificReason
                        else
                            "Derived from natural or industrial sources. Verify label for exact origin.",
                        fontSize = 11.5.sp,
                        color = Color(0xFF444444),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── I Understand button ──────────────────────────────────────────
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "I Understand",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_safe_tick1),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NoInternetDialog(
    onRetry: () -> Unit,
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
