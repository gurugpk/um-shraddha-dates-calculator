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
 *   1. Full multi-lingual dictionary with expansive phonetic variants for Indian & global names,
 *      matha parampara, surnames, and geographical places.
 *   2. Granular phonetic rules for consonant clusters (samyuktaksharas), Arkavattu/Repha (ರ್ / र् / ర్ / ர்),
 *      Paninian Natva (रषाभ्यां नो णः - retroflex nasal ಣ / ण / ణ in -rn-, -shn-), aspirates,
 *      sibilants, and word-final virama / halant.
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
        "theertha" to "ತೀರ್ಥ", "tirtha" to "ತೀರ್ಥ", "guru" to "ಗುರು", "gurubhyo" to "ಗುರುಭ್ಯೋ",
        "namaha" to "ನಮಃ", "namah" to "ನಮಃ",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "ಸತ್ಯಾತ್ಮತೀರ್ಥ", "satyatma" to "ಸತ್ಯಾತ್ಮ", "satyatmatirtha" to "ಸತ್ಯಾತ್ಮತೀರ್ಥ",
        "satyapramoda" to "ಸತ್ಯಪ್ರಮೋದ", "satyadhyana" to "ಸತ್ಯಧ್ಯಾನ", "satyadharma" to "ಸತ್ಯಧರ್ಮ",
        "madhwacharya" to "ಮಧ್ವಾಚಾರ್ಯ", "madhva" to "ಮಧ್ವ", "madhwa" to "ಮಧ್ವ",
        "jayatheertha" to "ಜಯತೀರ್ಥ", "jayatirtha" to "ಜಯತೀರ್ಥ", "teekacharya" to "ಟೀಕಾಚಾರ್ಯ",
        "raghavendra" to "ರಾಘವೇಂದ್ರ", "raghavendrateertha" to "ರಾಘವೇಂದ್ರತೀರ್ಥ", "rayaru" to "ರಾಯರು",
        "raghavendraswamy" to "ರಾಘವೇಂದ್ರಸ್ವಾಮಿ", "raghavendraswami" to "ರಾಘವೇಂದ್ರಸ್ವಾಮಿ",
        "vadiraja" to "ವಾದಿರಾಜ", "vyasatheertha" to "ವ್ಯಾಸತೀರ್ಥ", "vyasatirtha" to "ವ್ಯಾಸತೀರ್ಥ",
        "purandaradasa" to "ಪುರಂದರದಾಸ", "kanakadasa" to "ಕನಕದಾಸ", "vijayadasa" to "ವಿಜಯದಾಸ",
        "uttaradi" to "ಉತ್ತರಾದಿ", "mantralaya" to "ಮಂತ್ರಾಲಯ", "mantralayam" to "ಮಂತ್ರಾಲಯ",
        "math" to "ಮಠ", "matha" to "ಮಠ", "mutt" to "ಮಠ",

        // Common First Names & Variations
        "pranesh" to "ಪ್ರಾಣೇಶ್", "pranesha" to "ಪ್ರಾಣೇಶ", "praneshrao" to "ಪ್ರಾಣೇಶರಾವ್",
        "gururaj" to "ಗುರುರಾಜ್", "gururaja" to "ಗುರುರಾಜ", "gururajarao" to "ಗುರುರಾಜರಾವ್",
        "ramachandra" to "ರಾಮಚಂದ್ರ", "ramchandra" to "ರಾಮಚಂದ್ರ", "ramachandrarao" to "ರಾಮಚಂದ್ರರಾವ್",
        "anand" to "ಆನಂದ್", "ananda" to "ಆನಂದ", "ananth" to "ಅನಂತ", "anantha" to "ಅನಂತ",
        "narayan" to "ನಾರಾಯಣ", "narayana" to "ನಾರಾಯಣ", "narayanarao" to "ನಾರಾಯಣರಾವ್",
        "krishna" to "ಕೃಷ್ಣ", "krishnamurthy" to "ಕೃಷ್ಣಮೂರ್ತಿ", "krishnamurti" to "ಕೃಷ್ಣಮೂರ್ತಿ",
        "venkatesh" to "ವೆಂಕಟೇಶ್", "venkatesha" to "ವೆಂಕಟೇಶ", "venkataramana" to "ವೆಂಕಟರಮಣ",
        "venkateshwara" to "ವೆಂಕಟೇಶ್ವರ", "venkatarama" to "ವೆಂಕಟರಾಮ",
        "vijay" to "ವಿಜಯ್", "vijaya" to "ವಿಜಯ", "vijayakrishna" to "ವಿಜಯಕೃಷ್ಣ",
        "srinivas" to "ಶ್ರೀನಿವಾಸ್", "srinivasa" to "ಶ್ರೀನಿವಾಸ", "srinivasarao" to "ಶ್ರೀನಿವಾಸರಾವ್",
        "shrinivas" to "ಶ್ರೀನಿವಾಸ್", "shrinivasa" to "ಶ್ರೀನಿವಾಸ",
        "tanmay" to "ತನ್ಮಯ್", "tammay" to "ತಮ್ಮಯ್",
        "raghav" to "ರಾಘವ್", "raghava" to "ರಾಘವ", "raghavarao" to "ರಾಘವರಾವ್",
        "keshav" to "ಕೇಶವ", "keshava" to "ಕೇಶವ", "keshavarao" to "ಕೇಶವರಾವ್",
        "govind" to "ಗೋವಿಂದ", "govinda" to "ಗೋವಿಂದ", "govindarao" to "ಗೋವಿಂದರಾವ್",
        "madhav" to "ಮಾಧವ", "madhava" to "ಮಾಧವ", "madhavarao" to "ಮಾಧವರಾವ್",
        "vishnu" to "ವಿಷ್ಣು", "vishnumurthy" to "ವಿಷ್ಣುಮೂರ್ತಿ",
        "prahlad" to "ಪ್ರಹ್ಲಾದ್", "prahlada" to "ಪ್ರಹ್ಲಾದ", "prahladarao" to "ಪ್ರಹ್ಲಾದರಾವ್",
        "ganesh" to "ಗಣೇಶ್", "ganesha" to "ಗಣೇಶ", "ganapathi" to "ಗಣಪತಿ", "ganapati" to "ಗಣಪತಿ",
        "subrahmanya" to "ಸುಬ್ರಹ್ಮಣ್ಯ", "subramanya" to "ಸುಬ್ರಹ್ಮಣ್ಯ", "subramaniam" to "ಸುಬ್ರಹ್ಮಣ್ಯಂ",
        "surya" to "ಸೂರ್ಯ", "suryanarayana" to "ಸೂರ್ಯನಾರಾಯಣ",
        "shankar" to "ಶಂಕರ್", "shankara" to "ಶಂಕರ", "shankaranarayana" to "ಶಂಕರನಾರಾಯಣ",
        "dattatreya" to "ದತ್ತಾತ್ರೇಯ", "dattatraya" to "ದತ್ತಾತ್ರೇಯ", "datta" to "ದತ್ತ",
        "hanuman" to "ಹನುಮಾನ್", "hanumantha" to "ಹನುಮಂತ", "hanumantharao" to "ಹನುಮಂತರಾವ್",
        "bhimsen" to "ಭೀಮಸೇನ್", "bheemsen" to "ಭೀಮಸೇನ್", "bhima" to "ಭೀಮ", "bhimasena" to "ಭೀಮಸೇನ",
        "arjun" to "ಅರ್ಜುನ್", "arjuna" to "ಅರ್ಜುನ", "arjunarao" to "ಅರ್ಜುನರಾವ್",
        "manjunath" to "ಮಂಜುನಾಥ್", "manjunatha" to "ಮಂಜುನಾಥ",
        "nagaraj" to "ನಾಗರಾಜ್", "nagaraja" to "ನಾಗರಾಜ", "nagendra" to "ನಾಗೇಂದ್ರ",
        "prashanth" to "ಪ್ರಶಾಂತ್", "prashant" to "ಪ್ರಶಾಂತ್",
        "pradeep" to "ಪ್ರದೀಪ್", "pradeepa" to "ಪ್ರದೀಪ",
        "prakash" to "ಪ್ರಕಾಶ್", "prakasha" to "ಪ್ರಕಾಶ",
        "pramod" to "ಪ್ರಮೋದ್", "pramoda" to "ಪ್ರಮೋದ",
        "suresh" to "ಸುರೇಶ್", "suresha" to "ಸುರೇಶ",
        "ramesh" to "ರಮೇಶ್", "ramesha" to "ರಮೇಶ",
        "mahesh" to "ಮಹೇಶ್", "mahesha" to "ಮಹೇಶ",
        "dinesh" to "ದಿನೇಶ್", "dinesha" to "ದಿನೇಶ",
        "rajesh" to "ರಾಜೇಶ್", "rajesha" to "ರಾಜೇಶ",
        "girish" to "ಗಿರೀಶ್", "girisha" to "ಗಿರೀಶ",
        "harish" to "ಹರೀಶ್", "harisha" to "ಹರೀಶ",
        "satish" to "ಸತೀಶ್", "satisha" to "ಸತೀಶ",
        "ravindra" to "ರವೀಂದ್ರ", "ravindranath" to "ರವೀಂದ್ರನಾಥ",
        "gopal" to "ಗೋಪಾಲ್", "gopala" to "ಗೋಪಾಲ", "gopalakrishna" to "ಗೋಪಾಲಕೃಷ್ಣ",
        "vasudev" to "ವಾಸುದೇವ", "vasudeva" to "ವಾಸುದೇವ",
        "damodar" to "ದಾಮೋದರ", "damodara" to "ದಾಮೋದರ",
        "janardana" to "ಜನಾರ್ದನ", "janardan" to "ಜನಾರ್ದನ",
        "padmanabha" to "ಪದ್ಮನಾಭ", "padmanabharao" to "ಪದ್ಮನಾಭರಾವ್",
        "achyuta" to "ಅಚ್ಯುತ", "achyutarao" to "ಅಚ್ಯುತರಾವ್",
        "lakshman" to "ಲಕ್ಷ್ಮಣ", "lakshmana" to "ಲಕ್ಷ್ಮಣ", "laxman" to "ಲಕ್ಷ್ಮಣ",
        "sudarshan" to "ಸುದರ್ಶನ", "sudharshan" to "ಸುದರ್ಶನ",
        "gangadhar" to "ಗಂಗಾಧರ", "gangadhara" to "ಗಂಗಾಧರ",
        "mahadev" to "ಮಹಾದೇವ", "mahadeva" to "ಮಹಾದೇವ",
        "parameshwara" to "ಪರಮೇಶ್ವರ", "parameshwar" to "ಪರಮೇಶ್ವರ",
        "vishwanatha" to "ವಿಶ್ವನಾಥ", "vishwanath" to "ವಿಶ್ವನಾಥ",
        "someshwara" to "ಸೋಮೇಶ್ವರ", "somashekar" to "ಸೋಮಶೇಖರ್",
        "chidananda" to "ಚಿದಾನಂದ", "sadananda" to "ಸದಾನಂದ", "shivananda" to "ಶಿವಾನಂದ",
        "giridhar" to "ಗಿರಿಧರ", "muralidhar" to "ಮುರಳಿಧರ",
        "chandrashekar" to "ಚಂದ್ರಶೇಖರ್", "chandrasekhar" to "ಚಂದ್ರಶೇಖರ್",
        "vinay" to "ವಿನಯ್", "vinayak" to "ವಿನಾಯಕ", "vinayaka" to "ವಿನಾಯಕ",
        "ashok" to "ಅಶೋಕ್", "ashoka" to "ಅಶೋಕ",
        "kalyan" to "ಕಲ್ಯಾಣ್", "kalyana" to "ಕಲ್ಯಾಣ",

        // Female First Names
        "lakshmi" to "ಲಕ್ಷ್ಮಿ", "laxmi" to "ಲಕ್ಷ್ಮಿ", "lakshmidevi" to "ಲಕ್ಷ್ಮಿದೇವಿ",
        "saraswati" to "ಸರಸ್ವತಿ", "saraswathi" to "ಸರಸ್ವತಿ",
        "parvati" to "ಪಾರ್ವತಿ", "parvathi" to "ಪಾರ್ವತಿ",
        "radha" to "ರಾಧಾ", "radhika" to "ರಾಧಿಕಾ",
        "rukmini" to "ರುಕ್ಮಿಣಿ", "rugmini" to "ರುಕ್ಮಿಣಿ",
        "sita" to "ಸೀತಾ", "seetha" to "ಸೀತಾ",
        "gayatri" to "ಗಾಯತ್ರಿ", "gayathri" to "ಗಾಯತ್ರಿ",
        "savithri" to "ಸಾವಿತ್ರಿ", "savitri" to "ಸಾವಿತ್ರಿ",
        "shakuntala" to "ಶಕುಂತಲಾ", "shakunthala" to "ಶಕುಂತಲಾ", "shakuntale" to "ಶಕುಂತಲೆ",
        "padma" to "ಪದ್ಮಾ", "padmavati" to "ಪದ್ಮಾವತಿ", "padmavathi" to "ಪದ್ಮಾವತಿ",
        "kamala" to "ಕಮಲಾ", "kamalamma" to "ಕಮಲಮ್ಮ",
        "sarala" to "ಸರಳಾ", "sharada" to "ಶಾರದಾ", "sharda" to "ಶಾರದಾ",
        "sumitra" to "ಸುಮಿತ್ರಾ", "sumithra" to "ಸುಮಿತ್ರಾ",
        "kausalya" to "ಕೌಸಲ್ಯಾ", "kousalya" to "ಕೌಸಲ್ಯಾ",
        "sulochana" to "ಸುಲೋಚನಾ", "anusuya" to "ಅನಸೂಯಾ", "anasuya" to "ಅನಸೂಯಾ",
        "shanta" to "ಶಾಂತಾ", "shanthi" to "ಶಾಂತಿ", "shanti" to "ಶಾಂತಿ",
        "leela" to "ಲೀಲಾ", "leelavathi" to "ಲೀಲಾವತಿ", "leelavati" to "ಲೀಲಾವತಿ",
        "geetha" to "ಗೀತಾ", "geeta" to "ಗೀತಾ", "kalyani" to "ಕಲ್ಯಾಣಿ",

        // Common Surnames & Family Names (with all spelling variations)
        "kulkarni" to "ಕುಲಕರ್ಣಿ", "kulakarni" to "ಕುಲಕರ್ಣಿ", "koolkarni" to "ಕುಲಕರ್ಣಿ",
        "kulkarny" to "ಕುಲಕರ್ಣಿ", "kulakarny" to "ಕುಲಕರ್ಣಿ",
        "rao" to "ರಾವ್", "rava" to "ರಾವ್",
        "joshi" to "ಜೋಷಿ", "jyoshi" to "ಜೋಷಿ", "josi" to "ಜೋಶಿ",
        "deshpande" to "ದೇಶಪಾಂಡೆ", "deshapande" to "ದೇಶಪಾಂಡೆ", "despande" to "ದೇಶಪಾಂಡೆ",
        "bhat" to "ಭಟ್", "bhatta" to "ಭಟ್ಟ", "bhatt" to "ಭಟ್",
        "sharma" to "ಶರ್ಮಾ", "shastri" to "ಶಾಸ್ತ್ರಿ",
        "pai" to "ಪೈ", "kamath" to "ಕಾಮತ್", "kamat" to "ಕಾಮತ್",
        "shenoy" to "ಶೆಣೈ", "prabhu" to "ಪ್ರಭು",
        "hegde" to "ಹೆಗಡೆ", "shetty" to "ಶೆಟ್ಟಿ", "gowda" to "ಗೌಡ", "gauda" to "ಗೌಡ",
        "patil" to "ಪಾಟೀಲ್", "patila" to "ಪಾಟೀಲ",
        "deshmukh" to "ದೇಶಮುಖ್", "deshamukha" to "ದೇಶಮುಖ",
        "mutalik" to "ಮುತಾಲಿಕ್", "muthalik" to "ಮುತಾಲಿಕ್", "mutalika" to "ಮುತಾಲಿಕ",
        "inamdar" to "ಇನಾಂದಾರ್", "eenaamdar" to "ಇನಾಂದಾರ್",
        "diwan" to "ದಿವಾನ್", "pujari" to "ಪೂಜಾರಿ", "poojari" to "ಪೂಜಾರಿ", "archak" to "ಅರ್ಚಕ",
        "karanth" to "ಕಾರಂತ್", "adiga" to "ಅಡಿಗ", "holla" to "ಹೊಳ್ಳ",
        "upadhyaya" to "ಉಪಾಧ್ಯಾಯ", "upadhyay" to "ಉಪಾಧ್ಯಾಯ",
        "puranik" to "ಪುರಾಣಿಕ್", "puraanik" to "ಪುರಾಣಿಕ್",

        // Indian Cities & States
        "bengaluru" to "ಬೆಂಗಳೂರು", "bangalore" to "ಬೆಂಗಳೂರು", "karnataka" to "ಕರ್ನಾಟಕ", "india" to "ಭಾರತ",
        "mysuru" to "ಮೈಸೂರು", "mysore" to "ಮೈಸೂರು", "udupi" to "ಉಡುಪಿ",
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
        "theertha" to "तीर्थ", "tirtha" to "तीर्थ", "guru" to "गुरु", "gurubhyo" to "गुरुभ्यो",
        "namaha" to "नमः", "namah" to "नमः",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "सत्यात्मतीर्थ", "satyatma" to "सत्यात्म", "satyatmatirtha" to "सत्यात्मतीर्थ",
        "satyapramoda" to "सत्यप्रमोद", "satyadhyana" to "सत्यध्यान", "satyadharma" to "सत्यधर्म",
        "madhwacharya" to "मध्वाचार्य", "madhva" to "मध्व", "madhwa" to "मध्व",
        "jayatheertha" to "जयतीर्थ", "jayatirtha" to "जयतीर्थ", "teekacharya" to "टीकाचार्य",
        "raghavendra" to "राघवेंद्र", "raghavendrateertha" to "राघवेंद्रतीर्थ", "rayaru" to "राघवेंद्रस्वामिनः",
        "vadiraja" to "वादिराज", "vyasatheertha" to "व्यासतीर्थ", "vyasatirtha" to "व्यासतीर्थ",
        "purandaradasa" to "पुरन्दरदास", "kanakadasa" to "कनकदास", "vijayadasa" to "विजयदास",
        "uttaradi" to "उत्तरादि", "mantralaya" to "मन्त्रालयम्", "mantralayam" to "मन्त्रालयम्",
        "math" to "मठ", "matha" to "मठ",

        // Common Names & Surnames
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
        "shakuntala" to "शकुन्तला", "shakunthala" to "शकुन्तला",

        // Surnames
        "kulkarni" to "कुलकर्णी", "kulakarni" to "कुलकर्णी", "koolkarni" to "कुलकर्णी",
        "kulkarny" to "कुलकर्णी", "kulakarny" to "कुलकर्णी",
        "rao" to "राव", "joshi" to "जोशी",
        "deshpande" to "देशपाण्डे", "deshapande" to "देशपाण्डे", "despande" to "देशपाण्डे",
        "bhat" to "भट्ट", "bhatta" to "भट्ट", "sharma" to "शर्मा", "shastri" to "शास्त्री",
        "pai" to "पै", "kamath" to "कामथ", "shenoy" to "शणै", "prabhu" to "प्रभु",
        "hegde" to "हेगडे", "shetty" to "शेट्टी", "gowda" to "गौडा", "patil" to "पाटील",
        "deshmukh" to "देशमुख", "mutalik" to "मुतालिक", "inamdar" to "इनामदार",
        "diwan" to "दीवान", "pujari" to "पुजारी", "archak" to "अर्चक", "karanth" to "कारन्त",
        "adiga" to "अडिग", "holla" to "होळ्ळ", "upadhyaya" to "उपाध्याय", "puranik" to "पौराणिक",

        // Locations
        "bengaluru" to "बेङ्गळूरु", "bangalore" to "बेङ्गळूरु", "karnataka" to "कर्णाटकम्", "india" to "भारतम्",
        "mysuru" to "मैसूरु", "mysore" to "मैसूरु", "udupi" to "उडुपी",
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
        "theertha" to "తీర్థ", "tirtha" to "తీర్థ", "guru" to "గురు", "gurubhyo" to "గురుభ్యో",
        "namaha" to "నమః", "namah" to "నమః",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "సత్యాత్మతీర్థ", "satyatma" to "సత్యాత్మ", "satyatmatirtha" to "సత్యాత్మతీర్థ",
        "satyapramoda" to "సత్యప్రమోద", "satyadhyana" to "సత్యధ్యాన", "satyadharma" to "సత్యధర్మ",
        "madhwacharya" to "మధ్వాచార్య", "madhva" to "మధ్వ", "madhwa" to "మధ్వ",
        "jayatheertha" to "జయతీర్థ", "jayatirtha" to "జయతీర్థ", "teekacharya" to "టీకాచార్య",
        "raghavendra" to "రాఘవేంద్ర", "raghavendrateertha" to "రాఘవేంద్రతీర్థ", "rayaru" to "రాయరు",
        "vadiraja" to "వాదిరాజ", "vyasatheertha" to "వ్యాసతీర్థ", "vyasatirtha" to "వ్యాసతీర్థ",
        "purandaradasa" to "పురందరదాస", "kanakadasa" to "కనకదాస", "vijayadasa" to "విజయదాస",
        "uttaradi" to "ఉత్తరాది", "mantralaya" to "మంత్రాలయం", "mantralayam" to "మంత్రాలయం",
        "math" to "మఠం", "matha" to "మఠం",

        // Common Names & Surnames
        "pranesh" to "ప్రాణేష్", "gururaj" to "గురురాజ్", "ramachandra" to "రామచంద్ర",
        "anand" to "ఆనంద్", "ananda" to "ఆనంద", "narayana" to "నారాయణ", "krishna" to "కృష్ణ",
        "venkatesh" to "వెంకటేష్", "venkataramana" to "వెంకటరమణ", "vijay" to "విజయ్",
        "srinivas" to "శ్రీనిவாస్", "srinivasa" to "శ్రీనివాస", "tanmay" to "తన్మయ్", "tammay" to "తమ్మయ్",
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
        "shakuntala" to "శకుంతల", "shakunthala" to "శకుంతల",

        // Surnames
        "kulkarni" to "కులకర్ణి", "kulakarni" to "కులకర్ణి", "koolkarni" to "కులకర్ణి",
        "kulkarny" to "కులకర్ణి", "kulakarny" to "కులకర్ణి",
        "rao" to "రావు", "joshi" to "జోషి",
        "deshpande" to "దేశ్‌పాండే", "deshapande" to "దేశ్‌పాండే", "despande" to "దేశ్‌పాండే",
        "bhat" to "భట్", "bhatta" to "భట్", "sharma" to "శర్మ", "shastri" to "శాస్త్రి",
        "pai" to "పై", "kamath" to "కామత్", "shenoy" to "శెణై", "prabhu" to "ప్రభు",
        "hegde" to "హెగ్డే", "shetty" to "శెట్టి", "gowda" to "గౌడ", "patil" to "పాటీల్",
        "deshmukh" to "దేశ్‌ముఖ్", "mutalik" to "ముతాలిక్", "inamdar" to "ఇనాందార్",
        "diwan" to "దివాన్", "pujari" to "పూజారి", "archak" to "అర్చక", "karanth" to "కారంత్",
        "adiga" to "అడిగ", "holla" to "హొళ్ళ", "upadhyaya" to "ఉపాధ్యాయ", "puranik" to "పురాణిక్",

        // Locations
        "bengaluru" to "బెంగళూరు", "bangalore" to "బెంగళూరు", "karnataka" to "కర్ణాటక", "india" to "భారతదేశం",
        "mysuru" to "మైసూరు", "mysore" to "మైసూరు", "udupi" to "ఉడుపి",
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
        "theertha" to "தீர்த்த", "tirtha" to "தீர்த்த", "guru" to "குரு", "gurubhyo" to "குருப்யோ",
        "namaha" to "நமஹ", "namah" to "நமஹ",

        // Parampara & Spiritual Names
        "satyatmatheertha" to "ஸத்யாத்மதீர்த்த", "satyatma" to "ஸத்யாத்ம", "satyatmatirtha" to "ஸத்யாத்மதீர்த்த",
        "satyapramoda" to "ஸத்யப்ரமோத", "satyadhyana" to "ஸத்யத்யான", "satyadharma" to "ஸத்யதர்ம",
        "madhwacharya" to "மத்வாசார்யா", "madhva" to "மத்வ", "madhwa" to "மத்வ",
        "jayatheertha" to "ஜயதீர்த்த", "jayatirtha" to "ஜயதீர்த்த", "teekacharya" to "டீகாசார்யா",
        "raghavendra" to "ராகவேந்திரா", "raghavendrateertha" to "ராகவேந்திரதீர்த்த", "rayaru" to "ராயரு",
        "vadiraja" to "வாதிராஜ", "vyasatheertha" to "வ்யாஸதீர்த்த", "vyasatirtha" to "வ்யாஸதீர்த்த",
        "purandaradasa" to "புரந்தரதாஸ", "kanakadasa" to "கனகதாஸ", "vijayadasa" to "விஜயதாஸ",
        "uttaradi" to "உத்தரதி", "mantralaya" to "மந்த்ராலயம்", "mantralayam" to "மந்த்ராலயம்",
        "math" to "மடம்", "matha" to "மடம்",

        // Common Names & Surnames
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
        "shakuntala" to "சகுந்தலா", "shakunthala" to "சகுந்தலா",

        // Surnames
        "kulkarni" to "குல்கர்னி", "kulakarni" to "குல்கர்னி", "koolkarni" to "குல்கர்னி",
        "kulkarny" to "குல்கர்னி", "kulakarny" to "குல்கர்னி",
        "rao" to "ராவ்", "joshi" to "ஜோஷி",
        "deshpande" to "தேஷ்பாண்டே", "deshapande" to "தேஷ்பாண்டே", "despande" to "தேஷ்பாண்டே",
        "bhat" to "பட்", "bhatta" to "பட்டர்", "sharma" to "சர்மா", "shastri" to "சாஸ்திரி",
        "pai" to "பை", "kamath" to "காமத்", "shenoy" to "ஷெனாய்", "prabhu" to "பிரபு",
        "hegde" to "ஹெக்டே", "shetty" to "ஷெட்டி", "gowda" to "கௌடா", "patil" to "பாட்டீல்",
        "deshmukh" to "தேஷ்முக்", "mutalik" to "முத்தாலிக்", "inamdar" to "இனாம்தார்",
        "diwan" to "திவான்", "pujari" to "பூசாரி", "archak" to "அர்ச்சகர்", "karanth" to "கராந்த்",
        "adiga" to "அடிகா", "holla" to "ஹொள்ளா", "upadhyaya" to "உபாத்யாயா", "puranik" to "புராணிக்",

        // Locations
        "bengaluru" to "பெங்களூரு", "bangalore" to "பெங்களூரு", "karnataka" to "கர்நாடகா", "india" to "இந்தியா",
        "mysuru" to "மைசூரு", "mysore" to "மைசூரு", "udupi" to "உடுப்பி",
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

        // Direct whole-phrase dictionary match (e.g. "New York", "San Francisco", "Sri Raghavendra Swami")
        val wholeMatch = dictionary[text.trim().lowercase()]
        if (wholeMatch != null) return wholeMatch

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

            // 2. Universal phonetic transliteration with Paninian Natva rules
            val phonetic = transliteratePhoneticWord(cleanLower, language)
            result.append(phonetic)
        }

        return result.toString()
    }

    /**
     * Official Unicode Consortium ICU Transliterator for inter-script conversion
     * (e.g. "Devanagari-Kannada", "Devanagari-Telugu", "Kannada-Devanagari").
     */
    fun icuTransliterate(text: String, id: String): String {
        return try {
            val transliterator = com.ibm.icu.text.Transliterator.getInstance(id)
            transliterator.transliterate(text)
        } catch (t: Throwable) {
            text
        }
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

            // Multi-char consonant patterns (longest match first)
            val sub5 = if (rem >= 5) word.substring(i, i + 5) else ""
            val sub4 = if (rem >= 4) word.substring(i, i + 4) else ""
            val sub3 = if (rem >= 3) word.substring(i, i + 3) else ""
            val sub2 = if (rem >= 2) word.substring(i, i + 2) else ""
            val sub1 = word.substring(i, i + 1)

            val (consLen, consKey) = when {
                sub5 == "shhtt" -> 5 to "shhtt"
                sub5 == "rshna" -> 5 to "rshna"
                sub4 == "shht" -> 4 to "shht"
                sub4 == "ksht" -> 4 to "ksht"
                sub4 == "rshn" -> 4 to "rshn"
                sub4 == "rsha" -> 4 to "rsha"
                sub4 == "cchh" -> 4 to "cchh"
                sub4 == "chhh" -> 4 to "chhh"
                sub3 == "ksh" -> 3 to "ksh"
                sub3 == "shh" -> 3 to "shh"
                sub3 == "chh" -> 3 to "chh"
                sub3 == "gny" || sub3 == "jny" -> 3 to "jny"
                sub3 == "shr" -> 3 to "shr"
                sub3 == "shl" -> 3 to "shl"
                sub3 == "shn" -> 3 to "shn" // Retroflex ष्ण / ಷ್ಣ / ష్ణ / ஷ்ண
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
                sub3 == "mbh" -> 3 to "mbh"
                sub3 == "ndh" -> 3 to "ndh"
                sub3 == "nth" -> 3 to "nth"
                sub3 == "nch" -> 3 to "nch"
                sub3 == "rth" -> 3 to "rth"
                sub3 == "rdh" -> 3 to "rdh"
                sub3 == "rbh" -> 3 to "rbh"
                sub3 == "rsh" -> 3 to "rsh"
                sub3 == "rgh" -> 3 to "rgh"
                sub3 == "rkh" -> 3 to "rkh"
                sub3 == "dhy" -> 3 to "dhy"
                sub3 == "thy" -> 3 to "thy"
                sub3 == "bhy" -> 3 to "bhy"
                sub3 == "shy" -> 3 to "shy"
                sub2 == "rn" -> 2 to "rn" // Retroflex Repha + Na (ರ್ಣ / र्ण / ర్ణ / ர்ண)
                sub2 == "rk" -> 2 to "rk"
                sub2 == "rg" -> 2 to "rg"
                sub2 == "rc" || sub2 == "rch" -> 2 to "rch"
                sub2 == "rj" -> 2 to "rj"
                sub2 == "rt" -> 2 to "rt"
                sub2 == "rd" -> 2 to "rd"
                sub2 == "rp" -> 2 to "rp"
                sub2 == "rb" -> 2 to "rb"
                sub2 == "rm" -> 2 to "rm"
                sub2 == "ry" -> 2 to "ry"
                sub2 == "rv" || sub2 == "rw" -> 2 to "rv"
                sub2 == "rl" -> 2 to "rl"
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
                sub2 == "nj" -> 2 to "nj"
                sub2 == "mb" -> 2 to "mb"
                sub2 == "mp" -> 2 to "mp"
                sub2 == "kk" -> 2 to "kk"
                sub2 == "gg" -> 2 to "gg"
                sub2 == "cc" -> 2 to "cc"
                sub2 == "jj" -> 2 to "jj"
                sub2 == "tt" -> 2 to "tt"
                sub2 == "dd" -> 2 to "dd"
                sub2 == "nn" -> 2 to "nn"
                sub2 == "pp" -> 2 to "pp"
                sub2 == "bb" -> 2 to "bb"
                sub2 == "mm" -> 2 to "mm"
                sub2 == "yy" -> 2 to "yy"
                sub2 == "ll" -> 2 to "ll"
                sub2 == "vv" || sub2 == "ww" -> 2 to "vv"
                sub2 == "ss" -> 2 to "ss"
                sub2 == "ky" -> 2 to "ky"
                sub2 == "gy" -> 2 to "gy"
                sub2 == "ty" -> 2 to "ty"
                sub2 == "dy" -> 2 to "dy"
                sub2 == "py" -> 2 to "py"
                sub2 == "by" -> 2 to "by"
                sub2 == "my" -> 2 to "my"
                sub2 == "ly" -> 2 to "ly"
                sub2 == "vy" -> 2 to "vy"
                sub2 == "sy" -> 2 to "sy"
                sub2 == "hy" -> 2 to "hy"
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
        // Base mapping breakdown into atomic phonetic components
        val parts = when (consKey) {
            "shhtt" -> listOf("shh", "t", "t")
            "rshna" -> listOf("r", "shh", "N")
            "shht" -> listOf("shh", "t")
            "ksht" -> listOf("ksh", "t")
            "rshn" -> listOf("r", "shh", "N")
            "rsha" -> listOf("r", "shh")
            "cchh" -> listOf("ch", "chh")
            "chhh" -> listOf("ch", "chh")
            "shr" -> listOf("sh", "r")
            "shl" -> listOf("sh", "l")
            "shn" -> listOf("shh", "N") // Retroflex ಷ್ಣ / ष्ण / ష్ణ
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
            "mbh" -> listOf("m", "bh")
            "ndh" -> listOf("n", "dh")
            "nth" -> listOf("n", "th")
            "nch" -> listOf("n", "ch")
            "rth" -> listOf("r", "th")
            "rdh" -> listOf("r", "dh")
            "rbh" -> listOf("r", "bh")
            "rsh" -> listOf("r", "shh")
            "rgh" -> listOf("r", "gh")
            "rkh" -> listOf("r", "kh")
            "dhy" -> listOf("dh", "y")
            "thy" -> listOf("th", "y")
            "bhy" -> listOf("bh", "y")
            "shy" -> listOf("sh", "y")
            "rn" -> listOf("r", "N") // Natva: r + Retroflex Na (ರ್ಣ / र्ण / ర్ణ)
            "rk" -> listOf("r", "k")
            "rg" -> listOf("r", "g")
            "rch" -> listOf("r", "ch")
            "rj" -> listOf("r", "j")
            "rt" -> listOf("r", "t")
            "rd" -> listOf("r", "d")
            "rp" -> listOf("r", "p")
            "rb" -> listOf("r", "b")
            "rm" -> listOf("r", "m")
            "ry" -> listOf("r", "y")
            "rv" -> listOf("r", "v")
            "rl" -> listOf("r", "l")
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
            "nj" -> listOf("n", "j")
            "mb" -> listOf("m", "b")
            "mp" -> listOf("m", "p")
            "kk" -> listOf("k", "k")
            "gg" -> listOf("g", "g")
            "cc" -> listOf("ch", "ch")
            "jj" -> listOf("j", "j")
            "tt" -> listOf("t", "t")
            "dd" -> listOf("d", "d")
            "nn" -> listOf("n", "n")
            "pp" -> listOf("p", "p")
            "bb" -> listOf("b", "b")
            "mm" -> listOf("m", "m")
            "yy" -> listOf("y", "y")
            "ll" -> listOf("l", "l")
            "vv" -> listOf("v", "v")
            "ss" -> listOf("s", "s")
            "ky" -> listOf("k", "y")
            "gy" -> listOf("g", "y")
            "ty" -> listOf("t", "y")
            "dy" -> listOf("d", "y")
            "py" -> listOf("p", "y")
            "by" -> listOf("b", "y")
            "my" -> listOf("m", "y")
            "ly" -> listOf("l", "y")
            "vy" -> listOf("v", "y")
            "sy" -> listOf("s", "y")
            "hy" -> listOf("h", "y")
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
                "T" -> "ಟ"; "Th" -> "ಠ"; "D" -> "ಡ"; "Dh" -> "ಢ"; "N" -> "ಣ" // Retroflex series
                "t" -> "ತ"; "th" -> "ಥ"; "d" -> "ದ"; "dh" -> "ಧ"; "n" -> "ನ" // Dental series
                "p" -> "ಪ"; "ph", "f" -> "ಫ"; "b" -> "ಬ"; "bh" -> "ಭ"; "m" -> "ಮ"
                "y" -> "ಯ"; "r" -> "ರ"; "l" -> "ಲ"; "L" -> "ಳ"; "v", "w" -> "ವ"
                "sh" -> "ಶ"; "shh" -> "ಷ"; "s" -> "ಸ"; "h" -> "ಹ"; "ksh", "x" -> "ಕ್ಷ"; "jny" -> "ಜ್ಞ"
                "q" -> "ಕ"
                else -> "ಕ"
            }
            AppLanguage.SANSKRIT -> when (c) {
                "k" -> "क"; "kh" -> "ख"; "g" -> "ग"; "gh" -> "घ"; "ng" -> "ङ"
                "ch", "c" -> "च"; "chh" -> "छ"; "j", "z" -> "ज"; "jh" -> "झ"; "ny" -> "ञ"
                "T" -> "ट"; "Th" -> "ठ"; "D" -> "ड"; "Dh" -> "ढ"; "N" -> "ण" // Retroflex series
                "t" -> "त"; "th" -> "थ"; "d" -> "द"; "dh" -> "ध"; "n" -> "न" // Dental series
                "p" -> "प"; "ph", "f" -> "फ"; "b" -> "ब"; "bh" -> "भ"; "m" -> "म"
                "y" -> "य"; "r" -> "र"; "l" -> "ल"; "L" -> "ळ"; "v", "w" -> "व"
                "sh" -> "श"; "shh" -> "ष"; "s" -> "स"; "h" -> "ह"; "ksh", "x" -> "क्ष"; "jny" -> "ज्ञ"
                "q" -> "क"
                else -> "क"
            }
            AppLanguage.TELUGU -> when (c) {
                "k" -> "క"; "kh" -> "ఖ"; "g" -> "గ"; "gh" -> "ఘ"; "ng" -> "ఙ"
                "ch", "c" -> "చ"; "chh" -> "ఛ"; "j", "z" -> "జ"; "jh" -> "ఝ"; "ny" -> "ఞ"
                "T" -> "ట"; "Th" -> "ఠ"; "D" -> "డ"; "Dh" -> "ఢ"; "N" -> "ణ" // Retroflex series
                "t" -> "త"; "th" -> "థ"; "d" -> "ద"; "dh" -> "ధ"; "n" -> "న" // Dental series
                "p" -> "ప"; "ph", "f" -> "ఫ"; "b" -> "బ"; "bh" -> "భ"; "m" -> "మ"
                "y" -> "య"; "r" -> "ర"; "l" -> "ల"; "L" -> "ళ"; "v", "w" -> "వ"
                "sh" -> "శ"; "shh" -> "ష"; "s" -> "స"; "h" -> "హ"; "ksh", "x" -> "క్ష"; "jny" -> "జ్ఞ"
                "q" -> "క"
                else -> "క"
            }
            AppLanguage.TAMIL -> when (c) {
                "k", "kh", "g", "gh", "q" -> "க"
                "ch", "chh", "c" -> "ச"
                "j", "jh", "z" -> "ஜ"
                "T", "Th", "D", "Dh", "t", "th", "d", "dh" -> "த"
                "N" -> "ண"
                "n" -> "ந"
                "p", "ph", "b", "bh", "f" -> "ப"
                "m" -> "ம"
                "y" -> "ய"
                "r" -> "ர"
                "l" -> "ல"
                "L" -> "ள"
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
