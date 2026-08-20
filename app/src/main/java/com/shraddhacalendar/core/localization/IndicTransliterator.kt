package com.shraddhacalendar.core.localization

/**
 * High-precision, Production-grade Indic Phonetic Transliterator and Lexicon Engine.
 * Converts any English / Latin name, title, honorific, place, or word into authentic,
 * orthographically correct Indic scripts:
 *   - Kannada (ಕನ್ನಡ)
 *   - Sanskrit (संस्कृतम् - Pure Devanagari)
 *   - Telugu (తెలుగు)
 *   - Tamil (தமிழ்)
 *
 * Handles:
 *   1. Full multi-lingual dictionary for standard Indian & global names, matha parampara, and places.
 *   2. Granular phonetic rules for consonant clusters (samyuktaksharas), aspirates, sibilants,
 *      nasals (anusvara), diphthongs, and word-final virama / halant.
 */
object IndicTransliterator {

    // ==========================================
    // 1. COMPREHENSIVE MULTILINGUAL LEXICONS
    // ==========================================

    private val KNOWN_WORDS_KN = mapOf(
        // Honorifics & Titles
        "late" to "ದಿವಂಗತ", "sri" to "ಶ್ರೀ", "shri" to "ಶ್ರೀ", "shree" to "ಶ್ರೀ",
        "smt" to "ಶ್ರೀಮತಿ", "shrimati" to "ಶ್ರೀಮತಿ", "dr" to "ಡಾ.", "prof" to "ಪ್ರೊ.",
        "mr" to "ಶ್ರೀ", "mrs" to "ಶ್ರೀಮತಿ", "miss" to "ಕುಮಾರಿ", "master" to "ಚಿ.",
        "pandit" to "ಪಂಡಿತ", "vidwan" to "ವಿದ್ವಾನ್", "acharya" to "ಆಚಾರ್ಯ",
        "shastri" to "ಶಾಸ್ತ್ರಿ", "bhatta" to "ಭಟ್ಟ", "swamy" to "ಸ್ವಾಮಿ", "swami" to "ಸ್ವಾಮಿ",
        "theertha" to "ತೀರ್ಥ", "tirtha" to "ತೀರ್ಥ",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "ಸತ್ಯಾತ್ಮತೀರ್ಥ", "satyatma" to "ಸತ್ಯಾತ್ಮ", "satyatmatirtha" to "ಸತ್ಯಾತ್ಮತೀರ್ಥ",
        "satyapramoda" to "ಸತ್ಯಪ್ರಮೋದ", "satyadhyana" to "ಸತ್ಯಧ್ಯಾನ", "satyadharma" to "ಸತ್ಯಧರ್ಮ",
        "madhwacharya" to "ಮಧ್ವಾಚಾರ್ಯ", "madhva" to "ಮಧ್ವ", "madhwa" to "ಮಧ್ವ",
        "jayatheertha" to "ಜಯತೀರ್ಥ", "jayatirtha" to "ಜಯತೀರ್ಥ", "teekacharya" to "ಟೀಕಾಚಾರ್ಯ",
        "raghavendra" to "ರಾಘವೇಂದ್ರ", "raghavendrateertha" to "ರಾಘವೇಂದ್ರತೀರ್ಥ", "rayaru" to "ರಾಯರು",
        "vadiraja" to "ವಾದಿರಾಜ", "vyasatheertha" to "ವ್ಯಾಸತೀರ್ಥ", "vyasatirtha" to "ವ್ಯಾಸತೀರ್ಥ",
        "purandaradasa" to "ಪುರಂದರದಾಸ", "kanakadasa" to "ಕನಕದಾಸ", "vijayadasa" to "ವಿಜಯದಾಸ",
        "uttaradi" to "ಉತ್ತರಾದಿ", "math" to "ಮಠ", "matha" to "ಮಠ",

        // Common First Names
        "pranesh" to "ಪ್ರಾಣೇಶ್", "gururaj" to "ಗುರುರಾಜ್", "ramachandra" to "ರಾಮಚಂದ್ರ",
        "anand" to "ಆನಂದ್", "ananda" to "ಆನಂದ", "narayana" to "ನಾರಾಯಣ", "krishna" to "ಕೃಷ್ಣ",
        "venkatesh" to "ವೆಂಕಟೇಶ್", "venkataramana" to "ವೆಂಕಟರಮಣ", "vijay" to "ವಿಜಯ್",
        "srinivas" to "ಶ್ರೀನಿವಾಸ್", "srinivasa" to "ಶ್ರೀನಿವಾಸ", "tanmay" to "ತನ್ಮಯ್", "tammay" to "ತಮ್ಮಯ್",
        "raghav" to "ರಾಘವ್", "raghava" to "ರಾಘವ", "keshava" to "ಕೇಶವ", "govinda" to "ಗೋವಿಂದ",
        "madhava" to "ಮಾಧವ", "vishnu" to "ವಿಷ್ಣು", "prahlad" to "ಪ್ರಹ್ಲಾದ್", "prahlada" to "ಪ್ರಹ್ಲಾದ",
        "ganesh" to "ಗಣೇಶ್", "ganesha" to "ಗಣೇಶ", "subrahmanya" to "ಸುಬ್ರಹ್ಮಣ್ಯ", "surya" to "ಸೂರ್ಯ",
        "shankar" to "ಶಂಕರ್", "shankara" to "ಶಂಕರ", "dattatreya" to "ದತ್ತಾತ್ರೇಯ", "hanuman" to "ಹನುಮಾನ್",
        "bhimsen" to "ಭೀಮಸೇನ್", "bhima" to "ಭೀಮ", "arjun" to "ಅರ್ಜುನ್", "arjuna" to "ಅರ್ಜುನ",
        "manjunath" to "ಮಂಜುನಾಥ್", "manjunatha" to "ಮಂಜುನಾಥ", "nagaraj" to "ನಾಗರಾಜ್",
        "prashanth" to "ಪ್ರಶಾಂತ್", "pradeep" to "ಪ್ರದೀಪ್", "prakash" to "ಪ್ರಕಾಶ್", "suresh" to "ಸುರೇಶ್",
        "ramesh" to "ರಮೇಶ್", "mahesh" to "ಮಹೇಶ್", "dinesh" to "ದಿನೇಶ್", "rajesh" to "ರಾಜೇಶ್",
        "girish" to "ಗಿರೀಶ್", "harish" to "ಹರೀಶ್", "satish" to "ಸತೀಶ್", "ravindra" to "ರವೀಂದ್ರ",
        "gopal" to "ಗೋಪಾಲ್", "gopala" to "ಗೋಪಾಲ", "vasudeva" to "ವಾಸುದೇವ", "damodara" to "ದಾಮೋದರ",
        "janardana" to "ಜನಾರ್ದನ", "padmanabha" to "ಪದ್ಮನಾಭ", "achyuta" to "ಅಚ್ಯುತ",
        "lakshmi" to "ಲಕ್ಷ್ಮಿ", "saraswati" to "ಸರಸ್ವತಿ", "parvati" to "ಪಾರ್ವತಿ", "radha" to "ರಾಧಾ",
        "rukmini" to "ರುಕ್ಮಿಣಿ", "sita" to "ಸೀತಾ", "gayatri" to "ಗಾಯತ್ರಿ", "savithri" to "ಸಾವಿತ್ರಿ",

        // Common Surnames & Family Names
        "kulkarni" to "ಕುಲಕರ್ಣಿ", "rao" to "ರಾವ್", "joshi" to "ಜೋಷಿ", "deshpande" to "ದೇಶಪಾಂಡೆ",
        "bhat" to "ಭಟ್", "bhatta" to "ಭಟ್ಟ", "sharma" to "ಶರ್ಮಾ", "shastri" to "ಶಾಸ್ತ್ರಿ",
        "pai" to "ಪೈ", "kamath" to "ಕಾಮತ್", "shenoy" to "ಶೆಣೈ", "prabhu" to "ಪ್ರಭು",
        "hegde" to "ಹೆಗಡೆ", "shetty" to "ಶೆಟ್ಟಿ", "gowda" to "ಗೌಡ", "patil" to "ಪಾಟೀಲ್",
        "deshmukh" to "ದೇಶಮುಖ್", "mutalik" to "ಮುತಾಲಿಕ್", "in转型" to "ಇನಾಂದಾರ್", "inamdar" to "ಇನಾಂದಾರ್",
        "diwan" to "ದಿವಾನ್", "pujari" to "ಪೂಜಾರಿ", "archak" to "ಅರ್ಚಕ", "karanth" to "ಕಾರಂತ್",
        "adiga" to "ಅಡಿಗ", "holla" to "ಹೊಳ್ಳ", "upadhyaya" to "ಉಪಾಧ್ಯಾಯ", "puranik" to "ಪುರಾಣಿಕ್",

        // Indian Cities & States
        "bengaluru" to "ಬೆಂಗಳೂರು", "bangalore" to "ಬೆಂಗಳೂರು", "karnataka" to "ಕರ್ನಾಟಕ", "india" to "ಭಾರತ",
        "mysuru" to "ಮೈಸೂರು", "mysore" to "ಮೈಸೂರು", "udupi" to "ಉಡುಪಿ", "mantralayam" to "ಮಂತ್ರಾಲಯ",
        "hubballi" to "ಹುಬ್ಬಳ್ಳಿ", "hubli" to "ಹುಬ್ಬಳ್ಳಿ", "dharwad" to "ಧಾರವಾಡ", "belagavi" to "ಬೆಳಗಾವಿ",
        "belgaum" to "ಬೆಳಗಾವಿ", "mangaluru" to "ಮಂಗಳೂರು", "mangalore" to "ಮಂಗಳೂರು", "shivamogga" to "ಶಿವಮೊಗ್ಗ",
        "shimoga" to "ಶಿವಮೊಗ್ಗ", "kalaburagi" to "ಕಲಬುರಗಿ", "gulbarga" to "ಕಲಬುರಗಿ", "bagalkote" to "ಬಾಗಲಕೋಟೆ",
        "vijayapura" to "ವಿಜಯಪುರ", "bijapur" to "ವಿಜಯಪುರ", "ballari" to "ಬಳ್ಳಾರಿ", "bellary" to "ಬಳ್ಳಾರಿ",
        "davangere" to "ದಾವಣಗೆರೆ", "tumakuru" to "ತುಮಕೂರು", "hassan" to "ಹಾಸನ", "bidar" to "ಬೀದರ್",
        "raichur" to "ರಾಯಚೂರು", "koppal" to "ಕೊಪ್ಪಳ", "yadgir" to "ಯಾದಗಿರಿ", "gadag" to "ಗದಗ",
        "mumbai" to "ಮುಂಬೈ", "bombay" to "ಮುಂಬೈ", "pune" to "ಪುಣೆ", "chennai" to "ಚೆನ್ನೈ", "madras" to "ಚೆನ್ನೈ",
        "hyderabad" to "ಹೈದರಾಬಾದ್", "new delhi" to "ನವದೆಹಲಿ", "delhi" to "ದೆಹಲಿ", "kolkata" to "ಕೋಲ್ಕತ್ತಾ",
        "varanasi" to "ವಾರಣಾಸಿ", "kashi" to "ಕಾಶಿ", "gaya" to "ಗಯಾ", "tirupati" to "ತಿರುಪತಿ",
        "rameswaram" to "ರಾಮೇಶ್ವರಂ", "haridwar" to "ಹರಿದ್ವಾರ", "rishikesh" to "ಋಷಿಕೇಶ",
        "mathura" to "ಮಥುರಾ", "vrindavan" to "ವೃಂದಾವನ", "ayodhya" to "ಅಯೋಧ್ಯೆ", "pandharpur" to "ಪಂಢರಪುರ",

        // Global Metro Cities & Countries
        "new york" to "ನ್ಯೂ ಯಾರ್ಕ್", "london" to "ಲಂಡನ್", "dubai" to "ದುಬೈ", "singapore" to "ಸಿಂಗಾಪುರ",
        "tokyo" to "ಟೋಕಿಯೋ", "paris" to "ಪ್ಯಾರಿಸ್", "toronto" to "ಟೊರೊಂಟೊ", "sydney" to "ಸಿಡ್ನಿ",
        "chicago" to "ಚಿಕಾಗೊ", "san francisco" to "ಸ್ಯಾನ್ ಫ್ರಾನ್ಸಿಸ್ಕೋ", "los angeles" to "ಲಾಸ್ ಏಂಜಲೀಸ್",
        "melbourne" to "ಮೆಲ್ಬೋರ್ನ್", "dallas" to "ಡಲ್ಲಾಸ್", "houston" to "ಹ್ಯೂಸ್ಟನ್", "seattle" to "ಸಿಯಾಟಲ್",
        "boston" to "ಬೋಸ್ಟನ್", "atlanta" to "ಅಟ್ಲಾಂಟಾ", "vancouver" to "ವ್ಯಾಂಕೋವರ್",
        "united states" to "ಅಮೆರಿಕ ಸಂಯುಕ್ತ ಸಂಸ್ಥಾನ", "usa" to "ಯು.ಎಸ್.ಎ", "united kingdom" to "ಯುನೈಟೆಡ್ ಕಿಂಗ್‌ಡಮ್",
        "uk" to "ಯು.ಕೆ", "canada" to "ಕೆನಡಾ", "australia" to "ಆಸ್ಟ್ರೇಲಿಯಾ", "uae" to "ಯು.ಎ.ಇ"
    )

