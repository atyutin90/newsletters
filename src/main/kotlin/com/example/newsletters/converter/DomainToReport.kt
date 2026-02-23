package com.example.newsletters.converter

import com.example.newsletters.entity.Request
import com.example.newsletters.entity.WorkerMeeting
import com.example.newsletters.entity.ArbitrationManager
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Debtor
import com.example.newsletters.entity.DebtorMeeting
import com.example.newsletters.entity.Queue
import com.example.newsletters.entity.WorkerMeetingParticipant
import com.example.newsletters.dto.report.Debtor as ReportDebtor
import com.example.newsletters.dto.report.Creditor as ReportCreditor
import com.example.newsletters.dto.report.DebtorMeeting as ReportDebtorMeeting
import com.example.newsletters.dto.report.ArbitrationManager as ReportArbitrationManager
import com.example.newsletters.dto.report.Queue as ReportQueue
import com.example.newsletters.dto.report.WorkerMeeting as ReportWorkerMeeting
import com.example.newsletters.dto.report.Request as ReportRequest
import com.example.newsletters.dto.report.WorkerMeetingParticipant as ReportWorkerMeetingParticipant

object DomainToReport {

    fun Debtor.toReportDebtor(): ReportDebtor =
        ReportDebtor(
            id = this.id,
            fullName = this.fullName,
            name = this.name,
            caseNumber = this.caseNumber,
            address = this.address,
            courtAct = this.courtAct,
            actDate = this.actDate,
            resolutionDate = this.resolutionDate,
            taxRegistrationReasonCode = this.taxRegistrationReasonCode,
            taxpayerIdentificationNumber = this.taxpayerIdentificationNumber,
            primaryStateRegistrationNumber = this.primaryStateRegistrationNumber,
            registryDate = this.registryDate,
            registryClosingDate = this.registryClosingDate,
            arbitrationCase = this.arbitrationCase,
            documentNumber = this.documentNumber,
        )

    fun Creditor.toReportCreditor(): ReportCreditor =
        ReportCreditor(
            id = this.id,
            clientType = this.clientType?.name,
            name = this.name,
            taxpayerIdentificationNumber = this.taxpayerIdentificationNumber,
            primaryStateRegistrationNumber = this.primaryStateRegistrationNumber,
            passportSerial = this.passportSerial,
            passportNumber = this.passportNumber,
            address = this.address,
            executionWrit = this.executionWrit,
            executionDate = this.executionDate,
        )

    fun DebtorMeeting.toReportDebtorMeeting(): ReportDebtorMeeting =
        ReportDebtorMeeting(
            id = this.id,
            date = this.date,
            time = this.time,
            registrationTimeFrom = this.registrationTimeFrom,
            registrationTimeTo = this.registrationTimeTo,
            address = this.address,
            familiarizationDateFrom = this.familiarizationDateFrom,
            familiarizationDateTo = this.familiarizationDateTo,
        )

    fun ArbitrationManager.toReportArbitrationManager(): ReportArbitrationManager =
        ReportArbitrationManager(
            id = this.id,
            fullName = this.fullName,
            taxpayerIdentificationNumber = this.taxpayerIdentificationNumber,
            personalInsurancePolicyNumber = this.personalInsurancePolicyNumber,
            postalAddress = this.postalAddress,
            actualPostalAddress = this.postalAddress,
            email = this.email,
            phone = this.phone,
            sroData = this.sroData,
            sroRegistrationNumber = this.sroRegistrationNumber,
            insuranceCompanyName = this.insuranceCompanyName,
            insuranceContractNumber = this.insuranceContractNumber,
            insuranceContractTerm = this.insuranceContractTerm,
        )

    fun Queue.toReportQueue(): ReportQueue =
        ReportQueue(
            id = this.id,
            type = this.type,
            creditorId = this.creditorId,
            entryDate = this.entryDate,
            obligationType = this.obligationType,
            documentNumberOfReasonClaim = this.documentNumberOfReasonClaim,
            claimDate = this.claimDate,
            claimAmount = this.claimAmount,
            determination = this.determination,
            repaymentDate = this.repaymentDate,
            repaymentDocumentDetails = this.repaymentDocumentDetails,
            repaymentAmount = this.repaymentAmount,
            outstandingAmount = this.outstandingAmount,
            exclusionFromRegisterDate = this.exclusionFromRegisterDate,
            exclusionFromRegister = this.exclusionFromRegister,
            exclusionDocument = this.exclusionDocument,
            depositLocation = this.depositLocation,
            depositDocumentDetails = this.depositDocumentDetails,
            depositAmount = this.depositAmount,
            principalAmount = this.principalAmount,
            stateDutyAmount = this.stateDutyAmount,
            percentAmount = this.percentAmount,
            penaltyAmount = this.penaltyAmount,
            fine = this.fine,
            percentOnPercentAmount = this.percentOnPercentAmount,
            creditor = this.creditor?.toReportCreditor()
        )

    fun WorkerMeeting.toReportWorkerMeeting(): ReportWorkerMeeting =
        ReportWorkerMeeting(
            id = this.id,
            date = this.date,
            time = this.time,
            registrationTimeFrom = this.registrationTimeFrom,
            registrationTimeTo = this.registrationTimeTo,
            address = this.address,
            topic = this.topic,
        )

    fun Request.toReportRequest(): ReportRequest =
        ReportRequest(
            id = this.id,
            destination = this.destination?.name,
            destinationDetail = this.destinationDetail,
            date = this.date,
            dateFrom = this.dateFrom,
            dateTo = this.dateTo,
            address = this.address,
            accounts = this.accounts
        )

    fun WorkerMeetingParticipant.toReportWorkerMeetingParticipant(): ReportWorkerMeetingParticipant =
        ReportWorkerMeetingParticipant(
            id = this.id,
            fullName = this.fullName,
            address = this.address,
        )
}
