package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.EducationalCeremonyInfo
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.core.models.ShraddhaType

/**
 * Authoritative Educational and Scriptural Repository for all 19 Shodasha Masikas,
 * Una Rites, Prathama Varshika, Annual Varshika, and Mahalaya Paksha.
 * Sourced directly from Garuda Purana (Preta Khanda Chapters 4, 5, 14),
 * Baudhayana Pitrimedha Sutras, Dharma Sindhu III, Nirnaya Sindhu III,
 * Smriti Muktavali (Sri Raghavendra Swamy), and Manu Smriti.
 */
object EducationalContentRepository {

    private val CONTENT_MAP = mapOf(
        "adya_masika" to EducationalCeremonyInfo(
            ceremonyKey = "adya_masika",
            titleEnglish = "Adya Masika (13th Day)",
            titleSanskrit = "आद्यमासिकम् (Adya Masikam)",
            dayTiming = "Observed on Day 13 following demise (completion of Ashaucha)",
            soulJourneyStation = "Departure from Home & Entry onto Yama Marga",
            stationDescription = "On the 13th day, the initial 10-day physical Ashaucha rites conclude and the subtle suffering body (Yātanā Śarīra) is fully formed. The soul bids farewell to its earthly residence and embarks on the grueling 86,000-Yojana journey across 16 intermediate stations towards Samyamani Puri (Yama Loka).",
            pretaConditionAndYatanaDeha = "The physical body having been reduced to ashes, the Yātanā Śarīra (formed limb-by-limb from the 10 daily Dasha-pindas) experiences acute sensory awareness of hunger, thirst, and grief. On Day 13, Yamadutas bind the subtle body with ethereal cords and lead it away from the family threshold.",
            pindaSignificanceAndRelief = "The Adya Masika Piṇḍa and Udaka libations sever the soul's lingering earthly attachment to the home, satisfy the intense initial onset of hunger and dehydration (Kṣut-Pipāsā Nivarana), and infuse the vital subtle energy (Tanmātra Prāṇa) required to begin the arduous march.",
            spiritualSignificance = "Marks the formal conclusion of the 12-day immediate bereavement rites and initiates the unbroken filial bridge of Year 1 Masika offerings.",
            whyNeeded = "Garuda Purana (Preta Khanda 5.1-14) declares that without the Adya Masika Pinda, the departed soul cannot muster the vital energy needed to step onto the blazing afterlife road and suffers agonizing exhaustion.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.1-14), Dharmasindhu III (Dasha-aha Prakarana), Smriti Muktavali",
            classicalVerse = "दशमे क्षुत्तृषोद्भवः । एकादशे द्वादशे च भुङ्क्ते निष्पन्नदेहवान् ॥\nत्रयोदशे दिने प्रेतः पाशबद्धः प्रणीयते । गृहीत्वा याम्यदूतैस्तु महामार्गे प्रवर्तते ॥"
        ),
        "unmasika" to EducationalCeremonyInfo(
            ceremonyKey = "unmasika",
            titleEnglish = "Unmasika (27th Day)",
            titleSanskrit = "ऊनमासिकम् (Unamasikam)",
            dayTiming = "Observed on Day 27 following demise",
            soulJourneyStation = "Arrival at Yāmyapura (First Intermediate City)",
            stationDescription = "Between Days 25 and 28, before the first solar month closes, the traveling soul reaches the scorching city of Yāmyapura (Yamya Pura). Exhausted by blistering solar radiation, dry winds, and lack of water, the subtle body experiences acute fatigue.",
            pretaConditionAndYatanaDeha = "The Yātanā Śarīra feels severe physical exhaustion and parched thirst from covering the first intense leg of the road without natural shade or water bodies. The soul feels faint and distressed.",
            pindaSignificanceAndRelief = "The Unmasika Piṇḍa provides timely subtle nourishment (Tanmātra Āhāra) right before the solar month ends, instantly revitalizing the subtle organs, restoring moisture, and granting the fortitude needed to enter Yāmyapura without collapsing.",
            spiritualSignificance = "Functions as an essential Shastric 'preliminary rescue offering' (Ūna Rite) designed by the sages so the soul does not starve waiting for the full 30th day.",
            whyNeeded = "Garuda Purana (5.19-24) explains that the distance to Yāmyapura is immense; if descendants wait until the full month completes, the departed soul endures unbearable agony. Unmasika is the scriptural rescue offering.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.19-24), Dharmasindhu III (Shraddha Prakarana)",
            classicalVerse = "ऊनमासिकदानेन तृप्तिर्भवति दारुणे । याम्यपुरं समासाद्य विश्रान्तिं लभते सुखम् ॥"
        ),
        "dvitiya_masika" to EducationalCeremonyInfo(
            ceremonyKey = "dvitiya_masika",
            titleEnglish = "Dvitiya Masika (2nd Month Tithi)",
            titleSanskrit = "द्वितीयमासिकम् (Dvitiya Masikam)",
            dayTiming = "Observed on the 2nd month recurring death Tithi during Aparahna",
            soulJourneyStation = "Crossing Śauripura (Town of King Janghama)",
            stationDescription = "The soul reaches the city of Śauripura, surrounded by dense thorny brambles (Kaṇṭhaka-vana), searing rocks, and intimidating terrain ruled by King Janghama.",
            pretaConditionAndYatanaDeha = "The Yātanā Śarīra must navigate sharp thorns and jagged stones that pierce subtle limbs, testing the traveler's endurance and spiritual resolve.",
            pindaSignificanceAndRelief = "The Piṇḍa offered on the exact recurring lunar death tithi transforms into celestial nectar (Amṛta-rūpa), coating and strengthening the subtle limbs to traverse thorny wilderness without pain.",
            spiritualSignificance = "Re-establishes the exact lunar connection of the moment of departure, reinforcing the soul's spiritual stamina for month 2.",
            whyNeeded = "Maintains the continuous monthly rhythm of filial duty, ensuring the traveler is never abandoned in hostile terrain.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.25-30), Nirnayasindhu III",
            classicalVerse = "शौरिपुरे महाघोरे कण्टकैः संवृते पथि । द्वितीयमासिके दत्ते सुखेन तरते नरः ॥"
        ),
        "traipakshika" to EducationalCeremonyInfo(
            ceremonyKey = "traipakshika",
            titleEnglish = "Traipakshika (45th Day)",
            titleSanskrit = "त्रैपाक्षिकम् (Traipakshikam)",
            dayTiming = "Observed on Day 45 (three Pakshas / 1.5 solar months)",
            soulJourneyStation = "Passing through Nāgabhavanapura (Serpent Realm)",
            stationDescription = "At the 45-day milestone, the traveler enters Nāgabhavanapura, an atmospheric region filled with biting cold, torrential rains, darkness, and formidable serpent guardians.",
            pretaConditionAndYatanaDeha = "The Yātanā Śarīra suffers from intense cold and shivering in pitch darkness, confronted by frightening astral forces that test the soul's mental courage.",
            pindaSignificanceAndRelief = "The Traipakshika Piṇḍa acts as spiritual armor (Abhaya-kavaca), generating internal warmth, dispelling astral terror, and pacifying atmospheric guardians.",
            spiritualSignificance = "A critical fixed-day interval rite observed at the 1.5-month mark to neutralize midway hazards between monthly tithis.",
            whyNeeded = "Garuda Purana (5.31-36) states that hostile guardians obstruct the path at the 45th day; the Traipakshika offering satisfies them and opens safe passage.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.31-36), Smriti Muktavali",
            classicalVerse = "त्रैपाक्षिकविधानेन नागलोकभयापहम् । दीयते यत्तु मन्त्राभ्यां तद्भवत्यमृताशनम् ॥"
        ),
        "tritiya_masika" to EducationalCeremonyInfo(
            ceremonyKey = "tritiya_masika",
            titleEnglish = "Tritiya Masika (3rd Month Tithi)",
            titleSanskrit = "तृतीयमासिकम् (Tritiya Masikam)",
            dayTiming = "Observed on the 3rd month recurring death Tithi during Aparahna",
            soulJourneyStation = "Traversing Gandharvapura",
            stationDescription = "The soul crosses the illusory realm of Gandharvapura, where mirages, illusory sounds, and vivid memories of earthly possessions and family attachments arise.",
            pretaConditionAndYatanaDeha = "The subtle mind experiences emotional agitation, grief, and nostalgic delusions (Māyā-moha) that threaten to stall forward movement.",
            pindaSignificanceAndRelief = "The Tritiya Masika Piṇḍa purifies residual mental impressions (Vāsanās), dissolves grief, and instills detachment (Vairāgya) to continue the spiritual ascent.",
            spiritualSignificance = "Frees the departing soul from emotional bondage to the physical realm and clarifies spiritual consciousness.",
            whyNeeded = "Garuda Purana (5.37-43) highlights this station as the mental and psychological trial of the journey.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.37-43), Dharmasindhu III",
            classicalVerse = "गान्धर्वपुरमासाद्य मोहं गच्छति चेतसा । तृतीये मासि यद्दत्तं तेन मोहात्प्रमुच्यते ॥"
        ),
        "chaturtha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "chaturtha_masika",
            titleEnglish = "Chaturtha Masika (4th Month Tithi)",
            titleSanskrit = "चतुर्थमासिकम् (Chaturtha Masikam)",
            dayTiming = "Observed on the 4th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Crossing Śailāgamapura (Falling Rocks Pass)",
            stationDescription = "A precipitous mountain pass where torrential showers of sharp gravel, hail, and rolling boulders strike travelers.",
            pretaConditionAndYatanaDeha = "The Yātanā Śarīra endures physical battering and bruised limbs while climbing steep rocky escarpments without footing.",
            pindaSignificanceAndRelief = "The Chaturtha Masika Piṇḍa provides cushioning, stamina, and subtle physical resilience, shielding the traveler from trauma while scaling steep cliffs.",
            spiritualSignificance = "Represents endurance through the most physically punishing mountain stage of the afterlife road.",
            whyNeeded = "Garuda Purana (5.44-50) states that without the 4th month Pinda, the subtle body is severely battered by falling debris.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.44-50), Nirnayasindhu III",
            classicalVerse = "शिलावर्षे महोत्पाते शैलागमपुरे स्थिते । चतुर्थमासिकं पिण्डं प्राप्य सुखमवाप्नुयात् ॥"
        ),
        "panchama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "panchama_masika",
            titleEnglish = "Panchama Masika (5th Month Tithi)",
            titleSanskrit = "पञ्चममासिकम् (Panchama Masikam)",
            dayTiming = "Observed on the 5th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Traversing Krauñcapura (Waterless Desert)",
            stationDescription = "A vast, desolate desert with zero water bodies under blistering temperatures and scorching sands.",
            pretaConditionAndYatanaDeha = "The subtle body suffers from intense dehydration, a parched throat, and blistered feet from traversing superheated desert sands.",
            pindaSignificanceAndRelief = "The Panchama Masika Piṇḍa and cooling sesame-water libations (Tila Tarpana) quench deep cellular thirst and restore inner moisture.",
            spiritualSignificance = "Guarantees sustained hydration and protection in arid astral environments.",
            whyNeeded = "Garuda Purana (5.51-57) warns that souls without filial water offerings collapse from heat exhaustion in Krauñcapura.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.51-57), Smriti Muktavali",
            classicalVerse = "क्रौञ्चपुरे जलहीने तृषाक्रान्तो भृशं नरः । पञ्चमे मासि पिण्डेन तृप्तिमेति सुदुर्लभाम् ॥"
        ),
        "shashtha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "shashtha_masika",
            titleEnglish = "Shanmasika (6th Month Tithi)",
            titleSanskrit = "षाण्मासिकम् (Shanmasikam / षष्ठमासिकम्)",
            dayTiming = "Observed on the 6th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Arrival at Vicitrapura (Midway Milestone)",
            stationDescription = "The grand midway capital ruled by King Vicitra (younger brother of Yama), situated directly before the Vaitarani river basin.",
            pretaConditionAndYatanaDeha = "The soul reaches the halfway marker of the 1-year journey, fatigued by 6 months of continuous travel and facing the judicial scrutiny of King Vicitra.",
            pindaSignificanceAndRelief = "The Shanmasika Piṇḍa strengthens the subtle faculties, satisfies the royal gatekeepers, and prepares the soul for the pivotal Vaitarani river crossing.",
            spiritualSignificance = "Marks the halfway completion of the afterlife journey and lays the groundwork for the sacred Vaitarani Godana.",
            whyNeeded = "Dharmasindhu III and Garuda Purana (5.58-65) prescribe Shanmasika as the crucial foundation for the 6-month rites.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.58-65), Dharmasindhu III",
            classicalVerse = "विचित्रनगरं रम्यं यमभ्रातृप्रशासितम् । षाण्मासिकेन पिण्डेन सुखेन प्रतिपद्यते ॥"
        ),
        "una_shanmasika" to EducationalCeremonyInfo(
            ceremonyKey = "una_shanmasika",
            titleEnglish = "Una-Shanmasika (~164th Day / Godāna)",
            titleSanskrit = "ऊनषाण्मासिकम् (Una-Shanmasikam with Vaitarani Godana)",
            dayTiming = "Observed prior to 6 months completion (~Day 163-170 with Godana)",
            soulJourneyStation = "Crossing the Ferocious Vaitaraṇī River",
            stationDescription = "The soul confronts the dreadful Vaitaraṇī (Vaitarani) river—a 100-Yojana wide boiling torrent of blood, pus, bones, and fire that cannot be crossed on foot.",
            pretaConditionAndYatanaDeha = "Souls without merit sink into the boiling torrent, bitten by aquatic monsters and burned by fiery currents, experiencing acute agony.",
            pindaSignificanceAndRelief = "Performing Una-Shanmasika accompanied by Vaitaraṇī Godāna (cow donation or sacred symbolic Sankalpa) manifests the celestial cow whose tail the soul clings to, gliding safely across the river into Vahnibhavana.",
            spiritualSignificance = "The supreme rescue ceremony of the first year, universally recognized as the single most critical expiation rite in Hindu eschatology.",
            whyNeeded = "Garuda Purana (Preta Khanda Ch. 4 & 5.66-75) explicitly declares that without Vaitarani Godana and Una-Shanmasika, souls are swept into the agonizing currents.",
            scripturalCitation = "Garuda Purana (Preta Khanda Chapters 4 & 5.66-75), Dharmasindhu III, Smriti Muktavali",
            classicalVerse = "धेनुर्वैतरणी नाम ततो वैतरणीं नदीम् । तर्तुं ददाति यां विप्राय सा तं तारयति ध्रुवम् ॥\nऊनषाण्मासिके काले गोदानं यः समाचरेत् । स तीर्त्वा वैतरणीं घोरां यमलोकं सुखं व्रजेत् ॥"
        ),
        "saptama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "saptama_masika",
            titleEnglish = "Saptama Masika (7th Month Tithi)",
            titleSanskrit = "सप्तममासिकम् (Saptama Masikam)",
            dayTiming = "Observed on the 7th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Traversing Bahwāpadmapura",
            stationDescription = "A challenging territory beyond the Vaitarani characterized by squalls, dense thickets, and stinging insects.",
            pretaConditionAndYatanaDeha = "The subtle body experiences lingering exhaustion from the Vaitarani crossing while marching through rough terrain under biting winds.",
            pindaSignificanceAndRelief = "The Saptama Masika Piṇḍa acts as a soothing balm for subtle tissues, restoring vigor and maintaining unbroken monthly nourishment.",
            spiritualSignificance = "Ensures steady post-Vaitarani progress towards the southern gateways.",
            whyNeeded = "Garuda Purana (5.76-82) underscores the need for continuous nourishment as the soul navigates the second half of the path.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.76-82), Nirnayasindhu III",
            classicalVerse = "बह्वापदपुरे घोरे नानाबाधासमन्विते । सप्तमे मासि पिण्डेन जीवः सुखमवाप्नुयात् ॥"
        ),
        "ashtama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "ashtama_masika",
            titleEnglish = "Ashtama Masika (8th Month Tithi)",
            titleSanskrit = "अष्टममासिकम् (Ashtama Masikam)",
            dayTiming = "Observed on the 8th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Passing through Duḥkhadapura (City of Remorse)",
            stationDescription = "A quiet region where the soul reflects deeply on its earthly lifetime, unfulfilled desires, karmic mistakes, and separated loved ones.",
            pretaConditionAndYatanaDeha = "The soul suffers acute psychological remorse (Paścāttāpa) and mental sorrow looking back at past lifetimes.",
            pindaSignificanceAndRelief = "The Ashtama Masika Piṇḍa brings deep spiritual peace (Śānti), pacifies mental anguish, and cleanses psychological distress through descendant prayers.",
            spiritualSignificance = "Soothes the conscience and frees the soul from emotional agitation.",
            whyNeeded = "Garuda Purana (5.83-89) highlights this ceremony as essential for spiritual solace and moral purification.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.83-89), Dharmasindhu III",
            classicalVerse = "दुःखदे नगरे प्राप्ते शोकेनाभिप्लुतो भवेत् । अष्टमे मासि पिण्डेन शोकमुत्सृज्य गच्छति ॥"
        ),
        "navama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "navama_masika",
            titleEnglish = "Navama Masika (9th Month Tithi)",
            titleSanskrit = "नवममासिकम् (Navama Masikam)",
            dayTiming = "Observed on the 9th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Passing Nānākrandanapura (Territory of Cries)",
            stationDescription = "A region where the wailing cries of other unliberated souls echo continuously.",
            pretaConditionAndYatanaDeha = "The traveler is exposed to disheartening sights and sounds of suffering souls, which can induce dread and spiritual despair.",
            pindaSignificanceAndRelief = "The Navama Masika Piṇḍa fortifies inner courage, protects against despair, and shields the soul from external distress.",
            spiritualSignificance = "Instills emotional resilience and focus as the final destination draws nearer.",
            whyNeeded = "Garuda Purana (5.90-96) explains that filial prayers safeguard the traveler from astral despondency.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.90-96), Smriti Muktavali",
            classicalVerse = "नानाक्रन्दपुरे घोरे क्रन्दमानेषु सर्वशः । नवमे मासि दानेन निर्भयो याति तत्पथम् ॥"
        ),
        "adhika_masika" to EducationalCeremonyInfo(
            ceremonyKey = "adhika_masika",
            titleEnglish = "Adhika Masika (Intercalary Month Tithi)",
            titleSanskrit = "अधिकमासिकम् (Adhika Masikam)",
            dayTiming = "Observed during an Adhika Masa on the recurring death Tithi",
            soulJourneyStation = "Traversing Adhika Maṇḍala in Yama Marga",
            stationDescription = "An intercalary cosmic sector traversed when an extra lunar month (Adhika Masa) occurs within the first year of demise.",
            pretaConditionAndYatanaDeha = "Because the afterlife journey spans 13 lunar cycles instead of 12 in an Adhika year, skipping monthly offerings would subject the Yātanā Śarīra to a month-long deprivation of pinda sustenance.",
            pindaSignificanceAndRelief = "The Adhika Masika Piṇḍa provides dedicated, uninterrupted subtle nourishment across the 13th lunar cycle, ensuring the traveler never suffers starvation while awaiting the true Nija anniversary.",
            spiritualSignificance = "Fulfills the canonical requirement of Dharmasindhu and Nirnayasindhu that monthly Shraddhas must never be omitted during leap months in Year 1.",
            whyNeeded = "Dharmasindhu III and Nirnayasindhu III explicitly mandate: 'अधिकेऽपि मासिकं श्राद्धं कर्तव्यमेव' — monthly offerings must not be skipped during Adhika Masa in Year 1.",
            scripturalCitation = "Dharmasindhu III (Adhikasamvatsara Shraddha Prakarana), Nirnayasindhu III",
            classicalVerse = "अधिकेऽपि च कर्तव्यं मासिकं प्रथमं तथा । न त्यजेन्मासिकं श्राद्धं वृद्धिहास्योः कदाचन ॥\nप्रतिमासं प्रदातव्यं प्रेतस्याप्यायनं महत् । त्रयोदशेऽपि मासे तु तृप्तिर्भवति शाश्वती ॥"
        ),
        "dashama_masika" to EducationalCeremonyInfo(
            ceremonyKey = "dashama_masika",
            titleEnglish = "Dashama Masika (10th Month Tithi)",
            titleSanskrit = "दशममासिकम् (Dashama Masikam)",
            dayTiming = "Observed on the 10th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Crossing Sutaptabhavanapura (Intense Heat Zone)",
            stationDescription = "Approaching the southern mountain corridors under intense thermal waves where the ground radiates like molten metal.",
            pretaConditionAndYatanaDeha = "The subtle body suffers from intense heat radiating from the ground, causing exhaustion and extreme fatigue in the subtle limbs.",
            pindaSignificanceAndRelief = "The Dashama Masika Piṇḍa and cooling water offerings (Udaka) soothe the soles and limbs, protecting the soul from thermal collapse.",
            spiritualSignificance = "Cools and shields the subtle body during the extreme high-heat corridor of the path.",
            whyNeeded = "Garuda Purana (5.97-103) highlights this station as the supreme thermal test of the afterlife journey.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.97-103), Dharmasindhu III",
            classicalVerse = "सुतप्तभवने तप्ते भूमौ प्रतप्तवालुके । दशमे मासि पिण्डेन शीतलं लभते पदम् ॥"
        ),
        "ekadasha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "ekadasha_masika",
            titleEnglish = "Ekadasha Masika (11th Month Tithi)",
            titleSanskrit = "एकादशमासिकम् (Ekadasha Masikam)",
            dayTiming = "Observed on the 11th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Crossing Raudrapura (Stormy Frontier)",
            stationDescription = "A dark, tempestuous corridor with thunderbolts and heavy winds enveloping Raudra Pura as the soul approaches the final frontier.",
            pretaConditionAndYatanaDeha = "The Yātanā Śarīra navigates through pitch darkness, roaring gales, and thunderbolts that test its final reserves of stamina.",
            pindaSignificanceAndRelief = "The Ekadasha Masika Piṇḍa radiates subtle light (Tejas) that dispels darkness, stills turbulent winds, and guides the traveler securely through the storm.",
            spiritualSignificance = "Guides the soul through the penultimate obstacle corridor before entering the capital outskirts.",
            whyNeeded = "Garuda Purana (5.104-110) states that the merit of this offering illuminates the path through Raudra Pura.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.104-110), Nirnayasindhu III",
            classicalVerse = "रौद्रे पुरे महाघोरे तमसा संवृते भृशम् । एकादशे तु पिण्डेन प्रकाशं लभते शुभम् ॥"
        ),
        "unabdika" to EducationalCeremonyInfo(
            ceremonyKey = "unabdika",
            titleEnglish = "Unabdika (~351st Day / Ūna-Vārṣika)",
            titleSanskrit = "ऊनाब्दिकम् (Unabdikam / ऊनवार्षिकम्)",
            dayTiming = "Observed prior to 1 year completion (~Day 345-351)",
            soulJourneyStation = "Outer Gates of Samyamani Puri",
            stationDescription = "The grand entrance gates of Lord Dharmaraja's celestial capital, Samyamani Puri, just before the full 360-day year closes.",
            pretaConditionAndYatanaDeha = "The soul reaches the end of the 86,000-Yojana journey. The Yātanā Śarīra prepares for complete dissolution and formal transition into Pitru Loka.",
            pindaSignificanceAndRelief = "The Unabdika Piṇḍa concludes all preliminary travel rites, washes away residual traveler fatigue, and purifies the subtle body for the sacred merger of Sapiṇḍīkaraṇa.",
            spiritualSignificance = "Mandatory preliminary milestone before Prathama Varshika and Sapindikarana can be performed.",
            whyNeeded = "Garuda Purana (5.111-120) and Smriti Muktavali state that without Unabdika, the annual Sapindikarana cannot formally take place.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.111-120), Dharmasindhu III, Smriti Muktavali",
            classicalVerse = "ऊनाब्दिकविधानेन वर्षपूर्तेः पुरोधसः । संयमनीपुरद्वारि विश्रान्तिं लभते पराम् ॥"
        ),
        "dvadasha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "dvadasha_masika",
            titleEnglish = "Dvadasha Masika (12th Month Tithi)",
            titleSanskrit = "द्वादशमासिकम् (Dvadasha Masikam)",
            dayTiming = "Observed on the 12th month recurring death Tithi during Aparahna",
            soulJourneyStation = "Arrival at Payovarṣaṇapura (Gardens of Dharmaraja)",
            stationDescription = "The serene, rain-cooled gardens outside Samyamani Puri directly adjacent to Dharmaraja's Dharmasabhā.",
            pretaConditionAndYatanaDeha = "The subtle body rests peacefully in pleasant surroundings, bathed in gentle celestial showers that completely soothe all past travel weariness.",
            pindaSignificanceAndRelief = "The Dvadasha Masika Piṇḍa concludes the 12 regular monthly lunar offerings, granting complete satiety and serene composure before entering the divine assembly.",
            spiritualSignificance = "Completes the full cycle of 12 monthly lunar offerings across the first year.",
            whyNeeded = "Garuda Purana (5.121-128) declares this the culminating regular monthly offering of the afterlife journey.",
            scripturalCitation = "Garuda Purana (Preta Khanda 5.121-128), Dharmasindhu III",
            classicalVerse = "पयोवर्षणमासाद्य द्वादशे मासि मानवः । पिण्डदानेन सम्पूर्णस्तृप्तिं प्राप्नोत्यनुत्तमाम् ॥"
        ),
        "trayodasha_masika" to EducationalCeremonyInfo(
            ceremonyKey = "trayodasha_masika",
            titleEnglish = "Trayodasha Masika (13th Month Tithi / Adhika)",
            titleSanskrit = "त्रयोदशमासिकम् (Trayodasha Masikam)",
            dayTiming = "Observed during an Adhika Masa on the recurring death Tithi",
            soulJourneyStation = "Traversing Adhika Maṇḍala in Yama Marga",
            stationDescription = "An intercalary station traversed during leap months (Adhika Masa) where special lunar expiation offerings are made.",
            pretaConditionAndYatanaDeha = "The soul journeys through the 13th lunar sector during an Adhika year, requiring continuous monthly pinda nourishment.",
            pindaSignificanceAndRelief = "The Trayodasha Masika Piṇḍa provides uninterrupted subtle nourishment, ensuring the traveler never suffers starvation during the 13-month cycle.",
            spiritualSignificance = "Maintains alignment between the lunar and solar year without interrupting the soul's monthly nourishment.",
            whyNeeded = "Dharmasindhu III and Nirnayasindhu III prescribe an additional 13th Masika when an Adhika Masa occurs within the first year of demise.",
            scripturalCitation = "Dharmasindhu III (Adhikasamvatsara Shraddha Prakarana), Nirnayasindhu III",
            classicalVerse = "अधिकेऽपि च कर्तव्यं मासिकं प्रथमं तथा । न त्यजेन्मासिकं श्राद्धं वृद्धिहास्योः कदाचन ॥"
        ),
        "prathama_varshika" to EducationalCeremonyInfo(
            ceremonyKey = "prathama_varshika",
            titleEnglish = "Prathama Varshika Shraddha & Sapiṇḍīkaraṇa",
            titleSanskrit = "प्रथमवार्षिकश्राद्धम् सपिण्डीकरणम् च",
            dayTiming = "Observed on the exact 1st annual death anniversary during Aparahna Kala",
            soulJourneyStation = "Dissolution of Preta State & Union with Divine Pitrus",
            stationDescription = "The soul reaches the celestial assembly hall (Dharmasabhā) in Samyamani Puri. Through the sacred rite of Sapiṇḍīkaraṇa, the individual Preta Piṇḍa is divided into three equal portions and mixed into the three ancestral Piṇḍas (Father, Grandfather, Great-grandfather).",
            pretaConditionAndYatanaDeha = "At the precise moment of Sapiṇḍīkaraṇa, the ghostly Preta state (Preta Avastha) is permanently dissolved. The soul sheds the Yātanā Śarīra and attains a radiant subtle form (Divya Deha) in Pitru Loka.",
            pindaSignificanceAndRelief = "The union of the Piṇḍas (Piṇḍaikya) elevates the departed soul to the exalted status of Vasu-Rudra-Aditya Swaroopa, granting eternal entry into the realm of the Pitrus under Sri Hari's divine grace.",
            spiritualSignificance = "The supreme spiritual milestone of the entire bereavement lifecycle, liberating the departed soul from unanchored wanderer status into honoured ancestor-hood.",
            whyNeeded = "Baudhayana Pitrimedha Sutras and Garuda Purana (Preta Khanda Ch. 14) declare: Without Prathama Varshika and Sapindikarana, the soul remains trapped in Preta Avastha and cannot enter Pitru Loka.",
            scripturalCitation = "Garuda Purana (Preta Khanda Ch. 14, v. 1-25), Baudhayana Pitrimedha Sutras, Dharmasindhu III",
            classicalVerse = "संयोज्य पितृभिः सार्धं प्रेतत्वं विनिवर्तते । पिण्डैक्येन यतो मुक्तिः पितृलोकं स गच्छति ॥\nवसुरूपधरा माता पिता च पितृरूपधृत् । सपिण्डीकरणे जाते पितृलोके महीयते ॥"
        ),
        "annual_varshika" to EducationalCeremonyInfo(
            ceremonyKey = "annual_varshika",
            titleEnglish = "Annual Varshika Shraddha (Pratyābdika Shraddham)",
            titleSanskrit = "वार्षिकश्राद्धम् (Pratyabdika Shraddham)",
            dayTiming = "Observed every year on the target death month & Tithi during Aparahna Kala",
            soulJourneyStation = "Eternal Abode in Pitru Loka under Sri Hari's Grace",
            stationDescription = "The elevated ancestor resides peacefully in Pitru Loka under the grace of Lord Sri Janardana-Vasudeva (the Antaryami / inner controller of all Pitrus).",
            pretaConditionAndYatanaDeha = "The ancestor enjoys divine celestial happiness in Pitru Loka, looking forward each year to the filial offerings of descendants on earth.",
            pindaSignificanceAndRelief = "Annual offering of Pārvaṇa Piṇḍas, Brahmana Bhojana, and Tarpana pleases Lord Sri Hari, who blesses the family with longevity (Āyu), progeny (Santāna-vṛddhi), health (Ārogya), and prosperity (Kalyāṇa).",
            spiritualSignificance = "Discharges the eternal filial debt (Pitru Ṛṇa) and secures the continuous divine blessings of three generations of ancestors.",
            whyNeeded = "Manu Smriti (3.266-275) states that ancestors eagerly await the annual offering from their descendants to shower them with blessings.",
            scripturalCitation = "Manu Smriti (3.266-275), Smriti Muktavali, Dharmasindhu III",
            classicalVerse = "हव्यं कव्यं च दातव्यं पितृभ्यो विधिवत् सदा । प्रीताः कुर्वन्ति ते पुष्टिं आयुः प्रज्ञां बलं यशः ॥\nआयुः प्रजां धनं विद्यां स्वर्गं मोक्षं सुखानि च । प्रयच्छन्ति तथा राज्यं प्रीता नृणां पितामहाः ॥"
        ),
        "mahalaya_paksha" to EducationalCeremonyInfo(
            ceremonyKey = "mahalaya_paksha",
            titleEnglish = "Mahalaya Paksha Shraddha (Pitru Paksha)",
            titleSanskrit = "महालयपक्षश्राद्धम् (Mahalaya Parvanam)",
            dayTiming = "Observed during Bhadrapada Krishna Paksha (Pitru Paksha) on the death Tithi",
            soulJourneyStation = "Earthly Visitation during Pitru Paksha",
            stationDescription = "When the Sun enters Kanya Rashi, Lord Yama permits all ancestral souls to visit the earth to receive offerings from their descendants.",
            pretaConditionAndYatanaDeha = "Ancestral spirits visit the earth realm in subtle form during the sacred fortnight, seeking water and pinda offerings from their descendants.",
            pindaSignificanceAndRelief = "Performing Parvana Shraddha on the death Tithi during Mahalaya satisfies the entire lineage (Paternal & Maternal lines, Gurus, and all departed relations) simultaneously.",
            spiritualSignificance = "The supreme collective ancestral festival; missing it causes spiritual debt to the Pitrus. (Note: Not applicable in Year 1 prior to Sapindikarana).",
            whyNeeded = "Nirnaya Sindhu and Dharma Sindhu state that Mahalaya is the supreme collective ancestral festival; fulfilling it ensures peace for all departed souls.",
            scripturalCitation = "Nirnaya Sindhu (Mahalaya Prakarana), Dharmasindhu III, Smriti Muktavali",
            classicalVerse = "कन्यागते सवितरि पितरो यान्ति मानवान् । तत्र श्राद्धं प्रकुर्वीत तृप्त्यर्थं सर्वदेहिनाम् ॥"
        )
    )

    fun findInfoForEvent(event: ShraddhaEvent): EducationalCeremonyInfo? {
        return when (event.type) {
            ShraddhaType.VARSHIKA -> {
                val lower = event.traditionalName.lowercase()
                if (lower.contains("prathama") || lower.contains("1st") || lower.contains("1ನೇ") || lower.contains("1వ") || lower.contains("1ஆம்") || lower.contains("ಪ್ರಥಮ") || lower.contains("प्रथम") || lower.contains("ప్రథమ") || lower.contains("ப்ரதம")) {
                    CONTENT_MAP["prathama_varshika"]
                } else {
                    CONTENT_MAP["annual_varshika"]
                }
            }
            ShraddhaType.MAHALAYA_PAKSHA -> CONTENT_MAP["mahalaya_paksha"]
            ShraddhaType.UNA_RITE,
            ShraddhaType.MASIKA -> {
                if (event.tithi.isAdhikaMasa || event.traditionalName.contains("adhika", ignoreCase = true) || event.traditionalName.contains("ಅಧಿಕ") || event.traditionalName.contains("अधिक") || event.traditionalName.contains("అధిక") || event.traditionalName.contains("அதிக")) {
                    CONTENT_MAP["adhika_masika"]
                } else {
                    findInfoForEvent(event.traditionalName)
                }
            }
        }
    }

    fun getInfo(ceremonyKey: String): EducationalCeremonyInfo? {
        return CONTENT_MAP[ceremonyKey.lowercase()]
    }

    fun findInfoForEvent(traditionalName: String): EducationalCeremonyInfo? {
        val lower = traditionalName.lowercase()
        return when {
            // Adhika Masika check FIRST before any sequence match
            lower.contains("adhika") || lower.contains("ಅಧಿಕ") || lower.contains("अधिक") || lower.contains("అధిక") || lower.contains("அதிக") -> CONTENT_MAP["adhika_masika"]
            
            // Unabdika check
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
            
            // Specific Masikas by name
            lower.contains("adya") || lower.contains("ಆದ್ಯ") || lower.contains("आद्य") || lower.contains("ఆద్య") || lower.contains("ஆத்ய") -> CONTENT_MAP["adya_masika"]
            lower.contains("unmasika") || lower.contains("ಊನಮಾಸಿಕ") || lower.contains("ऊनमासिक") || lower.contains("ఊనమాసిక") || lower.contains("ஊநமாஸிக") -> CONTENT_MAP["unmasika"]
            lower.contains("traipakshika") || lower.contains("ತ್ರೈಪಕ್ಷಿಕ") || lower.contains("त्रैपाक्षिक") || lower.contains("త్రైపాక్షిక") || lower.contains("த்ரைபாக்ஷிக") -> CONTENT_MAP["traipakshika"]
            lower.contains("una-shanmasika") || lower.contains("una shanmasika") || lower.contains("godana") || lower.contains("ಊನಷಾಣ್ಮಾಸಿಕ") || lower.contains("ऊनषाण्मासिक") || lower.contains("ఊనషాణ్ಮಾಸಿಕ") || lower.contains("ஊநஷாண்மாஸிக") -> CONTENT_MAP["una_shanmasika"]
            lower.contains("trayodasha") || lower.contains("ತ್ರಯೋದಶ") || lower.contains("त्रयोदश") || lower.contains("త్రయోదశ") || lower.contains("த்ரயோதச") -> CONTENT_MAP["trayodasha_masika"] ?: CONTENT_MAP["adhika_masika"]
            lower.contains("dvadasha") || lower.contains("ದ್ವಾದಶ") || lower.contains("द्वादश") || lower.contains("ద్వాదశ") || lower.contains("த்வாதச") -> CONTENT_MAP["dvadasha_masika"]
            lower.contains("ekadasha") || lower.contains("ಏಕಾದಶ") || lower.contains("एकादश") || lower.contains("ఏకాదశ") || lower.contains("ஏகாதச") -> CONTENT_MAP["ekadasha_masika"]
            lower.contains("dashama") || lower.contains("ದಶಮ") || lower.contains("दशम") || lower.contains("దశమ") || lower.contains("தசம") -> CONTENT_MAP["dashama_masika"]
            lower.contains("navama") || lower.contains("ನವಮ") || lower.contains("नवम") || lower.contains("నవಮ") || lower.contains("நவம") -> CONTENT_MAP["navama_masika"]
            lower.contains("ashtama") || lower.contains("ಅಷ್ಟಮ") || lower.contains("अष्टम") || lower.contains("అష్టಮ") || lower.contains("அஷ்டம") -> CONTENT_MAP["ashtama_masika"]
            lower.contains("saptama") || lower.contains("ಸಪ್ತಮ") || lower.contains("सप्तम") || lower.contains("సప్తమ") || lower.contains("ஸப்தம") -> CONTENT_MAP["saptama_masika"]
            lower.contains("shashtha") || lower.contains("shanmasika") || lower.contains("ಷಷ್ಠ") || lower.contains("षष्ठ") || lower.contains("షష్ఠ") || lower.contains("ஷஷ்ட") -> CONTENT_MAP["shashtha_masika"]
            lower.contains("panchama") || lower.contains("ಪಂಚಮ") || lower.contains("पञ्चम") || lower.contains("పంచమ") || lower.contains("பஞ்சம") -> CONTENT_MAP["panchama_masika"]
            lower.contains("chaturtha") || lower.contains("ಚತುರ್ಥ") || lower.contains("चतुर्थ") || lower.contains("చతుర్థ") || lower.contains("சதுர்த்த") -> CONTENT_MAP["chaturtha_masika"]
            lower.contains("tritiya") || lower.contains("ತೃತೀಯ") || lower.contains("तृतीय") || lower.contains("తృతೀಯ") || lower.contains("திருதீய") -> CONTENT_MAP["tritiya_masika"]
            lower.contains("dwitiya") || lower.contains("dvitiya") || lower.contains("ದ್ವಿತೀಯ") || lower.contains("द्वितीय") || lower.contains("ద్వితీయ") || lower.contains("த்விதீய") -> CONTENT_MAP["dvitiya_masika"]
            
            // Fallback by sequence regex
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
                        17 -> CONTENT_MAP["dvadasha_masika"]
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