    private val KNOWN_WORDS_SA = mapOf(
        // Honorifics & Titles
        "late" to "दिवङ्गत", "sri" to "श्री", "shri" to "श्री", "shree" to "श्री",
        "smt" to "श्रीमती", "shrimati" to "श्रीमती", "dr" to "डा.", "prof" to "प्रो.",
        "mr" to "श्रीमान्", "mrs" to "श्रीमती", "miss" to "कुमारी", "master" to "चि.",
        "pandit" to "पण्डित", "vidwan" to "विद्वान्", "acharya" to "आचार्य",
        "shastri" to "शास्त्री", "bhatta" to "भट्ट", "swamy" to "स्वामी", "swami" to "स्वामी",
        "theertha" to "तीर्थ", "tirtha" to "तीर्थ",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "सत्यात्मतीर्थ", "satyatma" to "सत्यात्म", "satyatmatirtha" to "सत्यात्मतीर्थ",
        "satyapramoda" to "सत्यप्रमोद", "satyadhyana" to "सत्यध्यान", "satyadharma" to "सत्यधर्म",
        "madhwacharya" to "मध्वाचार्य", "madhva" to "मध्व", "madhwa" to "मध्व",
        "jayatheertha" to "जयतीर्थ", "jayatirtha" to "जयतीर्थ", "teekacharya" to "टीकाचार्य",
        "raghavendra" to "राघवेंद्र", "raghavendrateertha" to "राघवेंद्रतीर्थ", "rayaru" to "राघवेंद्रस्वामिनः",
        "vadiraja" to "वादिराज", "vyasatheertha" to "व्यासतीर्थ", "vyasatirtha" to "व्यासतीर्थ",
        "purandaradasa" to "पुरन्दरदास", "kanakadasa" to "कनकदास", "vijayadasa" to "विजयदास",
        "uttaradi" to "उत्तरादि", "math" to "मठ", "matha" to "मठ",

        // Common First Names
        "pranesh" to "प्राणेश", "gururaj" to "गुरुराज", "ramachandra" to "रामचन्द्र",
        "anand" to "आनन्द", "ananda" to "आनन्द", "narayana" to "नारायण", "krishna" to "कृष्ण",
        "venkatesh" to "व्यङ्कटेश", "venkataramana" to "वेङ्कटरमण", "vijay" to "विजय",
        "srinivas" to "श्रीनिवास", "srinivasa" to "श्रीनिवास", "tanmay" to "तन्मय", "tammay" to "तन्मय",
        "raghav" to "राघव", "raghava" to "राघव", "keshava" to "केशव", "govinda" to "गोविन्द",
        "madhava" to "माधव", "vishnu" to "विष्णु", "prahlad" to "प्रह्लाद", "prahlada" to "प्रह्लाद",
        "ganesh" to "गणेश", "ganesha" to "गणेश", "subrahmanya" to "सुब्रह्मण्य", "surya" to "सूर्य",
        "shankar" to "शङ्कर", "shankara" to "शङ्कर", "dattatreya" to "दत्तात्रेय", "hanuman" to "हनुमान्",
        "bhimsen" to "भीमसेन", "bhima" to "भीम", "arjun" to "अर्जुन", "arjuna" to "अर्जुन",
        "manjunath" to "मञ्जुनाथ", "manjunatha" to "मञ्जुनाथ", "nagaraj" to "नागराज",
        "prashanth" to "प्रशान्त", "pradeep" to "प्रदीप", "prakash" to "प्रकाश", "suresh" to "सुरेश",
        "ramesh" to "रमेश", "mahesh" to "महेश", "dinesh" to "दिनेश", "rajesh" to "राजेश",
        "girish" to "गिरीश", "harish" to "हरीश", "satish" to "सतीश", "ravindra" to "रवीन्द्र",
        "gopal" to "गोपाल", "gopala" to "गोपाल", "vasudeva" to "वासुदेव", "damodara" to "दामोदर",
        "janardana" to "जनार्दन", "padmanabha" to "पद्मनाभ", "achyuta" to "अच्युत",
        "lakshmi" to "लक्ष्मी", "saraswati" to "सरस्वती", "parvati" to "पार्वती", "radha" to "राधा",
        "rukmini" to "रुक्मिणी", "sita" to "सीता", "gayatri" to "गायत्री", "savithri" to "सावित्री",

        // Common Surnames
        "kulkarni" to "कुलकर्णी", "rao" to "राव", "joshi" to "जोशी", "deshpande" to "देशपाण्डे",
        "bhat" to "भट्ट", "bhatta" to "भट्ट", "sharma" to "शर्मा", "shastri" to "शास्त्री",
        "pai" to "पै", "kamath" to "कामथ", "shenoy" to "शणै", "prabhu" to "प्रभु",
        "hegde" to "हेगडे", "shetty" to "शेट्टी", "gowda" to "गौडा", "patil" to "पाटील",
        "deshmukh" to "देशमुख", "mutalik" to "मुतालिक", "inamdar" to "इनामदार",
        "diwan" to "दीवान", "pujari" to "पुजारी", "archak" to "अर्चक", "karanth" to "कारन्त",
        "adiga" to "अडिग", "holla" to "होळ्ळ", "upadhyaya" to "उपाध्याय", "puranik" to "पौराणिक",

        // Locations
        "bengaluru" to "बेङ्गळूरु", "bangalore" to "बेङ्गळूरु", "karnataka" to "कर्णाटकम्", "india" to "भारतम्",
        "mysuru" to "मैसूरु", "mysore" to "मैसूरु", "udupi" to "उडुपी", "mantralayam" to "मन्त्रालयम्",
        "hubballi" to "हुब्बळ्ळी", "hubli" to "हुब्बळ्ळी", "dharwad" to "धारवाड", "belagavi" to "बेळगावी",
        "mangaluru" to "मङ्गळूरु", "shivamogga" to "शिवमोग्गा", "kalaburagi" to "कलबुरगी",
        "mumbai" to "मुम्बई", "pune" to "पुणे", "chennai" to "चेन्नै", "hyderabad" to "हैदराबाद",
        "new delhi" to "नवदेहली", "delhi" to "देहली", "kolkata" to "कोलकाता",
        "varanasi" to "वाराणसी", "kashi" to "काशी", "gaya" to "गया", "tirupati" to "तिरुपति",
        "rameswaram" to "रामेश्वरम्", "haridwar" to "हरिद्वारम्", "rishikesh" to "ऋषिकेशः",
        "mathura" to "मथुरा", "vrindavan" to "वृन्दावनम्", "ayodhya" to "अयोध्या", "pandharpur" to "पण्डरपुरम्",

        // Global
        "new york" to "न्यू यार्क", "london" to "लण्डन्", "dubai" to "दुबई", "singapore" to "सिंगापुरम्",
        "tokyo" to "टोक्यो", "paris" to "पेरिस", "toronto" to "टोरोण्टो", "sydney" to "सिडनी",
        "chicago" to "शिकागो", "san francisco" to "सान् फ्रान्सिस्को", "los angeles" to "लास् एञ्जलस्",
        "united states" to "अमेरिकासंयुक्तसंस्थानम्", "usa" to "यू.एस.ए", "united kingdom" to "संयुक्तराजतन्त्रम्",
        "canada" to "कनाडा", "australia" to "आस्ट्रेलिया", "uae" to "यू.ए.ई"
    )

