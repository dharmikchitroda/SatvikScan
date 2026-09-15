package com.example.satvikscan.ui

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R
import com.example.satvikscan.viewmodel.SatvikViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Unified scan item representing either local database or mock data
data class HistoryItem(
    val barcode: String,
    val productName: String,
    val isSatvik: Boolean,
    val timestamp: Long,
    val dietName: String,
    val isMock: Boolean, // history screen are empty ?
    val imageUrl: String?,
    val imageIndex: Int
)


@Composable
fun HistoryScreen(
    paddingValues: PaddingValues,
    viewModel: SatvikViewModel,
    onStartScanningClick: () -> Unit,
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchActiveChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    showClearHistoryDialog: Boolean,
    onShowClearHistoryDialogChange: (Boolean) -> Unit
) {

    val scanHistory by viewModel.scanHistory.collectAsState()
    val currentDiet by viewModel.dietName.collectAsState()
    val focusManager = LocalFocusManager.current // return FocusManager object so esily find user click kaha kar raha ahin kaha nahi

    // State parameters
    var selectedFilter_btn by remember { mutableStateOf("All") }
    var expandedMenuBarcode by remember { mutableStateOf<String?>(null) }


    // Map database items to unified model
    val dbItems = remember(scanHistory) {
        scanHistory.map { entity ->
            HistoryItem(
                barcode = entity.barcode,
                productName = entity.productName,
                isSatvik = entity.isSatvik,
                timestamp = entity.timestamp,
                dietName = entity.dietName,
                isMock = false,
                imageUrl = entity.imageUrl,
                imageIndex = entity.imageIndex
            )
        }
    }

    // Determine final source items
    val baseItems = dbItems

    // Filter items based on selected button and search query
    val filteredItems = remember(baseItems, selectedFilter_btn, searchQuery) {
        baseItems.filter { item ->
            //  Filter Check (All, Safe, or Avoid)
            val matchesFilter = when (selectedFilter_btn) {
                "Safe" -> item.isSatvik
                "Avoid" -> !item.isSatvik
                else -> true
            }
            val matchesSearch = item.productName.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F9)) // sleek off-white background
            .padding(paddingValues) // align with both top bar and bottom bar
    ) {
        // Custom Top bar


        if (dbItems.isEmpty()) {
            EmptyHistoryState(
                onStartScanningClick = onStartScanningClick,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Filter chips row matching the style: oval button shapes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val chips = listOf(
                    "All" to R.string.history_filter_all,
                    "Safe" to R.string.history_filter_safe,
                    "Avoid" to R.string.history_filter_avoid
                )
                chips.forEach { (chipKey, chipRes) ->
                    val isSelected = selectedFilter_btn == chipKey
                    val bgColor = if (isSelected) Color(0xFF1B3A2D) else Color(0xFFF1F5F2)
                    val textColor = if (isSelected) Color.White else Color(0xFF555555)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(bgColor)
                            .clickable { selectedFilter_btn = chipKey }
                            .padding(horizontal = 24.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(chipRes),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = textColor
                        )
                    }
                }
            }

            // List of history card items
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
            ) {
                if (filteredItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.history_no_items),
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(filteredItems, key = { it.barcode }) { item ->
                        HistoryCardItem(
                            item = item,
                            currentDiet = currentDiet,
                            expandedMenuBarcode = expandedMenuBarcode,
                            onMenuClick = { barcode -> expandedMenuBarcode = barcode },
                            onMenuDismiss = { expandedMenuBarcode = null },
                            onDeleteClick = {
                                viewModel.deleteScan(item.barcode)
                            }
                        )
                    }
                }

                // Always display the beautiful promo card at the bottom of the list
                item {
                    PromoCard()
                }
            }
        }

        if (showClearHistoryDialog) {
            ClearHistoryDialog(
                onDismissRequest = { onShowClearHistoryDialogChange(false) },
                onConfirm = {
                    onShowClearHistoryDialogChange(false)
                    viewModel.clearAllScans()
                }
            )
        }
    }
}

