package com.example.satvikscan.ui.viewmodel

object OcrTextValidator {

    private const val MINIMUM_FOOD_SCORE = 8

    private val TIER_3_KEYWORDS = setOf(
        "ingredients",
        "ingredient list",
        "nutrition facts",
        "nutritional information",
        "allergen",
        "allergens",
        "allergen information",
        "fssai"
    )
    private val TIER_2_KEYWORDS = setOf(
        "contains",
        "may contain",
        "serving",
        "serving size",
        "per 100g",
        "per serving",
        "total fat",
        "saturated fat",
        "trans fat",
        "net weight",
        "best before",
        "expiry",
        "batch no",
        "lic no"
    )
    private val TIER_1_KEYWORDS = setOf(
        "protein",
        "energy",
        "kcal",
        "fat",
        "carbohydrate",
        "carbohydrates",
        "sugar",
        "sugars",
        "fiber",
        "fibre",
        "vitamin",
        "calcium",
        "iron",
        "sodium",
        "salt"
    )

    fun validate(rawText: String): ValidationResult {

        val text = rawText
            .lowercase()
            .trim()

        var score = 0

        if (text.length < 20) {
            return ValidationResult.Invalid(
                "Text too short. Please scan again."
            )
        }

        score += calculateKeywordScore( text, TIER_3_KEYWORDS, 5 )
        score += calculateKeywordScore( text, TIER_2_KEYWORDS, 2 )
        score += calculateKeywordScore( text, TIER_1_KEYWORDS, 1 )

        if (score < MINIMUM_FOOD_SCORE) {
            return ValidationResult.Invalid(
                "Please scan the ingredients or nutrition section of a food packet"
            )
        }


        return ValidationResult.Valid(score)
    }
    private fun calculateKeywordScore(
        text: String,
        keywords: Set<String>,
        points: Int
    ): Int {
        var score = 0
        keywords.forEach { keyword ->

            if (text.contains(keyword)) {
                score += points
            }
        }
        return score
    }

}

sealed interface ValidationResult {

    data class Valid(val score: Int) : ValidationResult

    data class Invalid(val message: String) : ValidationResult
}