    private val KNOWN_WORDS_TE = mapOf(
        // Honorifics & Titles
        "late" to "దివంగత", "sri" to "శ్రీ", "shri" to "శ్రీ", "shree" to "శ్రీ",
        "smt" to "శ్రీమతి", "shrimati" to "శ్రీమతి", "dr" to "డా.", "prof" to "ప్రొ.",
        "mr" to "శ్రీ", "mrs" to "శ్రీమతి", "miss" to "కుమారి", "master" to "చి.",
        "pandit" to "పండిత", "vidwan" to "విద్వాన్", "acharya" to "ఆచార్య",
        "shastri" to "శాస్త్రి", "bhatta" to "భట్", "swamy" to "స్వామి", "swami" to "స్వామి",
        "theertha" to "తీర్థ", "tirtha" to "తీర్థ",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "సత్యాత్మతీర్థ", "satyatma" to "సత్యాత్మ", "satyatmatirtha" to "సత్యాత్మతీర్థ",
        "satyapramoda" to "సత్యప్రమోద", "satyadhyana" to "సత్యధ్యాన", "satyadharma" to "సత్యధర్మ",
        "madhwacharya" to "మధ్వాచార్య", "madhva" to "మధ్వ", "madhwa" to "మధ్వ",
        "jayatheertha" to "జయతీర్థ", "jayatirtha" to "జయతీర్థ", "teekacharya" to "టీకాచార్య",
        "raghavendra" to "రాఘవేంద్ర", "raghavendrateertha" to "రాఘవేంద్రతీర్థ", "rayaru" to "రాయరు",
        "vadiraja" to "వాదిరాజ", "vyasatheertha" to "వ్యాసతీర్థ", "vyasatirtha" to "వ్యాసతీర్థ",
        "purandaradasa" to "పురందరదాస", "kanakadasa" to "కనకదాస", "vijayadasa" to "విజయదాస",
        "uttaradi" to "ఉత్తరాది", "math" to "మఠం", "matha" to "మఠం",

        // Common First Names
        "pranesh" to "ప్రాణేష్", "gururaj" to "గురురాజ్", "ramachandra" to "రామచంద్ర",
        "anand" to "ఆనంద్", "ananda" to "ఆనంద", "narayana" to "నారాయణ", "krishna" to "కృష్ణ",
        "venkatesh" to "వెంకటేష్", "venkataramana" to "వెంకటరమణ", "vijay" to "విజయ్",
        "srinivas" to "శ్రీనివాస్", "srinivasa" to "శ్రీనివాస", "tanmay" to "తన్మయ్", "tammay" to "తమ్మయ్",
        "raghav" to "రాఘవ్", "raghava" to "రాఘవ", "keshava" to "కేశవ", "govinda" to "గోవింద",
        "madhava" to "మాధవ", "vishnu" to "విష్ణు", "prahlad" to "ప్రహ్లాద్", "prahlada" to "ప్రహ్లాద",
        "ganesh" to "గణేష్", "ganesha" to "గణేశ", "subrahmanya" to "సుబ్రహ్మణ్య", "surya" to "సూర్య",
        "shankar" to "శంకర్", "shankara" to "శంకర", "dattatreya" to "దత్తాత్రేయ", "hanuman" to "హనుమాన్",
        "bhimsen" to "భీమ్‌సేన్", "bhima" to "భీమ", "arjun" to "అర్జున్", "arjuna" to "అర్జున",
        "manjunath" to "మంజునాథ్", "manjunatha" to "మంజునాథ", "nagaraj" to "నాగరాజ్",
        "prashanth" to "ప్రశాంత్", "pradeep" to "ప్రదీప్", "prakash" to "ప్రకాష్", "suresh" to "సురేష్",
        "ramesh" to "రమేష్", "mahesh" to "మహేష్", "dinesh" to "దినేష్", "rajesh" to "రాజేష్",
        "girish" to "గిరీష్", "harish" to "హరీష్", "satish" to "సతీష్", "ravindra" to "రవీంద్ర",
        "gopal" to "గోపాల్", "gopala" to "గోపాల", "vasudeva" to "వాసుదేవ", "damodara" to "దామోదర",
        "janardana" to "జనార్దన", "padmanabha" to "పద్మనాభ", "achyuta" to "అచ్యుత",
        "lakshmi" to "లక్ష్మి", "saraswati" to "సరస్వతి", "parvati" to "పార్వతి", "radha" to "రాధా",
        "rukmini" to "రుక్మిణి", "sita" to "సీతా", "gayatri" to "గాయత్రి", "savithri" to "సావిత్రి",

        // Surnames
        "kulkarni" to "కులకర్ణి", "rao" to "రావు", "joshi" to "జోషి", "deshpande" to "దేశ్‌పాండే",
        "bhat" to "భట్", "bhatta" to "భట్", "sharma" to "శర్మ", "shastri" to "శాస్త్రి",
        "pai" to "పై", "kamath" to "కామత్", "shenoy" to "శెణై", "prabhu" to "ప్రభు",
        "hegde" to "హెగ్డే", "shetty" to "శెట్టి", "gowda" to "గౌడ", "patil" to "పాటీల్",
        "deshmukh" to "దేశ్‌ముఖ్", "mutalik" to "ముతాలిక్", "inamdar" to "ఇనాందార్",
        "diwan" to "దివాన్", "pujari" to "పూజారి", "archak" to "అర్చక", "karanth" to "కారంత్",
        "adiga" to "అడిగ", "holla" to "హొళ్ళ", "upadhyaya" to "ఉపాధ్యాయ", "puranik" to "పురాణిక్",

        // Locations
        "bengaluru" to "బెంగళూరు", "bangalore" to "బెంగళూరు", "karnataka" to "కర్ణాటక", "india" to "భారతదేశం",
        "mysuru" to "మైసూరు", "mysore" to "మైసూరు", "udupi" to "ఉడుపి", "mantralayam" to "మంత్రాలయం",
        "hubballi" to "హుబ్బళ్ళి", "hubli" to "హుబ్బళ్ళి", "dharwad" to "ధార్వాడ్", "belagavi" to "బెళగావి",
        "mangaluru" to "మంగళూరు", "shivamogga" to "శివమొగ్గ", "kalaburagi" to "కలబురగి",
        "mumbai" to "ముంబై", "pune" to "పుణె", "chennai" to "చెన్నై", "hyderabad" to "హైదరాబాద్",
        "new delhi" to "న్యూఢిల్లీ", "delhi" to "ఢిల్లీ", "kolkata" to "కోల్‌కతా",
        "varanasi" to "వారణాసి", "kashi" to "కాశీ", "gaya" to "గయ", "tirupati" to "తిరుపతి",
        "rameswaram" to "రామేశ్వరం", "haridwar" to "హరిద్వార్", "rishikesh" to "ఋషికేశ్",
        "mathura" to "మథుర", "vrindavan" to "బృందావనం", "ayodhya" to "అయోధ్య", "pandharpur" to "పండరీపురం",

        // Global
        "new york" to "న్యూయార్క్", "london" to "లండన్", "dubai" to "దుబాయ్", "singapore" to "సింగపూర్",
        "tokyo" to "టోక్యో", "paris" to "పారిస్", "toronto" to "టొరంటో", "sydney" to "సిడ్నీ",
        "chicago" to "చికాగో", "san francisco" to "శాన్ ఫ్రాన్సిస్కో", "los angeles" to "లాస్ ఏంజిల్స్",
        "united states" to "అమెరికా సంయుక్త రాష్ట్రాలు", "usa" to "యు.ఎస్.ఎ", "united kingdom" to "యునైటెడ్ కింగ్‌డమ్",
        "canada" to "కెనడా", "australia" to "ఆస్ట్రేలియా", "uae" to "యు.ఎ.ఇ"
    )

