package com.shraddhacalendar.core.localization

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Supported Application Languages with Native Script Display Names.
 * Requirement: The language selector must display the language name itself in that respective language:
 *  - English
 *  - ಕನ್ನಡ
 *  - संस्कृतम्
 *  - తెలుగు
 *  - தமிழ்
 */
enum class AppLanguage(val code: String, val nativeDisplayName: String) {
    ENGLISH("en", "English"),
    KANNADA("kn", "ಕನ್ನಡ"),
    SANSKRIT("sa", "संस्कृतम्"),
    TELUGU("te", "తెలుగు"),
    TAMIL("ta", "தமிழ்");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}

object LocaleManager {
    private const val PREFS_NAME = "um_shraddha_prefs"
    private const val KEY_LANGUAGE = "app_language"

    fun getSavedLanguage(context: Context): AppLanguage {
        return try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val code = prefs.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
            AppLanguage.fromCode(code)
        } catch (_: Exception) {
            AppLanguage.ENGLISH
        }
    }

    fun saveLanguage(context: Context, language: AppLanguage) {
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
        } catch (_: Exception) {
        }
    }

    fun applyLocale(context: Context, language: AppLanguage): Context {
        return try {
            val locale = Locale(language.code)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } catch (_: Exception) {
            context
        }
    }
}
