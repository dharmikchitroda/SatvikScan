package com.example.satvikscan.ui

import android.content.ContentValues
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.e
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satvikscan.R
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

// Guideline detail model with resource IDs for localization
data class GuidelineSection(
    val titleRes: Int,
    val subtitleRes: Int,
    val iconRes: Int,
    val religiousTextRes: Int,
    val scientificTextRes: Int,
    val chipsRes: List<Int> = emptyList(),
    val showImage: Boolean = false
)

// Helper function: load string from strings.xml for a specific language (en / gu / hi)
fun getLocalizedText(context: Context, lang: String, resId: Int): String {
    val locale = Locale(lang.ifEmpty { "en" })
    val configuration = Configuration(context.resources.configuration).apply {
        setLocale(locale)
    }
    return context.createConfigurationContext(configuration).getString(resId)
}

@OptIn(ExperimentalLayoutApi::class)

@Composable
fun DietGyanScreen(
    dietType: String, // "Jain" or "Swaminarayan"
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: com.example.satvikscan.viewmodel.SatvikViewModel = viewModel()
) {
    val context = LocalContext.current

    // Connected to global ViewModel — reflects user's saved language choice
    val globalLang by viewModel.appLanguage.collectAsState()

    // Local override for this screen's language toggle
    // Starts from global setting, user can change just for this screen
    var currentLang by remember(globalLang) { mutableStateOf(globalLang) }

    // Theme & Colors Setup
    val primaryColor = if (dietType == "Jain") Color(0xFF2E7D32) else Color(0xFFD35400)
    val accentColor = Color(0xFFD4AF37) // Gold/Amber accent
    val surfaceColor = Color(0xFFFEFDFB)
    val textColor = Color(0xFF1F1F1F)
    val subTextColor = Color(0xFF666666)
    val backgroundColor = Color(0xFFFDFBF7)

    // Setup Localized Headings & Taglines — using currentLang (en/gu/hi)
    val topbarTitle = getLocalizedText(
        context, currentLang,
        if (dietType == "Jain") R.string.diet_gyan_title_jain else R.string.diet_gyan_title_swami
    )
    val badgeText = getLocalizedText(
        context, currentLang,
        if (dietType == "Jain") R.string.diet_gyan_badge_jain else R.string.diet_gyan_badge_swami
    )
    val taglineText = getLocalizedText(
        context, currentLang,
        if (dietType == "Jain") R.string.diet_gyan_tagline_jain else R.string.diet_gyan_tagline_swami
    )
    val religiousHeader = getLocalizedText(context, currentLang, R.string.diet_gyan_label_religious)
    val scientificHeader = getLocalizedText(context, currentLang, R.string.diet_gyan_label_scientific)
    val footerText = getLocalizedText(
        context, currentLang,
        if (dietType == "Jain") R.string.diet_gyan_footer_jain else R.string.diet_gyan_footer_swami
    )
    val footerTagline = getLocalizedText(context, currentLang, R.string.diet_gyan_footer_tagline)

    // Guidelines List mapped to resource IDs
    val sections = remember(dietType) {
        if (dietType == "Jain") {
            listOf(
                // 1. Root Vegetables
                GuidelineSection(
                    titleRes = R.string.jain_root_title,
                    subtitleRes = R.string.jain_root_subtitle,
                    iconRes = R.drawable.ic_root_vegetable,
                    religiousTextRes = R.string.jain_root_rel,
                    scientificTextRes = R.string.jain_root_sci
                ),
                // 2. Vinegar
                GuidelineSection(
                    titleRes = R.string.jain_vinegar_title,
                    subtitleRes = R.string.jain_vinegar_subtitle,
                    iconRes = R.drawable.ic_vinegar,
                    religiousTextRes = R.string.jain_vinegar_rel,
                    scientificTextRes = R.string.jain_vinegar_sci
                ),
                // 3. Citric Acid E330
                GuidelineSection(
                    titleRes = R.string.jain_citric_title,
                    subtitleRes = R.string.jain_citric_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.jain_citric_rel,
                    scientificTextRes = R.string.jain_citric_sci
                ),
                // 4. Ajinomoto / MSG E621
                GuidelineSection(
                    titleRes = R.string.jain_msg_title,
                    subtitleRes = R.string.jain_msg_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.jain_msg_rel,
                    scientificTextRes = R.string.jain_msg_sci
                ),
                // 5. Sabudana
                GuidelineSection(
                    titleRes = R.string.jain_sabudana_title,
                    subtitleRes = R.string.jain_sabudana_subtitle,
                    iconRes = R.drawable.ic_powder,
                    religiousTextRes = R.string.jain_sabudana_rel,
                    scientificTextRes = R.string.jain_sabudana_sci
                ),
                // 6. Mahavigai (Honey, Meat, Butter, Alcohol)
                GuidelineSection(
                    titleRes = R.string.jain_mahavigai_title,
                    subtitleRes = R.string.jain_mahavigai_subtitle,
                    iconRes = R.drawable.ic_honey,
                    religiousTextRes = R.string.jain_mahavigai_rel,
                    scientificTextRes = R.string.jain_mahavigai_sci
                ),
                // 7. Stale Paneer
                GuidelineSection(
                    titleRes = R.string.jain_paneer_title,
                    subtitleRes = R.string.jain_paneer_subtitle,
                    iconRes = R.drawable.ic_cheese,
                    religiousTextRes = R.string.jain_paneer_rel,
                    scientificTextRes = R.string.jain_paneer_sci
                ),
                // 8. Cheese
                GuidelineSection(
                    titleRes = R.string.jain_cheese_title,
                    subtitleRes = R.string.jain_cheese_subtitle,
                    iconRes = R.drawable.ic_cheese,
                    religiousTextRes = R.string.jain_cheese_rel,
                    scientificTextRes = R.string.jain_cheese_sci
                ),
                // 9. Fig (Anjeer)
                GuidelineSection(
                    titleRes = R.string.jain_fig_title,
                    subtitleRes = R.string.jain_fig_subtitle,
                    iconRes = R.drawable.ic_root_vegetable,
                    religiousTextRes = R.string.jain_fig_rel,
                    scientificTextRes = R.string.jain_fig_sci
                ),
                // 10. Poppy Seeds
                GuidelineSection(
                    titleRes = R.string.jain_poppy_title,
                    subtitleRes = R.string.jain_poppy_subtitle,
                    iconRes = R.drawable.ic_poppy_seeds,
                    religiousTextRes = R.string.jain_poppy_rel,
                    scientificTextRes = R.string.jain_poppy_sci
                ),
                // 11. Makhana
                GuidelineSection(
                    titleRes = R.string.jain_makhana_title,
                    subtitleRes = R.string.jain_makhana_subtitle,
                    iconRes = R.drawable.ic_powder,
                    religiousTextRes = R.string.jain_makhana_rel,
                    scientificTextRes = R.string.jain_makhana_sci
                ),
                // 12. Water Chestnut (Singhoda)
                GuidelineSection(
                    titleRes = R.string.jain_singhoda_title,
                    subtitleRes = R.string.jain_singhoda_subtitle,
                    iconRes = R.drawable.ic_root_vegetable,
                    religiousTextRes = R.string.jain_singhoda_rel,
                    scientificTextRes = R.string.jain_singhoda_sci
                ),
                // 13. Arrowroot
                GuidelineSection(
                    titleRes = R.string.jain_arrowroot_title,
                    subtitleRes = R.string.jain_arrowroot_subtitle,
                    iconRes = R.drawable.ic_powder,
                    religiousTextRes = R.string.jain_arrowroot_rel,
                    scientificTextRes = R.string.jain_arrowroot_sci
                ),
                // 14. Chocolate & Cocoa
                GuidelineSection(
                    titleRes = R.string.jain_chocolate_title,
                    subtitleRes = R.string.jain_chocolate_subtitle,
                    iconRes = R.drawable.ic_chocolate,
                    religiousTextRes = R.string.jain_chocolate_rel,
                    scientificTextRes = R.string.jain_chocolate_sci
                ),
                // 15. Maida
                GuidelineSection(
                    titleRes = R.string.jain_maida_title,
                    subtitleRes = R.string.jain_maida_subtitle,
                    iconRes = R.drawable.ic_powder,
                    religiousTextRes = R.string.jain_maida_rel,
                    scientificTextRes = R.string.jain_maida_sci
                ),
                // 16. Rava / Suji
                GuidelineSection(
                    titleRes = R.string.jain_rava_title,
                    subtitleRes = R.string.jain_rava_subtitle,
                    iconRes = R.drawable.ic_powder,
                    religiousTextRes = R.string.jain_rava_rel,
                    scientificTextRes = R.string.jain_rava_sci
                ),
                // 17. Dry Fruits (Seasonal)
                GuidelineSection(
                    titleRes = R.string.jain_dryfruits_title,
                    subtitleRes = R.string.jain_dryfruits_subtitle,
                    iconRes = R.drawable.ic_root_vegetable,
                    religiousTextRes = R.string.jain_dryfruits_rel,
                    scientificTextRes = R.string.jain_dryfruits_sci
                ),
                // 18. E471/E472/E422/E570/E627
                GuidelineSection(
                    titleRes = R.string.jain_additives_veg_title,
                    subtitleRes = R.string.jain_additives_veg_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.jain_additives_veg_rel,
                    scientificTextRes = R.string.jain_additives_veg_sci,
                    chipsRes = listOf(
                        R.string.jain_additives_veg_chip1,
                        R.string.jain_additives_veg_chip2
                    )
                ),
                // 19. E1105 Lysozyme
                GuidelineSection(
                    titleRes = R.string.jain_lysozyme_title,
                    subtitleRes = R.string.jain_lysozyme_subtitle,
                    iconRes = R.drawable.ic_egg,
                    religiousTextRes = R.string.jain_lysozyme_rel,
                    scientificTextRes = R.string.jain_lysozyme_sci
                ),
                // 20. E901 Beeswax
                GuidelineSection(
                    titleRes = R.string.jain_beeswax_title,
                    subtitleRes = R.string.jain_beeswax_subtitle,
                    iconRes = R.drawable.ic_honey,
                    religiousTextRes = R.string.jain_beeswax_rel,
                    scientificTextRes = R.string.jain_beeswax_sci
                ),
                // 21. E120/E441/E542/E904
                GuidelineSection(
                    titleRes = R.string.jain_additives_animal_title,
                    subtitleRes = R.string.jain_additives_animal_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.jain_additives_animal_rel,
                    scientificTextRes = R.string.jain_additives_animal_sci,
                    chipsRes = listOf(
                        R.string.jain_additives_animal_chip1,
                        R.string.jain_additives_animal_chip2
                    )
                ),
                // 22. E631/E635
                GuidelineSection(
                    titleRes = R.string.jain_flavor_enhancers_title,
                    subtitleRes = R.string.jain_flavor_enhancers_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.jain_flavor_enhancers_rel,
                    scientificTextRes = R.string.jain_flavor_enhancers_sci,
                    chipsRes = listOf(R.string.jain_flavor_enhancers_chip1)
                ),
                // 23. All Alcohol & Spirits
                GuidelineSection(
                    titleRes = R.string.jain_alcohol_title,
                    subtitleRes = R.string.jain_alcohol_subtitle,
                    iconRes = R.drawable.ic_vinegar,
                    religiousTextRes = R.string.jain_alcohol_rel,
                    scientificTextRes = R.string.jain_alcohol_sci
                ),
                // 24. Meat, Eggs & Seafood
                GuidelineSection(
                    titleRes = R.string.jain_meat_title,
                    subtitleRes = R.string.jain_meat_subtitle,
                    iconRes = R.drawable.ic_meat,
                    religiousTextRes = R.string.jain_meat_rel,
                    scientificTextRes = R.string.jain_meat_sci,
                    chipsRes = listOf(R.string.jain_meat_chip1, R.string.jain_meat_chip2)
                )
            )
        } else {
            listOf(
                // 1. Meat, Eggs & Non-Veg
                GuidelineSection(
                    titleRes = R.string.swami_meat_title,
                    subtitleRes = R.string.swami_meat_subtitle,
                    iconRes = R.drawable.ic_meat,
                    religiousTextRes = R.string.swami_meat_rel,
                    scientificTextRes = R.string.swami_meat_sci,
                    chipsRes = listOf(R.string.swami_meat_chip1, R.string.swami_meat_chip2)
                ),
                // 2. Onion & Garlic (Allium)
                GuidelineSection(
                    titleRes = R.string.swami_allium_title,
                    subtitleRes = R.string.swami_allium_subtitle,
                    iconRes = R.drawable.ic_onion,
                    religiousTextRes = R.string.swami_allium_rel,
                    scientificTextRes = R.string.swami_allium_sci
                ),
                // 3. Alcohol & Spirits
                GuidelineSection(
                    titleRes = R.string.swami_alcohol_title,
                    subtitleRes = R.string.swami_alcohol_subtitle,
                    iconRes = R.drawable.ic_vinegar,
                    religiousTextRes = R.string.swami_alcohol_rel,
                    scientificTextRes = R.string.swami_alcohol_sci
                ),
                // 4. Cannabis & Narcotics
                GuidelineSection(
                    titleRes = R.string.swami_narcotics_title,
                    subtitleRes = R.string.swami_narcotics_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.swami_narcotics_rel,
                    scientificTextRes = R.string.swami_narcotics_sci,
                    chipsRes = listOf(
                        R.string.swami_narcotics_chip1,
                        R.string.swami_narcotics_chip2
                    )
                ),
                // 5. Animal-derived Additives
                GuidelineSection(
                    titleRes = R.string.swami_additives_animal_title,
                    subtitleRes = R.string.swami_additives_animal_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.swami_additives_animal_rel,
                    scientificTextRes = R.string.swami_additives_animal_sci,
                    chipsRes = listOf(
                        R.string.swami_additives_animal_chip1,
                        R.string.swami_additives_animal_chip2
                    )
                ),
                // 6. Source-dependent Additives
                GuidelineSection(
                    titleRes = R.string.swami_additives_source_title,
                    subtitleRes = R.string.swami_additives_source_subtitle,
                    iconRes = R.drawable.ic_chemical,
                    religiousTextRes = R.string.swami_additives_source_rel,
                    scientificTextRes = R.string.swami_additives_source_sci,
                    chipsRes = listOf(
                        R.string.swami_additives_source_chip1,
                        R.string.swami_additives_source_chip2
                    )
                ),
                // 7. Hing (Asafoetida)
                GuidelineSection(
                    titleRes = R.string.swami_hing_title,
                    subtitleRes = R.string.swami_hing_subtitle,
                    iconRes = R.drawable.ic_turmeric,
                    religiousTextRes = R.string.swami_hing_rel,
                    scientificTextRes = R.string.swami_hing_sci
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        // Topbar (Back button, Title, Download PDF button, Diet Logo)
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
                    tint = textColor
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = topbarTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

            // PDF Download Icon
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .clickable {
                        //  toast show download start

                        Toast.makeText(
                            context,
                            getLocalizedText(context, currentLang, R.string.diet_gyan_toast_downloading),
                            Toast.LENGTH_SHORT
                        ).show()

                        //  Diet Type and selected Language → asset file select
                        val assetFileName = when {
                            dietType == "Jain" && currentLang == "gu" -> "jain_gujrati.pdf"
                            dietType == "Jain" && currentLang == "hi" -> "jain_hindi.pdf"
                            dietType == "Jain" -> "jain_english.pdf"
                            currentLang == "gu" -> "swaminarayan_gujrati.pdf"
                            currentLang == "hi" -> "swaminarayan_hindi.pdf"
                            else -> "swaminarayan_english.pdf"
                        }

                        // Downloaded file public name set
                        val outputFileName = when {
                            dietType == "Jain" && currentLang == "gu" -> "Jain_Diet_Guide_Gujarati.pdf"
                            dietType == "Jain" && currentLang == "hi" -> "Jain_Diet_Guide_Hindi.pdf"
                            dietType == "Jain" -> "Jain_Diet_Guide_English.pdf"
                            currentLang == "gu" -> "Swaminarayan_Diet_Guide_Gujarati.pdf"
                            currentLang == "hi" -> "Swaminarayan_Diet_Guide_Hindi.pdf"
                            else -> "Swaminarayan_Diet_Guide_English.pdf"
                        }

                        //  File write trigger karei
                        val success = savePdfToDownloads(context, assetFileName, outputFileName)

                        // outcome Toast show of user
                        if (success) {
                            Toast.makeText(
                                context,
                                when (currentLang) {
                                    "gu" -> "માર્ગદર્શિકા ડાઉનલોડ થઈ ગઈ!"
                                    "hi" -> "मार्गदर्शिका डाउनलोड हो गई!"
                                    else -> "Guide PDF downloaded successfully!"
                                },
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                when (currentLang) {
                                    "gu" -> "ડાઉનલોડ કરવામાં ભૂલ આવી!"
                                    "hi" -> "डाउनलोड में त्रुटि हुई!"
                                    else -> "Failed to download PDF"
                                },
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download), // download icon
                    contentDescription = "Download Guide PDF",
                    tint = Color.Black,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PDF",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Diet Logo
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        primaryColor.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = if (dietType == "Jain") R.drawable.jain_logo else R.drawable.swaminarayan_tilak
                    ),
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Language Toggle — 3 buttons: EN / GU / HI
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFEBEBEB),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // English
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (currentLang == "en") accentColor else Color.Transparent)
                            .clickable { currentLang = "en" }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "English",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentLang == "en") Color.White else subTextColor
                        )
                    }

                    // Gujarati
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (currentLang == "gu") accentColor else Color.Transparent)
                            .clickable { currentLang = "gu" }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "ગુજરાતી",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentLang == "gu") Color.White else subTextColor
                        )
                    }

                    // Hindi
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (currentLang == "hi") accentColor else Color.Transparent)
                            .clickable { currentLang = "hi" }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "हिन्दी",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentLang == "hi") Color.White else subTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Wisdom Badge
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = accentColor.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline
                Text(
                    text = taglineText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            items(sections) { section ->
                val title = getLocalizedText(context, currentLang, section.titleRes)
                val subtitle = getLocalizedText(context, currentLang, section.subtitleRes)
                val religiousText = getLocalizedText(context, currentLang, section.religiousTextRes)
                val scientificText = getLocalizedText(context, currentLang, section.scientificTextRes)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFF0EFEA)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Title + Subtitle Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = Color(0xFFF9F1D8), // gold circular background as in mockup
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = section.iconRes),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = subtitle,
                                    fontSize = 13.sp,
                                    color = subTextColor
                                )
                            }
                        }

                        // Add Chips if present
                        if (section.chipsRes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                section.chipsRes.forEach { chipRes ->
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                primaryColor.copy(alpha = 0.07f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                width = 0.5.dp,
                                                color = primaryColor.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText(context, currentLang, chipRes),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryColor
                                        )
                                    }
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Religious Reason Box with curved left border (starts at full width/aligned with header icon)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFFAF6EE),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .drawBehind {
                                    val radius = 16.dp.toPx()
                                    val barWidth = 4.dp.toPx()
                                    val halfBar = barWidth / 2f
                                    val clipWidth = radius + 8.dp.toPx() // ~24.dp clip width
                                    clipRect(right = clipWidth) {
                                        drawRoundRect(
                                            color = accentColor,
                                            topLeft = Offset(halfBar, halfBar),
                                            size = Size(
                                                size.width - barWidth,
                                                size.height - barWidth
                                            ),
                                            cornerRadius = CornerRadius(
                                                radius - halfBar,
                                                radius - halfBar
                                            ),
                                            style = Stroke(width = barWidth)
                                        )
                                    }
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = religiousHeader,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = religiousText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic,
                                color = textColor,
                                lineHeight = 19.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Scientific Origin Box
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFFAF6EE),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .drawBehind {
                                    val radius = 16.dp.toPx()
                                    val barWidth = 4.dp.toPx()
                                    val halfBar = barWidth / 2f
                                    val clipWidth = radius + 8.dp.toPx() // ~24.dp clip width
                                    clipRect(right = clipWidth) {
                                        drawRoundRect(
                                            color = accentColor,
                                            topLeft = Offset(halfBar, halfBar),
                                            size = Size(size.width - barWidth, size.height - barWidth),
                                            cornerRadius = CornerRadius(radius - halfBar, radius - halfBar),
                                            style = Stroke(width = barWidth)
                                        )
                                    }
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = scientificHeader,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scientificText,
                                fontSize = 14.sp,
                                color = textColor,
                                lineHeight = 20.sp
                            )
                        }

                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Footer
                Text(
                    text = footerText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = accentColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = footerTagline,
                    fontSize = 12.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

fun savePdfToDownloads(context: Context, assetName: String, outputFileName: String): Boolean {
    val resolver = context.contentResolver

    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, outputFileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    context.assets.open(assetName).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                true
            } else {
                false
            }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, outputFileName)
            FileOutputStream(file).use { outputStream ->
                context.assets.open(assetName).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