    private val KNOWN_WORDS_TA = mapOf(
        // Honorifics & Titles
        "late" to "மறைந்த", "sri" to "ஸ்ரீ", "shri" to "ஸ்ரீ", "shree" to "ஸ்ரீ",
        "smt" to "ஸ்ரீமதி", "shrimati" to "ஸ்ரீமதி", "dr" to "டாக்டர்", "prof" to "பேராசிரியர்",
        "mr" to "திரு", "mrs" to "திருமதி", "miss" to "செல்வி", "master" to "செல்வன்",
        "pandit" to "பண்டிதர்", "vidwan" to "வித்வான்", "acharya" to "ஆச்சார்யா",
        "shastri" to "சாஸ்திரி", "bhatta" to "பட்டர்", "swamy" to "ஸ்வாமி", "swami" to "ஸ்வாமி",
        "theertha" to "தீர்த்த", "tirtha" to "தீர்த்த",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "ஸத்யாத்மதீர்த்த", "satyatma" to "ஸத்யாத்ம", "satyatmatirtha" to "ஸத்யாத்மதீர்த்த",
        "satyapramoda" to "ஸத்யப்ரமோத", "satyadhyana" to "ஸத்யத்யான", "satyadharma" to "ஸத்யதர்ம",
        "madhwacharya" to "மத்வாசார்யா", "madhva" to "மத்வ", "madhwa" to "மத்வ",
        "jayatheertha" to "ஜயதீர்த்த", "jayatirtha" to "ஜயதீர்த்த", "teekacharya" to "டீகாசார்யா",
        "raghavendra" to "ராகவேந்திரா", "raghavendrateertha" to "ராகவேந்திரதீர்த்த", "rayaru" to "ராயரு",
        "vadiraja" to "வாதிராஜ", "vyasatheertha" to "வ்யாஸதீர்த்த", "vyasatirtha" to "வ்யாஸதீர்த்த",
        "purandaradasa" to "புரந்தரதாஸ", "kanakadasa" to "கனகதாஸ", "vijayadasa" to "விஜயதாஸ",
        "uttaradi" to "உத்தரதி", "math" to "மடம்", "matha" to "மடம்",

        // Common First Names
        "pranesh" to "பிராணேஷ்", "gururaj" to "குருராஜ்", "ramachandra" to "ராமசந்த்ர",
        "anand" to "ஆனந்த்", "ananda" to "ஆனந்த", "narayana" to "நாராயண", "krishna" to "கிருஷ்ணா",
        "venkatesh" to "வெங்கடேஷ்", "venkataramana" to "வெங்கடரமணா", "vijay" to "விஜய்",
        "srinivas" to "ஸ்ரீனிவாஸ்", "srinivasa" to "ஸ்ரீனிவாஸா", "tanmay" to "தன்மய்", "tammay" to "தம்மய்",
        "raghav" to "ராகவ்", "raghava" to "ராகவா", "keshava" to "கேசவா", "govinda" to "கோவிந்தா",
        "madhava" to "மாதவா", "vishnu" to "விஷ்ணு", "prahlad" to "பிரஹ்லாத்", "prahlada" to "பிரஹ்லாதா",
        "ganesh" to "கணேஷ்", "ganesha" to "கணேஷா", "subrahmanya" to "சுப்ரமண்ய", "surya" to "சூர்யா",
        "shankar" to "சங்கர்", "shankara" to "சங்கரா", "dattatreya" to "தத்தாத்ரேய", "hanuman" to "ஹனுமான்",
        "bhimsen" to "பீம்ஸேன்", "bhima" to "பீமா", "arjun" to "அர்ஜுன்", "arjuna" to "அர்ஜுனா",
        "manjunath" to "மஞ்சுநாத்", "manjunatha" to "மஞ்சுநாதா", "nagaraj" to "நாகராஜ்",
        "prashanth" to "பிரசாந்த்", "pradeep" to "பிரதீப்", "prakash" to "பிரகாஷ்", "suresh" to "சுரேஷ்",
        "ramesh" to "ரமேஷ்", "mahesh" to "மகேஷ்", "dinesh" to "தினேஷ்", "rajesh" to "ராஜேஷ்",
        "girish" to "கிரீஷ்", "harish" to "ஹரீஷ்", "satish" to "சதீஷ்", "ravindra" to "ரவீந்திரா",
        "gopal" to "கோபால்", "gopala" to "கோபாலா", "vasudeva" to "வாசுதேவா", "damodara" to "தாமோதரா",
        "janardana" to "ஜனார்தனா", "padmanabha" to "பத்மநாபா", "achyuta" to "அச்யுதா",
        "lakshmi" to "லக்ஷ்மி", "saraswati" to "சரஸ்வதி", "parvati" to "பார்வதி", "radha" to "ராதா",
        "rukmini" to "ருக்மிணி", "sita" to "சீதா", "gayatri" to "காயத்ரி", "savithri" to "சாவித்ரி",

        // Surnames
        "kulkarni" to "குல்கர்னி", "rao" to "ராவ்", "joshi" to "ஜோஷி", "deshpande" to "தேஷ்பாண்டே",
        "bhat" to "பட்", "bhatta" to "பட்டர்", "sharma" to "சர்மா", "shastri" to "சாஸ்திரி",
        "pai" to "பை", "kamath" to "காமத்", "shenoy" to "ஷெனாய்", "prabhu" to "பிரபு",
        "hegde" to "ஹெக்டே", "shetty" to "ஷெட்டி", "gowda" to "கௌடா", "patil" to "பாட்டீல்",
        "deshmukh" to "தேஷ்முக்", "mutalik" to "முத்தாலிக்", "inamdar" to "இனாம்தார்",
        "diwan" to "திவான்", "pujari" to "பூசாரி", "archak" to "அர்ச்சகர்", "karanth" to "கராந்த்",
        "adiga" to "அடிகா", "holla" to "ஹொள்ளா", "upadhyaya" to "உபாத்யாயா", "puranik" to "புராணிக்",

        // Locations
        "bengaluru" to "பெங்களூரு", "bangalore" to "பெங்களூரு", "karnataka" to "கர்நாடகா", "india" to "இந்தியா",
        "mysuru" to "மைசூரு", "mysore" to "மைசூரு", "udupi" to "உடுப்பி", "mantralayam" to "மந்த்ராலயம்",
        "hubballi" to "ஹுப்பள்ளி", "hubli" to "ஹுப்பள்ளி", "dharwad" to "தார்வாட்", "belagavi" to "பெலகாவி",
        "mangaluru" to "மங்களூரு", "shivamogga" to "சிவமொக்கா", "kalaburagi" to "கலபுரகி",
        "mumbai" to "மும்பை", "pune" to "புனே", "chennai" to "சென்னை", "hyderabad" to "ஹைதராபாத்",
        "new delhi" to "புது தில்லி", "delhi" to "தில்லி", "kolkata" to "கொல்கத்தா",
        "varanasi" to "வாரணாசி", "kashi" to "காசி", "gaya" to "கயா", "tirupati" to "திருப்பதி",
        "rameswaram" to "ராமேஸ்வரம்", "haridwar" to "ஹரித்வார்", "rishikesh" to "ரிஷிகேஷ்",
        "mathura" to "மதுரா", "vrindavan" to "பிருந்தாவனம்", "ayodhya" to "அயோத்தி", "pandharpur" to "பண்டரிபுரம்",

        // Global
        "new york" to "நியூ யார்க்", "london" to "லண்டன்", "dubai" to "துபாய்", "singapore" to "சிங்கப்பூர்",
        "tokyo" to "டோக்கியோ", "paris" to "பாரிஸ்", "toronto" to "டொராண்டோ", "sydney" to "சிட்னி",
        "chicago" to "சிகாகோ", "san francisco" to "சான் பிரான்சிஸ்கோ", "los angeles" to "லாஸ் ஏஞ்சல்ஸ்",
        "united states" to "அமெரிக்க ஐக்கிய நாடுகள்", "usa" to "யு.எஸ்.ஏ", "united kingdom" to "ஐக்கிய இராச்சியம்",
        "canada" to "கனடா", "australia" to "ஆஸ்திரேலியா", "uae" to "யு.ஏ.இ"
    )

