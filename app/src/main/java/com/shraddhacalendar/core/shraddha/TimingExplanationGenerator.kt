package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.core.models.TithiInfo
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import com.shraddhacalendar.core.panchang.TithiCalculator
import java.time.ZoneId
import java.time.ZonedDateTime

object TimingExplanationGenerator {

    fun generateAnalysis(
        event: ShraddhaEvent,
        location: GeoLocation,
        language: AppLanguage
    ): TithiTimingAnalysis {
        val zoneId = ZoneId.of(location.timezoneId)
        val date = event.gregorianDate
        val kala = event.kalaDetails

        val sunriseZdt = ZonedDateTime.of(date, kala.sunrise, zoneId)
        val sunsetZdt = ZonedDateTime.of(date, kala.sunset, zoneId)
        val aparahnaStartZdt = ZonedDateTime.of(date, kala.aparahnaStart, zoneId)
        val aparahnaEndZdt = ZonedDateTime.of(date, kala.aparahnaEnd, zoneId)

        val sunriseTithi = TithiCalculator.getTithiAt(sunriseZdt)
        val targetTithi = event.tithi.tithi

        // Calculate overlap of target tithi in Aparahna
        var overlapSeconds = 0L
        var sample = aparahnaStartZdt
        val stepSec = 300L
        while (sample.isBefore(aparahnaEndZdt)) {
            if (TithiCalculator.getTithiAt(sample).number == targetTithi.number) {
                overlapSeconds += stepSec
            }
            sample = sample.plusSeconds(stepSec)
        }
        val overlapMinutes = (overlapSeconds / 60L).toInt()

        val isSunriseDifferent = (sunriseTithi.number != targetTithi.number)

        val canonicalShloka = when (language) {
            AppLanguage.KANNADA -> "ಪೂರ್ವಾಹ್ಣೇ ದೈವಿಕಂ ಕಾರ್ಯಮಪರಾಹ್ಣೇ ತು ಪೈತೃಕಮ್ ।\nಪ್ರಾತಃಕಾಲೇ ಕೃತೇ ಶ್ರಾದ್ಧೇ ರಾಕ್ಷಸೈರ್ಭುಜ್ಯತೇ ಧ್ರುವಮ್ ॥"
            AppLanguage.SANSKRIT -> "पूर्वाह्णे दैविकं कार्यमपराह्णे तु पैतृकम् ।\nप्रातःकाले कृते श्राद्धे राक्षसैर्भुज्यते ध्रुवम् ॥"
            AppLanguage.TELUGU -> "పూర్వాహ్ణే దైవికం కార్యమపరాహ్ణే తు పైతృకమ్ ।\nప్రాతఃకాలే కృతే శ్రాద్ధే రాక్షసైర్భుజ్యతే ధ్రువమ్ ॥"
            AppLanguage.TAMIL -> "பூர்வாஹ்ணே தைவிகம் கார்யமபராஹ்ணே து பைத்ருகம் ।\nப்ராதஃகாலே க்ருதே ச்ராத்தே ராக்ஷஸைர்புஜ்யதே த்ருவம் ॥"
            AppLanguage.ENGLISH -> "पूर्वाह्णे दैविकं कार्यमपराह्णे तु पैतृकम् ।\nप्रातःकाले कृते श्राद्धे राक्षसैर्भुज्यते ध्रुवम् ॥"
        }

        val canonicalTranslit = "Pūrvāhṇe daivikaṁ kāryamaparāhṇe tu paitṛkam |\nPrātaḥkāle kṛte śrāddhe rākṣasairbhujyate dhruvam ||"

        val canonicalMeaning = when (language) {
            AppLanguage.KANNADA -> "ಧರ್ಮಶಾಸ್ತ್ರ (ಮನುಸ್ಮೃತಿ, ನಿರ್ಣಯಸಿಂಧು): ಮುಂಜಾನೆಯು ದೇವಪೂಜೆಗಳಿಗೆ ಪ್ರಶಸ್ತ; ಅಪರಾಹ್ನ ಕಾಲವು (ಮಧ್ಯಾಹ್ನ ನಂತರ) ಪಿತೃಕಾರ್ಯಕ್ಕೆ ಕಡ್ಡಾಯ. ಪ್ರಾತಃಕಾಲದಲ್ಲಿ ಶ್ರಾದ್ಧ ಮಾಡಿದರೆ ಅದು ಪಿತೃಗಳಿಗೆ ತಲುಪದೆ ರಾಕ್ಷಸರ ಪಾಲಾಗುತ್ತದೆ."
            AppLanguage.SANSKRIT -> "धर्मशास्त्रे (मनुस्मृतिः, निर्णयसिन्धुः): पूर्वाह्णे देवकार्यं कुर्यात्, अपराह्णे तु पितृकार्यम्। प्रातःकाले कृतं श्राद्धं राक्षसैरेव भुज्यते।"
            AppLanguage.TELUGU -> "ధర్మశాస్త్రం (మనుస్మృతి, నిర్ణయసింధు): ఉదయం దేవకార్యాలకు, అపరాహ్ణం పితృకార్యాలకు నిర్దేశించబడింది. ప్రాతఃకాలంలో శ్రాద్ధం చేస్తే అది పితృదేవతలకు చేరకుండా రాక్షస పాలవుతుంది."
            AppLanguage.TAMIL -> "தர்ம சாஸ்திரம்: காலையில் தேவ காரியங்களும், மதியம் அபராஹ்ணத்தில் பித்ரு காரியங்களும் செய்யப்பட வேண்டும். காலையில் சிராத்தம் செய்தால் அது அரக்கர்களால் அபகரிக்கப்படும்."
            AppLanguage.ENGLISH -> "Dharma Shastra (Manu Smriti, Nirnaya Sindhu): Morning (Purvahna) is ordained for divine worship, while afternoon (Aparahna) is exclusively ordained for ancestral rites. Shraddha performed in the morning is rejected by Pitrus and seized by Rakshasas."
        }

        val whyThisDate = if (isSunriseDifferent) {
            when (language) {
                AppLanguage.KANNADA -> "ಸಾಮಾನ್ಯ ಕ್ಯಾಲೆಂಡರ್‌ಗಳು ಸೂರ್ಯೋದಯದ ಸಮಯದ (${sunriseTithi.name}) ತಿಥಿಯನ್ನು ತೋರಿಸುತ್ತವೆ. ಆದರೆ ಶಾಸ್ತ್ರದಂತೆ ಶ್ರಾದ್ಧವನ್ನು ಮಧ್ಯಾಹ್ನದ ನಂತರದ ಅಪರಾಹ್ನ ಕಾಲದಲ್ಲೇ (${kala.aparahnaStart} - ${kala.aparahnaEnd}) ಮಾಡಬೇಕು. ಈ ದಿನದ ಅಪರಾಹ್ನದಲ್ಲಿ ಮೃತ ತಿಥಿಯಾದ '${targetTithi.name}' (${overlapMinutes} ನಿಮಿಷಗಳ ಕಾಲ) ವ್ಯಾಪಿಸಿರುವುದರಿಂದ, ಈ ದಿನವನ್ನೇ ಶ್ರಾದ್ಧಕ್ಕೆ ನಿಗದಿಪಡಿಸಲಾಗಿದೆ."
                AppLanguage.SANSKRIT -> "सामान्यपञ्चाङ्गेषु सूर्योदयकालीनतिथिः (${sunriseTithi.name}) दृश्यते। किन्तु शास्त्रनियमानुसारेण श्राद्धकर्म अपराह्नकाले एव कर्तव्यम्। अस्मिन् दिने अपराह्ने मृततिथिः '${targetTithi.name}' (${overlapMinutes} निमेषात्मकं) वर्तते, अतः अयमेव दिवसः श्राद्धार्थं स्वीकृतः।"
                AppLanguage.TELUGU -> "సాధారణ క్యాలెండర్లు సూర్యోదయ తిథిని (${sunriseTithi.name}) చూపుతాయి. కానీ శాస్త్రం ప్రకారం శ్రాద్ధాన్ని అపరాహ్ణ కాలంలోనే (${kala.aparahnaStart} - ${kala.aparahnaEnd}) ఆచరించాలి. ఈ రోజు అపరాహ్ణంలో మృత తిథి '${targetTithi.name}' (${overlapMinutes} నిమిషాలు) ఉండటం వలన ఈ రోజే శ్రాద్ధ దినంగా నిర్ణయించబడింది."
                AppLanguage.TAMIL -> "வழக்கமான காலண்டர்கள் சூரியோதய திதியை (${sunriseTithi.name}) காட்டும். ஆனால் சாஸ்திரப்படி சிராத்தம் அபராஹ்ண காலத்தில் (${kala.aparahnaStart} - ${kala.aparahnaEnd}) செய்யப்பட வேண்டும். இந்த நாளில் அபராஹ்ணத்தில் '${targetTithi.name}' திதி நிலவுவதால் இந்த நாளே தேர்ந்தெடுக்கப்பட்டுள்ளது."
                AppLanguage.ENGLISH -> "Standard wall calendars display the sunrise tithi (${sunriseTithi.name}). However, Dharma Shastra ordains that Shraddha must be performed during the afternoon Aparahna window (${kala.aparahnaStart} - ${kala.aparahnaEnd}). Because the target demise tithi '${targetTithi.name}' prevails during Aparahna on this day (${overlapMinutes} mins), this day is canonically selected."
            }
        } else {
            when (language) {
                AppLanguage.KANNADA -> "ಮೃತ ತಿಥಿಯಾದ '${targetTithi.name}' ಸೂರ್ಯೋದಯದಲ್ಲೂ ಇದ್ದು, ಸಂಪೂರ್ಣ ಅಪರಾಹ್ನ ಶ್ರಾದ್ಧ ಕಾಲದಲ್ಲೂ (${kala.aparahnaStart} - ${kala.aparahnaEnd}) ವ್ಯಾಪಿಸಿರುವುದರಿಂದ ಇದು ಏಕ-ಅಪರಾಹ್ನ ವ್ಯಾಪ್ತಿಯ ಪರಿಪೂರ್ಣ ದಿನವಾಗಿದೆ."
                AppLanguage.SANSKRIT -> "मृततिथिः '${targetTithi.name}' सूर्योदये तथा सम्पूर्णेऽपि अपराह्नकाले वर्तते। अतः अयमेव प्रशस्ततमः दिवसः।"
                AppLanguage.TELUGU -> "మృత తిథి '${targetTithi.name}' సూర్యోదయంలోనూ ఉండి, అపరాహ్ణ కాలంలోనూ పూర్తిగా వ్యాపించి ఉన్నందున ఇది శ్రాద్ధానికి ఉత్తమమైన రోజు."
                AppLanguage.TAMIL -> "'${targetTithi.name}' திதி சூரியோதயத்திலும் இருந்து, முழு அபராஹ்ண காலத்திலும் நிலவுவதால் இது மிகச்சிறந்த சிராத்த தினமாகும்."
                AppLanguage.ENGLISH -> "The target demise tithi '${targetTithi.name}' was active at sunrise and prevailed throughout the afternoon Shraddha window (${kala.aparahnaStart} - ${kala.aparahnaEnd}), fulfilling pure Eka-Aparahna Vyapti."
            }
        }

        val isEkadashiDemise = event.isEkadashiObservance || event.isEkadashiShifted || targetTithi.pakshaTithiNumber == 11
        val ekadashiGuidanceText = if (isEkadashiDemise) {
            val ritualDvadashiDate = if (event.isEkadashiShifted) event.gregorianDate else event.gregorianDate.plusDays(1)
            when (language) {
                AppLanguage.KANNADA -> "\n\n🌿 ಏಕಾದಶಿ ವಿಶೇಷ ಸೂಚನೆ: ಏಕಾದಶಿಯಂದು ಉಪವಾಸ ಕಡ್ಡಾಯವಾಗಿರುವುದರಿಂದ ಅನ್ನಶ್ರಾದ್ಧವು ನಿಷಿದ್ಧ (ಪದ್ಮಪುರಾಣ: 'ಏಕಾದಶ್ಯಾಂ ಯದಾ ರಾಮ ಶ್ರಾದ್ಧಂ ನೈಮಿತ್ತಿಕಂ ಭವೇತ್ । ತದ್ದಿನೇ ತು ಪರಿತ್ಯಜ್ಯ ದ್ವಾದಶ್ಯಾಂ ಶ್ರಾದ್ಧಮಾಚರೇತ್ ॥'). ಪೂರ್ಣ ಅನ್ನಶ್ರಾದ್ಧವನ್ನು ದ್ವಾದಶಿಯಂದು ($ritualDvadashiDate) ಆಚರಿಸಬೇಕು."
                AppLanguage.SANSKRIT -> "\n\n🌿 एकादशीविशेषसूचना: एकादश्याम् उपवासस्य नित्यत्वात् अन्नश्राद्धं निषिद्धम् (पद्मपुराणे: 'एकादश्यां यदा राम श्राद्धं नैमित्तिकं भवेत् । तद्दिने तु परित्यज्य द्वादश्यां श्राद्धमाचरेत् ॥')। सम्पूर्णम् अन्नश्राद्धं द्वादश्यामेव ($ritualDvadashiDate) कर्तव्यम्।"
                AppLanguage.TELUGU -> "\n\n🌿 ఏకాదశి ప్రత్యేక సూచన: ఏకాదశి నాడు ఉపవాసం తప్పనిసరి కావున అన్నశ్రాద్ధం నిషిద్ధం (పద్మపురాణం: 'ఏకాదశ్యాం యదా రామ... ద్వాదశ్యాం శ్రాద్ధమాచరేత్'). సంపూర్ణ అన్నశ్రాద్ధాన్ని ద్వాదశి నాడు ($ritualDvadashiDate) నిర్వహించాలి."
                AppLanguage.TAMIL -> "\n\n🌿 ஏகாதசி சிறப்பு குறிப்பு: ஏகாதசியில் உபவாசம் கட்டாயம் என்பதால் அன்ன சிராத்தம் நிஷித்தம். முழு அன்ன சிராத்தத்தை துவாதசியில் ($ritualDvadashiDate) செய்ய வேண்டும்."
                AppLanguage.ENGLISH -> "\n\n🌿 Ekadashi Shastric Note: Because Ekadashi fasting is mandatory, Anna-Shraddha with cooked rice is strictly prohibited on Ekadashi (Padma Purana: 'Ekādaśyāṁ yadā rāma śrāddhaṁ naimittikaṁ bhavet | Taddine tu parityajya dvādaśyāṁ śrāddhamācaret ||'). The full Anna-Shraddha is performed on Dvadashi ($ritualDvadashiDate)."
            }
        } else ""

        val finalWhyThisDate = whyThisDate + ekadashiGuidanceText

        val whyNotMorning = when (language) {

            AppLanguage.KANNADA -> "ಪ್ರಶ್ನೆ: ಮರುದಿನ ಬೆಳಿಗ್ಗೆ (ಉದಾ: 7 AM) '${targetTithi.name}' ತಿಥಿ ಇರುವಾಗ ಶ್ರಾದ್ಧ ಮಾಡಬಹುದೇ?\nಉತ್ತರ: ಇಲ್ಲ. ಧರ್ಮಶಾಸ್ತ್ರದ ಪ್ರಕಾರ ವಾರ್ಷಿಕ ಮತ್ತು ಪಕ್ಷ ಶ್ರಾದ್ಧಗಳನ್ನು ಪ್ರಾತಃಕಾಲದಲ್ಲಿ ಮಾಡುವುದು ಸಂಪೂರ್ಣ ನಿಷಿದ್ಧ. ಮರುದಿನ ಅಪರಾಹ್ನಕ್ಕೆ '${targetTithi.name}' ತಿಥಿ ಮುಗಿದು ಮುಂದಿನ ತಿಥಿ ಆರಂಭವಾಗುವುದರಿಂದ, ಈ ದಿನದ ಅಪರಾಹ್ನವೇ ಏಕೈಕ ಶಾಸ್ತ್ರೋಕ್ತ ಕಾಲವಾಗಿದೆ."
            AppLanguage.SANSKRIT -> "प्रश्नः: श्वः प्रातःकाले (यथा ७ वादनसमये) '${targetTithi.name}' सति श्राद्धं कर्तुं शक्यते किम्?\nउत्तरम्: नैव। धर्मशास्त्रानुसारं प्रातश्श्राद्धं सर्वथा निषिद्धम्। श्वः अपराह्ने एषा तिथिः न भविष्यति, अतः अद्यतनोऽपराह्नकाल एव एकमात्रं शास्त्रसम्मतः।"
            AppLanguage.TELUGU -> "ప్రశ్న: మరుసటి రోజు ఉదయం (ఉదా: 7 AM) '${targetTithi.name}' తిథి ఉన్నప్పుడు శ్రాద్ధం చేయవచ్చా?\nసమాధానం: లేదు. ధర్మశాస్త్రం ప్రకారం ఉదయకాలంలో శ్రాద్ధం చేయడం నిషిద్ధం. మరుసటి రోజు అపరాహ్ణానికి '${targetTithi.name}' ముగిసిపోతుంది కాబట్టి, ఈ రోజు అపరాహ్ణమే ఏకైక శాస్త్రోక్త సమయం."
            AppLanguage.TAMIL -> "கேள்வி: மறுநாள் காலையில் (காலை 7 மணிக்கு) '${targetTithi.name}' இருக்கும் போது சிராத்தம் செய்யலாமா?\nபதில்: கூடாது. தர்ம சாஸ்திரப்படி காலையில் சிராத்தம் செய்வது நிஷித்தம். மறுநாள் மதியத்திற்குள் இத்திதி முடிந்துவிடும் என்பதால், இந்த நாளின் அபராஹ்ணமே சாஸ்திரபூர்வமானது."
            AppLanguage.ENGLISH -> "Q: Can we perform the Shraddha tomorrow morning (e.g. 7 AM) when '${targetTithi.name}' is still active at sunrise?\nAnswer: No. Dharma Shastra strictly prohibits performing Varshika and Paksha Shraddha in the morning (Purvahna/Pratah Kala). Tomorrow afternoon, '${targetTithi.name}' will have already expired, making today's Aparahna the sole scripturally valid window."
        }

        return TithiTimingAnalysis(
            targetTithi = targetTithi,
            sunriseTithi = sunriseTithi,
            aparahnaTithi = targetTithi,
            sunriseTime = kala.sunrise,
            sunsetTime = kala.sunset,
            aparahnaStart = kala.aparahnaStart,
            aparahnaEnd = kala.aparahnaEnd,
            kutapaStart = kala.kutapaStart,
            kutapaEnd = kala.kutapaEnd,
            targetOverlapMinutes = overlapMinutes,
            isSunriseDifferentFromRitual = isSunriseDifferent,
            canonicalProhibitionShloka = canonicalShloka,
            canonicalProhibitionShlokaTranslit = canonicalTranslit,
            canonicalProhibitionMeaning = canonicalMeaning,
            whyThisDateExplanation = finalWhyThisDate,
            whyNotMorningExplanation = whyNotMorning
        )

    }
}
