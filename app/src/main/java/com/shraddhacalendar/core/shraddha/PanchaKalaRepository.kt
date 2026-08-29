package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import java.time.LocalDate

object PanchaKalaRepository {

    fun getPanchaKalas(
        date: LocalDate,
        location: GeoLocation,
        language: AppLanguage
    ): List<PanchaKalaItem> {
        val windows = DinmanaCalculator.calculatePanchaKalaWindows(date, location)

        return VedicKala.entries.map { kala ->
            val window = windows[kala] ?: Pair(java.time.LocalTime.of(6, 0), java.time.LocalTime.of(8, 24))
            val details = getKalaText(kala, language)
            PanchaKalaItem(
                kala = kala,
                name = details.name,
                divisionLabel = details.divisionLabel,
                startTime = window.first,
                endTime = window.second,
                shlokaNativeScript = details.shlokaNative,
                shlokaTransliteration = details.shlokaTranslit,
                meaning = details.meaning,
                prescribedDuties = details.prescribed,
                prohibitedDuties = details.prohibited,
                isSacredAncestralWindow = (kala == VedicKala.APARAHNA)
            )
        }
    }

    private data class KalaTextBundle(
        val name: String,
        val divisionLabel: String,
        val shlokaNative: String,
        val shlokaTranslit: String,
        val meaning: String,
        val prescribed: List<String>,
        val prohibited: List<String>
    )

