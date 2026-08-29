package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.CircumstanceGuidance
import com.shraddhacalendar.core.models.DemiseCircumstance
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.ScripturalSourceReference

object ShastricCircumstanceRepository {

    fun getGuidance(
        circumstance: DemiseCircumstance,
        language: AppLanguage,
        tradition: MadhwaTradition = MadhwaTradition.UTTARADI_MATHA
    ): CircumstanceGuidance {
        return when (circumstance) {
            DemiseCircumstance.NATURAL -> getNaturalGuidance(language, tradition)
            DemiseCircumstance.SNAKEBITE -> getSnakebiteGuidance(language, tradition)
            DemiseCircumstance.DROWNING -> getDrowningGuidance(language, tradition)
            DemiseCircumstance.FIRE_BURNS -> getFireBurnsGuidance(language, tradition)
            DemiseCircumstance.LIGHTNING -> getLightningGuidance(language, tradition)
            DemiseCircumstance.TRAUMA_ACCIDENT -> getTraumaAccidentGuidance(language, tradition)
            DemiseCircumstance.POISONING -> getPoisoningGuidance(language, tradition)
            DemiseCircumstance.FALL_HEIGHT -> getFallHeightGuidance(language, tradition)
            DemiseCircumstance.ANIMAL_ATTACK -> getAnimalAttackGuidance(language, tradition)
            DemiseCircumstance.SELF_INFLICTED -> getSelfInflictedGuidance(language, tradition)
            DemiseCircumstance.UNRECOVERED_BODY -> getUnrecoveredBodyGuidance(language, tradition)
            DemiseCircumstance.PREGNANCY_CHILDBIRTH -> getPregnancyGuidance(language, tradition)
            DemiseCircumstance.OTHER_DURMARANA -> getOtherDurmaranaGuidance(language, tradition)
        }
    }

    private fun getNaturalGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        val (name, meaning, scriptTerm, remedy, remedyScript, timing, purpose, source, tradNote) = when (lang) {
            AppLanguage.KANNADA -> Tuple9(
                "ಪ್ರಾಕೃತ ಮರಣ (ಸ್ವಾಭಾವಿಕ ಮರಣ)",
                "ವೃದ್ಧಾಪ್ಯ ಅಥವಾ ನೈಸರ್ಗಿಕ ಕಾರಣಗಳಿಂದ ಸಂಭವಿಸಿದ ಶಾಂತಿಯುತ ಮರಣ.",
                "ಪ್ರಾಕೃತಮರಣಮ್",
                "ಸಾಮಾನ್ಯ ಆಶೌಚ, ಶವದಹನ, ನವಶ್ರಾದ್ಧ, ಏಕಾದಶಾಹ, ಸಪಿಂಡೀಕರಣ ಹಾಗೂ ೧೬ ಮಾಸಿಕಗಳು",
                "ಪ್ರಾಕೃತ ಅಂತ್ಯೇಷ್ಠಿ ಹಾಗೂ ಷೋಡಶ ಮಾಸಿಕಗಳು",
                "ತಕ್ಷಣದಿಂದ (೧-೧೨ ದಿನಗಳು), ನಂತರ ಪ್ರತಿ ಮಾಸಿಕ ಮತ್ತು ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ ತಿಥಿಗಳಲ್ಲಿ",
                "ಶಾಸ್ತ್ರೋಕ್ತವಾಗಿ ಪಿತೃಲೋಕ ಪ್ರಾಪ್ತಿಗಾಗಿ ವಿಧಿಸಲಾದ ಕರ್ಮಗಳು.",
                ScripturalSourceReference(
                    "ಧರ್ಮಸಿಂಧು (ತೃತೀಯ ಪರಿಚ್ಛೇದ) & ಸ್ಮೃತ್ಯರ್ಥಸಾಗರ",
                    "ಅಂತ್ಯಕರ್ಮ ಹಾಗೂ ಶ್ರಾದ್ಧ ಪ್ರಕರಣ",
                    "ಧರ್ಮಸಿಂಧು - ತೃತೀಯ ಪರಿಚ್ಛೇದಃ, ಉತ್ತರಾರ್ಧ",
                    "जातानुसारेण यथाविधि संस्कारं कुर्यात् । षोडशश्राद्धैः पितृत्वमापद्यते ॥",
                    "ಶಾಸ್ತ್ರವಿಧಿಯಂತೆ ೧೬ ಶ್ರಾದ್ಧಗಳಿಂದ ಜೀವನು ಪ್ರೇತತ್ವ ಮುಕ್ತನಾಗಿ ಪಿತೃತ್ವವನ್ನು ಹೊಂದುತ್ತಾನೆ."
                ),
                "ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠ ಹಾಗೂ ಮಧ್ವ ಸಂಪ್ರದಾಯದ ಪ್ರಾಮಾಣಿಕ ಪದ್ಧತಿಯಂತೆ ನಡೆಯುತ್ತದೆ."
            )
            AppLanguage.SANSKRIT -> Tuple9(
                "प्राकृतमरणम् (स्वाभाविकमरणम्)",
                "वार्धक्येन वा स्वाभाविकरोगोपशमेन शान्त्या प्राप्तं मरणम्।",
                "प्राकृतमरणम्",
                "प्राकृत-और्ध्वदैहिक-संस्काराः, नवश्राद्धानि, सपिण्डीकरणं तथा षोडशमासिकानि",
                "प्राकृतान्त्येष्टिः एवं षोडशमासिकानि",
                "सद्यः (१-१२ दिनानि), ततः प्रति-मासिक-वार्षिक-तिथौ",
                "शास्त्रविहितमार्गेण प्रेतत्वनिवृत्तिपूर्वकं पितृलोकप्राप्त्यर्थम्।",
                ScripturalSourceReference(
                    "धर्मसिन्धुः (तृतीयः परिच्छेदः) & स्मृत्यर्थसागरः",
                    "अन्त्यकर्म तथा श्राद्धप्रकरणम्",
                    "धर्मसिन्धुः - तृतीयपरिच्छेदः (उत्तरार्धम्)",
                    "जातानुसारेण यथाविधि संस्कारं कुर्यात् । षोडशश्राद्धैः पितृत्वमापद्यते ॥",
                    "शास्त्रोक्तरीत्या षोडशश्राद्धसम्पादनेन जीवः पितृभावं प्राप्नोति।"
                ),
                "श्रीमदुत्तरादिमठ-परम्परानुसारं प्रामाणिकम्।"
            )
            AppLanguage.TELUGU -> Tuple9(
                "ప్రాకృత మరణం (స్వాభావిక మరణం)",
                "వృద్ధాప్యం లేదా సహజ కారణాల వల్ల శాంతియుతంగా సంభవించిన మరణం.",
                "ప్రాకృతమరణమ్",
                "సాధారణ అంత్యేష్టి, నవశ్రాద్ధాలు, ఏకాదశాహం, సపిండీకరణం మరియు 16 మాసికాలు",
                "ప్రాకృత అంత్యేష్టి మరియు షోడశ మాసికాలు",
                "వెంటనే (1-12 రోజులు), తరువాత ప్రతి మాసిక మరియు వార్షిక శ్రాద్ధ తిథులలో",
                "శాస్త్రోక్తంగా పితృలోక ప్రాప్తి కోసం నిర్దేశించబడిన కర్మలు.",
                ScripturalSourceReference(
                    "ధర్మసింధు (తృతీయ పరిచ్ఛేదం) & స్మృత్యర్థసాగర",
                    "అంత్యకర్మ మరియు శ్రాద్ధ ప్రకరణం",
                    "ధర్మసింధు - తృతీయ పరిచ్ఛేదః, ఉత్తరార్ధము",
                    "जातानुसारेण यथाविधि संस्कारं कुर्यात् । षोडशश्राद्धैः पितृत्वमापद्यते ॥",
                    "శాస్త్రవిధి ప్రకారం 16 శ్రాద్ధాల ద్వారా జీవుడు ప్ర削除మై పితృత్వాన్ని పొందుతాడు."
                ),
                "శ్రీ ఉత్తరాది మఠం మరియు మధ్వ సాంప్రదాయం ప్రకారం ప్రామాణికం."
            )
            AppLanguage.TAMIL -> Tuple9(
                "ப்ராக்ருத மரணம் (இயற்கை மரணம்)",
                "முதுமை அல்லது இயற்கை காரணங்களால் அமைதியாக ஏற்பட்ட மரணம்.",
                "ப்ராக்ருதமரணம்",
                "இயற்கை அந்தியேஷ்டி, நவச்ராத்தம், ஏகாதசாரம், சபிண்டீகரணம் மற்றும் 16 மாஸிகங்கள்",
                "ப்ராக்ருத அந்தியேஷ்டி மற்றும் ஷோடச மாஸிகங்கள்",
                "உடனடியாக (1-12 நாட்கள்), பின்னர் மாஸிக மற்றும் வார்ஷிக திதிகளில்",
                "சாஸ்திரப்படி பித்ருலோக ப்ராப்திக்காக விதிக்கப்பட்ட கர்மங்கள்.",
                ScripturalSourceReference(
                    "தர்மஸிந்து (த்ருதீய பரிச்சேதம்) & ஸ்மிருத்யர்த்தஸாகரம்",
                    "அந்தியகர்ம மற்றும் ச்ராத்த ப்ரகரணம்",
                    "தர்மஸிந்து - த்ருதீய பரிச்சேதம், உத்தரார்தம்",
                    "जातानुसारेण यथाविधि संस्कारं कुर्यात् । षोडशश्राद्धैः पितृत्वमापद्यते ॥",
                    "சாஸ்திர விதிகளின்படி 16 ச்ராத்தங்களால் ஜீவன் பித்ருத்வத்தை அடைகிறான்."
                ),
                "ஸ்ரீ உத்தரflowதி மடம் மற்றும் மத்வ சம்பிரதாய முறைப்படி பிரமாணமானது."
            )
            AppLanguage.ENGLISH -> Tuple9(
                "Natural Demise (Prākṛta Maraṇam)",
                "Demise due to natural causes, old age, or peaceful physiological cessation.",
                "प्राकृतमरणम्",
                "Standard Antyesti, Dasha-gatra, Ekadashaha, Sapindikarana and Shodasha Masikas",
                "Prākṛta Antyeṣṭi & Ṣoḍaśa Māsikāni",
                "Immediately (Days 1–12), followed by regular Masika and Varshika Shraddha dates",
                "Standard Vedic obsequies ordained to relieve the departed soul and grant admission to Pitruloka.",
                ScripturalSourceReference(
                    "Dharmasindhu (Section 3, Uttarardha) & Smrityarthasagara",
                    "Antya Karma & Shraddha Prakarana",
                    "Dharmasindhu III, Uttarardha",
                    "जातानुसारेण यथाविधि संस्कारं कुर्यात् । षोडशश्राद्धैः पितृत्वमापद्यते ॥",
                    "Through the sixteen ordained shraddhas performed in due order, the soul attains the state of Pitru."
                ),
                "Standard Shastric practice followed across all Vedic traditions and Uttaradi Matha."
            )
        }

