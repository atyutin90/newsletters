package com.example.newsletters.service.report.jasper

import com.example.newsletters.entity.Document
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

const val ARBITRATION_MANAGER = "arbitrationManager"
const val DEBTOR = "debtor"
const val DEBTOR_MEETING = "debtorMeeting"
const val QUEUES = "queues"
const val QUEUE_HELPER = "queueHelper"
const val WORKER_MEETING = "workerMeeting"
const val REQUEST_PARAM = "request"
const val REQUEST_NUMBER = "requestNumber"

interface JasperDataService {
    fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>>
}