    private fun getKalaText(kala: VedicKala, language: AppLanguage): KalaTextBundle {
        return when (kala) {
            VedicKala.PRATAH -> KalaTextBundle(
                name = when (language) {
                    AppLanguage.KANNADA -> "ಪ್ರಾತಃಕಾಲ (ಮುಂಜಾನೆ)"
                    AppLanguage.SANSKRIT -> "प्रातःकालः (उषःकालः)"
                    AppLanguage.TELUGU -> "ప్రాతఃకాలం (ఉదయకాలం)"
                    AppLanguage.TAMIL -> "பிராதஃகாலம் (அதிகாலை)"
                    AppLanguage.ENGLISH -> "Prātah Kāla (Early Morning)"
                },
                divisionLabel = when (language) {
                    AppLanguage.KANNADA -> "೧ನೇ ಭಾಗ (ದಿನಮಾನದ ೧/೫)"
                    AppLanguage.SANSKRIT -> "प्रथमः भागः (दिनमानस्य १/५)"
                    AppLanguage.TELUGU -> "1వ భాగం (దినమానంలో 1/5)"
                    AppLanguage.TAMIL -> "1ஆம் பிரிவு (1/5 பங்கு)"
                    AppLanguage.ENGLISH -> "1st Division (1/5th of Day)"
                },
                shlokaNative = when (language) {
                    AppLanguage.KANNADA -> "ಬ್ರಾಹ್ಮೇ ಮುಹೂರ್ತೇ ಬುಧ್ಯೇತ ಧರ್ಮಾರ್ಥೌ ಚಾನುಚಿನ್ತಯೇತ್ ।\nಪ್ರಾತಃ ಸ್ನಾತ್ವಾ ಶುಚಿರ್ಭೂತ್ವಾ ಸನ್ಧ್ಯೋಪಾಸನಮಾಚರೇತ್ ॥"
                    AppLanguage.SANSKRIT -> "ब्राह्मे मुहूर्ते बुध्येत धर्मार्थौ चानुचिन्तयेत् ।\nप्रातः स्नात्वा शुचिर्भूत्वा सन्ध्योपासनमाचरेत् ॥"
                    AppLanguage.TELUGU -> "బ్రాహ్మే ముహూర్తే బుధ్యేత ధర్మార్థౌ చానుచిన్తయేత్ ।\nప్రాతః స్నాత్వా శుచిర్భూత్వా సన్ధ్యోపాసనమాచరేత్ ॥"
                    AppLanguage.TAMIL -> "ப்ராஹ்மே முஹூர்தே புத்யேத தர்மார்தௌ சானுசிந்தயேத் ।\nப்ராதஃ ஸ்நாத்வா சுசிர்பூத்வா ஸந்த்யோபாஸனமாசரேத் ॥"
                    AppLanguage.ENGLISH -> "ब्राह्मे मुहूर्ते बुध्येत धर्मार्थौ चानुचिन्तयेत् ।\nप्रातः स्नात्वा शुचिर्भूत्वा सन्ध्योपासनमाचरेत् ॥"
                },
                shlokaTranslit = "Brāhme muhūrte budhyeta dharmārthau cānucintayet |\nPrātaḥ snātvā śucirbhūtvā sandhyopāsanamācaret ||",
                meaning = when (language) {
                    AppLanguage.KANNADA -> "ಬ್ರಾಹ್ಮೀ ಮುಹೂರ್ತದಲ್ಲಿ ಎದ್ದು ಧರ್ಮ-ಕರ್ತವ್ಯಗಳನ್ನು ಚಿಂತಿಸಬೇಕು. ಪ್ರಾತಃಕಾಲದಲ್ಲಿ ಪವಿತ್ರ ಸ್ನಾನ ಮಾಡಿ ಶುಚಿರ್ಭೂತರಾಗಿ ಪ್ರಾತಃ ಸಂಧ್ಯಾವಂದನೆ ಮತ್ತು ಗಾಯತ್ರೀ ಜಪವನ್ನು ಆಚರಿಸಬೇಕು."
                    AppLanguage.SANSKRIT -> "ब्राह्मे मुहूर्ते उत्थाय धर्मं चिन्तयेत्। प्रातः स्नानेन शुचिः सन् सन्ध्योपासनं गायत्रीजपं च समाचरेत्।"
                    AppLanguage.TELUGU -> "బ్రాహ్మీ ముహూర్తంలో మేల్కొని ధర్మాన్ని చింతించాలి. ఉదయమే పవిత్ర స్నానం చేసి శుచిగా సంధ్యావందనం మరియు గాయత్రీ జపం ఆచరించాలి."
                    AppLanguage.TAMIL -> "பிரம்ம முகூர்த்தத்தில் எழுந்து தர்மத்தை சிந்திக்க வேண்டும். அதிகாலை புனித நீராடி தூய்மையடைந்து சந்தியாவந்தனம் மற்றும் காயத்ரி ஜபம் செய்ய வேண்டும்."
                    AppLanguage.ENGLISH -> "One should awaken in the sacred Brahma Muhurta and contemplate righteousness and truth. Bathing early in the morning and attaining purity, one must perform morning Sandhyavandana and sacred prayers."
                },
                prescribed = when (language) {
                    AppLanguage.KANNADA -> listOf("ಪ್ರಾತಃ ಸ್ನಾನ ಮತ್ತು ಶೌಚ", "ಪ್ರಾತಃ ಸಂಧ್ಯಾವಂದನೆ ಮತ್ತು ಗಾಯತ್ರೀ ಜಪ", "ಅಗ್ನಿಹೋತ್ರ / ಔಪಾಸನ ಹೋಮ", "ಶ್ರೀಹರಿ, ವಾಯು, ಗುರುಗಳ ಸ್ಮರಣೆ")
                    AppLanguage.SANSKRIT -> listOf("प्रातःस्नानं शौचं च", "प्रातःसन्ध्यावन्दनं गायत्रीजपः", "अग्निहोत्रम् / औपासनम्", "श्रीहरि-वायु-गुरुस्मरणम्")
                    AppLanguage.TELUGU -> listOf("ప్రాతః స్నానం మరియు శౌచం", "ప్రాతః సంధ్యావందనం మరియు గాయత్రీ జపం", "అగ్నిహోత్రం / ఔపాసనం", "శ్రీహరి, వాయు, గురు స్మరణ")
                    AppLanguage.TAMIL -> listOf("அதிகாலை நீராடல் & தூய்மை", "பிராதஃ சந்தியாவந்தனம் & காயத்ரி ஜபம்", "அக்னிஹோத்ரம் / ஔபாசனம்", "ஸ்ரீஹரி, வாயு, குரு ஸ்மரணம்")
                    AppLanguage.ENGLISH -> listOf("Prātah Snāna (Morning purification bath)", "Prātah Sandhyāvandana & Gāyatrī Japa", "Prātah Agnihotra / Aupāsana fire offerings", "Smarana of Sri Hari, Sri Vayu, Guru Parampara")
                },
                prohibited = when (language) {
                    AppLanguage.KANNADA -> listOf("ಶ್ರಾದ್ಧ, ಪಿಂಡದಾನ ಅಥವಾ ತರ್ಪಣ ಕರ್ಮಗಳು ಸಂಪೂರ್ಣ ನಿಷಿದ್ಧ.")
                    AppLanguage.SANSKRIT -> listOf("श्राद्धं पिण्डदानं तर्पणं च सर्वथा निषिद्धम्।")
                    AppLanguage.TELUGU -> listOf("శ్రాద్ధం, పిండదానం లేదా తర్పణం పూర్తిగా నిషిద్ధం.")
                    AppLanguage.TAMIL -> listOf("சிராத்தம், பிண்டதானம் அல்லது தர்பணம் முற்றிலும் நிஷித்தமாகும்.")
                    AppLanguage.ENGLISH -> listOf("No Shraddha, Pinda Dana, or ancestral Tarpana is permitted.")
                }
            )

            VedicKala.SANGAVA -> KalaTextBundle(
                name = when (language) {
                    AppLanguage.KANNADA -> "ಸಂಗವ ಕಾಲ (ಪೂರ್ವಾಹ್ನ / ಅಧ್ಯಯನ)"
                    AppLanguage.SANSKRIT -> "सङ्गवकालः (वेदाध्ययनम्)"
                    AppLanguage.TELUGU -> "సంగవ కాలం (వేదాధ్యయనం)"
                    AppLanguage.TAMIL -> "சங்கவ காலம் (வேத பாராயணம்)"
                    AppLanguage.ENGLISH -> "Saṅgava Kāla (Forenoon Study)"
                },
                divisionLabel = when (language) {
                    AppLanguage.KANNADA -> "೨ನೇ ಭಾಗ (ದಿನಮಾನದ ೨/೫)"
                    AppLanguage.SANSKRIT -> "द्वितीयः भागः (दिनमानस्य २/೫)"
                    AppLanguage.TELUGU -> "2వ భాగం (దినమానంలో 2/5)"
                    AppLanguage.TAMIL -> "2ஆம் பிரிவு (2/5 பங்கு)"
                    AppLanguage.ENGLISH -> "2nd Division (2/5th of Day)"
                },
                shlokaNative = when (language) {
                    AppLanguage.KANNADA -> "ಸಙ್ಗವೇ ವೇದಶಾಸ್ತ್ರಾಣಿ ವಿಚಾರ್ಯಾಣಿ ಪ್ರಯತ್ನತಃ ।\nಗೋಸೇವಾ ದೇವಕಾರ್ಯಾರ್ಥಂ ಕುಶತುಲಸ್ಯಾದಿಸಞ್ಚಯಃ ॥"
                    AppLanguage.SANSKRIT -> "सङ्गवे वेदशास्त्राणि विचार्याणि प्रयत्नतः ।\nगोसेवा देवकार्यार्थं कुशतुलस्यादिसञ्चयः ॥"
                    AppLanguage.TELUGU -> "సఙ్గవే వేదశాస్త్రాణి విచార్యాణి ప్రయత్నతః ।\nగోసేవా దేవకార్యార్థం కుశతులస్యాదిసఞ్చయః ॥"
                    AppLanguage.TAMIL -> "ஸங்கவே வேதசாஸ்த்ராணி விசாரியாணி ப்ரயத்நதஃ ।\nகோஸேவா தேவகார்யார்தம் குசதுலஸ்யாதிஸஞ்சயஃ ॥"
                    AppLanguage.ENGLISH -> "सङ्गवे वेदशास्त्राणि विचार्याणि प्रयत्नतः ।\nगोसेवा देवकार्यार्थं कुशतुलस्यादिसञ्चयः ॥"
                },
                shlokaTranslit = "Saṅgave vedaśāstrāṇi vicāryāṇi prayatnataḥ |\nGosevā devakāryārthaṁ kuśatulasīsañcayaḥ ||",
                meaning = when (language) {
                    AppLanguage.KANNADA -> "ಸಂಗವ ಕಾಲದಲ್ಲಿ ವೇದಾಧ್ಯಯನ, ಸತ್ಶಾಸ್ತ್ರ ವಿಚಾರ, ಗೋಸೇವೆ ಮಾಡಬೇಕು ಮತ್ತು ಮಧ್ಯಾಹ್ನದ ಪೂಜೆಗೆ ಬೇಕಾದ ತುಳಸೀ, ಪುಷ್ಪ, ದರ್ಭೆ (ಕುಶ) ಮತ್ತು ಪವಿತ್ರ ಜಲವನ್ನು ಸಂಗ್ರಹಿಸಬೇಕು."
                    AppLanguage.SANSKRIT -> "सङ्गवे वेदाध्ययनं शास्त्रचिन्तनं गोसेवा च कर्तव्या। देवपूजार्थं तुलसी-पुष्प-कुश-जलादिकं सञ्चिनुयात्।"
                    AppLanguage.TELUGU -> "సంగవ కాలంలో వేదాధ్యయనం, శాస్త్ర విచారణ, గోసేవ చేయాలి మరియు మధ్యాహ్న పూజకు అవసరమైన తులసి, పుష్పాలు, దర్భలు, పవిత్ర జలం సేకరించాలి."
                    AppLanguage.TAMIL -> "சங்கவ காலத்தில் வேத பாராயணம், சாஸ்திர சிந்தனை, பசு சேவை செய்ய வேண்டும். மதிய பூஜைக்கு தேவையான துளசி, புஷ்பம், தர்பை, தீர்த்தம் சேகரிக்க வேண்டும்."
                    AppLanguage.ENGLISH -> "During Sangava Kala, one should study the Vedas and sacred scriptures, serve sacred cows (Go-Seva), and gather holy items (Tulasi, flowers, sacred Kusha grass, and water) for midday worship."
                },
                prescribed = when (language) {
                    AppLanguage.KANNADA -> listOf("ವೇದಾಧ್ಯಯನ ಮತ್ತು ಶಾಸ್ತ್ರ ಪಾಠ", "ಗೋಸೇವೆ ಮತ್ತು ಗೋಪೂಜೆ", "ದೇವಪೂಜೆಗೆ ತುಳಸೀ, ಪುಷ್ಪ, ದರ್ಭೆ ಸಂಗ್ರಹ", "ನಾಂದೀ / ವೃದ್ಧಿ ಶ್ರಾದ್ಧ (ವಿವಾಹ, ಉಪನಯನ ಶುಭ ಸಂದರ್ಭಗಳಲ್ಲಿ ಮಾತ್ರ)")
                    AppLanguage.SANSKRIT -> listOf("वेदाध्ययनं शास्त्रपाठः", "गोसेवा गोपालनं च", "पूजार्थं तुलसी-पुष्प-कुशसञ्चयः", "नान्दी/वृद्धि-श्राद्धम् (केवलं मङ्गलप्रसङ्गेषु)")
                    AppLanguage.TELUGU -> listOf("వేదాధ్యయనం మరియు శాస్త్ర పఠనం", "గోసేవ మరియు గోపూజ", "పూజార్థం తులసి, పుష్పాలు, దర్భల సేకరణ", "నాందీ / వృద్ధి శ్రాద్ధం (వివాహం వంటి శుభ కార్యాలలో మాత్రమే)")
                    AppLanguage.TAMIL -> listOf("வேத பாராயணம் & சாஸ்திர பாடம்", "பசு சேவை & கோபூஜை", "பூஜைக்கு துளசி, புஷ்பம், தர்பை சேகரித்தல்", "நாந்தீ / விருத்தி சிராத்தம் (சுப காரியங்களில் மட்டும்)")
                    AppLanguage.ENGLISH -> listOf("Vedādhyayana (Vedic chanting and scriptural study)", "Go-Sevā (Tending to sacred cows)", "Gathering Tulasi, flowers, Kusha grass, and pure water", "Nāndī / Vṛddhi Śrāddha (only on auspicious family events like Vivaha or Upanayana)")
                },
                prohibited = when (language) {
                    AppLanguage.KANNADA -> listOf("ಸಾಮಾನ್ಯ ಪಿತೃ ಶ್ರಾದ್ಧ, ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ ಮತ್ತು ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ ನಿಷಿದ್ಧ.")
                    AppLanguage.SANSKRIT -> listOf("वार्षिकश्राद्धं महालयश्राद्धं च निषिद्धम्।")
                    AppLanguage.TELUGU -> listOf("సాధారణ వార్షిక శ్రాద్ధం మరియు మహాలయ శ్రాద్ధం నిషిద్ధం.")
                    AppLanguage.TAMIL -> listOf("வழக்கமான வருடாந்திர சிராத்தம் மற்றும் மஹாளய சிராத்தம் நிஷித்தம்.")
                    AppLanguage.ENGLISH -> listOf("Regular Varshika and Mahalaya Pitru Shraddhas are prohibited.")
                }
            )

            VedicKala.MADHYAHNA -> KalaTextBundle(
                name = when (language) {
                    AppLanguage.KANNADA -> "ಮಧ್ಯಾಹ್ನ ಕಾಲ ಮತ್ತು ಕುತಪ ಮುಹೂರ್ತ"
                    AppLanguage.SANSKRIT -> "मध्याह्नकालः तथा कुतपमुहूर्तः"
                    AppLanguage.TELUGU -> "మధ్యాహ్న కాలం మరియు కుతప ముహూర్తం"
                    AppLanguage.TAMIL -> "மத்யாஹ்ன காலம் & குதப முகூர்த்தம்"
                    AppLanguage.ENGLISH -> "Madhyāhna Kāla & Kutapa Muhūrta"
                },
                divisionLabel = when (language) {
                    AppLanguage.KANNADA -> "೩ನೇ ಭಾಗ (ದಿನಮಾನದ ೩/೫)"
                    AppLanguage.SANSKRIT -> "तृतीयः भागः (दिनमानस्य ३/५)"
                    AppLanguage.TELUGU -> "3వ భాగం (దినమానంలో 3/5)"
                    AppLanguage.TAMIL -> "3ஆம் பிரிவு (3/5 பங்கு)"
                    AppLanguage.ENGLISH -> "3rd Division (3/5th of Day)"
                },
                shlokaNative = when (language) {
                    AppLanguage.KANNADA -> "ಮಧ್ಯಾಹ್ನೇ ಸ್ನಾನಮಾಚರ್ಯ ದೇವಪೂಜಾಂ ಸಮಾಚರೇತ್ ।\nಅಷ್ಟಮೋ ಮುಹೂರ್ತಃ ಕುತಪಃ ಶ್ರಾದ್ಧಾರಂಭೇ ಪ್ರಶಸ್ಯತೇ ॥"
                    AppLanguage.SANSKRIT -> "मध्याह्ने स्नानमाचर्य देवपूजां समाचरेत् ।\nअष्टमो मुहूर्तः कुतपः श्राद्धारम्भे प्रशस्यते ॥"
                    AppLanguage.TELUGU -> "మధ్యాహ్నే స్నానమాచర్య దేవపూజాం సమాచరేత్ ।\nఅష్టమో ముహూర్తః కుతపః శ్రాద్ధారమ్భే ప్రశస్యతే ॥"
                    AppLanguage.TAMIL -> "மத்யாஹ்நே ஸ்நாநமாசர்ய தேவபூஜாம் ஸமாசரேத் ।\nஅஷ்டமோ முஹூர்தஃ குதபஃ ச்ராத்தாரம்பே ப்ரசஸ்யதே ॥"
                    AppLanguage.ENGLISH -> "मध्याह्ने स्नानमाचर्य देवपूजां समाचरेत् ।\nअष्टमो मुहूर्तः कुतपः श्राद्धारम्भे प्रशस्यते ॥"
                },
                shlokaTranslit = "Madhyāhne snānamācarya devapūjāṁ samācaret |\nAṣṭamo muhūrtaḥ kutapaḥ śrāddhārambhe praśasyate ||",
                meaning = when (language) {
                    AppLanguage.KANNADA -> "ಮಧ್ಯಾಹ್ನ ಕಾಲದಲ್ಲಿ ಮಾಧ್ಯಾಹ್ನಿಕ ಸ್ನಾನ, ಸಂಧ್ಯಾವಂದನೆ, ದೇವರ ಮಹಾಪೂಜೆ, ನೈವೇದ್ಯ ಮತ್ತು ವೈಶ್ವದೇವ ಕರ್ಮಗಳನ್ನು ಮಾಡಬೇಕು. ದಿನದ ೮ನೇ ಮುಹೂರ್ತವಾದ ಕುತಪ ಮುಹೂರ್ತವು ಶ್ರಾದ್ಧದ ಅಡುಗೆ ಮತ್ತು ಪ್ರಾಥಮಿಕ ಸಂಕಲ್ಪವನ್ನು ಆರಂಭಿಸಲು ಅತ್ಯಂತ ಪ್ರಶಸ್ತ."
                    AppLanguage.SANSKRIT -> "मध्याह्ने स्नान-सन्ध्या-देवपूजा-नैवेद्य-वैश्वदेवादिकं कुर्यात्। दिनस्याष्टमो मुहूर्तः कुतपनामा श्राद्धारम्भाय परमप्रशस्तः।"
                    AppLanguage.TELUGU -> "మధ్యాహ్న కాలంలో స్నానం, సంధ్యావందనం, దేవతా మహా పూజ, నైవేద్యం, వైశ్వదేవం ఆచరించాలి. 8వ ముహూర్తమైన కుతప ముహూర్తం శ్రాద్ధ పాకం మరియు సంకల్పం ప్రారంభించడానికి అత్యంత ప్రశస్తమైనది."
                    AppLanguage.TAMIL -> "மதிய வேளையில் நீராடி சந்தியாவந்தனம், தேவ பூஜை, நைவேத்யம், வைச்வதேவம் செய்ய வேண்டும். 8வது முகூர்த்தமான குதப முகூர்த்தம் சிராத்த சமையல் மற்றும் சங்கல்பத்திற்கு உகந்தது."
                    AppLanguage.ENGLISH -> "In Madhyahna, one performs midday bath, Sandhyavandana, Maha Deva Puja (Saligrama archana & Naivedya), Brahma Yajna, and Vaishvadeva. The 8th Muhurta, Kutapa Muhurta, is the sacred time when Shraddha cooking and initial Sankalpa commence."
                },
                prescribed = when (language) {
                    AppLanguage.KANNADA -> listOf("ಮಾಧ್ಯಾಹ್ನಿಕ ಸ್ನಾನ ಮತ್ತು ಸಂಧ್ಯಾವಂದನೆ", "ಸಾಲಿಗ್ರಾಮ ಮಹಾಪೂಜೆ, ಅಭಿಷೇಕ, ಮಹಾನೈವೇದ್ಯ", "ಬ್ರಹ್ಮಯಜ್ಞ ಮತ್ತು ದೇವ-ಋಷಿ ತರ್ಪಣ", "ವೈಶ್ವದೇವ ಮತ್ತು ಬಲಿಹರಣ", "ಕುತಪ ಮುಹೂರ್ತದಲ್ಲಿ ಶ್ರಾದ್ಧದ ಅಡುಗೆ ಮತ್ತು ಸಂಕಲ್ಪ ಆರಂಭ")
                    AppLanguage.SANSKRIT -> listOf("माध्याह्निकस्नानं सन्ध्यावन्दनं च", "सालिग्रामपूजा नैवेद्यं च", "ब्रह्मयज्ञः देवर्षितर्पणं च", "वैश्वदेवः बलिहरणं च", "कुतपमुहूर्ते श्राद्धारम्भः")
                    AppLanguage.TELUGU -> listOf("మాధ్యాహ్నిక స్నానం మరియు సంధ్యావందనం", "సాలగ్రామ పూజ, అభిషేకం, మహానైవేద్యం", "బ్రహ్మయజ్ఞం మరియు దేవ-ఋషి తర్పణం", "వైశ్వదేవం మరియు బలిహరణం", "కుతప ముహూర్తంలో శ్రాద్ధ పాకం మరియు సంకల్పం")
                    AppLanguage.TAMIL -> listOf("மாத்யாஹ்னிக நீராடல் & சந்தியாவந்தனம்", "சாளக்கிராம பூஜை, அபிஷேகம், நைவேத்யம்", "பிரம்மயக்ஞம் & தேவ-ரிஷி தர்பணம்", "வைச்வதேவம்", "குதப முகூர்த்தத்தில் சிராத்த சமையல் & சங்கல்பம்")
                    AppLanguage.ENGLISH -> listOf("Madhyāhnika Snāna & Sandhyāvandana", "Maha Deva Puja (Śāligrāma archana & Naivedya)", "Brahma Yajna & regular Deva-Rishi Tarpana", "Vaiśvadeva & Bali-harana", "Kutapa Muhūrta: Shraddha cooking and Sankalpa commencement")
                },
                prohibited = when (language) {
                    AppLanguage.KANNADA -> listOf("ಪಿಂಡದಾನ ಮತ್ತು ಬ್ರಾಹ್ಮಣ ಭೋಜನವನ್ನು ಅಪರಾಹ್ನ ಕಾಲ ಬರುವ ಮುಂಚೆ ಮಾಡಬಾರದು.")
                    AppLanguage.SANSKRIT -> listOf("अपराह्नात् पूर्वं पिण्डदानं ब्राह्मणभोजनं च न कार्यम्।")
                    AppLanguage.TELUGU -> listOf("అపరాహ్ణ కాలం రాకముందు పిండదానం మరియు బ్రాహ్మణ భోజనం చేయకూడదు.")
                    AppLanguage.TAMIL -> listOf("அபராஹ்ண காலத்திற்கு முன்பாக பிண்டதானம் மற்றும் பிராமண போஜனம் செய்யக்கூடாது.")
                    AppLanguage.ENGLISH -> listOf("Final Pinda Dana and Brahmana Bhojana must wait until Aparahna Kala begins.")
                }
            )

            VedicKala.APARAHNA -> KalaTextBundle(
                name = when (language) {
                    AppLanguage.KANNADA -> "ಅಪರಾಹ್ನ ಕಾಲ (ಪವಿತ್ರ ಪಿತೃ ಶ್ರಾದ್ಧ ಕಾಲ)"
                    AppLanguage.SANSKRIT -> "अपराह्नकालः (परमपवित्रः पितृश्राद्धकालः)"
                    AppLanguage.TELUGU -> "అపరాహ్ణ కాలం (పవిత్ర పితృ శ్రాద్ధ కాలం)"
                    AppLanguage.TAMIL -> "அபராஹ்ண காலம் (புனித பித்ரு சிராத்த காலம்)"
                    AppLanguage.ENGLISH -> "Aparāhna Kāla (The Sacred Ancestral Window)"
                },
                divisionLabel = when (language) {
                    AppLanguage.KANNADA -> "೪ನೇ ಭಾಗ (ದಿನಮಾನದ ೪/೫ - ಮುಖ್ಯ ಶ್ರಾದ್ಧ ಕಾಲ)"
                    AppLanguage.SANSKRIT -> "चतुर्थः भागः (दिनमानस्य ४/೫ - मुख्यश्राद्धकालः)"
                    AppLanguage.TELUGU -> "4వ భాగం (దినమానంలో 4/5 - ప్రధాన శ్రాద్ధ కాలం)"
                    AppLanguage.TAMIL -> "4ஆம் பிரிவு (4/5 பங்கு - முதன்மை சிராத்த காலம்)"
                    AppLanguage.ENGLISH -> "4th Division (4/5th of Day - Exclusive Shraddha Window)"
                },
                shlokaNative = when (language) {
                    AppLanguage.KANNADA -> "ಪೂರ್ವಾಹ್ಣೇ ದೈವಿಕಂ ಕಾರ್ಯಮಪರಾಹ್ಣೇ ತು ಪೈತೃಕಮ್ ।\nಪ್ರಾತಃಕಾಲೇ ಕೃತೇ ಶ್ರಾದ್ಧೇ ರಾಕ್ಷಸೈರ್ಭುಜ್ಯತೇ ಧ್ರುವಮ್ ॥\nಅಪರಾಹ್ಣೋ ಹಿ ಪಿತೄಣಾಂ ಸ್ವಯಂಭುವಾ ವಿನಿರ್ಮಿತಃ ॥"
                    AppLanguage.SANSKRIT -> "पूर्वाह्णे दैविकं कार्यमपराह्णे तु पैतृकम् ।\nप्रातःकाले कृते श्राद्धे राक्षसैर्भुज्यते ध्रुवम् ॥\nअपराह्णो हि पितॄणां स्वयम्भुवा विनिर्मितः ॥"
                    AppLanguage.TELUGU -> "పూర్వాహ్ణే దైవికం కార్యమపరాహ్ణే తు పైతృకమ్ ।\nప్రాతఃకాలే కృతే శ్రాద్ధే రాక్షసైర్భుజ్యతే ధ్రువమ్ ॥\nఅపరాహ్ణో హి పితౄణాం స్వయమ్భువా వినిర్మితః ॥"
                    AppLanguage.TAMIL -> "பூர்வாஹ்ணே தைவிகம் கார்யமபராஹ்ணே து பைத்ருகம் ।\nப்ராதஃகாலே க்ருதே ச்ராத்தே ராக்ஷஸைர்புஜ்யதே த்ருவம் ॥\nஅபராஹ்ணோ ஹி பித்ரூணாம் ஸ்வயம்புவா விநிர்மிதஃ ॥"
                    AppLanguage.ENGLISH -> "पूर्वाह्णे दैविकं कार्यमपराह्णे तु पैतृकम् ।\nप्रातःकाले कृते श्राद्धे राक्षसैर्भुಜ್ಯते ध्रुवम् ॥\nअपराह्णो हि पितॄणां स्वयम्भुवा विनिर्मितः ॥"
                },
                shlokaTranslit = "Pūrvāhṇe daivikaṁ kāryamaparāhṇe tu paitṛkam |\nPrātaḥkāle kṛte śrāddhe rākṣasairbhujyate dhruvam ||\nAparāhṇo hi pitṝṇāṁ svayambhuvā vinirmitaḥ ||",
                meaning = when (language) {
                    AppLanguage.KANNADA -> "ಬೆಳಗಿನ ಜಾವ ದೇವರ ಪೂಜೆಗೆ ಮೀಸಲು; ಅಪರಾಹ್ನ ಕಾಲವು ಪಿತೃಕಾರ್ಯಕ್ಕೆ ಅತ್ಯಂತ ಶ್ರೇಷ್ಠ ಹಾಗೂ ಕಡ್ಡಾಯ. ಪ್ರಾತಃಕಾಲದಲ್ಲಿ ಶ್ರಾದ್ಧ ಮಾಡಿದರೆ ಅದು ಪಿತೃಗಳಿಗೆ ತಲುಪದೆ ರಾಕ್ಷಸರ ಪಾಲಾಗುತ್ತದೆ. ಸ್ವಯಂ ಶ್ರೀಹರಿಯೇ ಅಪರಾಹ್ನ ಕಾಲವನ್ನು ಪಿತೃಗಳಿಗಾಗಿ ನಿರ್ಮಿಸಿದ್ದಾನೆ."
                    AppLanguage.SANSKRIT -> "पूर्वाह्णे देवकार्यं कुर्यात्, अपराह्णे तु पितृकार्यम्। प्रातःकाले कृतं श्राद्धं राक्षसैरेव भुज्यते। भगवान् स्वयमेव पितृभ्यः अपराह्नकालं व्यदधात्।"
                    AppLanguage.TELUGU -> "ఉదయం దేవతా కార్యాలకు, అపరాహ్ణ కాలం పితృకార్యాలకు నిర్దేశించబడింది. ప్రాతఃకాలంలో శ్రాద్ధం చేస్తే అది పితృదేవతలకు చేరకుండా రాక్షస పాలవుతుంది. స్వయంగా శ్రీహరే అపరాహ్ణాన్ని పితృదేవతల కొరకు నిర్మించాడు."
                    AppLanguage.TAMIL -> "காலையில் தெய்வ காரியங்களும், மதியம் அபராஹ்ண காலத்தில் பித்ரு காரியங்களும் செய்யப்பட வேண்டும். காலையில் சிராத்தம் செய்தால் அது அரக்கர்களால் அபகரிக்கப்படும். இறைவனே அபராஹ்ணத்தை பித்ருக்களுக்காக அமைத்துள்ளார்."
                    AppLanguage.ENGLISH -> "Divine worship belongs to the morning; ancestral rites (Pitru Shraddha) belong exclusively to Aparahna. Shraddha performed in the morning is seized by Rakshasas and does not reach ancestors. The Supreme Lord Himself created Aparahna specifically for the Pitrus."
                },
                prescribed = when (language) {
                    AppLanguage.KANNADA -> listOf("ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (ಪ್ರತ್ಯಾಬ್ದಿಕ ಶ್ರಾದ್ಧ)", "ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ", "೧೬ ಪ್ರಥಮ ವರ್ಷದ ಮಾಸಿಕ ಶ್ರಾದ್ಧಗಳು", "ಪಿಂಡದಾನ ಮತ್ತು ಬ್ರಾಹ್ಮಣ ಭೋಜನ", "ಪಿತೃ ತರ್ಪಣ ಮತ್ತು ತಿಲತರ್ಪಣ")
                    AppLanguage.SANSKRIT -> listOf("वार्षिकश्राद्धम् (प्रत्याब्दिकश्राद्धम्)", "महालयपक्षश्राद्धम्", "षोडश-मासिकश्राद्धानि", "पिण्डदानं ब्राह्मणभोजनं च", "पितृतर्पणं तिलतर्पणं च")
                    AppLanguage.TELUGU -> listOf("వార్షిక శ్రాద్ధం (ప్రత్యాబ్దిక శ్రాద్ధం)", "మహాలయ పక్ష శ్రాద్ధం", "16 ప్రథమ సంవత్సర మాసిక శ్రాద్ధాలు", "పిండప్రదానం మరియు బ్రాహ్మణ భోజనం", "పితృ తర్పణం మరియు తిలతర్పణం")
                    AppLanguage.TAMIL -> listOf("வருடாந்திர சிராத்தம் (பிரத்யாப்திகம்)", "மஹாளய பக்ஷ சிராத்தம்", "16 மாத மாஸிக சிராத்தங்கள்", "பிண்டதானம் & பிராமண போஜனம்", "பித்ரு தர்பணம் & திலதர்பணம்")
                    AppLanguage.ENGLISH -> listOf("Vārṣika Śrāddha (Annual death anniversaries)", "Mahālaya / Pitru Pakṣa Śrāddha", "16 First-Year Māsika Śrāddhas & Pārvana rites", "Piṇḍa Dāna and Brāhmaṇa Bhojana", "Pitru Tarpaṇa & Tila Tarpaṇa")
                },
                prohibited = when (language) {
                    AppLanguage.KANNADA -> listOf("ಈ ಪವಿತ್ರ ಕಾಲವನ್ನು ವ್ಯರ್ಥ ಮಾಡುವುದು ಅಥವಾ ಶ್ರಾದ್ಧವನ್ನು ಸಾಯಾಹ್ನಕ್ಕೆ ಮುಂದೂಡುವುದು ನಿಷಿದ್ಧ.")
                    AppLanguage.SANSKRIT -> listOf("श्राद्धकर्म सायाह्ने न कुर्यात्।")
                    AppLanguage.TELUGU -> listOf("శ్రాద్ధ కర్మను సాయంకాలానికి వాయిదా వేయడం నిషిద్ధం.")
                    AppLanguage.TAMIL -> listOf("சிராத்தத்தை மாலை வேளைக்கு தள்ளிப்போடுவது நிஷித்தமாகும்.")
                    AppLanguage.ENGLISH -> listOf("Postponing Shraddha to Sayahna (sunset) or night is strictly prohibited.")
                }
            )

            VedicKala.SAYAHNA -> KalaTextBundle(
                name = when (language) {
                    AppLanguage.KANNADA -> "ಸಾಯಾಹ್ನ ಕಾಲ (ಸಂಧ್ಯಾಕಾಲ / ಸೂರ್ಯಾಸ್ತ)"
                    AppLanguage.SANSKRIT -> "सायाह्नकालः (सन्ध्यासमयः / सूर्यास्तः)"
                    AppLanguage.TELUGU -> "సాయాహ్న కాలం (సంధ్యా సమయం / సూర్యాస్తమయం)"
                    AppLanguage.TAMIL -> "சாயாஹ்ன காலம் (மாலை சந்தியா / சூரியாஸ்தமனம்)"
                    AppLanguage.ENGLISH -> "Sāyāhna Kāla (Late Afternoon to Sunset)"
                },
                divisionLabel = when (language) {
                    AppLanguage.KANNADA -> "೫ನೇ ಭಾಗ (ದಿನಮಾನದ ೫/೫ - ಮುಕ್ತಾಯ)"
                    AppLanguage.SANSKRIT -> "पञ्चमः भागः (दिनमानस्य ५/೫ - उपसंहारः)"
                    AppLanguage.TELUGU -> "5వ భాగం (దినమానంలో 5/5 - సమాప్తి)"
                    AppLanguage.TAMIL -> "5ஆம் பிரிவு (5/5 பங்கு - நிறைவு)"
                    AppLanguage.ENGLISH -> "5th Division (5/5th of Day - Closure)"
                },
                shlokaNative = when (language) {
                    AppLanguage.KANNADA -> "ಸಾಯಂಸನ್ಧ್ಯಾಂ ತತೋ ಧ್ಯಾಯೇದ್ದೀಪಪ್ರಜ್ವಲನಂ ತಥಾ ।\nಪುರಾಣಶ್ರವಣಂ ಕುರ್ಯಾನ್ನ ಶ್ರಾದ್ಧಂ ಸಾಯಮರ್ಚಯೇತ್ ॥"
                    AppLanguage.SANSKRIT -> "सायंसन्ध्यां ततो ध्यायेद्दीपप्रज्वलनं तथा ।\nपुराणश्रवणं कुर्यान्न श्राद्धं सायमर्चयेत् ॥"
                    AppLanguage.TELUGU -> "సాయంసంధ్యాం తతో ధ్యాయేద్దీపప్రజ్వలనం తథా ।\nపురాణశ్రవణం కుర్యాన్న శ్రాద్ధం సాయమర్చయేత్ ॥"
                    AppLanguage.TAMIL -> "ஸாயம்ஸந்த்யாம் ததோ த்யாயேத்தீபப்ரஜ்வலனம் ததா ।\nபுராணச்ரவணம் குர்யான்ன ச்ராத்தம் ஸாயமர்சயேத் ॥"
                    AppLanguage.ENGLISH -> "सायंसन्ध्यां ततो ध्यायेद्दीपप्रज्वलनं तथा ।\nपुराणश्रवणं कुर्यान्न श्राद्धं सायमर्चयेत् ॥"
                },
                shlokaTranslit = "Sāyaṁsandhyāṁ tato dhyāyeddīpaprajvalanaṁ tathā |\nPurāṇaśravaṇaṁ kuryānna śrāddhaṁ sāyamarcayet ||",
                meaning = when (language) {
                    AppLanguage.KANNADA -> "ಸೂರ್ಯಾಸ್ತ ಸಮಯದಲ್ಲಿ ಸಾಯಂ ಸಂಧ್ಯಾವಂದನೆ, ಗಾಯತ್ರೀ ಜಪ, ದೀಪಾರಾಧನೆ ಮಾಡಬೇಕು ಮತ್ತು ಪುರಾಣ ಶ್ರವಣ-ಹರಿಕಥೆಗಳಲ್ಲಿ ತೊಡಗಬೇಕು. ಸಾಯಾಹ್ನ ಕಾಲದಲ್ಲಿ ಯಾವುದೇ ಶ್ರಾದ್ಧ ಅಥವಾ ಹೋಮಗಳನ್ನು ಮಾಡಬಾರದು."
                    AppLanguage.SANSKRIT -> "सायंकाले सायंसन्ध्यावन्दनं दीपप्रज्वलनं पुराणश्रवणं च कुर्यात्। सायाह्ने रात्रौ च श्राद्धं नैव कुर्यात्।"
                    AppLanguage.TELUGU -> "సూర్యాస్తమయ సమయంలో సాయం సంధ్యావందనం, దీపారాధన, పురాణ శ్రవణం చేయాలి. సాయంకాలంలో శ్రాద్ధం లేదా హోమాలు చేయరాదు."
                    AppLanguage.TAMIL -> "சூரியாஸ்தமன வேளையில் மாலை சந்தியாவந்தனம், தீபமேற்றுதல், புராண பாராயணம் செய்ய வேண்டும். மாலை அல்லது இரவில் சிராத்தம் செய்யக்கூடாது."
                    AppLanguage.ENGLISH -> "At dusk, one should contemplate the Divine through Sayam Sandhyavandana, light sacred lamps (Deeparadhana), and listen to Puranas/Harikatha. No Shraddha or heavy rituals are permitted in Sayahna."
                },
                prescribed = when (language) {
                    AppLanguage.KANNADA -> listOf("ಸಾಯಂ ಸ್ನಾನ ಮತ್ತು ಸಾಯಂ ಸಂಧ್ಯಾವಂದನೆ", "ಗಾಯತ್ರೀ ಜಪ (ಸೂರ್ಯಾಸ್ತದ ಮುನ್ನ)", "ದೀಪ ಪ್ರಜ್ವಲನೆ ಮತ್ತು ತುಳಸೀ ಪೂಜೆ", "ಪುರಾಣ ಶ್ರವಣ, ಭಜನೆ, ಹರಿಕಥೆ")
                    AppLanguage.SANSKRIT -> listOf("सायंस्नानं सायंसन्ध्यावन्दनं च", "गायत्रीजपः", "दीपप्रज्वलनं तुलसीपूजा च", "पुराणश्रवणं हरिकीर्तनं च")
                    AppLanguage.TELUGU -> listOf("సాయం స్నానం మరియు సాయం సంధ్యావందనం", "గాయత్రీ జపం", "దీపారాధన మరియు తులసి పూజ", "పురాణ శ్రవణం, భజన, హరికథ")
                    AppLanguage.TAMIL -> listOf("மாலை நீராடல் & சந்தியாவந்தனம்", "காயத்ரி ஜபம்", "தீபம் ஏற்றுதல் & துளசி பூஜை", "புராண பாராயணம் & பஜனை")
                    AppLanguage.ENGLISH -> listOf("Sāyaṁ Snāna & Sāyaṁ Sandhyāvandana", "Gāyatrī Japa at dusk", "Lighting evening lamps (Dīpārādhana)", "Listening to Purāṇas, Itihāsas, and Harikathā")
                },
                prohibited = when (language) {
                    AppLanguage.KANNADA -> listOf("ಶ್ರಾದ್ಧ ಕರ್ಮಗಳು, ಪಿಂಡದಾನ ಅಥವಾ ತರ್ಪಣ ಸಂಪೂರ್ಣ ನಿಷಿದ್ಧ.")
                    AppLanguage.SANSKRIT -> listOf("श्राद्धं पिण्डदानं च सर्वथा निषिद्धम्।")
                    AppLanguage.TELUGU -> listOf("శ్రాద్ధం, పిండదానం లేదా తర్పణం పూర్తిగా నిషిద్ధం.")
                    AppLanguage.TAMIL -> listOf("சிராத்தம், பிண்டதானம் முற்றிலும் நிஷித்தமாகும்.")
                    AppLanguage.ENGLISH -> listOf("No Shraddha, Pinda Dana, or Tarpana is permitted during sunset or night.")
                }
            )
        }
    }

