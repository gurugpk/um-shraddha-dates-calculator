package com.shraddhacalendar.core.pdf

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
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
 * High-quality, print-ready, multi-lingual PDF generator for Madwa Shraddha Thithi Calculator.
 * Strictly adheres to multi-tradition devotional invocations, full chronological tables (Masikas, Varshikas, Mahalaya Pakshas),
 * Dosha considerations, and canonical disclaimers.
 */
object ShraddhaPdfExporter {

    private const val PAGE_WIDTH = 595 // A4 standard width in points (72 dpi)
    private const val PAGE_HEIGHT = 842 // A4 standard height in points
    private const val MARGIN = 36f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)

    fun generateAndSharePdf(
        context: Context,
        result: ShraddhaCalculationResult,
        language: AppLanguage,
        targetYearIndex: Int? = null
    ): File {
        val pdfFile = createPdfFile(context, result, targetYearIndex)
        val document = PdfDocument()

        var currentPageNum = 1

        // Colors
        val primarySaffron = Color.rgb(201, 107, 26)
        val saffronDark = Color.rgb(156, 75, 9)
        val textPrimary = Color.rgb(44, 30, 20)
        val textSecondary = Color.rgb(107, 93, 83)
        val surfaceCardBg = Color.rgb(254, 252, 248)
        val cardBorderColor = Color.rgb(240, 226, 210)
        val rowAltBg = Color.rgb(250, 246, 240)
        val highlightBg = Color.rgb(255, 243, 224)

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
            textSize = 16f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val subtitlePaint = TextPaint().apply {
            color = textSecondary
            textSize = 9.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val tableHeaderPaint = TextPaint().apply {
            color = Color.WHITE
            textSize = 9f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val tableCellPaint = TextPaint().apply {
            color = textPrimary
            textSize = 8.5f
            isAntiAlias = true
        }

        val tableCellBoldPaint = TextPaint().apply {
            color = textPrimary
            textSize = 8.5f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val dedicationTextPaint = TextPaint().apply {
            color = textSecondary
            textSize = 9f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val dedicationBoldPaint = TextPaint().apply {
            color = saffronDark
            textSize = 10f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val pageFooterPaint = TextPaint().apply {
            color = textSecondary
            textSize = 7.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        // --- PAGE 1 SETUP ---
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var currentY = MARGIN

        val invocationText = when (result.tradition) {
            MadhwaTradition.UTTARADI_MATHA -> context.getString(R.string.invocation_um)
            MadhwaTradition.MANTRALAYA_MUTT -> context.getString(R.string.invocation_srs)
            MadhwaTradition.UDUPI_ASHTA_MATHA -> context.getString(R.string.invocation_udupi)
        }

        fun drawPageHeader() {
            // 1. Top Dedication Banner Box
            val bannerRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 74f)
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

            var bannerY = currentY + 13f

            // Devotional Invocation
            canvas.drawText("🕉️ $invocationText 🕉️", PAGE_WIDTH / 2f, bannerY, invocationPaint)
            bannerY += 6f

            // Subtle divider inside banner
            val bannerLinePaint = Paint().apply {
                color = Color.argb(60, 201, 107, 26)
                strokeWidth = 0.8f
            }
            canvas.drawLine(MARGIN + 60f, bannerY, PAGE_WIDTH - MARGIN - 60f, bannerY, bannerLinePaint)
            bannerY += 13f

            // Step 1: Dedication
            val dedService = context.getString(R.string.dedication_service)
            canvas.drawText(dedService, PAGE_WIDTH / 2f, bannerY, dedicationTextPaint)
            bannerY += 13f

            // Step 2: Father Memorial
            val dedFather = "🌸 " + context.getString(R.string.dedication_father) + " 🌸"
            val dedFatherPaint = TextPaint().apply {
                color = textPrimary
                textSize = 9f
                isFakeBoldText = true
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(dedFather, PAGE_WIDTH / 2f, bannerY, dedFatherPaint)
            bannerY += 13f

            // Step 3: Developer attribution
            val developedBy = context.getString(R.string.developed_by)
            canvas.drawText(developedBy, PAGE_WIDTH / 2f, bannerY, dedicationBoldPaint)

            currentY += 82f

            // 2. Application Title & Subtitle
            val appTitle = context.getString(R.string.app_name)
            val reportScope = if (targetYearIndex != null) "Year $targetYearIndex Report" else "Complete Observance Report"
            val appSubtitle = "${result.personRecord.tradition.displayNameEnglish} • $reportScope"
            canvas.drawText(appTitle, PAGE_WIDTH / 2f, currentY + 12f, titlePaint)
            currentY += 16f
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
        val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 70f)
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

        canvas.drawText("Name: $localizedPersonName (${person.relationship.displayNameEnglish})", col1X, cardY, tableCellBoldPaint)
        canvas.drawText("Location: $localizedLocation", col2X, cardY, tableCellPaint)
        cardY += 14f

        val deathDateFormatted = person.deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val deathTimeFormatted = person.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
        canvas.drawText("Death: $deathDateFormatted ($deathTimeFormatted)", col1X, cardY, tableCellPaint)
        canvas.drawText("Tradition: ${person.tradition.displayNameEnglish}", col2X, cardY, tableCellPaint)
        cardY += 14f

        canvas.drawText("Mruta Tithi: $fullPanchanga", col1X, cardY, tableCellBoldPaint)

        currentY += 80f

        // Helper to check for new page
        fun ensureSpace(neededHeight: Float) {
            if (currentY + neededHeight > PAGE_HEIGHT - MARGIN - 36f) {
                // Draw footer for current page
                val hariVayu = "॥ Śrī Hariḥ Sarvottamaḥ • Vāyuḥ Jīvottamaḥ ॥"
                canvas.drawText(
                    hariVayu,
                    PAGE_WIDTH / 2f,
                    PAGE_HEIGHT - MARGIN - 8f,
                    dedicationBoldPaint
                )
                canvas.drawText(
                    "— Page $currentPageNum —",
                    PAGE_WIDTH / 2f,
                    PAGE_HEIGHT - MARGIN + 6f,
                    pageFooterPaint
                )
                document.finishPage(page)

                currentPageNum++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
                page = document.startPage(pageInfo)
                canvas = page.canvas
                currentY = MARGIN

                // Minimal Header on subsequent pages
                val topInv = "🕉️ $invocationText 🕉️"
                canvas.drawText(topInv, PAGE_WIDTH / 2f, currentY + 10f, invocationPaint)
                currentY += 20f

                val subLinePaint = Paint().apply {
                    color = primarySaffron
                    strokeWidth = 0.8f
                }
                canvas.drawLine(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY, subLinePaint)
                currentY += 12f
            }
        }

        // Draw Dosha Consideration if present
        if (result.doshaEvaluation.hasDosha) {
            ensureSpace(40f)
            val doshaRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 34f)
            val doshaBg = Paint().apply { color = Color.rgb(255, 248, 231); style = Paint.Style.FILL }
            val doshaBorder = Paint().apply { color = primarySaffron; style = Paint.Style.STROKE; strokeWidth = 0.8f }
            canvas.drawRoundRect(doshaRect, 6f, 6f, doshaBg)
            canvas.drawRoundRect(doshaRect, 6f, 6f, doshaBorder)

            val d = result.doshaEvaluation.doshas.firstOrNull()
            val doshaTitle = "⚠️ Traditional Consideration: ${d?.title ?: "Dosha Identified"}"
            val doshaRemedy = "Remedy: ${d?.prescribedRemedy ?: "Consult Family Acharya"}"
            canvas.drawText(doshaTitle, MARGIN + 8f, currentY + 13f, tableCellBoldPaint)
            canvas.drawText(doshaRemedy, MARGIN + 8f, currentY + 26f, tableCellPaint)
            currentY += 40f
        }

        // Table Column X Positions
        val colX_Name = MARGIN + 8f
        val colX_Date = MARGIN + 180f
        val colX_Tithi = MARGIN + 270f
        val colX_Aparahna = MARGIN + 395f

        // Draw Events Table for each YearlyObservanceGroup (filtered if targetYearIndex is set)
        val groupsToRender = if (targetYearIndex != null) {
            result.yearlyObservanceGroups.filter { it.yearIndex == targetYearIndex }
        } else {
            result.yearlyObservanceGroups
        }

        groupsToRender.forEach { group ->
            ensureSpace(50f)

            // Section Header
            val headerBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 18f)
            val headerBgPaint = Paint().apply {
                color = primarySaffron
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(headerBg, 4f, 4f, headerBgPaint)
            val sectionTitle = "${group.yearTitle} (${group.samvatsaraName})"
            canvas.drawText(sectionTitle, MARGIN + 8f, currentY + 13f, tableHeaderPaint)
            currentY += 22f

            // Table Column Headers
            val thBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 15f)
            val thPaint = Paint().apply {
                color = saffronDark
                style = Paint.Style.FILL
            }
            canvas.drawRect(thBg, thPaint)
            canvas.drawText("Observance / Rite", colX_Name, currentY + 11f, tableHeaderPaint)
            canvas.drawText("Date & Day", colX_Date, currentY + 11f, tableHeaderPaint)
            canvas.drawText("Panchanga Tithi", colX_Tithi, currentY + 11f, tableHeaderPaint)
            canvas.drawText("Aparahna Window", colX_Aparahna, currentY + 11f, tableHeaderPaint)
            currentY += 15f

            // Flatten all events in group (Masikas + Varshika + Paksha)
            val groupEvents = mutableListOf<ShraddhaEvent>()
            groupEvents.addAll(group.masikas)
            groupEvents.add(group.varshikaEvent)
            if (group.pakshaEvent != null) groupEvents.add(group.pakshaEvent)

            groupEvents.forEachIndexed { idx, event ->
                ensureSpace(18f)

                val isUpcoming = result.nextUpcomingObservance != null &&
                        result.nextUpcomingObservance.gregorianDate == event.gregorianDate &&
                        result.nextUpcomingObservance.traditionalName == event.traditionalName

                val rowBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 16f)
                if (isUpcoming) {
                    val rPaint = Paint().apply { color = highlightBg; style = Paint.Style.FILL }
                    canvas.drawRect(rowBg, rPaint)
                } else if (idx % 2 == 1) {
                    val rPaint = Paint().apply { color = rowAltBg; style = Paint.Style.FILL }
                    canvas.drawRect(rowBg, rPaint)
                }
                val rBorder = Paint().apply { color = cardBorderColor; style = Paint.Style.STROKE; strokeWidth = 0.5f }
                canvas.drawRect(rowBg, rBorder)

                val localizedName = (if (isUpcoming) "★ " else "") + PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
                val localizedDate = "${event.gregorianDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} (${event.dayOfWeek.take(3)})"
                val localizedMasa = PanchangaLocalizer.localizeMasa(event.tithi.masa, event.tithi.isAdhikaMasa, language)
                val localizedT = PanchangaLocalizer.localizeTithi(event.tithi.tithi, language)
                val tithiShort = "$localizedMasa $localizedT"
                val aparahnaTime = "${event.kalaDetails.aparahnaStart} - ${event.kalaDetails.aparahnaEnd}"

                val paintToUse = if (isUpcoming) tableCellBoldPaint else (if (idx % 2 == 0) tableCellBoldPaint else tableCellPaint)

                canvas.drawText(localizedName, colX_Name, currentY + 11.5f, paintToUse)
                canvas.drawText(localizedDate, colX_Date, currentY + 11.5f, tableCellPaint)
                canvas.drawText(tithiShort, colX_Tithi, currentY + 11.5f, tableCellPaint)
                canvas.drawText(aparahnaTime, colX_Aparahna, currentY + 11.5f, tableCellPaint)

                currentY += 16f
            }

            if (group.yearIndex == 1 && group.pakshaNotApplicableReason != null) {
                ensureSpace(16f)
                val noteBg = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 14f)
                val nPaint = Paint().apply { color = Color.rgb(245, 245, 245); style = Paint.Style.FILL }
                canvas.drawRect(noteBg, nPaint)
                canvas.drawText("• Paksha: ${group.pakshaNotApplicableReason}", MARGIN + 8f, currentY + 10f, tableCellPaint)
                currentY += 16f
            }

            currentY += 10f
        }

        // Disclaimer Note at end of document
        ensureSpace(30f)
        val discText = "Disclaimer: This document is for informational purposes only. Please consult your family Acharya to resolve any date-related issues."
        canvas.drawText(discText, PAGE_WIDTH / 2f, currentY + 12f, pageFooterPaint)

        // --- CLOSING DEVOTIONAL FOOTER ---
        val hariVayuClosing = "॥ Śrī Hariḥ Sarvottamaḥ • Vāyuḥ Jīvottamaḥ ॥"
        canvas.drawText(
            hariVayuClosing,
            PAGE_WIDTH / 2f,
            PAGE_HEIGHT - MARGIN - 8f,
            dedicationBoldPaint
        )
        canvas.drawText(
            "— Page $currentPageNum —",
            PAGE_WIDTH / 2f,
            PAGE_HEIGHT - MARGIN + 6f,
            pageFooterPaint
        )
        document.finishPage(page)

        // Save PDF to file
        val outputStream = FileOutputStream(pdfFile)
        document.writeTo(outputStream)
        outputStream.flush()
        outputStream.close()
        document.close()

        // Trigger Android Share Intent
        val uri = getShareUri(context, pdfFile)
        val shareSubject = if (targetYearIndex != null) {
            "${result.personRecord.name} - Pitru Panchanga (Year $targetYearIndex)"
        } else {
            "${result.personRecord.name} - Pitru Panchanga"
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Pitru Panchanga PDF").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })

        return pdfFile
    }

    private fun createPdfFile(context: Context, result: ShraddhaCalculationResult, targetYearIndex: Int? = null): File {
        val pdfDir = File(context.cacheDir, "pdf").apply { if (!exists()) mkdirs() }
        val sanitizedName = result.personRecord.name.replace("\\s+".toRegex(), "_").lowercase()
        val fileName = if (targetYearIndex != null) {
            "pitru_panchanga_${sanitizedName}_year_${targetYearIndex}.pdf"
        } else {
            "pitru_panchanga_${sanitizedName}.pdf"
        }
        return File(pdfDir, fileName)
    }

    fun getShareUri(context: Context, file: File): android.net.Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