        return CircumstanceGuidance(
            circumstance = DemiseCircumstance.NATURAL,
            localizedName = name,
            localizedMeaning = meaning,
            sanskritTermLocalScript = scriptTerm,
            remedyName = remedy,
            remedySanskritLocalScript = remedyScript,
            timingGuidance = timing,
            purposeExplanation = purpose,
            scripturalSource = source,
            traditionNotes = tradNote,
            isMandatory = true
        )
    }

    private fun getSnakebiteGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        val (name, meaning, scriptTerm, remedy, remedyScript, timing, purpose, source, tradNote) = when (lang) {
            AppLanguage.KANNADA -> Tuple9(
                "ಸರ್ಪದಷ್ಟ ಮರಣ (ಹಾವು ಕಡಿತ)",
                "ವಿಷಪೂರಿತ ಸರ್ಪ ಅಥವಾ ಸರೀಸೃಪಗಳ ಕಡಿತದಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
                "ಸರ್ಪದಷ್ಟಮರಣಮ್",
                "ನಾರಾಯಣ ಬಲಿ ಹಾಗೂ ನಾಗಬಲಿ (ನಾಗಪ್ರತಿಮಾ ದಾನ ಸಹಿತ)",
                "ನಾರಾಯಣಬಲಿಃ ಹಾಗೂ ನಾಗಬಲಿಃ",
                "ಸಪಿಂಡೀಕರಣಕ್ಕೆ ಮೊದಲು ಅಥವಾ ಪವಿತ್ರ ಕ್ಷೇತ್ರದಲ್ಲಿ (ಉದಾ: ತ್ರಯಂಬಕೇಶ್ವರ, ಗಯಾ, ಗೋಕರ್ಣ)",
                "ಸರ್ಪದೋಷ ಮತ್ತು ದುರ್ಮರಣ ನಿವೃತ್ತಿಗಾಗಿ ಹಾಗೂ ಸದ್ಗತಿಗಾಗಿ ಗರುಡ ಪುರಾಣೋಕ್ತ ವಿಧಿ.",
                ScripturalSourceReference(
                    "ಗರುಡ ಪುರಾಣ (ಪ್ರೇತ ಖಂಡ - ೪೦ನೇ ಅಧ್ಯಾಯ) & ಧರ್ಮಸಿಂಧು",
                    "ಸರ್ಪದಷ್ಟ ದುರ್ಮರಣ ಪ್ರಾಯಶ್ಚಿತ್ತ ಪ್ರಕರಣ",
                    "ಗರುಡ ಪುರಾಣ ಪ್ರೇತ ಖಂಡಃ ೪೦.೪-೧೨; ಧರ್ಮಸಿಂಧುಃ ತೃತೀಯ ಪರಿಚ್ಛೇದಃ",
                    "सर्पदष्टस्य च तथा नारायणबलिः स्मृतः । नागबलिं प्रकुर्वीत नागदोषप्रशान्तये ॥",
                    "ಸರ್ಪದಷ್ಟ ಮರಣ ಹೊಂದಿದವರಿಗೆ ನಾರಾಯಣ ಬಲಿ ಹಾಗೂ ನಾಗದೋಷ ಶಾಂತಿಗಾಗಿ ನಾಗಬಲಿಯನ್ನು ಮಾಡಬೇಕು."
                ),
                "ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠ ಮತ್ತು ಮಾಧ್ವ ಪರಂಪರೆಯಲ್ಲಿ ಸರ್ಪದಷ್ಟ ಮರಣಕ್ಕೆ ನಾರಾಯಣ-ನಾಗಬಲಿ ಶಾಸ್ತ್ರಸಮ್ಮತವಾಗಿದೆ."
            )
            AppLanguage.SANSKRIT -> Tuple9(
                "सर्पदष्टमरणम् (सर्पदंशमरणम्)",
                "विषधरसर्पादीनां दंशनेन प्राप्तम् अकालमरणम्।",
                "सर्पदष्टमरणम्",
                "नारायणबलिः एवं नागबलिः (नागप्रतिमादानसहितः)",
                "नारायणबलिः एवं नागबलिः",
                "सपिण्डीकरणात् प्राक् अथवा तीर्थक्षेत्रे (यथा त्र्यम्बकेश्वर-गया-गोकर्णादौ)",
                "सर्पदोषोपशमनार्थं प्रेतत्वविमुक्तिपूर्वक-सद्गतिप्राप्तये गरुडपुराणोक्तो विधिः।",
                ScripturalSourceReference(
                    "गरुडपुराणम् (प्रेतखण्डः - ४० अध्यायः) & धर्मसिन्धुः",
                    "सर्पदष्ट-दुर्मरण-प्रायश्चित्त-प्रकरणम्",
                    "गरुडपुराणम् २.४०.४-१२; धर्मसिन्धुः तृतीयपरिच्छेदः",
                    "सर्पदष्टस्य च तथा नारायणबलिः स्मृतः । नागबलिं प्रकुर्वीत नागदोषप्रशान्तये ॥",
                    "सर्पदष्टमरणे नारायणबलिः नागदोषशान्त्यर्थं च नागबलिर्विधेयः।"
                ),
                "माध्वसम्प्रदाये उत्तरादिमठरीत्या नागबलि-सहित-नारायणबलिः अनुष्ठेयः।"
            )
            AppLanguage.TELUGU -> Tuple9(
                "సర్పదష్ట మరణం (పాము కాటు)",
                "విషసర్పం లేదా విషజీవుల కాటు వల్ల సంభవించిన మరణం.",
                "సర్పదష్టమరణమ్",
                "నారాయణ బలి మరియు నాగబలి (నాగప్రతిమా దాన సహితం)",
                "నారాయణబలిః మరియు నాగబలిః",
                "సపిండీకరణకు ముందు లేదా పుణ్యక్షేత్రంలో (ఉదా: త్రయంబకేశ్వర్, గయ, గోకర్ణం)",
                "సర్పదోష నివృత్తికి మరియు ఆత్మకు సద్గతి కలగడానికి గరుడ పురాణోక్త విధి.",
                ScripturalSourceReference(
                    "గరుడ పురాణం (ప్రేత ఖండం - 40వ అధ్యాయం) & ధర్మసింధు",
                    "సర్పదష్ట దుర్మరణ ప్రాయశ్చిత్త ప్రకరణం",
                    "గరుడ పురాణం 2.40.4-12; ధర్మసింధుః తృతీయ పరిచ్ఛేదః",
                    "सर्पदष्टस्य च तथा नारायणबलिः स्मृतः । नागबलिं प्रकुर्वीत नागदोषप्रशान्तये ॥",
                    "పాము కాటు వల్ల మరణించిన వారికి నారాయణ బలి మరియు నాగదోష శాంతికి నాగబలి చేయాలి."
                ),
                "శ్రీ ఉత్తరాది మఠం మరియు మధ్వ సంప్రదాయంలో నారాయణ-నాగబలి నిర్దేశించబడింది."
            )
            AppLanguage.TAMIL -> Tuple9(
                "ஸர்பதஷ்ட மரணம் (பாம்புக்கடி)",
                "விஷப் பாம்பு அல்லது விஷ ஜந்துக்களின் கடியினால் ஏற்பட்ட மரணம்.",
                "ஸர்பதஷ்டமரணம்",
                "நாராயண பலி மற்றும் நாகபலி (நாகப் ப்ரதிமை தானத்துடன்)",
                "நாராயணபலிஃ மற்றும் நாகபலிஃ",
                "சபிண்டீகரனத்திற்கு முன் அல்லது புண்ணிய க்ஷேத்ரத்தில் (உதா: த்ரயம்பகேச்வரம், கயா, கோகர்ணம்)",
                "ஸர்பதோஷ நிவர்த்திக்கும் ஆத்ம சாந்திக்கும் கருட புராணத்தில் கூறப்பட்ட விதி.",
                ScripturalSourceReference(
                    "கருட புராணம் (ப்ரேத காண்டம் - 40ஆம் அத்தியாயம்) & தர்மஸிந்து",
                    "ஸர்பதஷ்ட துர்மரண பிராயச்சித்த ப்ரகரணம்",
                    "கருட புராணம் 2.40.4-12; தர்மஸிந்து த்ருதீய பரிச்சேதம்",
                    "सर्पदष्टस्य च तथा नारायणबलिः स्मृतः । नागबलिं प्रकुर्वीत नागदोषप्रशान्तये ॥",
                    "பாம்புக்கடியால் இறந்தவர்களுக்கு நாராயண பலியும் நாகதோஷ சாந்திக்கு நாகபலியும் செய்ய வேண்டும்."
                ),
                "ஸ்ரீ உத்தரflowதி மடம் மற்றும் மத்வ சம்பிரதாயத்தில் நாராயண-நாகபலி பரிந்துரைக்கப்பட்டுள்ளது."
            )
            AppLanguage.ENGLISH -> Tuple9(
                "Snakebite Demise (Sarpa-daṣṭa Maraṇam)",
                "Demise caused by venomous snakebite or toxic reptile sting.",
                "सर्पदष्टमरणम्",
                "Narayana Bali and Nagabali (with Naga Pratima Dana)",
                "Nārāyaṇa Bali & Nāgabali",
                "Prior to Sapindikarana or at a designated pilgrimage Kshetra (e.g. Trimbakeshwar, Gaya, Gokarna)",
                "Prescribed in Garuda Purana to alleviate Sarpa-dosha and release the soul from intermediate distress.",
                ScripturalSourceReference(
                    "Garuda Purana (Preta Khanda, Ch. 40, v. 4-12) & Dharmasindhu III",
                    "Durmarana Prayaschitta Prakarana",
                    "Garuda Purana II.40.4-12; Dharmasindhu Tritiya Parichheda",
                    "सर्पदष्टस्य च तथा नारायणबलिः स्मृतः । नागबलिं प्रकुर्वीत नागदोषप्रशान्तये ॥",
                    "For one deceased of snakebite, Narayana Bali is ordained, along with Nagabali for the pacification of serpent afflictions."
                ),
                "Authentic procedure in Madhva tradition and Uttaradi Matha: Narayana-Nagabali performed under qualified Acharya guidance."
            )
        }

        return CircumstanceGuidance(
            circumstance = DemiseCircumstance.SNAKEBITE,
            localizedName = name,
            localizedMeaning = meaning,
            sanskritTermLocalScript = scriptTerm,
            remedyName = remedy,
            remedySanskritLocalScript = remedyScript,
            timingGuidance = timing,
            purposeExplanation = purpose,
            scripturalSource = source,
            traditionNotes = tradNote,
            isMandatory = false
        )
    }

    private fun getDrowningGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        val (name, meaning, scriptTerm, remedy, remedyScript, timing, purpose, source, tradNote) = when (lang) {
            AppLanguage.KANNADA -> Tuple9(
                "ಜಲ ಮರಣ (ನೀರಿನಲ್ಲಿ ಮುಳುಗುವಿಕೆ)",
                "ನದಿ, ಸಮುದ್ರ, ಬಾವಿ ಅಥವಾ ನೀರಿನ ಅಪಘಾತದಲ್ಲಿ ಮುಳುಗಿ ಸಂಭವಿಸಿದ ಮರಣ.",
                "ಜಲೇ ಮಜ್ಜನಮರಣಮ್",
                "ನಾರಾಯಣ ಬಲಿ (ಜಲಪತನ ಪ್ರಾಯಶ್ಚಿತ್ತ ಹಾಗೂ ಕೃಚ್ಛ್ರ ದಾನ ಸಹಿತ)",
                "ನಾರಾಯಣಬಲಿಃ",
                "ಸಪಿಂಡೀಕರಣಕ್ಕೆ ಮೊದಲು ಅಥವಾ ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧದ ಮುನ್ನ",
                "ಜಲ ದುರ್ಮರಣ ನಿವೃತ್ತಿಗಾಗಿ ಹಾಗೂ ಮುಕ್ತಿಗಾಗಿ ಬೋಧಾಯನ ಹಾಗೂ ಧರ್ಮಸಿಂಧೂಕ್ತ ವಿಧಿ.",
                ScripturalSourceReference(
                    "ಬೋಧಾಯನ ಗೃಹ್ಯಶೇಷ ಸೂತ್ರ (೩.೨೦-೨೧) & ಧರ್ಮಸಿಂಧು",
                    "ಜಲಮರಣ ಪ್ರಾಯಶ್ಚಿತ್ತ ಪ್ರಕರಣ",
                    "ಬೋಧಾಯನ ಗೃಹ್ಯಶೇಷ ಸೂತ್ರಮ್ ೩.೨೦-೨೧; ಧರ್ಮಸಿಂಧುಃ ತೃತೀಯ ಪರಿಚ್ಛೇದಃ",
                    "जले मृतानां दहने विशेषो नारायणबलिः कार्यः ॥",
                    "ನೀರಿನಲ್ಲಿ ಮುಳುಗಿ ಮೃತರಾದವರಿಗೆ ನಾರಾಯಣ ಬಲಿ ಕರ್ಮವನ್ನು ವಿಧಿಸಲಾಗಿದೆ."
                ),
                "ಮಧ್ವ ಸಂಪ್ರದಾಯ ಹಾಗೂ ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠದ ಪದ್ಧತಿಯಂತೆ ವಿದ್ವಾಂಸರ ಮಾರ್ಗದರ್ಶನದಲ್ಲಿ ಆಚರಿಸುವುದು."
            )
            AppLanguage.SANSKRIT -> Tuple9(
                "जले मज्जनमरणम् (तोयमरणम्)",
                "नद्यां समुद्रे कूपे वा जले निमज्जनकारणतः प्राप्तम् अकालमरणम्।",
                "जले मज्जनमरणम्",
                "नारायणबलिः (जलपतनप्रायश्चित्त-कृच्छ्रदानसहितः)",
                "नारायणबलिः",
                "सपिण्डीकरणात् प्राक् अथवा प्रथमवार्षिकात् पूर्वम्",
                "जलदुर्मरणदोषनिवृत्त्यर्थं सद्गतिप्राप्तये बोधायनोक्तो विधिः।",
                ScripturalSourceReference(
                    "बौधायनगृह्यशेषसूत्रम् (३.२०-२१) & धर्मसिन्धुः",
                    "जलमरण-प्रायश्चित्त-प्रकरणम्",
                    "बौधायनगृह्यशेषसूत्रम् ३.२०-२१; धर्मसिन्धुः तृतीयपरिच्छेदः",
                    "जले मृतानां दहने विशेषो नारायणबलिः कार्यः ॥",
                    "तोये मृतानां जीवानां सद्गतये नारायणबलिः विधीयते।"
                ),
                "उत्तरादिमठ-परम्परानुसारं विदुषां मार्गदर्शनेन कार्यम्।"
            )
            AppLanguage.TELUGU -> Tuple9(
                "జల మరణం (నీటిలో మునుగుట)",
                "నది, సముద్రం, బావి లేదా జల ప్రమాదంలో మునిగి సంభవించిన మరణం.",
                "జలే మజ్జనమరణమ్",
                "నారాయణ బలి (జలపతన ప్రాయశ్చిత్త మరియు కృచ్ఛ్ర దాన సహితం)",
                "నారాయణబలిః",
                "సపిండీకరణకు ముందు లేదా ప్రథమ వార్షిక శ్రాద్ధానికి ముందు",
                "జల దుర్మరణ దోష నివృత్తికి మరియు సద్గతికి బోధాయన మరియు ధర్మసింధు సూచించిన విధి.",
                ScripturalSourceReference(
                    "బోధాయన గృహ్యశేష సూత్రం (3.20-21) & ధర్మసింధు",
                    "జలమరణ ప్రాయశ్చిత్త ప్రకరణం",
                    "బోధాయన గృహ్యశేష సూత్రం 3.20-21; ధర్మసింధుః తృతీయ పరిచ్ఛేదః",
                    "जले मृतानां दहने विशेषो नारायणबलिः कार्यः ॥",
                    "నీటిలో మునిగి మరణించిన వారికి నారాయణ బలి నిర్వహించాలి."
                ),
                "శ్రీ ఉత్తరాది మఠం మరియు మధ్వ సాంప్రదాయం ప్రకారం విద్వాంసుల పర్యవేక్షణలో చేయవలెను."
            )
            AppLanguage.TAMIL -> Tuple9(
                "ஜல மரணம் (நீரில் மூழ்குதல்)",
                "ஆறு, கடல், கிணறு அல்லது நீர் விபத்தில் மூழ்கி ஏற்பட்ட மரணம்.",
                "ஜலே மஜ்ஜனமரணம்",
                "நாராயண பலி (ஜலபதன பிராயச்சித்தம் மற்றும் கிருச்சிர தானத்துடன்)",
                "நாராயணபலிஃ",
                "சபிண்டீகரனத்திற்கு முன் அல்லது முதல் வார்ஷிக ச்ராத்தத்திற்கு முன்",
                "ஜல துர்மரண தோஷ நிவர்த்திக்கும் நற்கதிக்கும் போதாயன சாஸ்திர விதி.",
                ScripturalSourceReference(
                    "போதாயன கிருஹ்யசேஷ சூத்திரம் (3.20-21) & தர்மஸிந்து",
                    "ஜலமரண பிராயச்சித்த ப்ரகரணம்",
                    "போதாயன கிருஹ்யசேஷ சூத்திரம் 3.20-21; தர்மஸிந்து த்ருதீய பரிச்சேதம்",
                    "जले मृतानां दहने विशेषो नारायणबलिः कार्यः ॥",
                    "நீரில் மூழ்கி இறந்தவர்களுக்கு நாராயண பலி கர்மம் விதிக்கப்பட்டுள்ளது."
                ),
                "ஸ்ரீ உத்தரflowதி மடம் மற்றும் மத்வ சம்பிரதாய முறைப்படி ஆசார்ய வழிகாட்டுதலுடன் செய்யத்தக்கது."
            )
            AppLanguage.ENGLISH -> Tuple9(
                "Drowning Demise (Jala-majjana Maraṇam)",
                "Demise caused by drowning in water bodies, floods, or maritime accidents.",
                "जले मज्जनमरणम्",
                "Narayana Bali (with Jala-patana expiation and Kricchra Dana)",
                "Nārāyaṇa Bali",
                "Prior to Sapindikarana or before the completion of the 1st year rites",
                "Prescribed in Baudhāyana Gṛhya Śeṣa Sūtra and Dharmasindhu to alleviate trauma and bestow peace.",
                ScripturalSourceReference(
                    "Baudhayana Grihya Shesha Sutra (III.20-21) & Dharmasindhu III",
                    "Jala-marana Prayaschitta Prakarana",
                    "Baudhayana Grihya Shesha Sutra III.20-21; Dharmasindhu III",
                    "जले मृतानां दहने विशेषो नारायणबलिः कार्यः ॥",
                    "For those who perish in water, Narayana Bali is specially ordained for the liberation of the soul."
                ),
                "Observed across Madhva tradition and Uttaradi Matha with guidance from learned Purohitas."
            )
        }

        return CircumstanceGuidance(
            circumstance = DemiseCircumstance.DROWNING,
            localizedName = name,
            localizedMeaning = meaning,
            sanskritTermLocalScript = scriptTerm,
            remedyName = remedy,
            remedySanskritLocalScript = remedyScript,
            timingGuidance = timing,
            purposeExplanation = purpose,
            scripturalSource = source,
            traditionNotes = tradNote,
            isMandatory = false
        )
    }

    private fun getFireBurnsGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.FIRE_BURNS,
            sanskritName = "अग्निदग्धमरणम्",
            lang = lang,
            englishName = "Fire / Burns Demise (Agni-dagdha Maraṇam)",
            englishMeaning = "Demise resulting from accidental burns, electrical conflagrations, or fire incidents.",
            kannadaName = "ಅಗ್ನಿದಗ್ಧ ಮರಣ (ಬೆಂಕಿ ಅನಾಹುತ)",
            kannadaMeaning = "ಆಕಸ್ಮಿಕ ಬೆಂಕಿ ಅಥವಾ ಸುಟ್ಟ ಗಾಯಗಳಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
            teluguName = "అగ్నిదగ్ధ మరణం (అగ్ని ప్రమాదం)",
            teluguMeaning = "ప్రమాదవశాత్తు అగ్ని లేదా కాలిన గాయాల వల్ల సంభవించిన మరణం.",
            tamilName = "அக்னிதக்த மரணம் (தீ விபத்து)",
            tamilMeaning = "தீக்காயங்கள் அல்லது தீ விபத்தினால் ஏற்பட்ட மரணம்.",
            sanskritMeaning = "अग्निना दग्धशरीरेण प्राप्तम् अकालमरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Dharmasindhu (Section 3, Uttarardha) & Nirnayasindhu III",
                "Durmarana Prayaschitta Prakarana",
                "Dharmasindhu III; Nirnayasindhu III (Parichheda 3)",
                "अग्निदग्धानां दुर्मरणप्रायश्चित्तार्थं नारायणबलिः स्मृतः ॥",
                "For those deceased from burns or fire accidents, Narayana Bali is prescribed as expiatory rite."
            )
        )
    }

    private fun getLightningGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.LIGHTNING,
            sanskritName = "विद्युत् / वज्रपातमरणम्",
            lang = lang,
            englishName = "Lightning / High-Voltage Demise (Vajrapāta Maraṇam)",
            englishMeaning = "Demise resulting from natural lightning strike or severe high-voltage electrocution.",
            kannadaName = "ವಿದ್ಯುತ್ / ವಜ್ರಪಾತ ಮರಣ",
            kannadaMeaning = "ಆಕಾಶದ ಸಿಡಿಲು-ಮಿಂಚು ಅಥವಾ ತೀವ್ರ ವಿದ್ಯುತ್ ಆಘಾತದಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
            teluguName = "విద్యుత్ / వజ్రపాత మరణం",
            teluguMeaning = "పిడుగుపాటు లేదా విద్యుదాఘాతం వల్ల సంభవించిన మరణం.",
            tamilName = "வித்யுத் / வஜ்ரபாத மரணம் (மின்னல் / மின்சாரம்)",
            tamilMeaning = "மின்னல் தாக்குதல் அல்லது மின் விபத்தினால் ஏற்பட்ட மரணம்.",
            sanskritMeaning = "वज्रपातेन विद्युदाघातेन वा प्राप्तम् अकालमरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Garuda Purana (Preta Khanda 40.8) & Dharmasindhu III",
                "Vajrapata Shanti Prakarana",
                "Garuda Purana II.40.8",
                "विद्युद्वज्रहतानां च नारायणबलिर्हितः ॥",
                "For those struck by lightning or heavenly thunderbolts, Narayana Bali is spiritually beneficial."
            )
        )
    }

    private fun getTraumaAccidentGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.TRAUMA_ACCIDENT,
            sanskritName = "शस्त्र / आयुधहतमरणम्",
            lang = lang,
            englishName = "Trauma / Vehicular / Weapon Demise (Śastra-hata Maraṇam)",
            englishMeaning = "Demise caused by vehicular accidents, severe mechanical trauma, or weapon impact.",
            kannadaName = "ಶಸ್ತ್ರ / ಆಯುಧ / ವಾಹನ ಅಪಘಾತ ಮರಣ",
            kannadaMeaning = "ವಾಹನ ಅಪಘಾತ, ಯಂತ್ರೋಪಕರಣ ಅಥವಾ ತೀವ್ರ ಪೆಟ್ಟಿನಿಂದ ಸಂಭವಿಸಿದ ಆಕಸ್ಮಿಕ ಮರಣ.",
            teluguName = "శస్త్ర / ఆయుధ / వాహన ప్రమాద మరణం",
            teluguMeaning = "వాహన ప్రమాదాలు, యంత్రాల గాయాలు లేదా తీవ్ర దెబ్బల వల్ల సంభవించిన మరణం.",
            tamilName = "சஸ்த்ர / ஆயுத / வாகன விபத்து மரணம்",
            tamilMeaning = "வாகன விபத்து, இயந்திர காயம் அல்லது ஆயுத தாக்குதலால் ஏற்பட்ட மரணம்.",
            sanskritMeaning = "वाहनाभिघातेन शस्त्रप्रहारेण वा प्राप्तं दुर्मरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Garuda Purana (Preta Khanda Ch. 40) & Smrityarthasagara",
                "Shastra-hata Durmarana Prakarana",
                "Garuda Purana II.40.6-10; Smrityarthasagara Durmarana Adhyaya",
                "शस्त्रघातेन ये मृतास्तेषां नारायणबलिर्विधेयः ॥",
                "For those who meet untimely end through vehicular collisions or sharp trauma, Narayana Bali is ordained."
            )
        )
    }

    private fun getPoisoningGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.POISONING,
            sanskritName = "विषभक्षणमरणम्",
            lang = lang,
            englishName = "Poison / Toxicity Demise (Viṣa-bhakṣaṇa Maraṇam)",
            englishMeaning = "Demise resulting from accidental poison consumption, chemical toxicity, or noxious gas inhalation.",
            kannadaName = "ವಿಷಪ್ರಾಶನ ಮರಣ",
            kannadaMeaning = "ಆಕಸ್ಮಿಕ ವಿಷ ಸೇವನೆ, ರಾಸಾಯನಿಕ ಅಥವಾ ವಿಷಾನಿಲ ಸೋಂಕಿನಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
            teluguName = "విషప్రాశన మరణం",
            teluguMeaning = "విషం లేదా ప్రమాదకర రసాయనాల ప్రభావం వల్ల సంభవించిన మరణం.",
            tamilName = "விஷபக்ஷண மரணம் (விஷம் / நச்சு வாயு)",
            tamilMeaning = "விஷ உட்கொள்ளல் அல்லது நச்சுப் பொருட்களால் ஏற்பட்ட மரணம்.",
            sanskritMeaning = "विषसंयोगेन वा विषाक्तपदार्थभक्षणेन प्राप्तं मरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Dharmasindhu III & Nirnayasindhu III",
                "Visha-hata Shanti Prakarana",
                "Dharmasindhu III (Uttarardha); Nirnayasindhu III",
                "विषभक्षणमृतानां नारायणबलिना प्रेतत्वनिवृत्तिः ॥",
                "Through Narayana Bali, the soul released from toxic afflictions attains peaceful transition."
            )
        )
    }

    private fun getFallHeightGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.FALL_HEIGHT,
            sanskritName = "वृक्ष / गिरिपातनेन मरणम्",
            lang = lang,
            englishName = "Fall from Height Demise (Pātana Maraṇam)",
            englishMeaning = "Demise caused by falling from heights, buildings, trees, cliffs, or structural collapse.",
            kannadaName = "ಎತ್ತರದಿಂದ ಬಿದ್ದು ಮರಣ (ವೃಕ್ಷ/ಪರ್ವತ ಪತನ)",
            kannadaMeaning = "ಕಟ್ಟಡ, ಮರ, ಬೆಟ್ಟ ಅಥವಾ ಎತ್ತರ ಪ್ರದೇಶದಿಂದ ಕೆಳಗೆ ಬಿದ್ದು ಸಂಭವಿಸಿದ ಮರಣ.",
            teluguName = "ఎత్తునుండి పడి మరణం",
            teluguMeaning = "భవనాలు, చెట్లు లేదా ఎత్తైన ప్రదేశాల నుండి పడిపోవడం వల్ల సంభవించిన మరణం.",
            tamilName = "உயரத்திலிருந்து விழுந்து மரணம்",
            tamilMeaning = "மரங்கள், மலைகள் அல்லது உயரமான இடங்களிலிருந்து கீழே விழுந்து ஏற்பட்ட மரணம்.",
            sanskritMeaning = "वृक्षाद् गिरेर्वा पतनेन प्राप्तम् अकालमरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Garuda Purana (Preta Khanda 40.7) & Dharmasindhu III",
                "Patana Marana Prakarana",
                "Garuda Purana II.40.7",
                "वृक्षप्रपतनेनैव गिरिपातकृते तथा । नारायणबलिः कार्यः ॥",
                "For those meeting demise through falls from heights, Narayana Bali is ordained."
            )
        )
    }

    private fun getAnimalAttackGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.ANIMAL_ATTACK,
            sanskritName = "श्वपद / शृङ्गिदंष्ट्रिहतमरणम्",
            lang = lang,
            englishName = "Wild Animal Attack Demise (Śvapada-hata Maraṇam)",
            englishMeaning = "Demise caused by dangerous wild animals, predators, horned cattle, or beasts.",
            kannadaName = "ಕಾಡುಪ್ರಾಣಿ ದಾಳಿ ಮರಣ",
            kannadaMeaning = "ಕಾಡುಮೃಗಗಳು, ಕೊಂಬುಳ್ಳ ಪ್ರಾಣಿಗಳು ಅಥವಾ ಶ್ವಪದಗಳ ಆಕ್ರಮಣದಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
            teluguName = "క్రూరమృగ దాడి మరణం",
            teluguMeaning = "క్రూర జంతువులు లేదా అడవి మృగాల దాడి వల్ల సంభవించిన మరణం.",
            tamilName = "காட்டு மிருக தாக்குதல் மரணம்",
            tamilMeaning = "வனவிலங்குகள் அல்லது கொடிய மிருகங்களின் தாக்குதலால் ஏற்பட்ட மரணம்.",
            sanskritMeaning = "व्याघ्रादि-क्रूरमृगैः शृङ्गिभिर्वा हतानां मरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Garuda Purana (Preta Khanda 40.9) & Dharmasindhu III",
                "Shvapada-hata Prakarana",
                "Garuda Purana II.40.9",
                "दंष्ट्रिशृङ्गिहतानां च नारायणबलिर्हितः ॥",
                "For those attacked by wild horned or fanged beasts, Narayana Bali is spiritually beneficial."
            )
        )
    }

    private fun getSelfInflictedGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        val (name, meaning, scriptTerm, remedy, remedyScript, timing, purpose, source, tradNote) = when (lang) {
            AppLanguage.KANNADA -> Tuple9(
                "ಆತ್ಮಹತ್ಯಾ / ಉದ್ಬಂಧನ ಮರಣ (ಸ್ವಯಂಕೃತ ಮರಣ)",
                "ಮಾನಸಿಕ ಕ್ಲೇಶದಿಂದ ಸ್ವಯಂಪ್ರೇರಿತವಾಗಿ ಅಥವಾ ಉದ್ಬಂಧನದಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
                "ಆತ್ಮಘಾತ / ಉದ್ಬಂಧನಮರಣಮ್",
                "ಮಹಾಪ್ರಾಯಶ್ಚಿತ್ತ ಸಹಿತ ನಾರಾಯಣ ಬಲಿ",
                "ಮಹಾಪ್ರಾಯಶ್ಚಿತ್ತಸಹಿತ ನಾರಾಯಣಬಲಿಃ",
                "ಆಚಾರ್ಯರ ಮಾರ್ಗದರ್ಶನದಂತೆ ಪ್ರಾಯಶ್ಚಿತ್ತ ದಾನಗಳ ನಂತರ ನಾರಾಯಣ ಬಲಿ ಮಾಡುವುದು",
                "ಶಾಸ್ತ್ರದಲ್ಲಿ ಇಂತಹ ಆತ್ಮಗಳಿಗೆ ಕೃಪೆಯಿಂದ ಶಾಂತಿ ದೊರಕಿಸಲು ನಾರಾಯಣ ಬಲಿ ಹಾಗೂ ವಿಶೇಷ ವಿಷ್ಣು ಆರಾಧನೆ ವಿಧಿಸಲಾಗಿದೆ.",
                ScripturalSourceReference(
                    "ಮನು ಸ್ಮೃತಿ (೫.೮೯), ಗರುಡ ಪುರಾಣ (೨.೪೦.೪-೧೫) & ಧರ್ಮಸಿಂಧು",
                    "ಆತ್ಮಘಾತ ಪ್ರಾಯಶ್ಚಿತ್ತ ಪ್ರಕರಣ",
                    "ಮನುಸ್ಮೃತಿಃ ೫.೮೯; ಗರುಡ ಪುರಾಣಮ್ ೨.೪೦; ಧರ್ಮಸಿಂಧುಃ ತೃತೀಯ ಪರಿಚ್ಛೇದಃ",
                    "आत्मघातिनां प्रेतत्वनिवृत्तये नारायणबलिरुपदिश्यते ॥",
                    "ಸ್ವಯಂಕೃತ ಮರಣ ಹೊಂದಿದವರಿಗೆ ಪ್ರೇತತ್ವ ನಿವಾರಣೆಗಾಗಿ ಕರುಣೆಯಿಂದ ನಾರಾಯಣ ಬಲಿ ಕರ್ಮವನ್ನು ಉಪದೇಶಿಸಲಾಗಿದೆ."
                ),
                "ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠ ಹಾಗೂ ಮಧ್ವ ಸಂಪ್ರದಾಯದಲ್ಲಿ ಯಾವುದೇ ನಿಂದನೆಯಿಲ್ಲದೆ ಆಪ್ತ ವಿದ್ವಾಂಸರ ಮಾರ್ಗದರ್ಶನದಲ್ಲಿ ಆಚರಿಸುವುದು."
            )
            AppLanguage.SANSKRIT -> Tuple9(
                "आत्मघात / उद्वन्धनमरणम् (स्वेच्छामरणम्)",
                "अत्यन्तमानसिकपीडया स्वेच्छया वा उद्वन्धनेन प्राप्तं मरणम्।",
                "आत्मघात / उद्वन्धनमरणम्",
                "महाप्रायश्चित्तसहित-नारायणबलिः",
                "महाप्रायश्चित्तसहित-नारायणबलिः",
                "विदुषां मार्गदर्शनेन प्रायश्चित्तपूर्वं नारायणबलिः विधेयः",
                "शास्त्रे दयया आत्मनः शान्त्यर्थं नारायणाराधनपूर्वक-नारायणबलिः उपदिष्टः।",
                ScripturalSourceReference(
                    "मनुस्मृतिः (५.८९), गरुडपुराणम् (२.४०.४-१५) & धर्मसिन्धुः",
                    "आत्मघात-प्रायश्चित्त-प्रकरणम्",
                    "मनुस्मृतिः ५.८९; गरुडपुराणम् २.४०; धर्मसिन्धुः तृतीयपरिच्छेदः",
                    "आत्मघातिनां प्रेतत्वनिवृत्तये नारायणबलिरुपदिश्यते ॥",
                    "स्वेच्छया मृतानां जीवानां सद्गतये शास्त्रेषु नारायणबलिः प्रोक्तः।"
                ),
                "माध्वसम्प्रदाये विदुषां सम्मत्या आदरपूर्वकं कर्तव्यम्।"
            )
            AppLanguage.TELUGU -> Tuple9(
                "ఆత్మహత్యా / ఉద్బంధన మరణం (స్వయంకృత మరణం)",
                "తీవ్ర మానసిక వేదన వల్ల స్వీయ నిర్ణయంతో లేదా ఉద్బంధనం వల్ల సంభవించిన మరణం.",
                "ఆత్మఘాత / ఉద్బంధనమరణమ్",
                "మహాప్రాయశ్చిత్త సహిత నారాయణ బలి",
                "మహాప్రాయశ్చిత్తసహిత నారాయణబలిః",
                "ఆచార్యుల మార్గదర్శకత్వంలో ప్రాయశ్చిత్త దానాల అనంతరం నారాయణ బలి నిర్వహించాలి",
                "శాస్త్రాలలో దయతో ఆత్మ శాంతికి నారాయణ బలి మరియు విష్ణు ఆరాధన సూచించబడింది.",
                ScripturalSourceReference(
                    "మను స్మృతి (5.89), గరుడ పురాణం (2.40) & ధర్మసింధు",
                    "ఆత్మఘాత ప్రాయశ్చిత్త ప్రకరణం",
                    "మనుస్మృతి 5.89; గరుడ పురాణం 2.40; ధర్మసింధుః తృతీయ పరిచ్ఛేదః",
                    "आत्मघातिनां प्रेतत्वनिवृत्तये नारायणबलिरुपदिश్యతే ॥",
                    "స్వయంకృత మరణం పొందిన వారికి ప్రేతత్వ నివారణకు నారాయణ బలి ఉపదేశించబడింది."
                ),
                "శ్రీ ఉత్తరాది మఠం మరియు మధ్వ సాంప్రదాయం ప్రకారం విద్వాంసుల సలహాతో చేయాలి."
            )
            AppLanguage.TAMIL -> Tuple9(
                "ஆத்மஹத்யா / உத்பந்தன மரணம் (சுய மரணம்)",
                "மனவேதனையினால் தன்னிச்சையாகவோ அல்லது தூக்கு மாட்டியோ ஏற்பட்ட மரணம்.",
                "ஆத்மঘাত / உத்பந்தனமரணம்",
                "மகாபிராயச்சித்தத்துடன் கூடிய நாராயண பலி",
                "மகாபிராயச்சித்தஸஹித நாராயணபலிஃ",
                "ஆசார்யர்களின் வழிகாட்டுதலுடன் பிராயச்சித்த தானங்களுக்குப் பின் நாராயண பலி செய்ய வேண்டும்",
                "சாஸ்திரங்களில் கருணையுடன் ஆத்ம சாந்திக்காக நாராயண பலி மற்றும் விஷ்ணு ஆராதனை கூறப்பட்டுள்ளது.",
                ScripturalSourceReference(
                    "மனு ஸ்மிருதி (5.89), கருட புராணம் (2.40) & தர்மஸிந்து",
                    "ஆத்மঘাত பிராயச்சித்த ப்ரகரணம்",
                    "மனுஸ்மிருதி 5.89; கருட புராணம் 2.40; தர்மஸிந்து த்ருதீய பரிச்சேதம்",
                    "आत्मघातिनां प्रेतत्वनिवृत्तये नारायणबलिरुपदिश्यते ॥",
                    "சுய மரணம் அடைந்த ஆத்மாக்களின் சாந்திக்காக நாராயண பலி உபதேசிக்கப்பட்டுள்ளது."
                ),
                "ஸ்ரீ உத்தரflowதி மடம் மற்றும் மத்வ சம்பிரதாய முறைப்படி ஆசார்ய ஆலோசனையுடன் செய்யத்தக்கது."
            )
            AppLanguage.ENGLISH -> Tuple9(
                "Self-Inflicted Demise (Ātmaghāta / Udbandhana)",
                "Demise resulting from intense psychological distress or intentional self-harm.",
                "आत्मघात / उद्वन्धनमरणम्",
                "Narayana Bali with Mahaprayaschitta",
                "Mahāprāyaścitta-sahita Nārāyaṇa Bali",
                "Conducted under the guidance of a qualified Matha Acharya after suitable prayaschitta offerings",
                "Compassionately ordained in classical Smriti and Garuda Purana texts to relieve distress and foster liberation.",
                ScripturalSourceReference(
                    "Manu Smriti (5.89), Garuda Purana (II.40.4-15) & Dharmasindhu III",
                    "Atmaghata Prayaschitta Prakarana",
                    "Manu Smriti 5.89; Garuda Purana II.40; Dharmasindhu III",
                    "आत्मघातिनां प्रेतत्वनिवृत्तये नारायणबलिरुपदिश्यते ॥",
                    "For those departing through distressful self-harm, Narayana Bali is compassionately ordained for spiritual liberation."
                ),
                "Observed strictly under the compassionate guidance of qualified Madhva Acharyas without social stigma."
            )
        }

        return CircumstanceGuidance(
            circumstance = DemiseCircumstance.SELF_INFLICTED,
            localizedName = name,
            localizedMeaning = meaning,
            sanskritTermLocalScript = scriptTerm,
            remedyName = remedy,
            remedySanskritLocalScript = remedyScript,
            timingGuidance = timing,
            purposeExplanation = purpose,
            scripturalSource = source,
            traditionNotes = tradNote,
            isMandatory = true
        )
    }

    private fun getUnrecoveredBodyGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        val (name, meaning, scriptTerm, remedy, remedyScript, timing, purpose, source, tradNote) = when (lang) {
            AppLanguage.KANNADA -> Tuple9(
                "ದೇಹ ಲಭಿಸದ ಮರಣ (ನಷ್ಟಶರೀರ / ಪರ್ಣನರದಾಹ)",
                "ವಿಮಾನ ಅಪಘಾತ, ಸಮುದ್ರ ದುರಂತ ಅಥವಾ ಯುದ್ಧದಲ್ಲಿ ಮರಣ ಖಚಿತವಾಗಿದ್ದು ಭೌತಿಕ ಶರೀರ ಲಭಿಸದ ಸಂದರ್ಭ.",
                "ನಷ್ಟಶರೀರ / ಪರ್ಣನರದಾಹಮರಣಮ್",
                "ಪರ್ಣನರ ದಾಹ (ಪಲಾಶವಿಧಿ - ೩೬೦ ಪಲಾಶ/ದರ್ಭೆಗಳ ಪ್ರತಿಕೃತಿ ದಹನ) ಹಾಗೂ ನಾರಾಯಣ ಬಲಿ",
                "ಪರ್ಣನರದಾಹಃ (ಪಲಾಶವಿಧಿಃ) ಮತ್ತು ನಾರಾಯಣಬಲಿಃ",
                "ಮರಣ ಖಚಿತವಾದ ತಕ್ಷಣವೇ ಪರ್ಣನರ ದಹನ ಹಾಗೂ ಶ್ರಾದ್ಧ ವಿಧಿಗಳು ಪ್ರಾರಂಭ",
                "ಭೌತಿಕ ಶರೀರ ಲಭಿಸದಿದ್ದಾಗ ಶಾಸ್ತ್ರೋಕ್ತವಾಗಿ ೩೬೦ ಕಡ್ಡಿಗಳ ಪ್ರತಿಕೃತಿ ನಿರ್ಮಿಸಿ ಸಂಸ್ಕಾರ ಮಾಡುವ ಬೋಧಾಯನೋಕ್ತ ವಿಧಿ.",
                ScripturalSourceReference(
                    "ಬೋಧಾಯನ ಪಿತೃಮೇಧ ಸೂತ್ರ, ನಿರ್ಣಯಸಿಂಧು (ತೃತೀಯ ಪರಿಚ್ಛೇದ) & ಧರ್ಮಸಿಂಧು",
                    "ನಷ್ಟಶರೀರ ದಹನ (ಪರ್ಣನರ ವಿಧಿ) ಪ್ರಕರಣ",
                    "ಬೋಧಾಯನ ಪಿತೃಮೇಧ ಸೂತ್ರಮ್; ನಿರ್ಣಯಸಿಂಧುಃ ತೃತೀಯ ಪರಿಚ್ಛೇದಃ; ಧರ್ಮಸಿಂಧುಃ",
                    "नष्टशरीरे त्रिषष्ट्यधिकत्रिशत (३६०) पलाशवृन्तैः प्रतिकृतिं कृत्वा दहेत् ॥",
                    "ದೇಹ ಲಭಿಸದಿದ್ದಾಗ ೩೬೦ ಪಲಾಶದ ಕಡ್ಡಿಗಳಿಂದ ಪ್ರತಿಕೃತಿ ರಚಿಸಿ ವಿಧಿವತ್ತಾಗಿ ದಹನ ಸಂಸ್ಕಾರ ಮಾಡಬೇಕು."
                ),
                "ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠ ಹಾಗೂ ಮಧ್ವ ಸಂಪ್ರದಾಯದಲ್ಲಿ ನಷ್ಟಶರೀರ ದಹನಕ್ಕೆ ಪಲಾಶವಿಧಿಯು ಪರಮ ಪ್ರಾಮಾಣಿಕವಾಗಿದೆ."
            )
            AppLanguage.SANSKRIT -> Tuple9(
                "नष्टशरीर / अदेहदाहमरणम् (पर्णनरविधिः)",
                "विमानदुर्घटनायां समुद्रे वा मरणे निश्चितेऽपि भौतिकशरीरालाभः।",
                "नष्टशरीर / अदेहदाहमरणम्",
                "पर्णनरदाहः (पलाशविधिः - ३६० पलाशवृन्तप्रतिकृतिदाहः) तथा नारायणबलिः",
                "पर्णनरदाहः (पलाशविधिः) एवं नारायणबलिः",
                "मरणनिश्चयानन्तरं सद्यः पर्णनरदाहपूर्वकम् और्ध्वदैहिकारम्भः",
                "भौतिकशरीरालाभे बोधायनोक्तरीत्या ३६० पलाशवृन्तैः शरीरप्रतिकृतिं कृत्वा संस्कारविधानम्।",
                ScripturalSourceReference(
                    "बौधायनपितृमेधसूत्रम्, निर्णयसिन्धुः (तृतीयः परिच्छेदः) & धर्मसिन्धुः",
                    "नष्टशरीर-दहन (पर्णनरविधि) प्रकरणम्",
                    "बौधायनपितृमेधसूत्रम्; निर्णयसिन्धुः ३; धर्मसिन्धुः तृतीयपरिच्छेदः",
                    "नष्टशरीरे त्रिषष्ट्यधिकत्रिशत (३६०) पलाशवृन्तैः प्रतिकृतिं कृत्वा दहेत् ॥",
                    "शरीरालाभे षष्ट्यधिकत्रिशतपलाशवृन्तैः प्रतिकृतिदाहः शास्त्रविहितः।"
                ),
                "माध्वसम्प्रदाये उत्तरादिमठरीत्या पलाशविधिपूर्वकं संस्कारः अनुष्ठेयः।"
            )
            AppLanguage.TELUGU -> Tuple9(
                "శరీరం లభించని మరణం (నష్టశరీరం / పర్ణనరదాహం)",
                "విమాన ప్రమాదం, సముద్ర ప్రమాదం లేదా యుద్ధంలో మరణం ఖాయమై భౌతిక శరీరం లభించని సందర్భం.",
                "నష్టశరీర / పర్ణనరదాహమరణమ్",
                "పర్ణనర దాహం (పలాశవిధి - 360 పలాశ/దర్భల ప్రతికృతి దహనం) మరియు నారాయణ బలి",
                "పర్ణనరదాహః (పలాశవిధిః) మరియు నారాయణబలిః",
                "మరణం ఖాయమైన వెంటనే పర్ణనర దహనం మరియు శ్రాద్ధ క్రియలు ప్రారంభం",
                "భౌతిక శరీరం లభించనప్పుడు 360 పలాశ పుల్లలతో ప్రతికృతిని చేసి దహనం చేసే బోధాయన విధి.",
                ScripturalSourceReference(
                    "బోధాయన పితృమేధ సూత్రం, నిర్ణయసింధు (తృతీయ పరిచ్ఛేదం) & ధర్మసింధు",
                    "నష్టశరీర దహన (పర్ణనర విధి) ప్రకరణం",
                    "బోధాయన పితృమేధ సూత్రం; నిర్ణయసింధు 3; ధర్మసింధుః తృతీయ పరిచ్ఛేదః",
                    "नष्टशरीरे त्रिषष्ट्यधिकत्रिशत (३६०) पलाशवृन्तैः प्रतिकृतिं कृत्वा दहेत् ॥",
                    "శరీరం లభించనప్పుడు 360 పలాశ పుల్లలతో ప్రతికృతి చేసి దహనం చేయాలి."
                ),
                "శ్రీ ఉత్తరాది మఠం మరియు మధ్వ సంప్రదాయంలో పలాశవిధి ప్రామాణికమైనది."
            )
            AppLanguage.TAMIL -> Tuple9(
                "உடல் கிடைக்காத மரணம் (நஷ்டசரீரம் / பர்ணநர தஹனம்)",
                "விமான விபத்து, கடல் விபத்து போன்றவற்றில் மரணம் உறுதியாகி உடல் கிடைக்காத நிலை.",
                "நஷ்டசரீர / பர்ணநரதாஹமரணம்",
                "பர்ணநர தஹனம் (பலாசவிதி - 360 பலாச இலைகள்/தர்பைகளால் உருவம் செய்து தகனம்) மற்றும் நாராயண பலி",
                "பர்ணநரதாஹஃ (பலாசவிதிஃ) மற்றும் நாராயணபலிஃ",
                "மரணம் உறுதியானவுடன் பர்ணநர தகனம் செய்து ச்ராத்த காரியங்களைத் தொடங்க வேண்டும்",
                "உடல் கிடைக்காத போது 360 பலாச குச்சிகளால் உருவம் செய்து தகனம் செய்ய போதாயன சாஸ்திர விதி.",
                ScripturalSourceReference(
                    "போதாயன பித்ருமேத சூத்திரம், நிர்ணயஸிந்து (த்ருதீய பரிச்சேதம்) & தர்மஸிந்து",
                    "நஷ்டசரீர தகன (பர்ணநர விதி) ப்ரகரணம்",
                    "போதாயன பித்ருமேத சூத்திரம்; நிர்ணயஸிந்து 3; தர்மஸிந்து த்ருதீய பரிச்சேதம்",
                    "नष्टशरीरे त्रिषष्ट्यधिकत्रिशत (३६०) पलाशवृन्तैः प्रतिकृतिं कृत्वा दहेत् ॥",
                    "உடல் கிடைக்காத போது 360 பலாச குச்சிகளால் உருவம் செய்து தகனம் செய்ய வேண்டும்."
                ),
                "ஸ்ரீ உத்தரflowதி மடம் மற்றும் மத்வ சம்பிரதாயத்தில் பலாசவிதி முறைப்படி செய்யத்தக்கது."
            )
            AppLanguage.ENGLISH -> Tuple9(
                "Unrecovered Body Demise (Naṣṭa-śarīra / Parṇa-nara)",
                "Confirmed demise (e.g. plane crash, deep sea, battlefield) where the physical body cannot be recovered.",
                "नष्टशरीर / अदेहदाहमरणम्",
                "Parṇa-nara-dāha (Palāśa-vidhi effigy cremation of 360 stalks) and Narayana Bali",
                "Parṇa-nara-dāhaḥ (Palāśa-vidhiḥ) & Nārāyaṇa Bali",
                "Immediately upon confirmation of death, initiating standard obsequies and Shraddhas",
                "Ordained in Baudhāyana Pitṛmedha Sūtra: constructing an effigy of 360 Palāśa or Darbha stalks representing bodily joints.",
                ScripturalSourceReference(
                    "Baudhayana Pitrimedha Sutra, Nirnayasindhu III & Dharmasindhu III",
                    "Nashta-sharira Daha (Parna-nara Vidhi) Prakarana",
                    "Baudhayana Pitrimedha Sutra; Nirnayasindhu III; Dharmasindhu III",
                    "नष्टशरीरे त्रिषष्ट्यधिकत्रिशत (३६०) पलाशवृन्तैः प्रतिकृतिं कृत्वा दहेत् ॥",
                    "When the physical body is lost, an effigy fashioned of 360 Palasha stalks is consecrated and cremated with Vedic mantras."
                ),
                "Recognized authority across all Vedic schools, Madhva sampradaya, and Uttaradi Matha."
            )
        }

        return CircumstanceGuidance(
            circumstance = DemiseCircumstance.UNRECOVERED_BODY,
            localizedName = name,
            localizedMeaning = meaning,
            sanskritTermLocalScript = scriptTerm,
            remedyName = remedy,
            remedySanskritLocalScript = remedyScript,
            timingGuidance = timing,
            purposeExplanation = purpose,
            scripturalSource = source,
            traditionNotes = tradNote,
            isMandatory = true
        )
    }

    private fun getPregnancyGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.PREGNANCY_CHILDBIRTH,
            sanskritName = "गर्भिणी / प्रसूतिकामरणम्",
            lang = lang,
            englishName = "Pregnancy / Childbirth Demise (Garbhiṇī Maraṇam)",
            englishMeaning = "Demise of a woman during pregnancy or in the immediate post-partum puerperal phase.",
            kannadaName = "ಗರ್ಭಿಣಿ / ಬಾಣಂತಿ ಮರಣ",
            kannadaMeaning = "ಗರ್ಭಾವಸ್ಥೆಯಲ್ಲಿ ಅಥವಾ ಪ್ರಸವದ ಸಮಯದಲ್ಲಿ ಸಂಭವಿಸಿದ ತಾಯಿಯ ಮರಣ.",
            teluguName = "గర్భిణీ / ప్రసూతికా మరణం",
            teluguMeaning = "గర్భధారణ సమయంలో లేదా ప్రసవ సమయంలో సంభవించిన మాతృ మరణం.",
            tamilName = "கர்பிணீ / ப்ரஸூதிகா மரணம் (கர்ப்பிணி / பிரசவ மரணம்)",
            tamilMeaning = "கர்ப்ப காலத்தில் அல்லது பிரசவ காலத்தில் ஏற்பட்ட தாயின் மரணம்.",
            sanskritMeaning = "गर्भधारणकाले प्रसवसमये वा प्राप्तं मरणम्।",
            remedy = "Garbhoddharana Vidhi & Specialized Antyesti",
            sanskritRemedy = "गर्भोद्धरणविधिः एवं विशेषसंस्कारः",
            sourceRef = ScripturalSourceReference(
                "Dharmasindhu III (Asaucha Prakarana) & Nirnayasindhu III",
                "Garbhini Marana Samskara Prakarana",
                "Dharmasindhu III; Nirnayasindhu III (Uttarardha)",
                "गर्भिण्यां मृतायां गर्भोद्धरणं कृत्वा यथाविधि दाहं कुर्यात् ॥",
                "For a pregnant woman passing away, the Shastric separation/purification protocol is performed prior to cremation."
            )
        )
    }

    private fun getOtherDurmaranaGuidance(lang: AppLanguage, tradition: MadhwaTradition): CircumstanceGuidance {
        return createStandardGuidance(
            circumstance = DemiseCircumstance.OTHER_DURMARANA,
            sanskritName = "अन्यथा दुर्मरणम्",
            lang = lang,
            englishName = "Other Untimely / Unnatural Demise (Anyathā Durmaraṇam)",
            englishMeaning = "Any sudden, untimely, or extraordinary circumstance not explicitly listed above.",
            kannadaName = "ಇತರ ಅಕಾಲಿಕ / ದುರ್ಮರಣ",
            kannadaMeaning = "ಮೇಲೆ ಹೆಸರಿಸದ ಇತರ ಆಕಸ್ಮಿಕ ಅಥವಾ ಅಕಾಲಿಕ ಕಾರಣಗಳಿಂದ ಸಂಭವಿಸಿದ ಮರಣ.",
            teluguName = "ఇతర అకాలిక / దుర్మరణం",
            teluguMeaning = "పైన పేర్కొనబడని ఇతర ఆకస్మిక లేదా అకాల కారణాల వల్ల సంభవించిన మరణం.",
            tamilName = "இதர அகால / துர்மரணம்",
            tamilMeaning = "மேலே குறிப்பிடப்படாத பிற விபத்து அல்லது எதிர்பாராத காரணங்களால் ஏற்பட்ட மரணம்.",
            sanskritMeaning = "अन्यैः अनिर्दिष्टैः कारणैः प्राप्तम् अकालमरणम्।",
            remedy = "Narayana Bali",
            sanskritRemedy = "नारायणबलिः",
            sourceRef = ScripturalSourceReference(
                "Garuda Purana (Preta Khanda Ch. 40) & Dharmasindhu III",
                "Durmarana Samanya Shanti Prakarana",
                "Garuda Purana II.40; Dharmasindhu III",
                "अकाले दुर्मृतानां च नारायणबलिः स्मृतः ॥",
                "For souls meeting unexpected untimely ends, Narayana Bali is ordained for peaceful spiritual transition."
            )
        )
    }

    private fun createStandardGuidance(
        circumstance: DemiseCircumstance,
        sanskritName: String,
        lang: AppLanguage,
        englishName: String,
        englishMeaning: String,
        kannadaName: String,
        kannadaMeaning: String,
        teluguName: String,
        teluguMeaning: String,
        tamilName: String,
        tamilMeaning: String,
        sanskritMeaning: String,
        remedy: String,
        sanskritRemedy: String,
        sourceRef: ScripturalSourceReference
    ): CircumstanceGuidance {
        val (name, meaning, scriptTerm, remedyName, timing, purpose, tradNote) = when (lang) {
            AppLanguage.KANNADA -> Tuple7(
                kannadaName,
                kannadaMeaning,
                sanskritName,
                "ನಾರಾಯಣ ಬಲಿ (ವಿಷ್ಣು ಆರಾಧನೆ ಸಹಿತ)",
                "ಸಪಿಂಡೀಕರಣಕ್ಕೆ ಮುನ್ನ ಅಥವಾ ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧದ ಒಳಗೆ",
                "ದುರ್ಮರಣ ದೋಷ ನಿವೃತ್ತಿಗಾಗಿ ಹಾಗೂ ಆತ್ಮಕ್ಕೆ ಸದ್ಗತಿ ದೊರಕಿಸಲು ಶಾಸ್ತ್ರೋಕ್ತ ವಿಧಿ.",
                "ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠ ಹಾಗೂ ಮಧ್ವ ಸಂಪ್ರದಾಯದ ಪದ್ಧತಿಯಂತೆ ವಿದ್ವಾಂಸರ ಮಾರ್ಗದರ್ಶನದಲ್ಲಿ ಆಚರಿಸುವುದು."
            )
            AppLanguage.SANSKRIT -> Tuple7(
                sanskritName,
                sanskritMeaning,
                sanskritName,
                "नारायणबलिः (विष्णुपूजासहितः)",
                "सपिण्डीकरणात् पूर्वं वा प्रथमवार्षिकात् प्राक्",
                "दुर्मरणदोषप्रशमनार्थं सद्गतिप्राप्तये च शास्त्रविहितो विधिः।",
                "श्रीमदुत्तरादिमठ-परम्परानुसारं विदुषां मार्गदर्शनेन कार्यम्।"
            )
            AppLanguage.TELUGU -> Tuple7(
                teluguName,
                teluguMeaning,
                sanskritName,
                "నారాయణ బలి (విష్ణు పూజా సహితం)",
                "సపిండీకరణకు ముందు లేదా ప్రథమ వార్షిక శ్రాద్ధానికి ముందు",
                "దుర్మరణ దోష నివృత్తికి మరియు ఆత్మకు సద్గతి కలగడానికి శాస్త్రోక్త విధి.",
                "శ్రీ ఉత్తరాది మఠం మరియు మధ్వ సాంప్రదాయం ప్రకారం విద్వాంసుల సలహాతో చేయవలెను."
            )
            AppLanguage.TAMIL -> Tuple7(
                tamilName,
                tamilMeaning,
                sanskritName,
                "நாராயண பலி (விஷ்ணு பூஜையுடன்)",
                "சபிண்டீகரனத்திற்கு முன் அல்லது முதல் வார்ஷிக ச்ராத்தத்திற்கு முன்",
                "துர்மரண தோஷ நிவர்த்திக்கும் ஆத்ம சாந்திக்கும் சாஸ்திர விதி.",
                "ஸ்ரீ உத்தரflowதி மடம் மற்றும் மத்வ சம்பிரதாய முறைப்படி ஆசார்ய ஆலோசனையுடன் செய்யத்தக்கது."
            )
            AppLanguage.ENGLISH -> Tuple7(
                englishName,
                englishMeaning,
                sanskritName,
                "$remedy (with propitiation of Lord Vishnu)",
                "Prior to Sapindikarana or before the completion of Year 1 rites",
                "Prescribed in classical scriptures to pacify intermediate distress and grant spiritual peace.",
                "Followed in Madhva tradition and Uttaradi Matha with guidance from qualified Purohitas."
            )
        }

        return CircumstanceGuidance(
            circumstance = circumstance,
            localizedName = name,
            localizedMeaning = meaning,
            sanskritTermLocalScript = scriptTerm,
            remedyName = remedyName,
            remedySanskritLocalScript = sanskritRemedy,
            timingGuidance = timing,
            purposeExplanation = purpose,
            scripturalSource = sourceRef,
            traditionNotes = tradNote,
            isMandatory = false
        )
    }

    private data class Tuple9<A, B, C, D, E, F, G, H, I>(
        val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H, val i: I
    )

    private data class Tuple7<A, B, C, D, E, F, G>(
        val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G
    )
}