@Composable
fun EmptyHistoryState(
    onStartScanningClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.history_empty_img),
            contentDescription = "No scans yet",
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(32.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = stringResource(R.string.history_empty_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1B3A2D),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = stringResource(R.string.history_empty_desc),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF555555),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Start Scanning Button
        Button(
            onClick = onStartScanningClick,
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
                    painter = painterResource(R.drawable.barcode_scan),
                    contentDescription = "Scanner Icon",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.history_empty_btn),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Footer Caption
        Text(
            text = stringResource(R.string.history_empty_footer),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF999999),
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
fun HistoryCardItem(
    item: HistoryItem,
    currentDiet: String,
    expandedMenuBarcode: String?,
    onMenuClick: (String?) -> Unit,
    onMenuDismiss: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dietText = if (item.isMock) {
        item.dietName
    } else {
        if (item.dietName.uppercase()
                .endsWith("DIET")
        ) item.dietName.uppercase() else "${item.dietName.uppercase()} DIET"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food product illustration box (with emojis as premium icons)
            ProductImage(imageUrl = item.imageUrl, isSatvik = item.isSatvik, imageIndex = item.imageIndex)

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.productName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Safe/Avoid badge with light container background
                    val badgeBg = if (item.isSatvik) Color(0xFFD4F3E1) else Color(0xFFFFDFDF)
                    val badgeTextColor = if (item.isSatvik) Color(0xFF27AE60) else Color(0xFFEB5757)
                    val badgeText = if (item.isSatvik) "SAFE" else "AVOID"

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = badgeTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Date Time row with history clock outline
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_history_24),
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = formatTimestamp(item.timestamp),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Saffron Sparkle logo and Diet restriction category
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "Diet Context",
                        tint = Color(0xFFE2A03F),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = dietText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8D6E63),
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Three-dot Options Menu for deleting scan history
            Box {
                IconButton(onClick = { onMenuClick(item.barcode) }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Gray
                    )
                }

                DropdownMenu(
                    expanded = expandedMenuBarcode == item.barcode,
                    onDismissRequest = { onMenuDismiss() },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFC62828)
                                )
                                Text(
                                    text = stringResource(R.string.history_menu_delete),
                                    color = Color(0xFFC62828),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        onClick = {
                            onMenuDismiss()
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}

private val satvikOcrDrawables = listOf(
    R.drawable.satvik1,
    R.drawable.satvik2,
    R.drawable.satvik3,
    R.drawable.safe_foodpacket
)
private val avoidOcrDrawables = listOf(
    R.drawable.nonsatvik1,
    R.drawable.nonsatvik2,
    R.drawable.nonsatvik3,
    R.drawable.avoid_foodpacket,
    R.drawable.foodpacket
)

@Composable
fun ProductImage(imageUrl: String?, isSatvik: Boolean, imageIndex: Int) {

    // Select drawable based on the database-stored index
    val ocrList = if (isSatvik) satvikOcrDrawables else avoidOcrDrawables
    val placeholderRes = ocrList[imageIndex % ocrList.size]

    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSatvik) Color(0xFFF0FDF4) else Color(0xFFFFFBFA)),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                placeholder = painterResource(id = placeholderRes),
                error = painterResource(id = placeholderRes),
                contentDescription = "History Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = placeholderRes),
                contentDescription = "Default Product Image",
                modifier = Modifier.fillMaxSize()

            )
        }
    }
}

@Composable
fun PromoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFE8F2EC))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 64.dp), // make space for watermark
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.history_promo_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B3A2D),
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.history_promo_subtitle),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF335C4A),
                lineHeight = 18.sp
            )
        }

        // Elegant semi-transparent Leaf Watermark outline on the bottom right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_safe_tick),
                contentDescription = null,
                tint = Color(0xFF1B3A2D).copy(alpha = 0.08f),
                modifier = Modifier.size(96.dp)
            )
        }
    }
}

// Format Unix Timestamp to matched mockup format e.g. Oct 24, 2:30 PM
fun formatTimestamp(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        "Just now"
    }
}

@Composable
fun ClearHistoryDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Red trash bin icon inside circle
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(color = Color(0xFFFFECEF), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Title
                Text(
                    text = stringResource(R.string.history_dialog_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = stringResource(R.string.history_dialog_desc),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Confirm Button (Clear All)
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text(
                        text = stringResource(R.string.history_dialog_confirm),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dismiss Button (Cancel)
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEAEAEA),
                        contentColor = Color(0xFF555555)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.history_dialog_cancel),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}