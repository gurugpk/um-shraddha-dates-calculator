package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage

object EkadashiShraddhaRepository {

    fun getGuide(language: AppLanguage): EkadashiShraddhaGuide {
        return when (language) {
            AppLanguage.KANNADA -> EkadashiShraddhaGuide(
                title = "ಏಕಾದಶಿ ದಿನದ ಶ್ರಾದ್ಧ ಹಾಗೂ ಪಕ್ಷ ನಿರ್ಣಯ",
                subtitle = "ಧರ್ಮಶಾಸ್ತ್ರ ಹಾಗೂ ಮಧ್ವ ಸಿದ್ಧಾಂತದ ಶಾಸ್ತ್ರೀಯ ನಿಯಮಗಳು",
                canonicalShloka1 = "ಏಕಾದಶ್ಯಾಂ ಯದಾ ರಾಮ ಶ್ರಾದ್ಧಂ ನೈಮಿತ್ತಿಕಂ ಭವೇತ್ ।\nತದ್ದಿನೇ ತು ಪರಿತ್ಯಜ್ಯ ದ್ವಾದಶ್ಯಾಂ ಶ್ರಾದ್ಧಮಾಚರೇತ್ ॥",
                canonicalShloka1Translit = "Ekādaśyāṁ yadā rāma śrāddhaṁ naimittikaṁ bhavet |\nTaddine tu parityajya dvādaśyāṁ śrāddhamācaret ||",
                canonicalShloka1Meaning = "ಪದ್ಮಪುರಾಣ (ಪುಷ್ಕರ ಖಂಡ) & ನಿರ್ಣಯಸಿಂಧು: ಹೇ ರಾಮ! ವಾರ್ಷಿಕ ಅಥವಾ ಮಾಸಿಕ ಶ್ರಾದ್ಧವು ಏಕಾದಶಿಯ ದಿನ ಬಂದರೆ, ಆ ದಿನ ಅನ್ನಶ್ರಾದ್ಧವನ್ನು ಬಿಟ್ಟು ಮರುದಿನ ದ್ವಾದಶಿಯಂದು ಸಂಪೂರ್ಣ ಶ್ರಾದ್ಧವನ್ನು ಆಚರಿಸಬೇಕು.",
                canonicalShloka2 = "ಉಪವಾಸೋ ಯದಾ ನಿತ್ಯಃ ಶ್ರಾದ್ಧಂ ನೈಮಿತ್ತಿಕಂ ಭವೇತ್ ।\nಉಪವಾಸಂ ತದಾ ಕುರ್ಯಾದಾಘ್ರಾಣಂ ಪಿತೃಸೇವಿತೇ ॥",
                canonicalShloka2Translit = "Upavāso yadā nityaḥ śrāddhaṁ naimittikaṁ bhavet |\nUpavāsaṁ tadā kuryādāghrāṇaṁ pitṛsevite ||",
                canonicalShloka2Meaning = "ಕಾತ್ಯಾಯನ ಸ್ಮೃತಿ & ಧರ್ಮಸಿಂಧು: ನಿತ್ಯ ಉಪವಾಸವಾದ ಏಕಾದಶಿ ವ್ರತದ ದಿನ ನೈಮಿತ್ತಿಕ ಶ್ರಾದ್ಧ ಬಂದಾಗ ಉಪವಾಸವನ್ನೇ ಪಾಲಿಸಬೇಕು. ಹವಿಸ್ಸನ್ನು ಸೇವಿಸದೆ ಕೇವಲ ಆಘ್ರಾಣ (ವಾಸನೆ ಗ್ರಹಣ) ಮಾತ್ರ ಮಾಡಬಹುದು.",
                nityaVsNaimittikaTitle = "೧. ನಿತ್ಯೋಪವಾಸ vs ನೈಮಿತ್ತಿಕ ಅನ್ನಶ್ರಾದ್ಧ",
                nityaVsNaimittikaDesc = "ಶ್ರೀಮದಾಚಾರ್ಯರ ಸದಾಚಾರ ಸ್ಮೃತಿಯ ಪ್ರಕಾರ ಏಕಾದಶಿ ಉಪವಾಸವು ಪರಮ ನಿತ್ಯವ್ರತ. ಏಕಾದಶಿಯಂದು ಅನ್ನ ಮತ್ತು ಧಾನ್ಯ ಸೇವನೆಯು ಮಹಾಪಾತಕ. ಶ್ರಾದ್ಧದಲ್ಲಿ ಬ್ರಾಹ್ಮಣ ಭೋಜನ ಹಾಗೂ ಪಿಂಡಪ್ರದಾನ ಕಡ್ಡಾಯವಾಗಿರುವುದರಿಂದ, ಏಕಾದಶಿಯಂದು ಅನ್ನಶ್ರಾದ್ಧ ಮಾಡುವುದು ಸಂಪೂರ್ಣ ನಿಷಿದ್ಧ.",
                varshikaRuleTitle = "೨. ವಾರ್ಷಿಕ (ಪ್ರತಿವಾರ್ಷಿಕ) ಶ್ರಾದ್ಧ ನಿರ್ಣಯ",
                varshikaRuleDesc = "ಮೃತ ತಿಥಿ ಏಕಾದಶಿಯಾಗಿದ್ದಲ್ಲಿ, ಕುಟುಂಬದವರು ಏಕಾದಶಿಯಂದು ಶ್ರೀ ಹರಿಯ ಉಪವಾಸ, ಪೂಜೆ, ಜಪಗಳನ್ನು ಮಾಡಿ ಮರುದಿನ ದ್ವಾದಶಿಯಂದು (ದ್ವಾದಶ್ಯಾಂ ಶ್ರಾದ್ಧಮಾಚರೇತ್) ಸಂಪೂರ್ಣ ಅನ್ನಶ್ರಾದ್ಧ, ಪಿಂಡಪ್ರದಾನ ಮತ್ತು ಬ್ರಾಹ್ಮಣ ಭೋಜನವನ್ನು ಆಚರಿಸುತ್ತಾರೆ.",
                pakshaRuleTitle = "೩. ಮಹಾಲಯ ಪಕ್ಷ (ಇಂದಿರಾ ಏಕಾದಶಿ) ನಿರ್ಣಯ",
                pakshaRuleDesc = "ಪಿತೃಪಕ್ಷದ ಏಕಾದಶಿಯಂದು (ಇಂದಿರಾ ಏಕಾದಶಿ):\n• ಆಯ್ಕೆ ೧: ಏಕಾದಶಿಯಂದೇ ಆಮಶ್ರಾದ್ಧ (ಹಸಿ ಅಕ್ಕಿ, ಬೇಳೆ, ತರಕಾರಿ, ದಕ್ಷಿಣೆ ದಾನ) ಅಥವಾ ಹಿರಣ್ಯಶ್ರಾದ್ಧ ಮಾಡುವುದು.\n• ಆಯ್ಕೆ ೨: ಪೂರ್ಣ ಅನ್ನಶ್ರಾದ್ಧವನ್ನು ದ್ವಾದಶಿಯಂದು (ಯತಿ ಮಹಾಲಯ ದಿನ) ಅಥವಾ ಸರ್ವಪಿತೃ ಮಹಾಲಯ ಅಮಾವಾಸ್ಯೆಯಂದು ಆಚರಿಸುವುದು.",
                dvadashiParaneTitle = "೪. ದ್ವಾದಶಿ ಪಾರಣೆ ಹಾಗೂ ಹರಿವಾಸರ ಜಾಗರೂಕತೆ",
                dvadashiParaneDesc = "ದ್ವಾದಶಿಯಂದು ಶ್ರಾದ್ಧ ಮಾಡುವಾಗ ದ್ವಾದಶಿ ಘಳಿಗೆ ಅಲ್ಪವಾಗಿದ್ದರೆ (ಅಲ್ಪ ದ್ವಾದಶಿ), ಮೊದಲು ತುಳಸೀ ತೀರ್ಥ ಪ್ರಾಶನ ಮತ್ತು ಪಾರಣಾ ಸಂಕಲ್ಪದ ನಿಯಮಗಳನ್ನು ಮಠದ ಪಂಚಾಂಗದಂತೆ ಪಾಲಿಸಬೇಕು.",
                disclaimerTitle = "ಸೂಚನೆ",
                disclaimerDesc = "ಕುಲಾಚಾರ ಹಾಗೂ ಮಠದ ಪರಂಪರೆಯಂತೆ ಸೂಕ್ಷ್ಮ ಬದಲಾವಣೆಗಳಿರುತ್ತವೆ. ನಿರ್ದಿಷ್ಟ ಆಚರಣೆಗೆ ನಿಮ್ಮ ಕುಲಗುರು / ಮಠದ ಪುರೋಹಿತರ ಮಾರ್ಗದರ್ಶನ ಪಡೆಯಿರಿ."
            )
            AppLanguage.SANSKRIT -> EkadashiShraddhaGuide(
                title = "एकादशीश्राद्ध-पक्षनिर्णयः",
                subtitle = "धर्मशास्त्र-माध्वसिद्धान्तानुसारिणः नियमाः",
                canonicalShloka1 = "एकादश्यां यदा राम श्राद्धं नैमित्तिकं भवेत् ।\nतद्दिने तु परित्यज्य द्वादश्यां श्राद्धमाचरेत् ॥",
                canonicalShloka1Translit = "Ekādaśyāṁ yadā rāma śrāddhaṁ naimittikaṁ bhavet |\nTaddine tu parityajya dvādaśyāṁ śrāddhamācaret ||",
                canonicalShloka1Meaning = "पद्मपुराणे (पुष्करखण्डे) निर्णयसिन्धौ च: हे राम! यदा एकादश्यां नैमित्तिकं श्राद्धं प्राप्नोति, तदा तद्दिने तद्विहाय द्वादश्यामेव सम्पूर्णम् अन्नश्राद्धं कुर्यात्।",
                canonicalShloka2 = "उपवासो यदा नित्यः श्राद्धं नैमित्तिकं भवेत् ।\nउपवासं तदा कुर्यादाघ्राणं पितृसेविते ॥",
                canonicalShloka2Translit = "Upavāso yadā nityaḥ śrāddhaṁ naimittikaṁ bhavet |\nUpavāsaṁ tadā kuryādāghrāṇaṁ pitṛsevite ||",
                canonicalShloka2Meaning = "कात्यायनस्मृतौ: नित्ये उपवासे प्राप्ते नैमित्तिकश्राद्धे च समागते, उपवासमेव रक्षेत्, पितृनिवेदितान्नस्य आघ्राणं मात्रं कुर्यात्।",
                nityaVsNaimittikaTitle = "१. नित्योपवासः vs नैमित्तिकम् अन्नश्राद्धम्",
                nityaVsNaimittikaDesc = "सदाचारस्मृतौ श्रीमदानन्दतीर्थभगवत्पादैः एकादशीव्रतं सर्वथा नित्यमित्युक्तम्। एकादश्याम् अन्नभोजनं महापातकम्। श्राद्धे च ब्राह्मणभोजनं विहितम्, अतः एकादश्यां पक्वान्नश्राद्धं सर्वथा निषिद्धम्।",
                varshikaRuleTitle = "२. वार्षिक (प्रतिवार्षिक) श्राद्धनिर्णयः",
                varshikaRuleDesc = "एकादश्यां मृतानां वार्षिकश्राद्धं द्वादश्यामेव कर्तव्यम् (द्वादश्यां श्राद्धमाचरेत्)। एकादश्यां विष्णुपूजोपवासादिकं कृत्वा श्वोभूते द्वादश्यां पिण्डदान-ब्राह्मणभोजनसहितं श्राद्धं सम्पादनीयम्।",
                pakshaRuleTitle = "३. महालयपक्ष (इन्दिरा एकादशी) निर्णयः",
                pakshaRuleDesc = "पितृपक्षे एकादशीतिथौ:\n• पक्षः १: एकादश्यामेव आमश्राद्धं (अपक्वान्न-दक्षिणादानम्) हिरण्यश्राद्धं वा कुर्यात्।\n• पक्षः २: पक्वान्नश्राद्धं द्वादश्यां (यतिमहालये) अथवा सर्वपितृ-महाParse्वायाम् (अमावास्यायाम्) आचरेत्।",
                dvadashiParaneTitle = "४. द्वादशीपारणं हरिवासरश्च",
                dvadashiParaneDesc = "द्वादश्यां श्राद्धानुष्ठाने अल्पद्वादशीसत्त्वे पञ्चाङ्गविहितकाले पारणं कृत्वा पितृकार्यं समापनीयम्।",
                disclaimerTitle = "सूचना",
                disclaimerDesc = "कुलपरम्परानुसारं मठनियमानुसारं च स्वकुलपुरोहितानां मार्गदर्शनं ग्राह्यम्।"
            )
            AppLanguage.TELUGU -> EkadashiShraddhaGuide(
                title = "ఏకాదశి శ్రాద్ధ & పక్ష నిర్ణయం",
                subtitle = "ధర్మశాస్త్రం మరియు మాధ్వ సిద్ధాంత నియమాలు",
                canonicalShloka1 = "ఏకాదశ్యాం యదా రామ శ్రాద్ధం నైమిత్తికం భవేత్ ।\nతద్దినే తు పరిత్యజ్య ద్వాదశ్యాం శ్రాద్ధమాచరేత్ ॥",
                canonicalShloka1Translit = "Ekādaśyāṁ yadā rāma śrāddhaṁ naimittikaṁ bhavet |\nTaddine tu parityajya dvādaśyāṁ śrāddhamācaret ||",
                canonicalShloka1Meaning = "పద్మపురాణం & నిర్ణయసింధు: ఓ రామా! ఏకాదశి నాడు నైమిత్తిక శ్రాద్ధం వస్తే, ఆ రోజున వదిలి మరుసటి రోజు ద్వాదశి నాడు శ్రాద్ధం ఆచరించాలి.",
                canonicalShloka2 = "ఉపవాసో యదా నిత్యః శ్రాద్ధం నైమిత్తికం భవేత్ ।\nఉపవాసం తదా కుర్యాదాఘ్రాణం పితృసేవితే ॥",
                canonicalShloka2Translit = "Upavāso yadā nityaḥ śrāddhaṁ naimittikaṁ bhavet |\nUpavāsaṁ tadā kuryādāghrāṇaṁ pitṛsevite ||",
                canonicalShloka2Meaning = "కాత్యాయన స్మృతి & ధర్మసింధు: నిత్య ఉపవాసమైన ఏకాదశి నాడు శ్రాద్ధం వస్తే ఉపవాసమే పాటించాలి, పిండ ప్రసాదాన్ని కేవలం ఆఘ్రాణించవచ్చు.",
                nityaVsNaimittikaTitle = "1. నిత్యోపవాసం vs నైమిత్తిక అన్నశ్రాద్ధం",
                nityaVsNaimittikaDesc = "శ్రీమదాచార్యుల సదాచార స్మృతి ప్రకారం ఏకాదశి ఉపవాసం పరమ పవిత్రమైన నిత్య వ్రతం. ఏకాదశి నాడు అన్నం తినడం మహాపాపం. శ్రాద్ధంలో బ్రాహ్మణ భోజనం ప్రధానం కావున, ఏకాదశి నాడు అన్నశ్రాద్ధం నిషిద్ధం.",
                varshikaRuleTitle = "2. వార్షిక (ప్రతివార్షిక) శ్రాద్ధ నిర్ణయం",
                varshikaRuleDesc = "ఏకాదశి నాడు మరణించిన వారికి వార్షిక శ్రాద్ధాన్ని మరుసటి రోజు ద్వాదశి నాడు పిండప్రదాన, బ్రాహ్మణ భోజనాలతో సంపూర్ణంగా నిర్వహించాలి.",
                pakshaRuleTitle = "3. మహాలయ పక్ష (ఇందిరా ఏకాదశి) నిర్ణయం",
                pakshaRuleDesc = "పితృపక్షంలో ఏకాదశి నాడు:\n• విధానం 1: ఆమశ్రాద్ధం (బియ్యం, పప్పులు, దక్షిణ దానం) లేదా హిరణ్యశ్రాద్ధం చేయడం.\n• విధానం 2: అన్నశ్రాద్ధాన్ని ద్వాదశి నాడు లేదా సర్వపితృ అమావాస్య నాడు నిర్వహించడం.",
                dvadashiParaneTitle = "4. ద్వాదశి పారణ సమయం",
                dvadashiParaneDesc = "ద్వాదశి నాడు శ్రాద్ధం చేసేటప్పుడు అల్ప ద్వాదశి నియమాలను మరియు పారణా సమయాలను మఠ పంచాంగం ప్రకారం గమనించాలి.",
                disclaimerTitle = "గమనిక",
                disclaimerDesc = "మీ కులాచారం మరియు మఠ సంప్రదాయం ప్రకారం మీ పురోహితుల సలహా తీసుకోండి."
            )
            AppLanguage.TAMIL -> EkadashiShraddhaGuide(
                title = "ஏகாதசி சிராத்தம் & பக்ஷ நிர்ணயம்",
                subtitle = "தர்ம சாஸ்திரம் மற்றும் மாத்வ சம்பிரதாய விதிகள்",
                canonicalShloka1 = "ஏகாதச்யாம் யதா ராம ச்ராத்தம் நைமித்திகம் பவேத் ।\nதத்தினே து பரித்யஜ்ய த்வாதச்யாம் ச்ராத்தமாசரேத் ॥",
                canonicalShloka1Translit = "Ekādaśyāṁ yadā rāma śrāddhaṁ naimittikaṁ bhavet |\nTaddine tu parityajya dvādaśyāṁ śrāddhamācaret ||",
                canonicalShloka1Meaning = "பத்ம புராணம் & நிர்ணய சிந்து: ஏகாதசியில் நைமித்திக சிராத்தம் வந்தால், அன்றைய தினம் அன்ன சிராத்தத்தை விடுத்து மறுநாள் துவாதசியில் சிராத்தம் செய்ய வேண்டும்.",
                canonicalShloka2 = "உபவாஸோ யதா நித்யஃ ச்ராத்தம் நைமித்திகம் பவேத் ।\nஉபவாஸம் ததா குர்யாதாக்ராணம் பித்ருஸேவிதே ॥",
                canonicalShloka2Translit = "Upavāso yadā nityaḥ śrāddhaṁ naimittikaṁ bhavet |\nUpavāsaṁ tadā kuryādāghrāṇaṁ pitṛsevite ||",
                canonicalShloka2Meaning = "காத்யாயன ஸ்மிருதி: ஏகாதசி உபவாசம் நித்திய கடமை; சிராத்தம் நைமித்திகம். எனவே உபவாசத்தை விடாமல் காக்க வேண்டும்.",
                nityaVsNaimittikaTitle = "1. ஏகாதசி உபவாசம் vs அன்ன சிராத்தம்",
                nityaVsNaimittikaDesc = "ஸ்ரீ மத்வாச்சாரியாரின் சதாசார ஸ்மிருதிப்படி ஏகாதசி விரதம் மிக முக்கியமானது. ஏகாதசியில் தானியம் உண்பது பாவம். சிராத்தத்தில் பிராமண போஜனம் அவசியமென்பதால் ஏகாதசியில் அன்ன சிராத்தம் செய்வது நிஷித்தம்.",
                varshikaRuleTitle = "2. வருடாந்திர சிராத்த நிர்ணயம்",
                varshikaRuleDesc = "ஏகாதசியில் மறைந்தவர்களுக்கு வருடாந்திர சிராத்தத்தை மறுநாள் துவாதசியில் பிண்ட தானம் மற்றும் பிராமண போஜனத்துடன் செய்ய வேண்டும்.",
                pakshaRuleTitle = "3. மஹாலய பக்ஷம் (இந்திரா ஏகாதசி)",
                pakshaRuleDesc = "பித்ரு பக்ஷ ஏகாதசியில்:\n• முறை 1: ஆம சிராத்தம் (பச்சரிசி, பருப்பு தானம்) அல்லது ஹிரண்ய சிராத்தம் செய்தல்.\n• முறை 2: அன்ன சிராத்தத்தை துவாதசி அல்லது மஹாலய அமாவாசையன்று செய்தல்.",
                dvadashiParaneTitle = "4. துவாதசி பாரணை நேரம்",
                dvadashiParaneDesc = "துவாதசியன்று சிராத்தம் செய்யும் போது பாரணை நேரங்களை பஞ்சாங்கப்படி கவனிக்க வேண்டும்.",
                disclaimerTitle = "குறிப்பு",
                disclaimerDesc = "குடும்ப ஆசாரப்படி உங்கள் குரு அல்லது புரோஹிதரின் வழிகாட்டுதலைப் பெறவும்."
            )
            AppLanguage.ENGLISH -> EkadashiShraddhaGuide(
                title = "Ekadashi Shraddha & Paksha Shastric Injunctions",
                subtitle = "Canonical Guidelines from Dharma Sindhu, Nirnaya Sindhu & Madhwa Siddhanta",
                canonicalShloka1 = "एकादश्यां यदा राम श्राद्धं नैमित्तिकं भवेत् ।\nतद्दिने तु परित्यज्य द्वादश्यां श्राद्धमाचरेत् ॥",
                canonicalShloka1Translit = "Ekādaśyāṁ yadā rāma śrāddhaṁ naimittikaṁ bhavet |\nTaddine tu parityajya dvādaśyāṁ śrāddhamācaret ||",
                canonicalShloka1Meaning = "Padma Purana (Pushkara Khanda) & Nirnaya Sindhu: O Rama! Whenever an occasional (Naimittika) ancestral rite falls on Ekadashi, one must forgo it on that day and perform the full Shraddha on the following Dvadashi.",
                canonicalShloka2 = "उपवासो यदा नित्यः श्राद्धं नैमित्तिकं भवेत् ।\nउपवासं तदा कुर्यादाघ्राणं पितृसेविते ॥",
                canonicalShloka2Translit = "Upavāso yadā nityaḥ śrāddhaṁ naimittikaṁ bhavet |\nUpavāsaṁ tadā kuryādāghrāṇaṁ pitṛsevite ||",
                canonicalShloka2Meaning = "Katyayana Smriti & Dharma Sindhu: When fasting is an eternal obligation (Nitya Vrata on Ekadashi) and the Shraddha is occasional (Naimittika), one must maintain the fast. If any offering is made on that day, one only smells the consecrated offering without ingestion.",
                nityaVsNaimittikaTitle = "1. Nitya Ekadashi Upavasa vs Naimittika Anna-Shraddha",
                nityaVsNaimittikaDesc = "In Sri Madhvacharya's Sadachara Smriti and Krishnamruta Maharnava, the Ekadashi fast is paramount. Consuming grains/rice on Ekadashi is strictly prohibited for both the Kartru and the invited Brahmanas. Because Shraddha requires Brahmana Bhojana with cooked food, Anna-Shraddha cannot be conducted on Ekadashi.",
                varshikaRuleTitle = "2. Annual (Varshika) Shraddha Determination",
                varshikaRuleDesc = "When the demise tithi is Ekadashi, the family observes complete fasting, Vishnu Pooja, and Japa on Ekadashi day. The full Anna-Shraddha with Pinda-pradana and Brahmana Bhojana is performed on the following Dvadashi (Dvādaśyāṁ śrāddhamācaret).",
                pakshaRuleTitle = "3. Mahalaya Paksha (Indira Ekadashi) Options",
                pakshaRuleDesc = "During Pitru Paksha on Indira Ekadashi:\n• Option A: Perform Āma-Śrāddha (donating uncooked rice, lentils, vegetables, and dakshina) or Hiraṇya-Śrāddha on Ekadashi itself.\n• Option B: Perform the full Anna-Shraddha on Dvadashi (coinciding with Yati Mahalaya) or on Sarvapitru Amavasya.",
                dvadashiParaneTitle = "4. Dvadashi Parane & Harivasara Timing",
                dvadashiParaneDesc = "When performing Shraddha on Dvadashi, special care is taken regarding Dvadashi Parane timings. If Dvadashi is very short (Alpa-Dvadashi), morning Parane rituals are observed per Matha Panchanga guidelines.",
                disclaimerTitle = "Traditional Advisory",
                disclaimerDesc = "Practices may have subtle variations across family lineages. Always verify specific ritual schedules with your family Acharya / Purohit."
            )
        }
    }
}
