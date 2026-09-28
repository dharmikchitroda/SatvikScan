package com.example.satvikscan.viewmodel

import com.example.satvikscan.R
import com.example.satvikscan.ui.IngredientData
import com.example.satvikscan.ui.IngredientType

fun ingredientsFilter(
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
    val detectedItems = mutableListOf<IngredientData>()

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
        val capitalizedName = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        // 1. Check Custom Exclusions first
        if (currentDiet == "Custom") {
            val matchedExclusion = customExclusions.find { nameLower.contains(it) }
            if (matchedExclusion != null) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
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
            val (titleRes, reasonRes, sciRes) = when {
                nameLower.contains("lysozyme") || nameLower.contains("1105") ->
                    Triple(R.string.jain_lysozyme_title, R.string.jain_lysozyme_rel, R.string.jain_lysozyme_sci)
                nameLower.contains("beeswax") || nameLower.contains("901") ->
                    Triple(R.string.jain_beeswax_title, R.string.jain_beeswax_rel, R.string.jain_beeswax_sci)
                nameLower.contains("gelatin") || nameLower.contains("carmine") || nameLower.contains("shellac") ||
                nameLower.contains("120") || nameLower.contains("441") || nameLower.contains("542") || nameLower.contains("904") -> {
                    if (currentDiet == "Swaminarayan") {
                        Triple(R.string.swami_additives_animal_title, R.string.swami_additives_animal_rel, R.string.swami_additives_animal_sci)
                    } else {
                        Triple(R.string.jain_additives_animal_title, R.string.jain_additives_animal_rel, R.string.jain_additives_animal_sci)
                    }
                }
                currentDiet == "Swaminarayan" ->
                    Triple(R.string.swami_meat_title, R.string.swami_meat_rel, R.string.swami_meat_sci)
                currentDiet == "Jain" ->
                    Triple(R.string.jain_meat_title, R.string.jain_meat_rel, R.string.jain_meat_sci)
                else ->
                    Triple(R.string.swami_meat_title, R.string.swami_meat_rel, R.string.swami_meat_sci)
            }

            detectedItems.add(
                IngredientData(
                    name = capitalizedName,
                    reason = "Avoided: Non-vegetarian origin ingredient detected",
                    type = IngredientType.NON_VEG_ORIGIN,
                    scientificReason = "Derived from animal, poultry, or marine sources.",
                    titleRes = titleRes,
                    reasonRes = reasonRes,
                    scientificReasonRes = sciRes
                )
            )
            continue
        }

        // Check Alcohol & Intoxicants (restricted in all three)
        if (alcoholIntoxicantRegex.containsMatchIn(nameLower)) {
            val isNarcotic = nameLower.contains("cannabis") || nameLower.contains("bhang") ||
                    nameLower.contains("marijuana") || nameLower.contains("ganja") ||
                    nameLower.contains("opium") || nameLower.contains("hashish") ||
                    nameLower.contains("narcotic")

            val (titleRes, reasonRes, sciRes) = when {
                currentDiet == "Swaminarayan" && isNarcotic ->
                    Triple(R.string.swami_narcotics_title, R.string.swami_narcotics_rel, R.string.swami_narcotics_sci)
                currentDiet == "Swaminarayan" ->
                    Triple(R.string.swami_alcohol_title, R.string.swami_alcohol_rel, R.string.swami_alcohol_sci)
                currentDiet == "Jain" ->
                    Triple(R.string.jain_alcohol_title, R.string.jain_alcohol_rel, R.string.jain_alcohol_sci)
                else ->
                    Triple(R.string.swami_alcohol_title, R.string.swami_alcohol_rel, R.string.swami_alcohol_sci)
            }

            detectedItems.add(
                IngredientData(
                    name = capitalizedName,
                    reason = "Avoided: Intoxicating substance/alcohol detected",
                    type = IngredientType.OTHER,
                    scientificReason = "Produced by fermentation of sugars, grains, or fruits yielding ethanol.",
                    titleRes = titleRes,
                    reasonRes = reasonRes,
                    scientificReasonRes = sciRes
                )
            )
            continue
        }

        // Check Allium Family (restricted in Swaminarayan, Jain)
        if (alliumRegex.containsMatchIn(nameLower)) {
            val (titleRes, reasonRes, sciRes) = if (currentDiet == "Swaminarayan") {
                Triple(R.string.swami_allium_title, R.string.swami_allium_rel, R.string.swami_allium_sci)
            } else {
                Triple(R.string.jain_root_title, R.string.jain_root_rel, R.string.jain_root_sci)
            }

            detectedItems.add(
                IngredientData(
                    name = capitalizedName,
                    reason = "Avoided: Allium family ingredient (onion/garlic) detected",
                    type = IngredientType.ROOT_VEGETABLE,
                    scientificReason = "Plants of the Allium family containing organosulfur compounds.",
                    titleRes = titleRes,
                    reasonRes = reasonRes,
                    scientificReasonRes = sciRes
                )
            )
            continue
        }

        // Check Suspect Additives (caution in Swaminarayan, Jain)
        if (suspectAdditivesRegex.containsMatchIn(nameLower)) {
            val (titleRes, reasonRes, sciRes) = if (currentDiet == "Swaminarayan") {
                Triple(R.string.swami_additives_source_title, R.string.swami_additives_source_rel, R.string.swami_additives_source_sci)
            } else {
                Triple(R.string.jain_additives_veg_title, R.string.jain_additives_veg_rel, R.string.jain_additives_veg_sci)
            }

            detectedItems.add(
                IngredientData(
                    name = capitalizedName,
                    reason = "Caution: May be derived from animal sources.",
                    type = IngredientType.OTHER,
                    scientificReason = "Industrially produced food additives that may be derived from vegetable oils or animal fats.",
                    titleRes = titleRes,
                    reasonRes = reasonRes,
                    scientificReasonRes = sciRes
                )
            )
            continue
        }

        // Diet-specific checks: Swaminarayan
        if (currentDiet == "Swaminarayan") {
            if (hingRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Asafoetida (Hing) is avoided in some Swaminarayan traditions.",
                        type = IngredientType.OTHER,
                        scientificReason = "Oleogum resin from Ferula plant roots.",
                        titleRes = R.string.swami_hing_title,
                        reasonRes = R.string.swami_hing_rel,
                        scientificReasonRes = R.string.swami_hing_sci
                    )
                )
                continue
            }
        }

        // Diet-specific checks: Jain
        if (currentDiet == "Jain") {
            if (otherRootVegRegex.containsMatchIn(nameLower)) {
                val (titleRes, reasonRes, sciRes) = when {
                    nameLower.contains("sabudana") || nameLower.contains("tapioca") || nameLower.contains("cassava") ->
                        Triple(R.string.jain_sabudana_title, R.string.jain_sabudana_rel, R.string.jain_sabudana_sci)
                    nameLower.contains("arrowroot") ->
                        Triple(R.string.jain_arrowroot_title, R.string.jain_arrowroot_rel, R.string.jain_arrowroot_sci)
                    else ->
                        Triple(R.string.jain_root_title, R.string.jain_root_rel, R.string.jain_root_sci)
                }

                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Avoided: Root vegetables (Anantkaya) are restricted in Jain diet",
                        type = IngredientType.ROOT_VEGETABLE,
                        scientificReason = "Underground storage organs harbouring countless micro-organisms.",
                        titleRes = titleRes,
                        reasonRes = reasonRes,
                        scientificReasonRes = sciRes
                    )
                )
                continue
            }
            if (honeyRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Avoided: Honey is restricted in Jain diet (Mahavigai principle)",
                        type = IngredientType.OTHER,
                        scientificReason = "Sweet substance produced by bees; classified under Mahavigai.",
                        titleRes = R.string.jain_mahavigai_title,
                        reasonRes = R.string.jain_mahavigai_rel,
                        scientificReasonRes = R.string.jain_mahavigai_sci
                    )
                )
                continue
            }
            if (jainButterRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Avoided: Butter is considered Mahavigai in Jain tradition",
                        type = IngredientType.OTHER,
                        scientificReason = "Dairy fat prone to rapid microbial growth; classified under Mahavigai.",
                        titleRes = R.string.jain_mahavigai_title,
                        reasonRes = R.string.jain_mahavigai_rel,
                        scientificReasonRes = R.string.jain_mahavigai_sci
                    )
                )
                continue
            }
            if (jainUdumbaraRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Avoided: Fig (Anjeer) is an Udumbara fruit prohibited in Jain diet",
                        type = IngredientType.OTHER,
                        scientificReason = "Ficus carica — a syconium fruit with a symbiotic life cycle involving fig wasps.",
                        titleRes = R.string.jain_fig_title,
                        reasonRes = R.string.jain_fig_rel,
                        scientificReasonRes = R.string.jain_fig_sci
                    )
                )
                continue
            }
            if (jainPoppyRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Avoided: Poppy seeds (Khus Khus) contain innumerable seeds",
                        type = IngredientType.OTHER,
                        scientificReason = "Multi-seeded (Bahubiji) seeds of Papaver somniferum.",
                        titleRes = R.string.jain_poppy_title,
                        reasonRes = R.string.jain_poppy_rel,
                        scientificReasonRes = R.string.jain_poppy_sci
                    )
                )
                continue
            }
            if (jainAquaticPlantsRegex.containsMatchIn(nameLower)) {
                val isSinghoda = nameLower.contains("water chestnut") || nameLower.contains("singhara") || nameLower.contains("singhada")
                val (titleRes, reasonRes, sciRes) = if (isSinghoda) {
                    Triple(R.string.jain_singhoda_title, R.string.jain_singhoda_rel, R.string.jain_singhoda_sci)
                } else {
                    Triple(R.string.jain_makhana_title, R.string.jain_makhana_rel, R.string.jain_makhana_sci)
                }

                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Aquatic plant seeds harvesting may harm aquatic life.",
                        type = IngredientType.OTHER,
                        scientificReason = "Aquatic plants growing in water bodies; harvesting disturbs aquatic life.",
                        titleRes = titleRes,
                        reasonRes = reasonRes,
                        scientificReasonRes = sciRes
                    )
                )
                continue
            }
            if (jainFermentedRegex.containsMatchIn(nameLower)) {
                val (titleRes, reasonRes, sciRes) = when {
                    nameLower.contains("vinegar") || nameLower.contains("sirka") ->
                        Triple(R.string.jain_vinegar_title, R.string.jain_vinegar_rel, R.string.jain_vinegar_sci)
                    nameLower.contains("paneer") ->
                        Triple(R.string.jain_paneer_title, R.string.jain_paneer_rel, R.string.jain_paneer_sci)
                    else ->
                        Triple(R.string.jain_cheese_title, R.string.jain_cheese_rel, R.string.jain_cheese_sci)
                }

                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Fermented dairy or condiment; verify source.",
                        type = IngredientType.OTHER,
                        scientificReason = "Produced by fermentation and coagulation processes.",
                        titleRes = titleRes,
                        reasonRes = reasonRes,
                        scientificReasonRes = sciRes
                    )
                )
                continue
            }
            if (jainFermentedAdditivesRegex.containsMatchIn(nameLower)) {
                val isCitric = nameLower.contains("citric") || nameLower.contains("330")
                val (titleRes, reasonRes, sciRes) = if (isCitric) {
                    Triple(R.string.jain_citric_title, R.string.jain_citric_rel, R.string.jain_citric_sci)
                } else {
                    Triple(R.string.jain_msg_title, R.string.jain_msg_rel, R.string.jain_msg_sci)
                }

                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Additive produced via fermentation.",
                        type = IngredientType.OTHER,
                        scientificReason = "Produced by microbial fermentation; verification recommended.",
                        titleRes = titleRes,
                        reasonRes = reasonRes,
                        scientificReasonRes = sciRes
                    )
                )
                continue
            }
            if (jainProcessedWheatRegex.containsMatchIn(nameLower)) {
                val isRava = nameLower.contains("rava") || nameLower.contains("suji") ||
                        nameLower.contains("sooji") || nameLower.contains("semolina")
                val (titleRes, reasonRes, sciRes) = if (isRava) {
                    Triple(R.string.jain_rava_title, R.string.jain_rava_rel, R.string.jain_rava_sci)
                } else {
                    Triple(R.string.jain_maida_title, R.string.jain_maida_rel, R.string.jain_maida_sci)
                }

                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Processed wheat flour product.",
                        type = IngredientType.OTHER,
                        scientificReason = "Refined or coarsely milled wheat endosperm.",
                        titleRes = titleRes,
                        reasonRes = reasonRes,
                        scientificReasonRes = sciRes
                    )
                )
                continue
            }
            if (jainCocoaRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Chocolate/Cocoa powder product.",
                        type = IngredientType.OTHER,
                        scientificReason = "Food products from Cocoa beans; may contain trace insect fragments.",
                        titleRes = R.string.jain_chocolate_title,
                        reasonRes = R.string.jain_chocolate_rel,
                        scientificReasonRes = R.string.jain_chocolate_sci
                    )
                )
                continue
            }
            if (jainDryFruitsRegex.containsMatchIn(nameLower)) {
                detectedItems.add(
                    IngredientData(
                        name = capitalizedName,
                        reason = "Caution: Dry fruits seasonal usage guidance.",
                        type = IngredientType.OTHER,
                        scientificReason = "Dried fruits and seeds of trees.",
                        titleRes = R.string.jain_dryfruits_title,
                        reasonRes = R.string.jain_dryfruits_rel,
                        scientificReasonRes = R.string.jain_dryfruits_sci
                    )
                )
                continue
            }
        }
    }
    return detectedItems
}



