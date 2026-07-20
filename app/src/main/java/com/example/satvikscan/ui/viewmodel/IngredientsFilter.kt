package com.example.satvikscan.ui.viewmodel

import com.example.satvikscan.ui.IngredientData
import com.example.satvikscan.ui.IngredientType

fun IngredientsFilter(
    rawIngredientData: String?,
    currentDiet: String,
    customExclusions: Set<String>
): List<IngredientData> {

    // check if the list is empty
    if (rawIngredientData.isNullOrBlank()) return emptyList()

    // filter the string and split them
    val rawList = rawIngredientData.split(",", ";", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    // store the detected ingredients
    val DetectedItems = mutableListOf<IngredientData>()

    // Regex for strict check
    val nonVegRegex = Regex(
        "\\b(meat|chicken|fish|seafood|prawn|shrimp|crab|oyster|pork|beef|mutton|lamb|turkey|egg|eggs|egg\\s+white|yolk|gelatin|gelatine|lard|albumin|carmine|cochineal|shellac|beeswax|lysozyme|animal|bone)\\b|" +
        "\\b(E|INS)\\s*-?\\s*(120|441|542|901|904|1105)\\b",
        RegexOption.IGNORE_CASE
    )

    val alliumRegex = Regex(
        "\\b(onion|dungri|pyaj|pyaz|garlic|lasan|lahsun|leek|shallot|chive|chives)\\b",
        RegexOption.IGNORE_CASE
    )

    val alcoholIntoxicantRegex = Regex(
        "\\b(alcohol|wine|beer|rum|whisky|whiskey|brandy|vodka|gin|spirit|spirits|ethanol|cannabis|bhang|marijuana|ganja|opium|hashish|narcotic|intoxicant)\\b",
        RegexOption.IGNORE_CASE
    )

    val suspectAdditivesRegex = Regex(
        "\\b(lecithin|glycerin|glycerol|mono\\s*-?\\s*and\\s*-?\\s*diglycerides|mono\\s*&\\s*diglycerides|fatty\\s+acid|disodium\\s+guanylate|disodium\\s+inosinate|disodium\\s+ribonucleotides|l-cysteine|cysteine)\\b|" +
        "\\b(E|INS)\\s*-?\\s*(322|422|471|472[a-f]?|570|627|631|635|920)\\b",
        RegexOption.IGNORE_CASE
    )

    val hingRegex = Regex(
        "\\b(asafoetida|hing)\\b",
        RegexOption.IGNORE_CASE
    )

    val otherRootVegRegex = Regex(
        "\\b(potato|potatoes|sweet\\s+potato|sweet\\s+potatoes|carrot|carrots|radish|radishes|beetroot|beetroots|ginger|turmeric|yam|yams|elephant\\s+foot\\s+yam|suran|root\\s+vegetable|root\\s+vegetables|tapioca|tapioca\\s+pearls|cassava|sabudana|arrowroot)\\b",
        RegexOption.IGNORE_CASE
    )

    val honeyRegex = Regex(
        "\\b(honey|shehad|madh)\\b",
        RegexOption.IGNORE_CASE
    )

    // Jain Specific Regexes
    val jainUdumbaraRegex = Regex(
        "\\b(fig|figs|anjeer|udumbara)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainPoppyRegex = Regex(
        "\\b(poppy\\s+seed|poppy\\s+seeds|khus\\s*khus|khuskhus)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainAquaticPlantsRegex = Regex(
        "\\b(makhana|fox\\s+nut|fox\\s+nuts|water\\s+chestnut|water\\s+chestnuts|singhara|singhada)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainButterRegex = Regex(
        "\\b(butter|makhan)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainFermentedRegex = Regex(
        "\\b(vinegar|sirka|cheese|paneer|cottage\\s+cheese|rennet)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainFermentedAdditivesRegex = Regex(
        "\\b(citric\\s+acid|monosodium\\s+glutamate|msg|ajinomoto)\\b|\\b(E|INS)\\s*-?\\s*(330|621)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainProcessedWheatRegex = Regex(
        "\\b(maida|refined\\s+flour|refined\\s+wheat\\s+flour|suji|sooji|rava|semolina)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainCocoaRegex = Regex(
        "\\b(chocolate|cocoa|cocoa\\s+powder|cacao)\\b",
        RegexOption.IGNORE_CASE
    )

    val jainDryFruitsRegex = Regex(
        "\\b(almond|almonds|badam|cashew|cashews|kaju|pistachio|pistachios|pista|walnut|walnuts|akhrot|dry\\s+fruit|dry\\s+fruits)\\b",
        RegexOption.IGNORE_CASE
    )

    // check available items are restricted using the loop
    for (ingredient in rawList) {
        val nameLower = ingredient.lowercase()

        // 1. Check Custom Exclusions first
        if (currentDiet == "Custom") {
            val matchedExclusion = customExclusions.find { nameLower.contains(it) }
            if (matchedExclusion != null) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Matches custom excluded ingredient '$matchedExclusion'",
                        type = IngredientType.OTHER,
                        scientificReason = "Personally excluded ingredient based on your custom diet preferences."
                    )
                )
            }
            continue
        }

        // 2. Standard Diets: Swaminarayan, Jain
        // Check Non-Veg (restricted in all three)
        if (nonVegRegex.containsMatchIn(nameLower)) {
            val reasonStr = when (currentDiet) {
                "Swaminarayan" -> "Avoided: Animal-derived food/meat is strictly prohibited (Shikshapatri Verse 15)"
                "Jain" -> "Avoided: Animal-derived ingredients are strictly prohibited in Jain diet (Ahimsa)"
                else -> "Avoided: Non-vegetarian origin ingredient detected"
            }
            DetectedItems.add(
                IngredientData(
                    name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    reason = reasonStr,
                    type = IngredientType.NON_VEG_ORIGIN,
                    scientificReason = "Derived from animal, poultry, or marine sources. Includes meat, eggs, gelatin (animal collagen), carmine (cochineal insect), shellac (lac insect resin), and beeswax."
                )
            )
            continue
        }

        // Check Alcohol & Intoxicants (restricted in all three)
        if (alcoholIntoxicantRegex.containsMatchIn(nameLower)) {
            val reasonStr = when (currentDiet) {
                "Swaminarayan" -> "Avoided: Alcohol/intoxicating substances are strictly prohibited (Shikshapatri Verses 15 & 18)"
                "Jain" -> "Avoided: Alcohol/intoxicants are restricted in Jain diet due to fermentation/violence"
                else -> "Avoided: Intoxicating substance/alcohol detected"
            }
            DetectedItems.add(
                IngredientData(
                    name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    reason = reasonStr,
                    type = IngredientType.OTHER,
                    scientificReason = "Produced by fermentation of sugars, grains, or fruits yielding ethanol. Cannabis and narcotics are psychoactive substances that impair neurological functions."
                )
            )
            continue
        }

        // Check Allium Family (restricted in Swaminarayan, Jain)
        if (alliumRegex.containsMatchIn(nameLower)) {
            val reasonStr = when (currentDiet) {
                "Swaminarayan" -> "Avoided: Onion, garlic, and Allium family are Rajasic/Tamasic and restricted (Shikshapatri & BAPS/Vadtal guidance)"
                "Jain" -> "Avoided: Onion, garlic, and Allium family are root vegetables, restricted in Jain diet"
                else -> "Avoided: Allium family ingredient (onion/garlic) detected"
            }
            DetectedItems.add(
                IngredientData(
                    name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    reason = reasonStr,
                    type = IngredientType.ROOT_VEGETABLE,
                    scientificReason = "Plants of the Allium family (Allium cepa, Allium sativum). Rich in organosulfur compounds responsible for their characteristic pungent aroma."
                )
            )
            continue
        }

        // Check Suspect Additives (caution in Swaminarayan, Jain)
        if (suspectAdditivesRegex.containsMatchIn(nameLower)) {
            val reasonStr = "Caution: May be derived from animal sources. Swaminarayan and Jain traditions advise verifying the source."
            DetectedItems.add(
                IngredientData(
                    name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    reason = reasonStr,
                    type = IngredientType.OTHER,
                    scientificReason = "Industrially produced food additives (E322 Lecithin, E471 Mono-diglycerides, E570 Fatty acids, E920 L-Cysteine) that may be derived from vegetable oils, animal fats, or eggs depending on manufacturer."
                )
            )
            continue
        }

        // Diet-specific checks: Swaminarayan
        if (currentDiet == "Swaminarayan") {
            if (hingRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Asafoetida (Hing) is avoided in some Swaminarayan traditions as it is considered Rajasic/Tamasic.",
                        type = IngredientType.OTHER,
                        scientificReason = "Oleogum resin from Ferula plant roots. Commercial Hing often contains wheat flour, rice flour, or gum arabic as carrier/filler substances."
                    )
                )
                continue
            }
        }

        // Diet-specific checks: Jain
        if (currentDiet == "Jain") {
            if (otherRootVegRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Root vegetables (Anantkaya) are restricted in Jain diet",
                        type = IngredientType.ROOT_VEGETABLE,
                        scientificReason = "Underground storage organs (bulbs, tubers, rhizomes) that harbour entire colonies of micro-organisms. Uprooting destroys the complete plant."
                    )
                )
                continue
            }
            if (honeyRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Honey is restricted in Jain diet (Mahavigai principle)",
                        type = IngredientType.OTHER,
                        scientificReason = "Natural sweet substance produced by honeybees (Apis spp.) from flower nectar. Its collection involves destruction of micro-organisms within the hive."
                    )
                )
                continue
            }
            if (jainButterRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Butter is considered Mahavigai in Jain tradition (avoided due to rapid microorganism growth)",
                        type = IngredientType.OTHER,
                        scientificReason = "Dairy fat separated from milk by churning. Fresh butter becomes stale rapidly due to microbial activity, leading to proliferation of micro-organisms."
                    )
                )
                continue
            }
            if (jainUdumbaraRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Fig (Anjeer) is an Udumbara fruit containing microscopic life/wasps, prohibited in Jain diet",
                        type = IngredientType.OTHER,
                        scientificReason = "Ficus carica — a syconium-type fruit whose pollination requires a symbiotic life cycle with the Fig Wasp (Blastophaga psenes) inside the fruit."
                    )
                )
                continue
            }
            if (jainPoppyRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Poppy seeds (Khus Khus) contain innumerable seeds and are avoided in Jain diet",
                        type = IngredientType.OTHER,
                        scientificReason = "Dried mature seeds of Papaver somniferum. Each pod contains thousands of tiny seeds (Bahubiji), making them multi-seeded. Seeds may also contain trace opioid compounds."
                    )
                )
                continue
            }
            if (jainAquaticPlantsRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Makhana/Water Chestnut grows in water bodies; harvesting may harm aquatic life. Avoided by some Acharyas.",
                        type = IngredientType.OTHER,
                        scientificReason = "Euryale ferox (Makhana) / Trapa spp. (Singhara) — aquatic plants growing in ponds and lakes. Harvesting disturbs and potentially destroys aquatic ecosystems."
                    )
                )
                continue
            }
            if (jainFermentedRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Fermented product (Vinegar/Cheese/Paneer). Cheese may contain animal rennet; verify source.",
                        type = IngredientType.OTHER,
                        scientificReason = "Vinegar: produced by Acetic Acid Bacteria fermenting alcohol. Cheese: coagulated milk using rennet (which can be animal, microbial, or vegetarian-sourced)."
                    )
                )
                continue
            }
            if (jainFermentedAdditivesRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Citric Acid (E330) / MSG (E621) are produced via fermentation. Verification of source recommended.",
                        type = IngredientType.OTHER,
                        scientificReason = "Citric Acid (E330): naturally from citrus fruits or industrially via Aspergillus niger fermentation. MSG (E621): produced by bacterial fermentation of starch, sugarcane, or molasses."
                    )
                )
                continue
            }
            if (jainProcessedWheatRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Refined flour (Maida) / Semolina (Rava/Suji) are wheat products; avoidance recommended by some Maharaj Sahebs.",
                        type = IngredientType.OTHER,
                        scientificReason = "Maida: finely milled endosperm of wheat, stripped of bran and germ. Rava/Suji: coarsely milled wheat endosperm. Both are refined wheat products with low nutritional fibre."
                    )
                )
                continue
            }
            if (jainCocoaRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Chocolate/Cocoa powder may contain microscopic insect fragments. Avoided by some Maharaj Sahebs.",
                        type = IngredientType.OTHER,
                        scientificReason = "Food products from Theobroma cacao beans. During industrial cocoa processing, regulatory limits allow trace insect fragments (FDA: up to 60 insect fragments per 100g cocoa powder)."
                    )
                )
                continue
            }
            if (jainDryFruitsRegex.containsMatchIn(nameLower)) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Caution: Dry fruits are avoided in some Jain traditions during specific lunar months (Phagan to Kartak).",
                        type = IngredientType.OTHER,
                        scientificReason = "Dried seeds and nuts (almonds, cashews, pistachios, walnuts) of various trees. During rainy seasons, these may harbour micro-organisms more readily as per traditional understanding."
                    )
                )
                continue
            }
        }
    }
    return DetectedItems
}