    // ==========================================
    // 2. PUBLIC TRANSLITERATION ENTRY POINT
    // ==========================================

    /**
     * Transliterates any arbitrary person name, place, title, or sentence
     * into the specified target language script.
     */
    fun transliterate(text: String, language: AppLanguage): String {
        if (text.isBlank()) return text
        if (language == AppLanguage.ENGLISH) return text

        // Check if text already contains non-Latin Unicode characters (already in native script)
        val hasIndicChar = text.any { it.code in 0x0900..0x0D7F }
        if (hasIndicChar) return text

        val dictionary = when (language) {
            AppLanguage.KANNADA -> KNOWN_WORDS_KN
            AppLanguage.SANSKRIT -> KNOWN_WORDS_SA
            AppLanguage.TELUGU -> KNOWN_WORDS_TE
            AppLanguage.TAMIL -> KNOWN_WORDS_TA
            AppLanguage.ENGLISH -> emptyMap()
        }

        // Tokenize text while preserving separators (spaces, commas, periods, hyphens, parentheses)
        val tokens = text.split(Regex("(?<=[\\s,.-/()&])|(?=[\\s,.-/()&])"))

        val result = StringBuilder()
        for (token in tokens) {
            val cleanLower = token.trim().lowercase()
            if (cleanLower.isBlank() || cleanLower.matches(Regex("[\\s,.-/()&]+"))) {
                result.append(token)
                continue
            }

            // 1. Direct dictionary match
            val dictMatch = dictionary[cleanLower]
            if (dictMatch != null) {
                result.append(dictMatch)
                continue
            }

            // 2. Universal phonetic transliteration for any word
            val phonetic = transliteratePhoneticWord(cleanLower, language)
            result.append(phonetic)
        }

        return result.toString()
    }

