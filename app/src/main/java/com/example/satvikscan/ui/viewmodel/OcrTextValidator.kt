package com.example.satvikscan.ui.viewmodel

object OcrTextValidator {

    private const val MINIMUM_FOOD_SCORE = 6

    private val TIER_3_KEYWORDS = setOf(
        "ingredients",
        "ingredient list",
        "nutrition facts",
        "nutritional information",
        "allergen",
        "allergens",
        "allergen information",
        "fssai",
        "potato",
        "Palmolein"
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
// OCR failed or image doesn't contain useful text
        if (text.length < 20) {
            return ValidationResult.Invalid(
                "No food label detected. Please scan the ingredients or nutrition label of a packaged food product."
            )
        }

        score += calculateKeywordScore(text, TIER_3_KEYWORDS, 5)
        score += calculateKeywordScore(text, TIER_2_KEYWORDS, 2)
        score += calculateKeywordScore(text, TIER_1_KEYWORDS, 1)
                  
//  OCR worked, but doesn't look like a food label
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
            val regex = Regex("\\b${Regex.escape(keyword)}\\b")
            if (regex.containsMatchIn(text)) {
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