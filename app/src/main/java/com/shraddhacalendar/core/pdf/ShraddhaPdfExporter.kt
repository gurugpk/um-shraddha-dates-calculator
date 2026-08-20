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
            // 1. Opening Invocation Header
            val invocation = "🕉️ " + context.getString(R.string.invocation_header) + " 🕉️"
            canvas.drawText(invocation, PAGE_WIDTH / 2f, currentY + 12f, invocationPaint)
            currentY += 24f

            // 2. Title & Subtitle
            val appTitle = context.getString(R.string.app_name)
            val appSubtitle = context.getString(R.string.app_subtitle)
            canvas.drawText(appTitle, PAGE_WIDTH / 2f, currentY + 12f, titlePaint)
            currentY += 18f
            canvas.drawText(appSubtitle, PAGE_WIDTH / 2f, currentY + 8f, subtitlePaint)
            currentY += 14f

            // Saffron Divider line
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
        canvas.drawText("Name: ${person.name}", col1X, cardY, tableCellBoldPaint)
        canvas.drawText("Location: ${person.location.displayName}", col2X, cardY, tableCellPaint)
        cardY += 14f

        val deathDateFormatted = person.deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val deathTimeFormatted = person.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
        canvas.drawText("Death: $deathDateFormatted ($deathTimeFormatted)", col1X, cardY, tableCellPaint)

        val masaName = PanchangaLocalizer.localizeMasa(result.mrutaTithi.masa, result.mrutaTithi.isAdhikaMasa, language)
        val pakshaName = PanchangaLocalizer.localizePaksha(result.mrutaTithi.tithi.paksha, language)
        val tithiName = PanchangaLocalizer.localizeTithi(result.mrutaTithi.tithi, language)
        canvas.drawText("Tithi: ${result.mrutaTithi.samvatsara}, $masaName, $pakshaName, $tithiName", col2X, cardY, tableCellPaint)

        currentY += 78f

        // Helper to check for new page
        fun ensureSpace(neededHeight: Float) {
            if (currentY + neededHeight > PAGE_HEIGHT - MARGIN - 30f) {
                // Draw footer for current page
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

        // --- FINAL DEDICATION SECTION (At the end of the report) ---
        ensureSpace(95f)
        currentY += 4f

        val dedBoxRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 84f)
        val dedBgPaint = Paint().apply {
            color = surfaceCardBg
            style = Paint.Style.FILL
        }
        val dedBorderPaint = Paint().apply {
            color = primarySaffron
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
        }
        canvas.drawRoundRect(dedBoxRect, 10f, 10f, dedBgPaint)
        canvas.drawRoundRect(dedBoxRect, 10f, 10f, dedBorderPaint)

        var dedY = currentY + 16f

        // Opening Invocation inside final card
        val dedInv = "🕉️ " + context.getString(R.string.invocation_header) + " 🕉️"
        canvas.drawText(dedInv, PAGE_WIDTH / 2f, dedY, dedicationBoldPaint)
        dedY += 16f

        // 1. Dedication to Sri Hari, Sri Vayu and Uttaradi Math Parampara
        val ded1 = context.getString(R.string.dedication_service)
        canvas.drawText(ded1, PAGE_WIDTH / 2f, dedY, dedicationTextPaint)
        dedY += 16f

        // 2. Dedication in memory of father
        val ded2 = "🌸 " + context.getString(R.string.dedication_father) + " 🌸"
        val dedFatherPaint = TextPaint().apply {
            color = textPrimary
            textSize = 10f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(ded2, PAGE_WIDTH / 2f, dedY, dedFatherPaint)
        dedY += 16f

        // 3. Developer attribution
        val ded3 = context.getString(R.string.developed_by)
        canvas.drawText(ded3, PAGE_WIDTH / 2f, dedY, dedicationBoldPaint)

        // Draw last page footer
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
