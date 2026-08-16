package com.example.satvikscan.data.Local

import android.content.Context

class PreferenceManager(context: Context) {

    //initialize SharedPreferences
    private val sharedPrefs =
        context.getSharedPreferences("satvik_scan_prefs", Context.MODE_PRIVATE)

    // save the diet selection jain ,swaminayan and custom
    fun SaveDiet(diet: String) {
        val sharedPrefsEdit = sharedPrefs.edit()
        sharedPrefsEdit.putString("selected_diet", diet).apply()
    }

    // Read Diet selection
    fun getdiet(): String? {
        return sharedPrefs.getString("selected_diet", null)
    }

    // Save Custom Exclusions Set
    fun saveCustomExclusions(exclusions: Set<String>) {
        sharedPrefs.edit().putStringSet("custom_avoid_ingredients", exclusions).apply()
    }

    // fetch/read Custom Exclusions Set
    fun getCustomExclusions(): Set<String> {
        return sharedPrefs.getStringSet("custom_avoid_ingredients", emptySet()) ?: emptySet()
    }

    // Save Selected Language (en, hi, gu)
    fun saveLanguage(language: String) {
        val sharedPrefsEdit = sharedPrefs.edit()
        sharedPrefsEdit.putString("selected_language", language).apply()
    }

    // Read Selected Language
    fun getLanguage(): String {
        return sharedPrefs.getString("selected_language", "en") ?: "en"
    }

    // Onboarding completion status
    fun setOnboardingCompleted(completed: Boolean) {
        sharedPrefs.edit().putBoolean("is_onboarding_completed", completed).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return sharedPrefs.getBoolean("is_onboarding_completed", false)
    }

    // Clear all preferences (for reset/logout)
    fun clearAllPreferences() {
        sharedPrefs.edit().clear().apply()
    }
}