    fun getKartruDevaPujaGuide(language: AppLanguage): KartruDevaPujaGuide {
        val shlokaNative = when (language) {
            AppLanguage.KANNADA -> "ಶ್ರಾದ್ಧದಿನೇ ಕರ್ತುಃ ಪ್ರಾತಃ ದೇವಪೂಜಾನಿಷೇಧಃ ।\nಶ್ರಾದ್ಧಶೇಷೇಣೈವ ದೇವಪೂಜಾಂ ಕುರ್ಯಾತ್ । ಅಥವಾ ಅನ್ಯೇನ ಕಾರಯೇತ್ ॥"
            AppLanguage.SANSKRIT -> "श्राद्धदिने कर्तुः प्रातः देवपूजानिषेधः ।\nश्राद्धशेषेणैव देवपूजां कुर्यात् । अथवा अन्येन कारयेत् ॥"
            AppLanguage.TELUGU -> "శ్రాద్ధదినే కర్తుః ప్రాతః దేవపూజానిషేధః ।\nశ్రాద్ధశేషేణైవ దేవపూజాం కుర్యాత్ । లేదా అన్యేన కారయేత్ ॥"
            AppLanguage.TAMIL -> "ச்ராத்ததினே கர்த்துஃ ப்ராதஃ தேவபூஜானிஷேதஃ ।\nச்ராத்தசேஷேணைவ தேவபூஜாம் குர்யாத் । அதவா அன்யேன காரயேத் ॥"
            AppLanguage.ENGLISH -> "श्राद्धदिने कर्तुः प्रातः देवपूजानिषेधः ।\nश्राद्धशेषेणैव देवपूजां कुर्यात् । अथवा अन्येन कारयेत् ॥"
        }

        val shlokaTranslit = "Śrāddhadine kartuḥ prātaḥ devapūjāniṣedhaḥ |\nŚrāddhaśeṣeṇaiva devapūjāṁ kuryāt | Athavā anyena kārayet ||"

        val shlokaMeaning = when (language) {
            AppLanguage.KANNADA -> "(ಧರ್ಮಸಿಂಧು, ನಿರ್ಣಯಸಿಂಧು, ಸ್ಮೃತಿ ಮುಕ್ತಾವಳಿ): ಶ್ರಾದ್ಧ ದಿನದಂದು ಕರ್ತೃವಿಗೆ ಮುಂಜಾನೆಯ ಸ್ವತಂತ್ರ ದೇವಪೂಜೆ ನಿಷಿದ್ಧ. ಶ್ರಾದ್ಧದ ಅಡುಗೆಯನ್ನೇ ನೈವೇದ್ಯ ಮಾಡಿ ಪೂಜಿಸಬೇಕು, ಅಥವಾ ಕುಟುಂಬದ ಇತರರಿಂದ ದೇವಪೂಜೆ ಮಾಡಿಸಬೇಕು."
            AppLanguage.SANSKRIT -> "(धर्मसिन्धुः, निर्णयसिन्धुः, स्मृतिमक्तावली): श्राद्धदिने कर्तुः पृथक् प्रातर्देवपूजा न विहिता। श्राद्धपाकेनैव देवपूजा कर्तव्या अथवा अन्येन कुटुम्बिना कारयितव्या।"
            AppLanguage.TELUGU -> "(ధర్మసింధు, నిర్ణయసింధు, స్మృతి ముక్తావళి): శ్రాద్ధ దినాన కర్త ఉదయం స్వతంత్ర దేవపూజ చేయరాదు. శ్రాద్ధ పాకంతోనే దేవునికి పూజ చేయాలి, లేదా కుటుంబంలోని ఇతరులతో చేయించాలి."
            AppLanguage.TAMIL -> "(தர்ம சிந்து, நிர்ணய சிந்து): சிராத்த நாளில் கர்த்தா காலையில் தனி பூஜை செய்யக்கூடாது. சிராத்த சமையலையே நைவேத்யம் செய்ய வேண்டும் அல்லது மற்றவரைக் கொண்டு செய்விக்க வேண்டும்."
            AppLanguage.ENGLISH -> "(Dharma Sindhu, Nirnaya Sindhu, Smriti Muktavali): On a Shraddha day, the Kartru is prohibited from performing independent morning Deva Puja. Worship is done using the sanctified Shraddha meal, or performed by another eligible family member."
        }

        val intro = when (language) {
            AppLanguage.KANNADA -> "ವೈದಿಕ ಧರ್ಮ ಮತ್ತು ಮುಖ್ಯವಾಗಿ ಮಾಧ್ವ ಸಿದ್ಧಾಂತದಲ್ಲಿ ಪ್ರತಿಯೊಂದು ಕರ್ಮವೂ ಶ್ರೀಹರಿಯ ಸಮರ್ಪಣೆಯೇ. ಆದರೆ ತಂದೆ/ತಾಯಿ ಅಥವಾ ಪಿತೃಗಳ ಶ್ರಾದ್ಧ ದಿನದಂದು ಕರ್ತೃವು ವಿಶೇಷ ಶ್ರಾದ್ಧ ದೀಕ್ಷೆಯಲ್ಲಿ ಇರುವುದರಿಂದ ಶಾಸ್ತ್ರವು ಕರ್ತೃವಿನ ಪೂಜಾ ವಿಧಿಯನ್ನು ಮಾರ್ಪಡಿಸಿದೆ:"
            AppLanguage.SANSKRIT -> "वैदिकधर्मे मध्वसिद्धान्ते च सर्वं कर्म श्रीहरि-समर्पणमेव। किन्तु पितृश्राद्धदिने कर्ता विशेषदीक्षायां वर्तते, अतः शास्त्रेण कर्तुः पूजाविधिः एवं नियमितः:"
            AppLanguage.TELUGU -> "వైదిక ధర్మంలో మరియు మాధ్వ సిద్ధాంతంలో ప్రతి కర్మ శ్రీహరి సమర్పణమే. అయితే శ్రాద్ధ దినాన కర్త ప్రత్యేక శ్రాద్ధ దీక్షలో ఉంటాడు, కాబట్టి శాస్త్రం కర్త యొక్క పూజా విధానాన్ని ఇలా నిర్దేశించింది:"
            AppLanguage.TAMIL -> "வைதிக தர்மத்திலும் மாத்வ சித்தாந்தத்திலும் அனைத்து கர்மங்களும் ஸ்ரீஹரி அர்ப்பணமே. ஆனால் சிராத்த நாளில் கர்த்தா சிறப்பு தீக்ஷையில் இருப்பதால் சாஸ்திரம் பூஜை விதியை இவ்வாறு அமைத்துள்ளது:"
            AppLanguage.ENGLISH -> "In Vedic Dharma and especially within Madhwa Siddhānta, every ritual is an offering to Supreme Lord Śrī Hari. On a parent's or ancestor's death anniversary, the Kartru enters a sacred state of austerity (Śrāddha Dīkṣā / Niyama), for which Shastras prescribe a specific worship discipline:"
        }

        val rationales = listOf(
            PujaRationaleItem(
                key = "fasting",
                title = when (language) {
                    AppLanguage.KANNADA -> "೧. ಕಟ್ಟುನಿಟ್ಟಾದ ನಿರಾಹಾರ ಉಪವಾಸ ನಿಯಮ (ಶ್ರಾದ್ಧೋಪವಾಸ)"
                    AppLanguage.SANSKRIT -> "१. निर्जल-निराहारोपवास-नियमः (श्राद्धोपवासः)"
                    AppLanguage.TELUGU -> "1. కఠిన నిరాహార ఉపవాస నియమం (శ్రాద్ధోపవాసం)"
                    AppLanguage.TAMIL -> "1. கடுமையான விரத நியமம் (சிராத்தோபவாசம்)"
                    AppLanguage.ENGLISH -> "1. Strict Fasting & Purity Niyama (Śrāddha Upavāsa)"
                },
                subtitle = when (language) {
                    AppLanguage.KANNADA -> "ಪಿತೃಗಳ ಭೋಜನಕ್ಕೆ ಮುಂಚೆ ತೀರ್ಥ-ಪ್ರಸಾದ ನಿಷಿದ್ಧ"
                    AppLanguage.SANSKRIT -> "पितृभोजनात् पूर्वं तीर्थप्रसादस्वीकारनिषेधः"
                    AppLanguage.TELUGU -> "పితృ భోజనానికి ముందు తీర్థప్రసాద నిషేధం"
                    AppLanguage.TAMIL -> "பித்ரு போஜனத்திற்கு முன் பிரசாதம் உட்கொள்ள தடை"
                    AppLanguage.ENGLISH -> "No Consumption of Prasada/Teertha Prior to Ancestral Feast"
                },
                description = when (language) {
                    AppLanguage.KANNADA -> "ಶ್ರಾದ್ಧ ಮುಗಿದು ಭೋಕ್ತೃ ಬ್ರಾಹ್ಮಣರು ಭೋಜನ ಮಾಡುವವರೆಗೆ ಕರ್ತೃವು ಸಂಪೂರ್ಣ ನಿರಾಹಾರವಾಗಿರಬೇಕು. ಬೆಳಿಗ್ಗೆ ಪ್ರತ್ಯೇಕ ಮಹಾಪೂಜೆ ಮಾಡಿದರೆ ಮಹಾನೈವೇದ್ಯ ಪ್ರಸಾದ ಹಾಗೂ ತೀರ್ಥ ಸ್ವೀಕರಿಸಬೇಕಾಗುತ್ತದೆ, ಇದು ಶ್ರಾದ್ಧದ ಮೂಲ ನಿಯಮವನ್ನು ಭಂಗಪಡಿಸುತ್ತದೆ."
                    AppLanguage.SANSKRIT -> "श्राद्धसमाप्तिपर्यन्तं भोक्तृब्राह्मणभोजनात् पूर्वं कर्ता निराहारः तिष्ठेत्। प्रातः पृथक् पूजायां कृतायां नैवेद्य-तीर्थस्वीकारः अनिवार्यः स्यात्, येन श्राद्धनियमभङ्गो भवेत्।"
                    AppLanguage.TELUGU -> "శ్రాద్ధం ముగిసి బ్రాహ్మణ భోజనం జరిగే వరకు కర్త పూర్తిగా నిరాహారంగా ఉండాలి. ఉదయం ప్రత్యేక పూజ చేస్తే నైవేద్య ప్రసాదం, తీర్థం స్వీకరించాల్సి వస్తుంది, ఇది శ్రాద్ధ నియమాన్ని ఉల్లంఘించడమే."
                    AppLanguage.TAMIL -> "சிராத்தம் முடிந்து பிராமண போஜனம் வரை கர்த்தா விரதம் இருக்க வேண்டும். காலையில் தனி பூஜை செய்தால் பிரசாதம் உட்கொள்ள நேரிடும், இது சிராத்த விரதத்திற்கு எதிரானது."
                    AppLanguage.ENGLISH -> "The Kartru must remain strictly fasting (Nirāhāra) from sunrise until the Śrāddha is completed and the Bhoktru Brāhmaṇas have eaten. Performing an elaborate morning Mahā Pūjā necessitates consuming Mahā Naivedya Prasāda and Tīrtha, violating foundational Śrāddha Niyama."
                }
            ),
            PujaRationaleItem(
                key = "antaryami",
                title = when (language) {
                    AppLanguage.KANNADA -> "೨. ಪಿತೃಗಳಲ್ಲಿ ಅಂತರ್ಯಾಮಿಯಾದ ಶ್ರೀ ಜನಾರ್ದನ ವಾಸುದೇವ"
                    AppLanguage.SANSKRIT -> "२. पितृ-अन्तर्यामी श्रीजनार्दनवासुदेवः"
                    AppLanguage.TELUGU -> "2. పితృలలో అంతర్యామి అయిన శ్రీ జనార్దన వాసుదేవుడు"
                    AppLanguage.TAMIL -> "2. பித்ருக்களின் அந்தர்யாமி ஸ்ரீ ஜனார்தன வாசுதேவன்"
                    AppLanguage.ENGLISH -> "2. Śrī Hari as the Inner Receiver (Antaryāmī) of Pitṛs"
                },
                subtitle = when (language) {
                    AppLanguage.KANNADA -> "\"ಪಿತೃ-ಅಂತರ್ಗತ-ಭಾರತೀರಮಣಮುಖ್ಯಪ್ರಾಣಾಂತರ್ಗತ-ಶ್ರೀಜನಾರ್ದನವಾಸುದೇವಪ್ರೀತ್ಯರ್ಥಮ್\""
                    AppLanguage.SANSKRIT -> "\"पितृ-अन्तर्गत-भारतीरमणमुख्यप्राणान्तर्गत-श्रीजनार्दनवासुदेवप्रीत्यर्थम्\""
                    AppLanguage.TELUGU -> "\"పితృ-అంతర్గత-భారతీరమణముఖ్యప్రాణాంతర్గత-శ్రీజనార్దనవాసుదేవప్రీత్యర్థమ్\""
                    AppLanguage.TAMIL -> "\"பித்ரு-அந்தர்கத-பாரதீரமணமுக்யப்ராணாந்தர்கத-ஸ்ரீஜனார்தனவாஸுதேவப்ரீத்யர்தம்\""
                    AppLanguage.ENGLISH -> "\"Pitṛ-Antargata-Bhāratīramaṇa-Mukhyaprāṇāntargata-Śrī Janārdana Vāsudeva-Prītyartham\""
                },
                description = when (language) {
                    AppLanguage.KANNADA -> "ಮಾಧ್ವ ಸಿದ್ಧಾಂತದಂತೆ ಶ್ರಾದ್ಧದಲ್ಲಿ ಅರ್ಪಿಸುವ ಹವ್ಯ-ಕವ್ಯಗಳು ಸಾಕ್ಷಾತ್ ಪಿತೃ-ಅಂತರ್ಯಾಮಿ ಶ್ರೀ ಜನಾರ್ದನ ವಾಸುದೇವನಿಗೇ ಸಲ್ಲುವ ಮಹಾನೈವೇದ್ಯ. ಆದ್ದರಿಂದ ಶ್ರಾದ್ಧಕ್ಕೆ ಮುನ್ನ ಪುನಃ ಪ್ರತ್ಯೇಕ ನೈವೇದ್ಯ ಮಾಡುವುದು ಪುನರುಕ್ತಿಯಾಗುತ್ತದೆ."
                    AppLanguage.SANSKRIT -> "श्राद्धे समर्पितं हव्य-कव्यं साक्षात् पितृणामन्तर्यामिणे श्रीजनार्दनवासुदेवायैव समर्प्यते। अतः श्राद्धात् पूर्वं पृथङ्नैवेद्यसमर्पणं न युज्यते।"
                    AppLanguage.TELUGU -> "మాధ్వ సిద్ధాంతం ప్రకారం శ్రాద్ధంలో సమర్పించే హవిస్సు సాక్షాత్తు పితృ అంతర్యామి అయిన శ్రీ జనార్దన వాసుదేవునికే చెందుతుంది. కాబట్టి ముందుగానే వేరే నైవేద్యం పెట్టడం సబబు కాదు."
                    AppLanguage.TAMIL -> "சிராத்தத்தில் அளிக்கப்படும் உணவு பித்ருக்களின் அந்தர்யாமியான ஸ்ரீ ஜனார்தன வாசுதேவனுக்கே நைவேத்யமாகிறது. எனவே முன்னதாக தனி நைவேத்யம் செய்வது தேவையற்றது."
                    AppLanguage.ENGLISH -> "In Madhwa philosophy, ancestral worship is an offering to Śrī Janārdana Vāsudeva indwelling the Pitrus. The entire Śrāddha meal (Havya-Kavya) is fundamentally a grand Naivedya to the Lord indwelling the ancestors."
                }
            ),
            PujaRationaleItem(
                key = "madi",
                title = when (language) {
                    AppLanguage.KANNADA -> "೩. ಅಡುಗೆಮನೆ ಮತ್ತು ಶ್ರಾದ್ಧ ಪಾಕದ ಪರಮ ಪಾವಿತ್ರ್ಯ (ಮಡಿ)"
                    AppLanguage.SANSKRIT -> "३. श्राद्धपाकशालायाः परमपवित्रता (मडी-नियमः)"
                    AppLanguage.TELUGU -> "3. శ్రాద్ధ పాకశాల పరమ పవిత్రత (మడి నియమం)"
                    AppLanguage.TAMIL -> "3. சிராத்த சமையலறையின் தூய்மை (மடி நியமம்)"
                    AppLanguage.ENGLISH -> "3. Kitchen & Cooking Sanctity (Madi / Pavitratā)"
                },
                subtitle = when (language) {
                    AppLanguage.KANNADA -> "ಶ್ರಾದ್ಧಕ್ಕೆ ನಿಷಿದ್ಧವಾದ ಸಾಸಿವೆ, ಇಂಗು, ಈರುಳ್ಳಿ ರಹಿತ ಶುದ್ಧ ಪಾಕ"
                    AppLanguage.SANSKRIT -> "हव्य-कव्यार्थं निषिद्धद्रव्यरहितः शुद्धपाकः"
                    AppLanguage.TELUGU -> "శ్రాద్ధానికి నిషిద్ధమైన పదార్థాలు లేని శుద్ధ పాకం"
                    AppLanguage.TAMIL -> "சிராத்தத்திற்கு நிஷித்தமான பொருட்கள் இல்லாத தூய சமையல்"
                    AppLanguage.ENGLISH -> "Dedicated strictly to Havya-Kavya using prescribed ingredients only"
                },
                description = when (language) {
                    AppLanguage.KANNADA -> "ಶ್ರಾದ್ಧ ದಿನದಂದು ಅಡುಗೆಮನೆಯು ಕಟ್ಟುನಿಟ್ಟಾದ ಮಡಿಗೆ ಒಳಪಟ್ಟಿರುತ್ತದೆ (ಎಳ್ಳು, ತುಪ್ಪ, ಬಾಳೆಕಾಯಿ ಮುಂತಾದ ಶಾಸ್ತ್ರೋಕ್ತ ದ್ರವ್ಯಗಳು ಮಾತ್ರ). ಸಾಮಾನ್ಯ ಮುಂಜಾನೆಯ ಅಡುಗೆಯು ಶ್ರಾದ್ಧದ ಪಾಕಕ್ಕಿಂತ ಮುಂಚೆ ಆಗಬಾರದು."
                    AppLanguage.SANSKRIT -> "श्राद्धदिने पाकशाला केवलं विहितद्रव्यैः (तिल-घृत-कदल्यादिभिः) युक्ता भवति। श्राद्धपाकात् पूर्वं लौकिकपाको निषिद्धः।"
                    AppLanguage.TELUGU -> "శ్రాద్ధ దినాన పాకశాల కఠినమైన మడి నియమాలతో ఉంటుంది (నువ్వులు, నెయ్యి వంటి శాస్త్రోక్త ద్రవ్యాలు మాత్రమే). శ్రాద్ధ పాకానికి ముందు సాధారణ వంట చేయరాదు."
                    AppLanguage.TAMIL -> "சிராத்த சமையலறை மிகுந்த மடியுடன் பராமரிக்கப்படுகிறது (எள், நெய் போன்ற சாஸ்திர சமையல் மட்டுமே). சிராத்த சமையலுக்கு முன் சாதாரண சமையல் செய்யக்கூடாது."
                    AppLanguage.ENGLISH -> "On a Śrāddha day, the kitchen is dedicated exclusively to the sanctified meal under strict Madi rules (using prescribed ingredients like sesame, cow's ghee; strictly avoiding mustard, onions, asafoetida). Regular cooking cannot precede Śrāddha cooking."
                }
            )
        )

        val options = listOf(
            PujaOptionItem(
                optionNumber = 1,
                title = when (language) {
                    AppLanguage.KANNADA -> "ಅನ್ಯೇನ ಕಾರಯೇತ್ (ಅತ್ಯಂತ ಪ್ರಶಸ್ತವಾದ ಕುಟುಂಬ ಪದ್ಧತಿ)"
                    AppLanguage.SANSKRIT -> "अन्येन कारयेत् (श्रेष्ठः कौटुम्बिकः पक्षः)"
                    AppLanguage.TELUGU -> "అన్యేన కారయేత్ (ఉత్తమ కుటుంబ పద్ధతి)"
                    AppLanguage.TAMIL -> "அன்யேன காரயேத் (சிறந்த குடும்ப முறை)"
                    AppLanguage.ENGLISH -> "Option 1: Anyena Kārayet (Recommended Family Practice)"
                },
                ruleSubtitle = when (language) {
                    AppLanguage.KANNADA -> "ಕರ್ತೃವಲ್ಲದ ಇತರ ಕುಟುಂಬ ಸದಸ್ಯರಿಂದ ಅಥವಾ ಆಚಾರ್ಯರಿಂದ ಪೂಜೆ"
                    AppLanguage.SANSKRIT -> "कर्तृव्यतिरिक्तेन अन्येन कुटुम्बिना पुरोहितेन वा पूजा"
                    AppLanguage.TELUGU -> "కర్త కాని ఇతర కుటుంబసభ్యులు లేదా పురోహితులతో పూజ"
                    AppLanguage.TAMIL -> "கர்த்தா அல்லாத குடும்பத்தினர் அல்லது சாஸ்திரிகளால் பூஜை"
                    AppLanguage.ENGLISH -> "Worship performed by non-Kartru family member or priest"
                },
                practicalPractice = when (language) {
                    AppLanguage.KANNADA -> "ಕರ್ತೃವಲ್ಲದ ಅಣ್ಣ-ತಮ್ಮ, ಮಗ, ಅಥವಾ ಕುಟುಂಬದ ಹಿರಿಯರು/ಪುರೋಹಿತರು ಬೆಳಿಗ್ಗೆ ಸಾಲಿಗ್ರಾಮ ಪೂಜೆ ಮಾಡಿ ಸರಳ ಹಾಲು/ಹಣ್ಣಿನ ನೈವೇದ್ಯ ಮಾಡುವುದು. ಇದರಿಂದ ಕರ್ತೃವಿನ ಉಪವಾಸಕ್ಕೆ ಅಥವಾ ಶ್ರಾದ್ಧದ ಸಿದ್ಧತೆಗೆ ಯಾವುದೇ ಅಡ್ಡಿಯಾಗುವುದಿಲ್ಲ."
                    AppLanguage.SANSKRIT -> "यः श्राद्धं न करोति सः भ्राता पुत्रो वा पुरोहितः प्रातः सालिग्रामपूजां कृत्वा दुग्धफलादिनैवेद्यं समर्पयेत्।"
                    AppLanguage.TELUGU -> "శ్రాద్ధం చేయని సోదరుడు, కుమారుడు లేదా పురోహితుడు ఉదయమే సాలగ్రామ పూజ చేసి పాలు/పండ్ల నైవేద్యం సమర్పిస్తారు."
                    AppLanguage.TAMIL -> "சிராத்தம் செய்யாத சகோதரர், மகன் அல்லது சாஸ்திரிகள் காலையில் சாளக்கிராம பூஜை செய்து பால்/பழம் நைவேத்யம் செய்வர்."
                    AppLanguage.ENGLISH -> "Another eligible family member who is not the Kartru (or family priest) performs the morning Śāligrāma Pūjā with simple milk/fruit Naivedya without disrupting the Kartru's fasting."
                }
            ),
            PujaOptionItem(
                optionNumber = 2,
                title = when (language) {
                    AppLanguage.KANNADA -> "ಶ್ರಾದ್ಧಶೇಷೇಣ ದೇವಪೂಜಾ (ಕರ್ತೃವಿನ ನೇರ ಪೂಜಾ ವಿಧಾನ)"
                    AppLanguage.SANSKRIT -> "श्राद्धशेषेण देवपूजा (कर्तुः साक्षात् पूजाविधिः)"
                    AppLanguage.TELUGU -> "శ్రాద్ధశేషేణ దేవపూజ (కర్త ప్రత్యక్ష పూజా విధానం)"
                    AppLanguage.TAMIL -> "ச்ராத்தசேஷ தேவபூஜை (கர்த்தாவின் நேரடி பூஜை)"
                    AppLanguage.ENGLISH -> "Option 2: Śrāddha-Śeṣa Pūjā (Direct Worship by the Kartru)"
                },
                ruleSubtitle = when (language) {
                    AppLanguage.KANNADA -> "ಮಧ್ಯಾಹ್ನ ಶ್ರಾದ್ಧದ ಪಾಕವನ್ನೇ ದೇವರಿಗೆ ನೈವೇದ್ಯ ಮಾಡಿ ನಂತರ ಶ್ರಾದ್ಧ ಮುಂದುವರಿಸುವುದು"
                    AppLanguage.SANSKRIT -> "मध्याह्ने श्राद्धपाकेन भगवतो नैवेद्यं कृत्वा श्राद्धं समापयेत्"
                    AppLanguage.TELUGU -> "మధ్యాహ్నం శ్రాద్ధ పాకాన్నే దేవునికి నైవేద్యం పెట్టి శ్రాద్ధం కొనసాగించడం"
                    AppLanguage.TAMIL -> "மதியம் சிராத்த சமையலையே இறைவனுக்கு நைவேத்யம் செய்து சிராத்தம் தொடங்குதல்"
                    AppLanguage.ENGLISH -> "Offering the sanctified Shraddha meal as Naivedya during Madhyahna"
                },
                practicalPractice = when (language) {
                    AppLanguage.KANNADA -> "ಕರ್ತೃವು ಬೆಳಿಗ್ಗೆ ಸಂಧ್ಯಾವಂದನೆ ಮತ್ತು ಗಾಯತ್ರೀ ಜಪ ಮುಗಿಸಿ ಪೂಜೆಯನ್ನು ಮುಂದೂಡುತ್ತಾನೆ. ಮಧ್ಯಾಹ್ನ ಶ್ರಾದ್ಧದ ಅಡುಗೆ ಸಿದ್ಧವಾದಾಗ ಅದೇ ಪವಿತ್ರ ಅನ್ನವನ್ನು ಸಾಲಿಗ್ರಾಮ ದೇವರಿಗೆ ನೈವೇದ್ಯ ಮಾಡಿ, ನಂತರ ಬ್ರಾಹ್ಮಣ ಭೋಜನ ಮತ್ತು ಪಿಂಡದಾನವನ್ನು ನೆರವೇರಿಸುತ್ತಾನೆ."
                    AppLanguage.SANSKRIT -> "कर्ता प्रातः केवलं सन्ध्यां जपं च कुर्यात्। मध्याह्ने श्राद्धपाके सिद्धे तेनैवान्नेन सालिग्रामपूजां नैवेद्यं च समर्प्य ततः श्राद्धं समापयेत्।"
                    AppLanguage.TELUGU -> "కర్త ఉదయం సంధ్యావందనం మాత్రమే చేసి పూజను వాయిదా వేస్తాడు. మధ్యాహ్నం శ్రాద్ధ పాకం సిద్ధమయ్యాక ఆ అన్నాన్ని సాలగ్రామానికి నైవేద్యం పెట్టి బ్రాహ్మణ భోజనం జరిపిస్తాడు."
                    AppLanguage.TAMIL -> "கர்த்தா காலையில் சந்தியாவந்தனம் மட்டும் செய்து பூஜையை தள்ளிப்போடுவார். மதியம் சிராத்த சமையல் தயாரானதும் சாளக்கிராமத்திற்கு நைவேத்யம் செய்து சிராத்தத்தை தொடருவார்."
                    AppLanguage.ENGLISH -> "The Kartru performs morning Sandhyāvandana, postponing the main archana. When the Śrāddha cooking is ready during Madhyāhna, he offers that meal as Naivedya to Śrī Hari / Śāligrāma, and proceeds directly with Brāhmaṇa Bhojana and Piṇḍa Dāna."
                }
            ),
            PujaOptionItem(
                optionNumber = 3,
                title = when (language) {
                    AppLanguage.KANNADA -> "ಸಂಕ್ಷಿಪ್ತ / ಮಾನಸಿಕ ಪೂಜೆ (ಒಂಟಿ ಕರ್ತೃವಿನ ಸಂದರ್ಭದಲ್ಲಿ)"
                    AppLanguage.SANSKRIT -> "संक्षिप्त-मानसपूजा (एकाकिनः कर्तुः पक्षे)"
                    AppLanguage.TELUGU -> "సంక్షిప్త / మానసిక పూజ (ఒంటరి కర్త కొరకు)"
                    AppLanguage.TAMIL -> "சுருக்கமான / மானசீக பூஜை (தனி நபர் கர்த்தாவுக்கு)"
                    AppLanguage.ENGLISH -> "Option 3: Saṅkṣipta Pūjā (Brief Worship for Solo Kartru)"
                },
                ruleSubtitle = when (language) {
                    AppLanguage.KANNADA -> "ತೀರ್ಥ-ಪ್ರಸಾದ ಸ್ವೀಕರಿಸದೆ ಗಂಧ-ಪುಷ್ಪ-ತುಳಸೀ-ಜಲ ಸಮರ್ಪಣೆ"
                    AppLanguage.SANSKRIT -> "तीर्थप्रसादस्वीकारं विना केवलं गन्धतुलसीजलनैवेद्यसमर्पणम्"
                    AppLanguage.TELUGU -> "తీర్థప్రసాదాలు తీసుకోకుండా గంధం, తులసి, జల సమర్పణ"
                    AppLanguage.TAMIL -> "தீர்த்த பிரசாதம் உட்கொள்ளாமல் சந்தனம், துளசி, தீர்த்தம் சமர்ப்பித்தல்"
                    AppLanguage.ENGLISH -> "Brief mental offering of Gandha, Tulasi, and water without consuming Prasada"
                },
                practicalPractice = when (language) {
                    AppLanguage.KANNADA -> "ಕರ್ತೃವು ಒಬ್ಬನೇ ಇರುವಾಗ, ದೇವರಿಗೆ ಗಂಧ, ಪುಷ್ಪ, ತುಳಸೀ ಮತ್ತು ಜಲ ನೈವೇದ್ಯವನ್ನು ಮಾನಸಿಕ ಭಕ್ತಿಯಿಂದ ಸಮರ್ಪಿಸಿ, ಶ್ರಾದ್ಧ ಮುಗಿಯುವವರೆಗೆ ಯಾವುದೇ ತೀರ್ಥ-ಆಹಾರ ಸೇವಿಸದೆ ಶ್ರಾದ್ಧವನ್ನು ಪೂರ್ಣಗೊಳಿಸುತ್ತಾನೆ."
                    AppLanguage.SANSKRIT -> "सहायकरहिते सति कर्ता गन्ध-पुष्प-तुलसी-जलनैवेद्यं समर्प्य मानसपूजां कुर्यात्, श्राद्धसमाप्तिपर्यन्तं तीर्थाहारं न गृह्णीयात्।"
                    AppLanguage.TELUGU -> "సహాయకులు లేనప్పుడు కర్త గంధం, పుష్పం, తులసి, జల నైవేద్యంతో సంక్షిప్త పూజ చేసి, శ్రాద్ధం ముగిసేవరకు తీర్థాహారాలు తీసుకోకుండా ఉంటాడు."
                    AppLanguage.TAMIL -> "உதவியாளர் இல்லாத போது கர்த்தா சந்தனம், துளசி, தீர்த்த நைவேத்யம் செய்து மானசீகமாக வழிபட்டு சிராத்தம் முடியும் வரை பிரசாதம் உட்கொள்ளாமல் இருப்பார்."
                    AppLanguage.ENGLISH -> "If living alone, the Kartru performs an essential brief worship (Gandha, Puṣpa, Tulasī, Jala-Naivedya) with mental devotion, strictly abstaining from consuming any food or Tīrtha until the ceremony concludes."
                }
            )
        )

        return KartruDevaPujaGuide(
            canonicalShlokaNative = shlokaNative,
            canonicalShlokaTransliteration = shlokaTranslit,
            shlokaMeaning = shlokaMeaning,
            philosophicalRationaleIntro = intro,
            rationales = rationales,
            canonicalOptions = options
        )
    }
}
