package com.example.newsletters.dto.report.poi

data class PoiReportData(
    val values: Map<String, String> = emptyMap(),
    val tableRows: List<PoiTableRow> = emptyList(),
)

data class PoiTableRow(
    val marker: String,
    val values: List<Map<String, String>>,
)
