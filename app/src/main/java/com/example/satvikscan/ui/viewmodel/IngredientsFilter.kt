package com.example.satvikscan.ui.viewmodel

import com.example.satvikscan.ui.IngredientData
import com.example.satvikscan.ui.IngredientType

fun parseIngredients(
    rawIngredientData: String?,
    currentDiet: String,
    customExclusions: Set<String>
): List<IngredientData> {

    // check the list are empty or not if empty so return the empty list
    if (rawIngredientData.isNullOrBlank()) return emptyList()

    // filter the string and split them
    val rawList = rawIngredientData.split(",", ";", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    // store the finding ingredients
    val DetectedItems = mutableListOf<IngredientData>()

    // check available items are restricted using the loop
    for (ingredient in rawList) {

        val nameLower = ingredient.lowercase()

        // Check Custom Exclusions first
        if (currentDiet == "Custom") {

            val matchedExclusion = customExclusions.find { nameLower.contains(it) } // har element me jayega aur check karega with compare bhi karega

            if (matchedExclusion != null) {
                DetectedItems.add(
                    IngredientData(
                        name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        reason = "Avoided: Matches custom excluded ingredient '$matchedExclusion'",
                        type = IngredientType.OTHER
                    )
                )
            }
            // already detected not to test jain/swami.
            continue
        }

        // 2. Standard Diets: Swaminarayan & Jain
        // return true or false
        val isGarlic =
            nameLower.contains("garlic") || nameLower.contains("lasan") || nameLower.contains("lahsun")
        val isOnion =
            nameLower.contains("onion") || nameLower.contains("dungri") || nameLower.contains("pyaj") || nameLower.contains(
                "pyaz"
            )

        val isOtherRootVeg = nameLower.contains("potato") || nameLower.contains("sweet potato") ||
                nameLower.contains("carrot") || nameLower.contains("radish") ||
                nameLower.contains("beetroot") || nameLower.contains("ginger") ||
                nameLower.contains("turmeric") || nameLower.contains("yam") ||
                nameLower.contains("root vegetable") || nameLower.contains("tapioca")

        val isHoney =
            nameLower.contains("honey") || nameLower.contains("shehad") || nameLower.contains("madh")

        val isNonVeg =
            nameLower.contains("chicken") || nameLower.contains("meat") || nameLower.contains("egg") || nameLower.contains(
                "fish"
            ) || nameLower.contains("gelatin") || nameLower.contains(
                "gelatine"
            ) || nameLower.contains("beef") || nameLower.contains("pork") || nameLower.contains(
                "mutton"
            ) || nameLower.contains("lard") || nameLower.contains("albumin")

            // chek is it nonveg consider
        if (isNonVeg) {
            DetectedItems.add(
                IngredientData(
                    name = ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    reason = "Avoided: Non-vegetarian origin ingredient detected",
                    type = IngredientType.NON_VEG_ORIGIN
                )
            )
        } else if (currentDiet == "Swaminarayan") {
            if (isGarlic || isOnion) {
                val displayName = if (isGarlic) "Garlic" else "Onion"
                DetectedItems.add(
                    IngredientData(
                        name = displayName,
                        reason = "Avoided: Onion/Garlic are restricted in Swaminarayan diet",
                        type = IngredientType.ROOT_VEGETABLE
                    )
                )
            }
        } else if (currentDiet == "Jain") {
            if (isGarlic || isOnion || isOtherRootVeg) {
                val displayName = when {
                    isGarlic -> "Garlic"
                    isOnion -> "Onion"
                    nameLower.contains("potato") -> "Potato"
                    nameLower.contains("ginger") -> "Ginger"
                    nameLower.contains("carrot") -> "Carrot"
                    else -> ingredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
                DetectedItems.add(
                    IngredientData(
                        name = displayName,
                        reason = "Avoided: Root vegetables are restricted in Jain diet",
                        type = IngredientType.ROOT_VEGETABLE
                    )
                )
            } else if (isHoney) {
                DetectedItems.add(
                    IngredientData(
                        name = "Honey",
                        reason = "Avoided: Honey is restricted in Jain diet",
                        type = IngredientType.OTHER
                    )
                )
            }
        }
    }
    return DetectedItems
}