    // ==========================================
    // 3. ADVANCED PHONETIC CONVERSION ENGINE
    // ==========================================

    private fun transliteratePhoneticWord(word: String, language: AppLanguage): String {
        var i = 0
        val len = word.length
        val out = StringBuilder()

        while (i < len) {
            val rem = len - i

            // Multi-char consonant patterns
            val sub4 = if (rem >= 4) word.substring(i, i + 4) else ""
            val sub3 = if (rem >= 3) word.substring(i, i + 3) else ""
            val sub2 = if (rem >= 2) word.substring(i, i + 2) else ""
            val sub1 = word.substring(i, i + 1)

            val (consLen, consKey) = when {
                sub4 == "shht" -> 4 to "shht"
                sub4 == "ksht" -> 4 to "ksht"
                sub3 == "ksh" -> 3 to "ksh"
                sub3 == "shh" -> 3 to "shh"
                sub3 == "chh" -> 3 to "chh"
                sub3 == "gny" || sub3 == "jny" -> 3 to "jny"
                sub3 == "shr" -> 3 to "shr"
                sub3 == "dhr" -> 3 to "dhr"
                sub3 == "thr" -> 3 to "thr"
                sub3 == "bhr" -> 3 to "bhr"
                sub3 == "ghr" -> 3 to "ghr"
                sub3 == "khr" -> 3 to "khr"
                sub3 == "str" -> 3 to "str"
                sub3 == "skr" -> 3 to "skr"
                sub3 == "spr" -> 3 to "spr"
                sub3 == "spl" -> 3 to "spl"
                sub3 == "sth" -> 3 to "sth"
                sub3 == "ndr" -> 3 to "ndr"
                sub3 == "ntr" -> 3 to "ntr"
                sub3 == "mpr" -> 3 to "mpr"
                sub2 == "kh" -> 2 to "kh"
                sub2 == "gh" -> 2 to "gh"
                sub2 == "ch" -> 2 to "ch"
                sub2 == "jh" -> 2 to "jh"
                sub2 == "th" -> 2 to "th"
                sub2 == "dh" -> 2 to "dh"
                sub2 == "ph" -> 2 to "ph"
                sub2 == "bh" -> 2 to "bh"
                sub2 == "sh" -> 2 to "sh"
                sub2 == "ny" -> 2 to "ny"
                sub2 == "ng" -> 2 to "ng"
                sub2 == "kr" -> 2 to "kr"
                sub2 == "pr" -> 2 to "pr"
                sub2 == "tr" -> 2 to "tr"
                sub2 == "dr" -> 2 to "dr"
                sub2 == "gr" -> 2 to "gr"
                sub2 == "br" -> 2 to "br"
                sub2 == "vr" -> 2 to "vr"
                sub2 == "fr" -> 2 to "fr"
                sub2 == "st" -> 2 to "st"
                sub2 == "sp" -> 2 to "sp"
                sub2 == "sk" -> 2 to "sk"
                sub2 == "sm" -> 2 to "sm"
                sub2 == "sn" -> 2 to "sn"
                sub2 == "sw" || sub2 == "sv" -> 2 to "sv"
                sub2 == "nd" -> 2 to "nd"
                sub2 == "nt" -> 2 to "nt"
                sub2 == "nk" -> 2 to "nk"
                sub2 == "mb" -> 2 to "mb"
                sub2 == "mp" -> 2 to "mp"
                "kgcjtdnpbmyrvlshfzqwx".contains(sub1) -> 1 to sub1
                else -> 0 to null
            }

            if (consKey != null) {
                i += consLen
                val vRem = len - i

                val vSub2 = if (vRem >= 2) word.substring(i, i + 2) else ""
                val vSub1 = if (vRem >= 1) word.substring(i, i + 1) else ""

                val (vLen, vowelKey) = when {
                    vSub2 == "aa" || vSub2 == "ee" || vSub2 == "ii" || vSub2 == "oo" || vSub2 == "uu" ||
                    vSub2 == "ai" || vSub2 == "au" || vSub2 == "ou" || vSub2 == "ru" || vSub2 == "ri" ||
                    vSub2 == "ei" || vSub2 == "ey" || vSub2 == "oa" || vSub2 == "ay" -> 2 to vSub2
                    "aeiou".contains(vSub1) -> 1 to vSub1
                    else -> 0 to null
                }

                if (vowelKey != null) {
                    i += vLen
                }

                // If at end of word and no trailing vowel: apply virama / halant if consonant
                val isEndOfWord = (i >= len)
                out.append(renderConsonantCluster(consKey, vowelKey, isEndOfWord, language))
            } else {
                // Standalone / Initial Vowel
                val vSub2 = if (rem >= 2) word.substring(i, i + 2) else ""
                val vSub1 = word.substring(i, i + 1)

                val (vLen, vowelKey) = when {
                    vSub2 == "aa" || vSub2 == "ee" || vSub2 == "ii" || vSub2 == "oo" || vSub2 == "uu" ||
                    vSub2 == "ai" || vSub2 == "au" || vSub2 == "ou" || vSub2 == "ru" || vSub2 == "ri" ||
                    vSub2 == "ei" || vSub2 == "ey" || vSub2 == "oa" || vSub2 == "ay" -> 2 to vSub2
                    "aeiou".contains(vSub1) -> 1 to vSub1
                    else -> 0 to null
                }

                if (vowelKey != null) {
                    i += vLen
                    out.append(getInitialVowel(vowelKey, language))
                } else {
                    // Unrecognized character or digit
                    out.append(sub1)
                    i += 1
                }
            }
        }

        return out.toString()
    }

