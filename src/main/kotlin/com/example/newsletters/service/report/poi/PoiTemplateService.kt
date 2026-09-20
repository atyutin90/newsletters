package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.dto.report.poi.PoiTableRow
import org.apache.poi.xwpf.usermodel.IBody
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFTable
import org.apache.poi.xwpf.usermodel.XWPFTableRow
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

@Service
class PoiTemplateService {

    fun generate(
        configuredPath: String,
        data: PoiReportData,
    ): ByteArray = FileInputStream(configuredPath).use { input ->
        XWPFDocument(input).use { wordDocument ->
            data.tableRows.forEach { replaceTableRows(wordDocument, it) }
            replacePlaceholders(wordDocument, data.values)
            ByteArrayOutputStream().use { output ->
                wordDocument.write(output)
                output.toByteArray()
            }
        }
    }

    private fun replaceTableRows(document: XWPFDocument, data: PoiTableRow) {
        val table = document.tables.firstOrNull { table ->
            table.rows.any { row -> row.tableCells.any { it.text.contains(data.marker) } }
        } ?: error("В DOCX-шаблоне не найдена строка таблицы ${data.marker}")
        val templateIndex = table.rows.indexOfFirst { row ->
            row.tableCells.any { it.text.contains(data.marker) }
        }
        val templateRow = table.getRow(templateIndex)

        data.values.forEachIndexed { offset, values ->
            val row = table.insertNewTableRow(templateIndex + offset)
            row.ctRow.set(templateRow.ctRow.copy())
            XWPFTableRow(row.ctRow, table).tableCells.forEach { replacePlaceholders(it, values) }
        }
        table.removeRow(templateIndex + data.values.size)
    }

    private fun replacePlaceholders(body: IBody, values: Map<String, String>) {
        body.paragraphs.forEach { replacePlaceholders(it, values) }
        body.tables.forEach { replacePlaceholders(it, values) }
    }

    private fun replacePlaceholders(table: XWPFTable, values: Map<String, String>) {
        table.rows.flatMap { it.tableCells }.forEach { replacePlaceholders(it, values) }
    }

    private fun replacePlaceholders(paragraph: XWPFParagraph, values: Map<String, String>) {
        paragraph.runs.forEach { run ->
            run.ctr.tList.forEach { textNode ->
                val text = textNode.stringValue
                textNode.stringValue = values.entries.fold(text) { result, (placeholder, value) ->
                    result.replace(placeholder, value)
                }
            }
        }

        val completeText = paragraph.text
        val replacedText = values.entries.fold(completeText) { result, (placeholder, value) ->
            result.replace(placeholder, value)
        }
        if (completeText != replacedText && paragraph.runs.isNotEmpty()) {
            paragraph.runs.first().setText(replacedText, 0)
            for (index in paragraph.runs.lastIndex downTo 1) paragraph.removeRun(index)
        }
    }
}
