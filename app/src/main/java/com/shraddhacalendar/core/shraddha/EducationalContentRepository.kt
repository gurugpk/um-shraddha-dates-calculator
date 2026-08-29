package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.EducationalCeremonyInfo
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.core.models.ShraddhaType

/**
 * Authoritative Educational and Scriptural Repository for all 16 Shodasha Masikas,
 * Una Rites, Prathama Varshika, Annual Varshika, and Mahalaya Paksha.
 * Sourced directly from Garuda Purana (Preta Khanda Chapters 4, 5, 13-15),
 * Smriti Muktavali (Sri Raghavendra Swamy), and Dharma Sindhu.
 */
object EducationalContentRepository {

    private val CONTENT_MAP = mapOf(
        "adya_masika" to EducationalCeremonyInfo(
            ceremonyKey = "adya_masika",
            titleEnglish = "Adya Masika (13th Day)",
            titleSanskrit = "आद्यमासिकम् (Adya Masikam)",
            dayTiming = "Observed on Day 13 following demise (completion of Ashaucha)",
            soulJourneyStation = "Departure from Home & Entry onto Yama Marga",
            stationDescription = "On the 13th day, the initial 10-day physical Ashaucha rites conclude and the subtle body (Yatana Deha) is fully formed. The soul bids farewell to its earthly residence and embarks on the long 86,000-Yojana journey (Marganusarana) across 16 intermediate stations towards Yama Loka.",
            spiritualSignificance = "The Adya Masika Pinda and Udaka (water libations) break the departed soul's lingering earthly attachment to the house and satisfy the sudden onset of acute thirst and hunger (Kshut-Pipasa Nivarana).",
            whyNeeded = "According to Garuda Purana (5.1-6), without the Adya Masika offering, the soul cannot muster the vital energy needed to step onto the arduous afterlife path and suffers agonizing dehydration.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.1-6), Smriti Muktavali (Pitrumedha Prakarana)"
        ),
        "unmasika" to EducationalCeremonyInfo(
            ceremonyKey = "unmasika",
            titleEnglish = "Unmasika (27th Day)",
            titleSanskrit = "ऊनमासिकम् (Unamasikam)",
            dayTiming = "Observed on Day 27 following demise",
            soulJourneyStation = "Arrival at Yamya Pura (First Intermediate City)",
            stationDescription = "Between Days 25 and 28, before the first solar month closes, the traveling soul reaches the scorching city of Yamya Pura. Exhausted by blistering solar heat and lack of water, the subtle body experiences severe collapse.",
            spiritualSignificance = "The Unmasika offering provides immediate subtle sustenance (Tanmatra Ahara) just before the month ends, reviving the soul and granting fortitude to enter Yamya Pura without fainting.",
            whyNeeded = "Garuda Purana (5.12-18) explains that the distance to Yamya Pura is immense; if descendants wait until the full month to offer food, the departed soul endures unbearable agony. Unmasika is the scriptural rescue offering.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.12-18), Dharma Sindhu (Shraddha Prakarana)"
        ),
        "dvitiya_masika" to EducationalCeremonyInfo(
            ceremonyKey = "dvitiya_masika",
            titleEnglish = "Dvitiya Masika (2nd Month Tithi)",
            titleSanskrit = "द्वितीयमासिकम् (Dvitiya Masikam)",
            dayTiming = "Observed on the 2nd month recurring death Tithi",
            soulJourneyStation = "Crossing Sauripura (Town of King Shatru)",
            stationDescription = "The soul reaches the city of Sauripura, surrounded by thorny terrain and fearsome obstacles.",
            spiritualSignificance = "The Pinda offered on the exact lunar death tithi transforms into celestial nectar (Amrita-Rupa), nourishing the subtle body.",
            whyNeeded = "Ensures continuous spiritual support on the exact lunar cycle of departure, reinforcing the soul's stamina.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.22-25), Nirnaya Sindhu"
        ),
        "traipakshika" to EducationalCeremonyInfo(
            ceremonyKey = "traipakshika",
            titleEnglish = "Traipakshika (45th Day)",
            titleSanskrit = "त्रैपाक्षिकम् (Traipakshikam)",
            dayTiming = "Observed on Day 45 (three Pakshas / 1.5 solar months)",
            soulJourneyStation = "Passing through Naga-bhavana Pura (Serpent Abode)",
            stationDescription = "At the 45-day milestone, the traveler traverses Naga-bhavana Pura, a dense region filled with biting cold and fierce atmospheric beings.",
            spiritualSignificance = "Acts as spiritual armor (Abhaya & Trana), shielding the soul from fear and granting safe passage through hostile astral zones.",
            whyNeeded = "Garuda Purana (5.30-34) states that the soul encounters terrifying guardians here; the Traipakshika pinda pacifies these obstacles instantly.",
            scripturalCitation = "Garuda Purana (5.30-34), Smriti Muktavali"
        ),
        "tritiya_masika" to EducationalCeremonyInfo(
            ceremonyKey = "tritiya_masika",
            titleEnglish = "Tritiya Masika (3rd Month Tithi)",
            titleSanskrit = "तृतीयमासिकम् (Tritiya Masikam)",
            dayTiming = "Observed on the 3rd month recurring death Tithi",
            soulJourneyStation = "Traversing Gandharva Pura",
            stationDescription = "The soul crosses Gandharva Pura, where powerful sensory illusions and memories of past family life arise.",
            spiritualSignificance = "Purifies residual mental impressions (Vasanas) and strengthens inner detachment to continue onward.",
            whyNeeded = "Provides the spiritual clarity needed to avoid getting stalled by emotional attachments.",
            scripturalCitation = "Garuda Purana (5.38-42)"
        ),
        "chaturtha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "chaturtha_masika",
            titleEnglish = "Chaturtha Masika (4th Month Tithi)",
            titleSanskrit = "चतुर्थमासिकम् (Chaturtha Masikam)",
            dayTiming = "Observed on the 4th month recurring death Tithi",
            soulJourneyStation = "Crossing Shailagama Pura (Falling Rocks Pass)",
            stationDescription = "A mountainous pass where falling stones and sharp gravel test the subtle body.",
            spiritualSignificance = "Bestows divine cushioning and protection against physical exhaustion.",
            whyNeeded = "Garuda Purana (5.45-49) highlights this as one of the physically grueling mountain stages of the path.",
            scripturalCitation = "Garuda Purana (5.45-49)"
        ),
        "panchama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "panchama_masika",
            titleEnglish = "Panchama Masika (5th Month Tithi)",
            titleSanskrit = "पञ्चममासिकम् (Panchama Masikam)",
            dayTiming = "Observed on the 5th month recurring death Tithi",
            soulJourneyStation = "Traversing Krauncha Pura (Waterless Desert)",
            stationDescription = "A desolate, arid desert with zero water bodies under blazing temperatures.",
            spiritualSignificance = "The sesame-water offerings (Tila Tarpana) quench the soul's intense thirst and cool the subtle organs.",
            whyNeeded = "Prevents the traveler from dehydration during the 4th month desert crossing.",
            scripturalCitation = "Garuda Purana (5.52-56)"
        ),
        "shashtha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "shashtha_masika",
            titleEnglish = "Shanmasika (6th Month Tithi)",
            titleSanskrit = "षाण्मासिकम् (Shanmasikam / षष्ठमासिकम्)",
            dayTiming = "Observed on the 6th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Arrival at Vichitra Pura (Midway Milestone)",
            stationDescription = "The midway point of the 1-year journey, approaching the capital of King Vichitra.",
            spiritualSignificance = "Renews the soul's endurance as it prepares for the most critical test of the afterlife: the Vaitarani river.",
            whyNeeded = "Sets the spiritual foundation for the upcoming Vaitarani crossing.",
            scripturalCitation = "Garuda Purana (5.60-64), Dharma Sindhu"
        ),
        "una_shanmasika" to EducationalCeremonyInfo(
            ceremonyKey = "una_shanmasika",
            titleEnglish = "Una-Shanmasika (~170th Day / Godana)",
            titleSanskrit = "ऊनषाण्मासिकम् (Una-Shanmasikam with Vaitarani Godana)",
            dayTiming = "Observed prior to the 6th month (~Day 163-170 with Godana)",
            soulJourneyStation = "Crossing the Ferocious Vaitarani River",
            stationDescription = "The soul confronts the dreadful Vaitarani river—a 100-Yojana boiling torrent of blood, pus, and fire that cannot be crossed on foot.",
            spiritualSignificance = "Performing Una-Shanmasika accompanied by Godana (cow donation or Vaitarani Sankalpa) manifests the sacred cow whose tail the departed soul holds to glide safely across the boiling torrent into Vahni Pura.",
            whyNeeded = "Garuda Purana (Ch. 4 & 5.68-75) explicitly declares that without Vaitarani Godana and Una-Shanmasika, souls are swept into the agonizing currents of the river.",
            scripturalCitation = "Garuda Purana (Preta Khanda Chapters 4 & 5.68-75), Dharma Sindhu"
        ),
        "saptama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "saptama_masika",
            titleEnglish = "Saptama Masika (7th Month Tithi)",
            titleSanskrit = "सप्तममासिकम् (Saptama Masikam)",
            dayTiming = "Observed on the 7th month recurring death Tithi",
            soulJourneyStation = "Traversing Bahvadupadrava Pura",
            stationDescription = "A land of harsh winds and stinging insects beyond the Vaitarani.",
            spiritualSignificance = "Protects the subtle body from atmospheric hardships.",
            whyNeeded = "Maintains the unbroken monthly chain of filial nourishment.",
            scripturalCitation = "Garuda Purana (5.80-84)"
        ),
        "ashtama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "ashtama_masika",
            titleEnglish = "Ashtama Masika (8th Month Tithi)",
            titleSanskrit = "अष्टममासिकम् (Ashtama Masikam)",
            dayTiming = "Observed on the 8th month recurring death Tithi",
            soulJourneyStation = "Crossing Duhkhada Pura (Valley of Sorrow)",
            stationDescription = "A region where souls reflect on past life actions and experience deep remorse.",
            spiritualSignificance = "Bestows spiritual peace and alleviates psychological anguish through descendant prayers.",
            whyNeeded = "Soothes the conscience and grants inner fortitude.",
            scripturalCitation = "Garuda Purana (5.88-92)"
        ),
        "navama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "navama_masika",
            titleEnglish = "Navama Masika (9th Month Tithi)",
            titleSanskrit = "नवममासिकम् (Navama Masikam)",
            dayTiming = "Observed on the 9th month recurring death Tithi",
            soulJourneyStation = "Passing through Nanakranda Pura",
            stationDescription = "A territory filled with weeping wanderers, testing the soul's courage.",
            spiritualSignificance = "Shields the soul from external distress and reinforces spiritual focus.",
            whyNeeded = "Sustains emotional balance as the destination draws near.",
            scripturalCitation = "Garuda Purana (5.95-99)"
        ),
        "dashama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "dashama_masika",
            titleEnglish = "Dashama Masika (10th Month Tithi)",
            titleSanskrit = "दशममासिकम् (Dashama Masikam)",
            dayTiming = "Observed on the 10th month recurring death Tithi",
            soulJourneyStation = "Sutaptabhavana Pura (Intense Heat Zone)",
            stationDescription = "Approaching the southern gateway under severe thermal waves.",
            spiritualSignificance = "Replenishes cooling water elements (Udaka) to soothe the subtle senses.",
            whyNeeded = "Prevents exhaustion in the 9th month heat corridor.",
            scripturalCitation = "Garuda Purana (5.102-106)"
        ),
        "ekadasha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "ekadasha_masika",
            titleEnglish = "Ekadasha Masika (11th Month Tithi)",
            titleSanskrit = "एकादशमासिकम् (Ekadasha Masikam)",
            dayTiming = "Observed on the 11th month recurring death Tithi",
            soulJourneyStation = "Crossing Raudra Pura",
            stationDescription = "Dark stormy winds envelope Raudra Pura as the soul approaches the final frontier.",
            spiritualSignificance = "Illuminates the path through the merit of the descendant's offerings.",
            whyNeeded = "Assists in traversing the 10th station securely.",
            scripturalCitation = "Garuda Purana (5.110-114)"
        ),
        "dvadasha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "dvadasha_masika",
            titleEnglish = "Dvadasha Masika (12th Month Tithi)",
            titleSanskrit = "द्वादशमासिकम् (Dvadasha Masikam)",
            dayTiming = "Observed on the 12th month recurring death Tithi",
            soulJourneyStation = "Arrival at Payovarshana Pura",
            stationDescription = "Outskirts of Samyamani Puri (the glorious capital of Dharmaraja).",
            spiritualSignificance = "Prepares the soul for the final entry and the audience before Dharmaraja.",
            whyNeeded = "Completes the 12 regular monthly lunar offerings.",
            scripturalCitation = "Garuda Purana (5.118-122)"
        ),
        "trayodasha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "trayodasha_masika",
            titleEnglish = "Trayodasha Masika (13th Month Tithi)",
            titleSanskrit = "त्रयोदशमासिकम् (Trayodasha Masikam)",
            dayTiming = "Observed during Adhika Masa on the recurring death Tithi",
            soulJourneyStation = "Traversing Adhika Mandala in Yama Marga",
            stationDescription = "An intercalary station traversed during leap months (Adhika Masa) where special lunar expiation offerings are made.",
            spiritualSignificance = "Maintains alignment between the lunar and solar year, ensuring the soul does not experience seasonal dislocation on the journey.",
            whyNeeded = "Dharma Sindhu and Nirnaya Sindhu prescribe an additional 13th Masika when an Adhika Masa occurs within the first year of demise.",
            scripturalCitation = "Dharma Sindhu (Adhikasamvatsara Shraddha Prakarana), Nirnaya Sindhu"
        ),
        "unabdika" to EducationalCeremonyInfo(
            ceremonyKey = "unabdika",
            titleEnglish = "Unabdika (~340th Day / Una-Varshika)",
            titleSanskrit = "ऊनाब्दिकम् (Unabdikam)",
            dayTiming = "Observed prior to 1 year completion (~Day 340-350)",
            soulJourneyStation = "Entrance to Samyamani Puri Gates",
            stationDescription = "The final preparatory milestone before the full 360-day year concludes.",
            spiritualSignificance = "Brings the preliminary 1-year traveler state to a close and prepares the subtle body for permanent elevation.",
            whyNeeded = "According to Smriti Muktavali and Garuda Purana (5.126-130), Unabdika is mandatory before Sapindikarana can take place.",
            scripturalCitation = "Garuda Purana (5.126-130), Smriti Muktavali"
        ),
        "prathama_varshika" to EducationalCeremonyInfo(
            ceremonyKey = "prathama_varshika",
            titleEnglish = "Prathama Varshika Shraddha (1st Death Anniversary)",
            titleSanskrit = "प्रथमवार्षिकश्राद्धम् सपिण्डीकरणम् च",
            dayTiming = "Observed on the exact 1st annual death anniversary during Aparahna Kala",
            soulJourneyStation = "Dissolution of Preta State & Union with Divine Pitrus",
            stationDescription = "The soul reaches the celestial assembly hall (Dharmasabha) in Samyamani Puri. Through Sapindikarana, the ghostly state (Preta Avastha) is permanently dissolved.",
            spiritualSignificance = "The departed soul is formally united with three generations of ancestors (Pitarah) and attains the divine status of Vasu-Rudra-Aditya Swaroopa.",
            whyNeeded = "Without Prathama Varshika and Sapindikarana, the soul remains trapped in Preta Avastha. This ritual opens the gates to Pitru Loka.",
            scripturalCitation = "Garuda Purana (Preta Khanda Ch. 14), Dharma Sindhu, Smriti Muktavali"
        ),
        "annual_varshika" to EducationalCeremonyInfo(
            ceremonyKey = "annual_varshika",
            titleEnglish = "Annual Varshika Shraddha (Yearly Death Anniversary)",
            titleSanskrit = "वार्षिकश्राद्धम् (Pratyabdika Shraddham)",
            dayTiming = "Observed every year on the target death month & Tithi during Aparahna Kala",
            soulJourneyStation = "Eternal Abode in Pitru Loka under Sri Hari's Grace",
            stationDescription = "The elevated ancestor resides peacefully in Pitru Loka, blessed by Sri Janardana Vasudeva.",
            spiritualSignificance = "Annual offering of Pinda, Brahmana Bhojana, and Tarpana pleases the Antaryami Sri Hari, who blesses the family with longevity, progeny, health, and peace (Santana-Vriddhi, Ayur-Arogya).",
            whyNeeded = "Manu Smriti (3.266-275) states that ancestors eagerly await the annual offering from their descendants to bestow blessings.",
            scripturalCitation = "Manu Smriti (3.266-275), Smriti Muktavali, Dharma Sindhu"
        ),
        "mahalaya_paksha" to EducationalCeremonyInfo(
            ceremonyKey = "mahalaya_paksha",
            titleEnglish = "Mahalaya Paksha Shraddha (Pitru Paksha)",
            titleSanskrit = "महालयपक्षश्राद्धम् (Mahalaya Parvanam)",
            dayTiming = "Observed during Bhadrapada Krishna Paksha (Pitru Paksha) on the death Tithi",
            soulJourneyStation = "Earthly Visitation during Pitru Paksha",
            stationDescription = "When the Sun enters Kanya Rashi, Lord Yama permits all ancestral souls to visit the earth to receive offerings from their descendants.",
            spiritualSignificance = "Performing Parvana Shraddha on the death Tithi during Mahalaya satisfies the entire lineage (Paternal & Maternal lines, Gurus, and all departed relations) simultaneously.",
            whyNeeded = "Nirnaya Sindhu and Dharma Sindhu state that Mahalaya is the supreme collective ancestral festival; missing it causes spiritual debt to the Pitrus. (Note: Not applicable in Year 1 prior to Sapindikarana).",
            scripturalCitation = "Nirnaya Sindhu (Mahalaya Prakarana), Dharma Sindhu, Smriti Muktavali"
        )
    )

    fun findInfoForEvent(event: ShraddhaEvent): EducationalCeremonyInfo? {
        return when (event.type) {
            com.shraddhacalendar.core.models.ShraddhaType.VARSHIKA -> {
                val lower = event.traditionalName.lowercase()
                if (lower.contains("prathama") || lower.contains("1st") || lower.contains("1ನೇ") || lower.contains("1వ") || lower.contains("1ஆம்") || lower.contains("ಪ್ರಥಮ") || lower.contains("प्रथम") || lower.contains("ప్రథమ") || lower.contains("ப்ரதம")) {
                    CONTENT_MAP["prathama_varshika"]
                } else {
                    CONTENT_MAP["annual_varshika"]
                }
            }
            com.shraddhacalendar.core.models.ShraddhaType.MAHALAYA_PAKSHA -> CONTENT_MAP["mahalaya_paksha"]
            com.shraddhacalendar.core.models.ShraddhaType.UNA_RITE,
            com.shraddhacalendar.core.models.ShraddhaType.MASIKA -> findInfoForEvent(event.traditionalName)
        }
    }

    fun getInfo(ceremonyKey: String): EducationalCeremonyInfo? {
        return CONTENT_MAP[ceremonyKey.lowercase()]
    }

    fun findInfoForEvent(traditionalName: String): EducationalCeremonyInfo? {
        val lower = traditionalName.lowercase()
        return when {
            // Unabdika check first
            lower.contains("unabdika") || lower.contains("una-varshika") || lower.contains("ಊನಾಬ್ದಿಕ") || lower.contains("ऊनाब्दिक") || lower.contains("ఊనాబ్దిక") || lower.contains("ஊனாப்திக") -> CONTENT_MAP["unabdika"]
            
            // Varshika checks
            lower.contains("varshika") || lower.contains("yearly") || lower.contains("annual") || lower.contains("ವಾರ್ಷಿಕ") || lower.contains("वार्षिक") || lower.contains("వార్షిక") || lower.contains("வார்ஷிக") -> {
                if (lower.contains("prathama") || lower.contains("ಪ್ರಥಮ") || lower.contains("प्रथम") || lower.contains("ప్రథమ") || lower.contains("ப்ரதம") || lower.contains("1st") || lower.contains("1ನೇ") || lower.contains("1వ") || lower.contains("1ஆம்")) {
                    CONTENT_MAP["prathama_varshika"]
                } else {
                    CONTENT_MAP["annual_varshika"]
                }
            }
            
            // Mahalaya Paksha check
            lower.contains("mahalaya") || lower.contains("pitru paksha") || lower.contains("ಮಹಾಲಯ") || lower.contains("महालय") || lower.contains("మహాలయ") || lower.contains("மஹாலய") -> CONTENT_MAP["mahalaya_paksha"]
            
            // Specific Masikas
            lower.contains("adya") || lower.contains("ಆದ್ಯ") || lower.contains("आद्य") || lower.contains("ఆద్య") || lower.contains("ஆத்ய") -> CONTENT_MAP["adya_masika"]
            lower.contains("unmasika") || lower.contains("ಊನಮಾಸಿಕ") || lower.contains("ऊनमासिक") || lower.contains("ఊనమాసిక") || lower.contains("ஊநமாஸிக") -> CONTENT_MAP["unmasika"]
            lower.contains("traipakshika") || lower.contains("ತ್ರೈಪಕ್ಷಿಕ") || lower.contains("त्रैपाक्षिक") || lower.contains("త్రైపాక్షిక") || lower.contains("த்ரைபாக்ஷிக") -> CONTENT_MAP["traipakshika"]
            lower.contains("una-shanmasika") || lower.contains("una shanmasika") || lower.contains("godana") || lower.contains("ಊನಷಾಣ್ಮಾಸಿಕ") || lower.contains("ऊनषाण्मासिक") || lower.contains("ఊనషాణ్ಮಾಸಿಕ") || lower.contains("ஊநஷாண்மாஸிக") -> CONTENT_MAP["una_shanmasika"]
            lower.contains("trayodasha") || lower.contains("ತ್ರಯೋದಶ") || lower.contains("त्रयोदश") || lower.contains("త్రయోదశ") || lower.contains("த்ரயோதச") -> CONTENT_MAP["trayodasha_masika"] ?: CONTENT_MAP["dvadasha_masika"]
            lower.contains("dvadasha") || lower.contains("ದ್ವಾದಶ") || lower.contains("द्वादश") || lower.contains("ద్వాదశ") || lower.contains("த்வாதச") -> CONTENT_MAP["dvadasha_masika"]
            lower.contains("ekadasha") || lower.contains("ಏಕಾದಶ") || lower.contains("एकादश") || lower.contains("ఏకాదశ") || lower.contains("ஏகாதச") -> CONTENT_MAP["ekadasha_masika"]
            lower.contains("dashama") || lower.contains("ದಶಮ") || lower.contains("दशम") || lower.contains("దశమ") || lower.contains("தசம") -> CONTENT_MAP["dashama_masika"]
            lower.contains("navama") || lower.contains("ನವಮ") || lower.contains("नवम") || lower.contains("నవమ") || lower.contains("நவம") -> CONTENT_MAP["navama_masika"]
            lower.contains("ashtama") || lower.contains("ಅಷ್ಟಮ") || lower.contains("अष्टम") || lower.contains("అష్టమ") || lower.contains("அஷ்டம") -> CONTENT_MAP["ashtama_masika"]
            lower.contains("saptama") || lower.contains("ಸಪ್ತಮ") || lower.contains("सप्तम") || lower.contains("సప్తమ") || lower.contains("ஸப்தம") -> CONTENT_MAP["saptama_masika"]
            lower.contains("shashtha") || lower.contains("shanmasika") || lower.contains("ಷಷ್ಠ") || lower.contains("षष्ठ") || lower.contains("షష్ఠ") || lower.contains("ஷஷ்ட") -> CONTENT_MAP["shashtha_masika"]
            lower.contains("panchama") || lower.contains("ಪಂಚಮ") || lower.contains("पञ्चम") || lower.contains("పంచమ") || lower.contains("பஞ்சம") -> CONTENT_MAP["panchama_masika"]
            lower.contains("chaturtha") || lower.contains("ಚತುರ್ಥ") || lower.contains("चतुर्थ") || lower.contains("చతుర్థ") || lower.contains("சதுர்த்த") -> CONTENT_MAP["chaturtha_masika"]
            lower.contains("tritiya") || lower.contains("ತೃತೀಯ") || lower.contains("तृतीय") || lower.contains("తృతీయ") || lower.contains("திருதீய") -> CONTENT_MAP["tritiya_masika"]
            lower.contains("dwitiya") || lower.contains("dvitiya") || lower.contains("ದ್ವಿತೀಯ") || lower.contains("द्वितीय") || lower.contains("ద్వితీయ") || lower.contains("த்விதீய") -> CONTENT_MAP["dvitiya_masika"]
            else -> {
                val seqMatch = Regex("""(?:Masika|ಮಾಸಿಕ|मासिकम्|మాసికం|மாஸிகம்)\s*(\d+)""").find(traditionalName)
                if (seqMatch != null) {
                    when (seqMatch.groupValues[1].toIntOrNull()) {
                        1 -> CONTENT_MAP["adya_masika"]
                        2 -> CONTENT_MAP["unmasika"]
                        3 -> CONTENT_MAP["dvitiya_masika"]
                        4 -> CONTENT_MAP["traipakshika"]
                        5 -> CONTENT_MAP["tritiya_masika"]
                        6 -> CONTENT_MAP["chaturtha_masika"]
                        7 -> CONTENT_MAP["panchama_masika"]
                        8 -> CONTENT_MAP["shashtha_masika"]
                        9 -> CONTENT_MAP["una_shanmasika"]
                        10 -> CONTENT_MAP["saptama_masika"]
                        11 -> CONTENT_MAP["ashtama_masika"]
                        12 -> CONTENT_MAP["navama_masika"]
                        13 -> CONTENT_MAP["dashama_masika"]
                        14 -> CONTENT_MAP["ekadasha_masika"]
                        15 -> CONTENT_MAP["dvadasha_masika"]
                        16 -> CONTENT_MAP["unabdika"]
                        else -> CONTENT_MAP["dvitiya_masika"]
                    }
                } else {
                    CONTENT_MAP["annual_varshika"]
                }
            }
        }
    }

    fun getAllCeremonies(): List<EducationalCeremonyInfo> = CONTENT_MAP.values.toList()
}