    private fun getInitialVowel(vowel: String, language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> when (vowel) {
                "a" -> "ಅ"; "aa" -> "ಆ"; "i" -> "ಇ"; "ee", "ii" -> "ಈ"
                "u" -> "ಉ"; "oo", "uu" -> "ಊ"; "e", "ei" -> "ಎ"; "ey", "ai", "ay" -> "ಐ"
                "o", "oa" -> "ಒ"; "au", "ou" -> "ಔ"; "ru", "ri" -> "ಋ"
                else -> "ಅ"
            }
            AppLanguage.SANSKRIT -> when (vowel) {
                "a" -> "अ"; "aa" -> "आ"; "i" -> "इ"; "ee", "ii" -> "ई"
                "u" -> "उ"; "oo", "uu" -> "ऊ"; "e", "ei" -> "ए"; "ey", "ai", "ay" -> "ऐ"
                "o", "oa" -> "ओ"; "au", "ou" -> "औ"; "ru", "ri" -> "ऋ"
                else -> "अ"
            }
            AppLanguage.TELUGU -> when (vowel) {
                "a" -> "అ"; "aa" -> "ఆ"; "i" -> "ఇ"; "ee", "ii" -> "ఈ"
                "u" -> "ఉ"; "oo", "uu" -> "ఊ"; "e", "ei" -> "ఎ"; "ey", "ai", "ay" -> "ఐ"
                "o", "oa" -> "ఒ"; "au", "ou" -> "ఔ"; "ru", "ri" -> "ఋ"
                else -> "అ"
            }
            AppLanguage.TAMIL -> when (vowel) {
                "a" -> "அ"; "aa" -> "ஆ"; "i" -> "இ"; "ee", "ii" -> "ஈ"
                "u" -> "உ"; "oo", "uu" -> "ஊ"; "e", "ei" -> "எ"; "ey", "ai", "ay" -> "ஐ"
                "o", "oa" -> "ஒ"; "au", "ou" -> "ஔ"; "ru", "ri" -> "ரு"
                else -> "அ"
            }
            AppLanguage.ENGLISH -> vowel
        }
    }

    private fun renderConsonantCluster(
        consKey: String,
        vowelKey: String?,
        isEndOfWord: Boolean,
        language: AppLanguage
    ): String {
        // Base mapping breakdown
        val parts = when (consKey) {
            "shht" -> listOf("shh", "t")
            "ksht" -> listOf("ksh", "t")
            "shr" -> listOf("sh", "r")
            "dhr" -> listOf("dh", "r")
            "thr" -> listOf("th", "r")
            "bhr" -> listOf("bh", "r")
            "ghr" -> listOf("gh", "r")
            "khr" -> listOf("kh", "r")
            "str" -> listOf("s", "t", "r")
            "skr" -> listOf("s", "k", "r")
            "spr" -> listOf("s", "p", "r")
            "spl" -> listOf("s", "p", "l")
            "sth" -> listOf("s", "th")
            "ndr" -> listOf("n", "d", "r")
            "ntr" -> listOf("n", "t", "r")
            "mpr" -> listOf("m", "p", "r")
            "kr" -> listOf("k", "r")
            "pr" -> listOf("p", "r")
            "tr" -> listOf("t", "r")
            "dr" -> listOf("d", "r")
            "gr" -> listOf("g", "r")
            "br" -> listOf("b", "r")
            "vr" -> listOf("v", "r")
            "fr" -> listOf("f", "r")
            "st" -> listOf("s", "t")
            "sp" -> listOf("s", "p")
            "sk" -> listOf("s", "k")
            "sm" -> listOf("s", "m")
            "sn" -> listOf("s", "n")
            "sv" -> listOf("s", "v")
            "nd" -> listOf("n", "d")
            "nt" -> listOf("n", "t")
            "nk" -> listOf("n", "k")
            "mb" -> listOf("m", "b")
            "mp" -> listOf("m", "p")
            else -> listOf(consKey)
        }

        val out = StringBuilder()
        for (idx in parts.indices) {
            val p = parts[idx]
            val isLast = (idx == parts.size - 1)
            val v = if (isLast) vowelKey else null

            val baseChar = getSingleConsonantBase(p, language)
            val matra = getVowelMatra(v, isEndOfWord && isLast, language)

            out.append(baseChar)
            out.append(matra)
        }

        return out.toString()
    }

    private fun getSingleConsonantBase(c: String, language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> when (c) {
                "k" -> "ಕ"; "kh" -> "ಖ"; "g" -> "ಗ"; "gh" -> "ಘ"; "ng" -> "ಙ"
                "ch", "c" -> "ಚ"; "chh" -> "ಛ"; "j", "z" -> "ಜ"; "jh" -> "ಝ"; "ny" -> "ಞ"
                "t" -> "ತ"; "th" -> "ಥ"; "d" -> "ದ"; "dh" -> "ಧ"; "n" -> "ನ"
                "p" -> "ಪ"; "ph", "f" -> "ಫ"; "b" -> "ಬ"; "bh" -> "ಭ"; "m" -> "ಮ"
                "y" -> "ಯ"; "r" -> "ರ"; "l" -> "ಲ"; "v", "w" -> "ವ"
                "sh" -> "ಶ"; "shh" -> "ಷ"; "s" -> "ಸ"; "h" -> "ಹ"; "ksh", "x" -> "ಕ್ಷ"; "jny" -> "ಜ್ಞ"
                "q" -> "ಕ"
                else -> "ಕ"
            }
            AppLanguage.SANSKRIT -> when (c) {
                "k" -> "क"; "kh" -> "ख"; "g" -> "ग"; "gh" -> "घ"; "ng" -> "ङ"
                "ch", "c" -> "च"; "chh" -> "छ"; "j", "z" -> "ज"; "jh" -> "झ"; "ny" -> "ञ"
                "t" -> "त"; "th" -> "थ"; "d" -> "द"; "dh" -> "ध"; "n" -> "न"
                "p" -> "प"; "ph", "f" -> "फ"; "b" -> "ब"; "bh" -> "भ"; "m" -> "म"
                "y" -> "य"; "r" -> "र"; "l" -> "ल"; "v", "w" -> "व"
                "sh" -> "श"; "shh" -> "ष"; "s" -> "स"; "h" -> "ह"; "ksh", "x" -> "क्ष"; "jny" -> "ज्ञ"
                "q" -> "क"
                else -> "क"
            }
            AppLanguage.TELUGU -> when (c) {
                "k" -> "క"; "kh" -> "ఖ"; "g" -> "గ"; "gh" -> "ఘ"; "ng" -> "ఙ"
                "ch", "c" -> "చ"; "chh" -> "ఛ"; "j", "z" -> "జ"; "jh" -> "ఝ"; "ny" -> "ఞ"
                "t" -> "త"; "th" -> "థ"; "d" -> "ద"; "dh" -> "ధ"; "n" -> "న"
                "p" -> "ప"; "ph", "f" -> "ఫ"; "b" -> "బ"; "bh" -> "భ"; "m" -> "మ"
                "y" -> "య"; "r" -> "ర"; "l" -> "ల"; "v", "w" -> "వ"
                "sh" -> "శ"; "shh" -> "ష"; "s" -> "స"; "h" -> "హ"; "ksh", "x" -> "క్ష"; "jny" -> "జ్ఞ"
                "q" -> "క"
                else -> "క"
            }
            AppLanguage.TAMIL -> when (c) {
                "k", "kh", "g", "gh", "q" -> "க"
                "ch", "chh", "c" -> "ச"
                "j", "jh", "z" -> "ஜ"
                "t", "th", "d", "dh" -> "த"
                "n" -> "ந"
                "p", "ph", "b", "bh", "f" -> "ப"
                "m" -> "ம"
                "y" -> "ய"
                "r" -> "ர"
                "l" -> "ல"
                "v", "w" -> "வ"
                "sh", "shh" -> "ஷ"
                "s" -> "ஸ"
                "h" -> "ஹ"
                "ksh", "x" -> "க்ஷ"
                "jny" -> "ஜ்ஞ"
                else -> "க"
            }
            AppLanguage.ENGLISH -> c
        }
    }

    private fun getVowelMatra(vowel: String?, isEndOfWord: Boolean, language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> when (vowel) {
                null -> if (isEndOfWord) "್" else "್"
                "a" -> ""
                "aa" -> "ಾ"
                "i" -> "ಿ"
                "ee", "ii" -> "ೀ"
                "u" -> "ು"
                "oo", "uu" -> "ೂ"
                "e", "ei" -> "ೆ"
                "ey", "ai", "ay" -> "ೈ"
                "o", "oa" -> "ೊ"
                "au", "ou" -> "ೌ"
                "ru", "ri" -> "ೃ"
                else -> ""
            }
            AppLanguage.SANSKRIT -> when (vowel) {
                null -> if (isEndOfWord) "्" else "्"
                "a" -> ""
                "aa" -> "ा"
                "i" -> "ि"
                "ee", "ii" -> "ी"
                "u" -> "ु"
                "oo", "uu" -> "ू"
                "e", "ei" -> "े"
                "ey", "ai", "ay" -> "ै"
                "o", "oa" -> "ो"
                "au", "ou" -> "ौ"
                "ru", "ri" -> "ृ"
                else -> ""
            }
            AppLanguage.TELUGU -> when (vowel) {
                null -> if (isEndOfWord) "్" else "్"
                "a" -> ""
                "aa" -> "ా"
                "i" -> "ి"
                "ee", "ii" -> "ీ"
                "u" -> "ు"
                "oo", "uu" -> "ూ"
                "e", "ei" -> "ె"
                "ey", "ai", "ay" -> "ై"
                "o", "oa" -> "ొ"
                "au", "ou" -> "ౌ"
                "ru", "ri" -> "ృ"
                else -> ""
            }
            AppLanguage.TAMIL -> when (vowel) {
                null -> if (isEndOfWord) "்" else "்"
                "a" -> ""
                "aa" -> "ா"
                "i" -> "ி"
                "ee", "ii" -> "ீ"
                "u" -> "ு"
                "oo", "uu" -> "ூ"
                "e", "ei" -> "ெ"
                "ey", "ai", "ay" -> "ை"
                "o", "oa" -> "ொ"
                "au", "ou" -> "ௌ"
                "ru", "ri" -> "்ரு"
                else -> ""
            }
            AppLanguage.ENGLISH -> vowel ?: ""
        }
    }
}
