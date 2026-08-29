package com.shraddhacalendar.core.shraddha

data class EkadashiShraddhaGuide(
    val title: String,
    val subtitle: String,
    val canonicalShloka1: String,
    val canonicalShloka1Translit: String,
    val canonicalShloka1Meaning: String,
    val canonicalShloka2: String,
    val canonicalShloka2Translit: String,
    val canonicalShloka2Meaning: String,
    val nityaVsNaimittikaTitle: String,
    val nityaVsNaimittikaDesc: String,
    val varshikaRuleTitle: String,
    val varshikaRuleDesc: String,
    val pakshaRuleTitle: String,
    val pakshaRuleDesc: String,
    val dvadashiParaneTitle: String,
    val dvadashiParaneDesc: String,
    val disclaimerTitle: String,
    val disclaimerDesc: String
)
