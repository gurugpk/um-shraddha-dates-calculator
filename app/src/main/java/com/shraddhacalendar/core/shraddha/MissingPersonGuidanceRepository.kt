package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.MissingPersonGuidance
import com.shraddhacalendar.core.models.MissingPersonWaitingPeriodInfo
import com.shraddhacalendar.core.models.ScripturalSourceReference
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object MissingPersonGuidanceRepository {

    fun calculateWaitingPeriod(
        ageAtDisappearance: Int?,
        lastSeenDate: LocalDate?
    ): MissingPersonWaitingPeriodInfo {
        val prescribedYears = when {
            ageAtDisappearance != null && ageAtDisappearance < 20 -> 20
            ageAtDisappearance != null && ageAtDisappearance > 50 -> 6
            else -> 12 // Default 12 years (Dvadasa-varsha niyamah)
        }

        val ruleAuthority = when {
            ageAtDisappearance != null && ageAtDisappearance < 20 ->
                "Kātyāyana & Baudhāyana Smṛti (20-year waiting period for youth under 20)"
            ageAtDisappearance != null && ageAtDisappearance > 50 ->
                "Nirṇayasindhu III & Smṛtimuktāphala (6-year waiting period for elders above 50)"
            else ->
                "Dharmasindhu III & Manusmṛti (Standard 12-year Dvādaśa-varṣa rule for adults)"
        }

        val elapsedYears = if (lastSeenDate != null) {
            ChronoUnit.YEARS.between(lastSeenDate, LocalDate.now()).toInt().coerceAtLeast(0)
        } else null

        val isElapsed = elapsedYears != null && elapsedYears >= prescribedYears
        val remainingYears = if (elapsedYears != null && !isElapsed) prescribedYears - elapsedYears else 0

        return MissingPersonWaitingPeriodInfo(
            ageAtDisappearance = ageAtDisappearance,
            prescribedWaitingYears = prescribedYears,
            authorityRule = ruleAuthority,
            elapsedYears = elapsedYears,
            isPeriodElapsed = isElapsed,
            remainingYears = remainingYears
        )
    }

    fun getGuidance(
        ageAtDisappearance: Int?,
        lastSeenDate: LocalDate?,
        language: AppLanguage,
        tradition: MadhwaTradition = MadhwaTradition.UTTARADI_MATHA
    ): MissingPersonGuidance {
        val waitingInfo = calculateWaitingPeriod(ageAtDisappearance, lastSeenDate)

        val sources = listOf(
            ScripturalSourceReference(
                primaryText = "Dharmasindhu (Section 3) & Nirnayasindhu III",
                sectionOrChapter = "Proshita-marana & Palasha-vidhi Prakarana",
                citationVerse = "Dharmasindhu III (Uttarardha); Nirnayasindhu III",
                sanskritText = "देशान्तरगते यस्मिन् वार्ता नैवोपलभ्यते । द्वादशाब्दानि सञ्चिन्त्य पश्चात् कुर्यात् क्रियाविधिम् ॥",
                translation = "When a person is missing in distant lands without any news of life or death, the family must wait for the prescribed period (standard 12 years) before any symbolic rites may be performed."
            ),
            ScripturalSourceReference(
                primaryText = "Garuda Purana (Preta Khanda Ch. 4, v. 1-15) & Smritimuktaphala",
                sectionOrChapter = "Jivata-Punaragamana (Ghataprasuti) Vidhi",
                citationVerse = "Garuda Purana II.4.1-15",
                sanskritText = "जीवतो यस्य कुर्वन्ति भ्रान्त्या पौत्रिकमाहवे । सर्पिषा स्नापयित्वा तु जातकर्म समाचरेत् ॥",
                translation = "If a person presumed dead undergoes symbolic effigy rites and later returns alive, he is consecrated with Ghata-Prasuti (re-birth bath in ghee/water), Jatakarma, Upanayana, re-marriage with his wife, and re-kindling of sacred fires."
            )
        )

        return when (language) {
            AppLanguage.KANNADA -> MissingPersonGuidance(
                title = "ಕಾಣೆಯಾದ ವ್ಯಕ್ತಿಯ ಶಾಸ್ತ್ರೀಯ ಮಾರ್ಗದರ್ಶನ (ಪ್ರೋಷಿತ ಮರಣ ವಿಚಾರ)",
                statusSummary = "ಸ್ಥಿತಿ: ಕಾಣೆಯಾಗಿದ್ದಾರೆ / ಮರಣವು ಧೃಢಪಟ್ಟಿಲ್ಲ",
                whyShraddhaProhibited = "ಜೀವಂತವಿರುವ ವ್ಯಕ್ತಿಗೆ ಶ್ರಾದ್ಧ ಮಾಡುವುದು ಶಾಸ್ತ್ರದಲ್ಲಿ ಮಹಾದೋಷವೆಂದು ಪರಿಗಣಿಸಲಾಗಿದೆ. ಆದ್ದರಿಂದ ಮರಣವು ಸ್ಪಷ್ಟವಾಗಿ ಧೃಢಪಡುವವರೆಗೂ ಶ್ರಾದ್ಧ ಅಥವಾ ತರ್ಪಣಗಳನ್ನು ಆಚರಿಸಬಾರದು.",
                waitingPeriodInfo = waitingInfo,
                recommendedPrayers = listOf(
                    "ಆಯುಷ್ಯ ಹೋಮ ಹಾಗೂ ಮಹಾ ಮೃತ್ಯುಂಜಯ ಜಪ (ದೀರ್ಘಾಯುಷ್ಯ ಹಾಗೂ ಸುರಕ್ಷಿತ ವಾಪಸಾತಿಗಾಗಿ)",
                    "ಶ್ರೀ ಸುದರ್ಶನ ಮಹಾಹೋಮ ಹಾಗೂ ಶ್ರೀ ವಿಷ್ಣು ಸಹಸ್ರನಾಮ ಸ್ತೋತ್ರ ಪಾರಾಯಣ",
                    "ಶ್ರೀ ಧನ್ವಂತರಿ ಜಪ ಹಾಗೂ ಗೋ-ದಾನ"
                ),
                postWaitingPeriodProtocol = listOf(
                    "೧. ಶಾಸ್ತ್ರೋಕ್ತ ಕಾಯುವಿಕೆ ಅವಧಿ (${waitingInfo.prescribedWaitingYears} ವರ್ಷಗಳು) ಮುಗಿದ ನಂತರ ಕುಟುಂಬದ ಹಿರಿಯರು ಮತ್ತು ಮಠದ ಆಚಾರ್ಯರ ಸಮ್ಮತಿ ಪಡೆಯುವುದು.",
                    "೨. ಪೂರ್ಣ ಆಶೌಚ ಆಚರಣೆ (೩ ದಿನ ಅಥವಾ ೧೦ ದಿನ).",
                    "೩. ಪರ್ಣನರ ದಾಹ (ಪಲಾಶವಿಧಿ - ೩೬೦ ಪಲಾಶ/ದರ್ಭೆಗಳ ಪ್ರತಿಕೃತಿ ದಹನ).",
                    "೪. ನಾರಾಯಣ ಬಲಿ ಕರ್ಮ ನೆರವೇರಿಸುವುದು ಹಾಗೂ ನಂತರ ಷೋಡಶ ಮಾಸಿಕಗಳು ಮತ್ತು ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ ಪ್ರಾರಂಭಿಸುವುದು."
                ),
                laterConfirmedWorkflow = "ಮರಣವು ಅಧಿಕೃತವಾಗಿ ಧೃಢಪಟ್ಟಲ್ಲಿ, ದೃಢಪಟ್ಟ ದಿನಾಂಕ ಮತ್ತು ತಿಥಿಯನ್ನು ನಮೂದಿಸಿ ಪೂರ್ಣ ಶ್ರಾದ್ಧ ಪಂಚಾಂಗವನ್ನು ಲೆಕ್ಕಹಾಕಬಹುದು (ವಾರ್ತಾಶ್ರವಣ ಆಶೌಚ ನಿಯಮದಂತೆ).",
                returnAliveRestorationProtocol = "ಕಾಣೆಯಾದ ವ್ಯಕ್ತಿಯು ಜೀವಂತರಾಗಿ ಹಿಂದಿರುಗಿದರೆ, ಗರುಡ ಪುರಾಣದಂತೆ ಘಟಪ್ರಸೂತಿ (ಪುನರ್ಜನ್ಮ ಸಂಸ್ಕಾರ), ಜಾತಕರ್ಮ, ಉಪನಯನ, ಪತ್ನಿಯೊಡನೆ ಪುನರ್ವಿವಾಹ ಹಾಗೂ ಅಗ್ನ್ಯಾಧಾನ ವಿಧಿಗಳನ್ನು ನೆರವೇರಿಸಿ ಪುನಃ ಗ್ರಹಸ್ಥ ಧರ್ಮಕ್ಕೆ ಸೇರಿಸಿಕೊಳ್ಳಬೇಕು.",
                scripturalSources = sources,
                acharyaConsultationNote = "ಗಮನಿಸಿ: ಕಾಣೆಯಾದ ವ್ಯಕ್ತಿಯ ಪ್ರಕರಣಗಳಲ್ಲಿ ಯಾವುದೇ ಧಾರ್ಮಿಕ ನಿರ್ಧಾರವನ್ನು ಸ್ವತಂತ್ರವಾಗಿ ತೆಗೆದುಕೊಳ್ಳದೆ, ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠದ ಅಥವಾ ತಮ್ಮ ಪರಂಪರೆಯ ಪ್ರಾಜ್ಞ ಆಚಾರ್ಯರೊಂದಿಗೆ ಸಮಾಲೋಚಿಸುವುದು ಅತ್ಯಗತ್ಯ."
            )
            AppLanguage.SANSKRIT -> MissingPersonGuidance(
                title = "नष्टवार्तिक / प्रोषितजन-शास्त्रनिर्णयः",
                statusSummary = "स्थितिः नष्टवार्तिकः / मरणम् अनिश्चितम्",
                whyShraddhaProhibited = "जीवति पुरुषे श्राद्धकरणं शास्त्रेषु महादोषत्वेन प्रतिषिद्धम्। अतः मरणनिश्चयं विना श्राद्ध-तर्पणादिकं नैव कार्यम्।",
                waitingPeriodInfo = waitingInfo,
                recommendedPrayers = listOf(
                    "आयुष्यहोमः तथा महामृत्यञ्जयजपः (दीर्घायुष्यार्थं कुशलप्रत्यागमनार्थं च)",
                    "श्रीमत्सुदर्शनहोमः तथा श्रीविष्णुसहस्रनामस्तोत्रपारायणम्",
                    "धन्वन्तरिमन्त्रजपः तथा गोदानम्"
                ),
                postWaitingPeriodProtocol = listOf(
                    "१. शास्त्रविहित-प्रतीक्षाकालस्य (${waitingInfo.prescribedWaitingYears} वर्षाणि) समाप्तावनन्तरम् आचार्यसम्मतिस्वीकारः।",
                    "२. पूर्णाशौचानुष्ठानम् (त्रिरात्रं दशरात्रं वा)।",
                    "३. पर्णनरदाहः (पलाशविधिः - त्रिषष्ट्यधिकत्रिशत ३६० पलाशवृन्तैः)।",
                    "४. नारायणबलिपूर्वकं षोडशमासिक-वार्षिकश्राद्धारम्भः।"
                ),
                laterConfirmedWorkflow = "प्रमाणपूर्वकं मरणनिश्चये जाते सति निश्चिततिथ्यनुसारेण पञ्चाङ्गश्राद्धगणना कर्तव्या (वार्ताश्रवणाशौचरीत्या)।",
                returnAliveRestorationProtocol = "यदि प्रोषितजनः जीवन्नेव प्रत्यागच्छेत् तर्हि गरुडपुराणोक्तरीत्या घटप्रसूतिसंस्कारः (पुनर्जन्म), जातकर्म, उपनयनं, स्वपत्न्या सह पुनर्विवाहः, अग्न्याधानं च विधेयम्।",
                scripturalSources = sources,
                acharyaConsultationNote = "महत्त्वपूर्णम्: प्रोषितविषये स्वबुद्ध्या किमपि न कर्तव्यम्; श्रीमदुत्तरादिमठस्य विदुषामाचार्याणां च मार्गदर्शनेनैव सर्वं करणीयम्।"
            )
            AppLanguage.TELUGU -> MissingPersonGuidance(
                title = "కనిపించని వ్యక్తి శాస్త్రీయ మార్గదర్శనం (ప్రోషిత మరణ విచారం)",
                statusSummary = "స్థితి: కనిపించకుండా పోయారు / మరణం ధృవీకరించబడలేదు",
                whyShraddhaProhibited = "జీవించి ఉన్న వ్యక్తికి శ్రాద్ధం చేయడం శాస్త్రాలలో మహాదోషం. కాబట్టి మరణం స్పష్టంగా ఖాయమయ్యే వరకు శ్రాద్ధం లేదా తర్పణాలు చేయకూడదు.",
                waitingPeriodInfo = waitingInfo,
                recommendedPrayers = listOf(
                    "ఆయుష్య హోమం మరియు మహా మృత్యుంజయ జపం (దీర్ఘాయుష్షు మరియు క్షేమంగా తిరిగి రావడం కోసం)",
                    "శ్రీ సుదర్శన మహావోమం మరియు విష్ణు సహస్రనామ స్తోత్ర పారాయణం",
                    "ధన్వంతరి జపం మరియు గో-దానం"
                ),
                postWaitingPeriodProtocol = listOf(
                    "1. శాస్త్రోక్త నిరీక్షణ కాలం (${waitingInfo.prescribedWaitingYears} సంవత్సరాలు) ముగిసిన తర్వాత ఆచార్యుల సమ్మతి పొందడం.",
                    "2. పూర్ణ ఆశౌచ ఆచరణ (3 రోజులు లేదా 10 రోజులు).",
                    "3. పర్ణనర దాహం (పలాశవిధి - 360 పలాశ పుల్లలతో ప్రతికృతి దహనం).",
                    "4. నారాయణ బలి నిర్వహించి తరువాత షోడశ మాసికాలు, వార్షిక శ్రాద్ధాలు ప్రారంభించడం."
                ),
                laterConfirmedWorkflow = "మరణం అధికారికంగా ధృవీకరించబడితే, ఆ తేదీ మరియు తిథిని నమోదు చేసి పూర్తి శ్రాద్ధ పంచాంగాన్ని లెక్కించవచ్చు.",
                returnAliveRestorationProtocol = "కనిపించని వ్యక్తి సజీవంగా తిరిగి వస్తే, గరుడ పురాణోక్త ఘటప్రసూతి (పునర్జన్మ సంస్కారం), జాతకర్మ, ఉపనయనం, పత్నీ పునర్వివాహం మరియు అగ్న్యాధానం జరిపించాలి.",
                scripturalSources = sources,
                acharyaConsultationNote = "గమనిక: శ్రీ ఉత్తరాది మఠం లేదా తమ సంప్రదాయ ఆచార్యుల సలహా లేకుండా ఎటువంటి నిర్ణయాలు తీసుకోకూడదు."
            )
            AppLanguage.TAMIL -> MissingPersonGuidance(
                title = "காணாமல் போன நபருக்கான சாஸ்திர வழிகாட்டுதல் (ப்ரோஷித மரண விளக்கம்)",
                statusSummary = "நிலை: காணாமல் போயுள்ளார் / மரணம் உறுதி செய்யப்படவில்லை",
                whyShraddhaProhibited = "உயிருடன் உள்ள நபருக்கு ச்ராத்தம் செய்வது சாஸ்திரத்தில் பெரும் தோஷமாகும். எனவே மரணம் உறுதியாகும் வரை ச்ராத்தம் அல்லது தர்ப்பணம் செய்யக்கூடாது.",
                waitingPeriodInfo = waitingInfo,
                recommendedPrayers = listOf(
                    "ஆயுஷ்ய ஹோமம் மற்றும் மகா மிருத்யுஞ்சய ஜபம் (நீண்ட ஆயுளுக்கும் க்ஷேமமாக திரும்புவதற்கும்)",
                    "ஸ்ரீ சுதர்சன மகா ஹோமம் மற்றும் ஸ்ரீ விஷ்ணு சஹஸ்ரநாம பாராயணம்",
                    "ஸ்ரீ தன்வந்திரி ஜபம் மற்றும் கோ தானம்"
                ),
                postWaitingPeriodProtocol = listOf(
                    "1. சாஸ்திரப்படி காத்திருக்கும் காலம் (${waitingInfo.prescribedWaitingYears} ஆண்டுகள்) முடிந்த பின் ஆசார்யரின் வழிகாட்டுதல் பெறுதல்.",
                    "2. பூரண ஆசௌசம் (3 நாட்கள் அல்லது 10 நாட்கள்).",
                    "3. பர்ணநர தஹனம் (பலாசவிதி - 360 பலாச குச்சிகளால் தகனம்).",
                    "4. நாராயண பலி செய்து, பின்னர் ஷோடச மாஸிகங்கள் மற்றும் வார்ஷிக ச்ராத்தம் தொடங்குதல்."
                ),
                laterConfirmedWorkflow = "மரணம் ஆதாரப்பூர்வமாக உறுதி செய்யப்பட்டால், உறுதி செய்யப்பட்ட தேதி மற்றும் திதியை வைத்து ச்ராத்த பஞ்சாங்கத்தைக் கணக்கிடலாம்.",
                returnAliveRestorationProtocol = "காணாமல் போனவர் உயிருடன் திரும்பினால், கருட புராணத்தின்படி கடப்ரஸூதி (மறுபிறவி சடங்கு), ஜாதகர்மம், உபநயனம், மனைவியுடன் மறுமணம் மற்றும் அக்னியாதானம் செய்ய வேண்டும்.",
                scripturalSources = sources,
                acharyaConsultationNote = "குறிப்பு: ஸ்ரீ உத்தரflowதி மடம் அல்லது தங்கள் சம்பிரதாய ஆசார்யர்களின் ஆலோசனை இன்றி சுயமாக எவ்வித முடிவும் எடுக்கக் கூடாது."
            )
            AppLanguage.ENGLISH -> MissingPersonGuidance(
                title = "Missing Person Shastric Advisory (Proṣita-maraṇa Guidance)",
                statusSummary = "Status: Missing / Death Unconfirmed",
                whyShraddhaProhibited = "In Dharma Shastra, performing Shraddha or funeral libations for a living soul constitutes a grave spiritual transgression. Therefore, death must never be presumed prematurely, and obsequies cannot be initiated while life status remains unknown.",
                waitingPeriodInfo = waitingInfo,
                recommendedPrayers = listOf(
                    "Āyuṣya Homa and Mahā Mṛtyuñjaya Japa (for health, longevity, and safe return)",
                    "Śrī Sudarśana Mahā Homa and regular chanting of Śrī Viṣṇu Sahasranāma Stotram",
                    "Śrī Dhanvantari Japa and Go-dāna (cow donation for protection)"
                ),
                postWaitingPeriodProtocol = listOf(
                    "1. Await the completion of the Shastric waiting period (${waitingInfo.prescribedWaitingYears} years based on age) under formal Acharya consultation.",
                    "2. Observe Purna Ashaucha (3 days or 10 days as per family tradition).",
                    "3. Perform Parṇa-nara-dāha (Palāśa-vidhi effigy cremation of 360 stalks representing bodily joints).",
                    "4. Perform Nārāyaṇa Bali and initiate regular Shodasha Masikas and annual Varshika Shraddhas."
                ),
                laterConfirmedWorkflow = "If official evidence or authentic confirmation of demise is received later, update the record with the confirmed date/tithi to generate the complete Panchanga calendar under Vārtāśravaṇa rules.",
                returnAliveRestorationProtocol = "If a person presumed deceased returns alive after symbolic effigy rites, Garuda Purana (Preta Khanda 4.1-15) ordains Ghaṭa-Prasūti (consecrated re-birth from a ghee/water vessel), Jātakarma, Upanayana renewal, ceremonial re-marriage with existing wife (Punar-pariṇaya), and rekindling of sacred fires.",
                scripturalSources = sources,
                acharyaConsultationNote = "Crucial Notice: Families must never make independent ritual assumptions. All decisions must be sanctioned by a qualified Matha Acharya according to Uttaradi Matha traditions."
            )
        }
    }
}
