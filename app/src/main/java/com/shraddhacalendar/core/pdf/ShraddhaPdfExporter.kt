package com.shraddhacalendar.core.pdf

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter

/**
 * High-quality, print-ready, multi-lingual PDF generator for Shraddha Calendar.
 * Strictly adheres to the opening devotional invocation and 3-step dedication order.
 */
object ShraddhaPdfExporter {

    private const val PAGE_WIDTH = 595 // A4 standard width in points (72 dpi)
    private const val PAGE_HEIGHT = 842 // A4 standard height in points
    private const val MARGIN = 36f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)

    fun generateAndSharePdf(
        context: Context,
        result: ShraddhaCalculationResult,
        language: AppLanguage
    ): File {
        val pdfFile = createPdfFile(context, result)
        val document = PdfDocument()

        val pages = mutableListOf<PdfDocument.Page>()
        var currentPageNum = 1

        // Colors
        val primarySaffron = Color.rgb(201, 107, 26)
        val saffronDark = Color.rgb(156, 75, 9)
        val textPrimary = Color.rgb(44, 30, 20)
        val textSecondary = Color.rgb(107, 93, 83)
        val surfaceCardBg = Color.rgb(254, 252, 248)
        val cardBorderColor = Color.rgb(240, 226, 210)
        val rowAltBg = Color.rgb(250, 246, 240)

        // Text Paints
        val invocationPaint = TextPaint().apply {
            color = saffronDark
            textSize = 12.5f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val titlePaint = TextPaint().apply {
            color = saffronDark
            textSize = 17f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = TextPaint().apply {
            color = textSecondary
            textSize = 10f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val sectionHeadingPaint = TextPaint().apply {
            color = saffronDark
            textSize = 12f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val tableHeaderPaint = TextPaint().apply {
            color = Color.WHITE
            textSize = 9.5f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val tableCellPaint = TextPaint().apply {
            color = textPrimary
            textSize = 8.8f
            isAntiAlias = true
        }

        val tableCellBoldPaint = TextPaint().apply {
            color = textPrimary
            textSize = 8.8f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val dedicationTextPaint = TextPaint().apply {
            color = textSecondary
            textSize = 9.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val dedicationBoldPaint = TextPaint().apply {
            color = saffronDark
            textSize = 10.5f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val pageFooterPaint = TextPaint().apply {
            color = textSecondary
            textSize = 8f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        // --- PAGE 1 SETUP ---
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var currentY = MARGIN

        fun drawPageHeader() {
            // 1. Top Dedication Banner Box (Matching Application Top Banner)
            val bannerRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 76f)
            val bannerBgPaint = Paint().apply {
                color = surfaceCardBg
                style = Paint.Style.FILL
            }
            val bannerBorderPaint = Paint().apply {
                color = cardBorderColor
                style = Paint.Style.STROKE
                strokeWidth = 1f
            }
            canvas.drawRoundRect(bannerRect, 8f, 8f, bannerBgPaint)
            canvas.drawRoundRect(bannerRect, 8f, 8f, bannerBorderPaint)

            var bannerY = currentY + 14f

            // Devotional Invocation
            val invocation = "🕉️ " + context.getString(R.string.invocation_header) + " 🕉️"
            canvas.drawText(invocation, PAGE_WIDTH / 2f, bannerY, invocationPaint)
            bannerY += 7f

            // Subtle divider inside banner
            val bannerLinePaint = Paint().apply {
                color = Color.argb(60, 201, 107, 26)
                strokeWidth = 0.8f
            }
            canvas.drawLine(MARGIN + 60f, bannerY, PAGE_WIDTH - MARGIN - 60f, bannerY, bannerLinePaint)
            bannerY += 13f

            // Step 1: Dedication to Sri Hari, Sri Vayu, and Uttaradi Math Parampara
            val dedService = context.getString(R.string.dedication_service)
            canvas.drawText(dedService, PAGE_WIDTH / 2f, bannerY, dedicationTextPaint)
            bannerY += 14f

            // Step 2: In Loving Memory of Father
            val dedFather = "🌸 " + context.getString(R.string.dedication_father) + " 🌸"
            val dedFatherPaint = TextPaint().apply {
                color = textPrimary
                textSize = 9.5f
                isFakeBoldText = true
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(dedFather, PAGE_WIDTH / 2f, bannerY, dedFatherPaint)
            bannerY += 14f

            // Step 3: Developed and managed by Gururaj Kulkarni
            val developedBy = context.getString(R.string.developed_by)
            canvas.drawText(developedBy, PAGE_WIDTH / 2f, bannerY, dedicationBoldPaint)

            currentY += 86f

            // 2. Application Title & Subtitle
            val appTitle = context.getString(R.string.app_name)
            val appSubtitle = context.getString(R.string.app_subtitle)
            canvas.drawText(appTitle, PAGE_WIDTH / 2f, currentY + 12f, titlePaint)
            currentY += 18f
            canvas.drawText(appSubtitle, PAGE_WIDTH / 2f, currentY + 8f, subtitlePaint)
            currentY += 14f

            // Saffron Divider line below title
            val linePaint = Paint().apply {
                color = primarySaffron
                strokeWidth = 1.5f
                isAntiAlias = true
            }
            canvas.drawLine(MARGIN + 40f, currentY, PAGE_WIDTH - MARGIN - 40f, currentY, linePaint)
            currentY += 12f
        }

        drawPageHeader()

        // 3. Person & Demise Details Card
        val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 68f)
        val cardBgPaint = Paint().apply {
            color = surfaceCardBg
            style = Paint.Style.FILL
        }
        val cardBorderPaint = Paint().apply {
            color = cardBorderColor
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        canvas.drawRoundRect(cardRect, 8f, 8f, cardBgPaint)
        canvas.drawRoundRect(cardRect, 8f, 8f, cardBorderPaint)

        val col1X = MARGIN + 12f
        val col2X = MARGIN + CONTENT_WIDTH / 2f + 6f
        var cardY = currentY + 14f

        val person = result.personRecord
        val localizedPersonName = PanchangaLocalizer.localizePersonName(person.name, language)
        val localizedLocation = PanchangaLocalizer.localizeLocation(person.location.displayName, language)
        val fullPanchanga = PanchangaLocalizer.localizeFullPanchanga(result.mrutaTithi, language)

        val nameLabel = when (language) {
            AppLanguage.KANNADA -> "ಹೆಸರು"
            AppLanguage.SANSKRIT -> "नाम"
            AppLanguage.TELUGU -> "పేరు"
            AppLanguage.TAMIL -> "பெயர்"
            AppLanguage.ENGLISH -> "Name"
        }
        val locLabel = when (language) {
            AppLanguage.KANNADA -> "ಸ್ಥಳ"
            AppLanguage.SANSKRIT -> "स्थानम्"
            AppLanguage.TELUGU -> "స్థలము"
            AppLanguage.TAMIL -> "இடம்"
            AppLanguage.ENGLISH -> "Location"
        }
        val deathLabel = when (language) {
            AppLanguage.KANNADA -> "ಮೃತ್ಯು ದಿನಾಂಕ"
            AppLanguage.SANSKRIT -> "मृत्युदिनम्"
            AppLanguage.TELUGU -> "మరణ దినము"
            AppLanguage.TAMIL -> "இறப்பு"
            AppLanguage.ENGLISH -> "Death"
        }
        val tithiLabel = when (language) {
            AppLanguage.KANNADA -> "ತಿಥಿ"
            AppLanguage.SANSKRIT -> "तिथिः"
            AppLanguage.TELUGU -> "తిథి"
            AppLanguage.TAMIL -> "திதி"
            AppLanguage.ENGLISH -> "Tithi"
        }

        canvas.drawText("$nameLabel: $localizedPersonName", col1X, cardY, tableCellBoldPaint)
        canvas.drawText("$locLabel: $localizedLocation", col2X, cardY, tableCellPaint)
        cardY += 14f

        val deathDateFormatted = person.deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val deathTimeFormatted = person.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
        canvas.drawText("$deathLabel: $deathDateFormatted ($deathTimeFormatted)", col1X, cardY, tableCellPaint)
        canvas.drawText("$tithiLabel: $fullPanchanga", col2X, cardY, tableCellPaint)

        currentY += 78f

        // Helper to check for new page
        fun ensureSpace(neededHeight: Float) {
            if (currentY + neededHeight > PAGE_HEIGHT - MARGIN - 30f) {
                // Draw footer for current page
                val hariVayu = context.getString(R.string.hari_vayu_footer)
                canvas.drawText(
                    hariVayu,
                    PAGE_WIDTH / 2f,
                    PAGE_HEIGHT - MARGIN - 4f,
                    dedicationBoldPaint
                )
                canvas.drawText(
                    "— Page $currentPageNum —",
                    PAGE_WIDTH / 2f,
                    PAGE_HEIGHT - MARGIN + 10f,
                    pageFooterPaint
                )
                document.finishPage(page)

                currentPageNum++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
                page = document.startPage(pageInfo)
                canvas = page.canvas
                currentY = MARGIN

                // Minimal Header on subsequent pages
                val topInv = "🕉️ " + context.getString(R.string.invocation_header) + " 🕉️"
                canvas.drawText(topInv, PAGE_WIDTH / 2f, currentY + 10f, invocationPaint)
                currentY += 22f

                val subLinePaint = Paint().apply {
                    color = primarySaffron
                    strokeWidth = 0.8f
                }
                canvas.drawLine(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY, subLinePaint)
                currentY += 12f
            }
        }

        // Draw Events Table
        result.yearlySections.forEach { section ->
            ensureSpace(50f)

            // Section Header
            val headerBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 20f)
            val headerBgPaint = Paint().apply {
                color = primarySaffron
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(headerBg, 4f, 4f, headerBgPaint)

            val sectionTitle = if (section.yearIndex == 1) {
                "Year 1 — Shodasha Masika & First Varshika Ceremonies (${section.yearTitle})"
            } else {
                "Year ${section.yearIndex} — Varshika Shraddha (${section.yearTitle})"
            }
            canvas.drawText(sectionTitle, MARGIN + 8f, currentY + 14f, tableHeaderPaint)
            currentY += 24f

            // Table Column Headers
            val colX_Name = MARGIN + 8f
            val colX_Date = MARGIN + 180f
            val colX_Tithi = MARGIN + 270f
            val colX_Aparahna = MARGIN + 390f

            val thBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 16f)
            val thPaint = Paint().apply {
                color = saffronDark
                style = Paint.Style.FILL
            }
            canvas.drawRect(thBg, thPaint)
            canvas.drawText("Ritual / Ceremony", colX_Name, currentY + 11.5f, tableHeaderPaint)
            canvas.drawText("Date & Day", colX_Date, currentY + 11.5f, tableHeaderPaint)
            canvas.drawText("Panchanga Tithi", colX_Tithi, currentY + 11.5f, tableHeaderPaint)
            canvas.drawText("Aparahna Window", colX_Aparahna, currentY + 11.5f, tableHeaderPaint)
            currentY += 16f

            // Rows
            section.events.forEachIndexed { idx, event ->
                ensureSpace(20f)

                val rowBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 18f)
                if (idx % 2 == 1) {
                    val rPaint = Paint().apply { color = rowAltBg; style = Paint.Style.FILL }
                    canvas.drawRect(rowBg, rPaint)
                }
                val rBorder = Paint().apply { color = cardBorderColor; style = Paint.Style.STROKE; strokeWidth = 0.5f }
                canvas.drawRect(rowBg, rBorder)

                val localizedName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
                val localizedDate = "${event.gregorianDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} (${event.dayOfWeek.take(3)})"
                val localizedMasa = PanchangaLocalizer.localizeMasa(event.tithi.masa, event.tithi.isAdhikaMasa, language)
                val localizedT = PanchangaLocalizer.localizeTithi(event.tithi.tithi, language)
                val tithiShort = "$localizedMasa $localizedT"
                val aparahnaTime = "${event.kalaDetails.aparahnaStart} - ${event.kalaDetails.aparahnaEnd}"

                canvas.drawText(localizedName, colX_Name, currentY + 12f, tableCellBoldPaint)
                canvas.drawText(localizedDate, colX_Date, currentY + 12f, tableCellPaint)
                canvas.drawText(tithiShort, colX_Tithi, currentY + 12f, tableCellPaint)
                canvas.drawText(aparahnaTime, colX_Aparahna, currentY + 12f, tableCellPaint)

                currentY += 18f
            }
            currentY += 12f
        }

        // --- CLOSING DEVOTIONAL FOOTER (Hari Sarvottama Vayu Jeevottama) ---
        val hariVayuClosing = context.getString(R.string.hari_vayu_footer)
        canvas.drawText(
            hariVayuClosing,
            PAGE_WIDTH / 2f,
            PAGE_HEIGHT - MARGIN - 4f,
            dedicationBoldPaint
        )
        canvas.drawText(
            "— Page $currentPageNum —",
            PAGE_WIDTH / 2f,
            PAGE_HEIGHT - MARGIN + 10f,
            pageFooterPaint
        )
        document.finishPage(page)

        // Save PDF to file
        val outputStream = FileOutputStream(pdfFile)
        document.writeTo(outputStream)
        outputStream.flush()
        outputStream.close()
        document.close()

        return pdfFile
    }

    private fun createPdfFile(context: Context, result: ShraddhaCalculationResult): File {
        val pdfDir = File(context.cacheDir, "pdf").apply { if (!exists()) mkdirs() }
        val sanitizedName = result.personRecord.name.replace("\\s+".toRegex(), "_").lowercase()
        return File(pdfDir, "shraddha_calendar_${sanitizedName}.pdf")
    }

    fun getShareUri(context: Context, file: File): android.net.Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
