package com.example.newsletters.dto.report.helper


import com.example.newsletters.dto.report.Queue
import com.example.newsletters.entity.enum.QueueType.THIRD
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode

class QueueHelper {

    @Deprecated(message = "Use the new strategy method")
    fun getThirdQueueAmountByCreditor(queues: List<Queue>, creditorId: Long, isRound: Boolean = false): BigDecimal =
        if (queues.isEmpty()) {
            ZERO
        } else {
            (queues.filter { it.creditorId == creditorId && it.type == THIRD }
                .map { (it.principalAmount ?: ZERO).plus(it.percentAmount ?: ZERO).plus(it.stateDutyAmount ?: ZERO) }
                .firstOrNull() ?: ZERO).let { if (isRound) it.setScale(0, RoundingMode.DOWN) else it }
        }

    companion object {
        fun getThirdQueueAmountByCreditor(queues: List<com.example.newsletters.entity.Queue>, creditorId: Long?, isRound: Boolean = false): BigDecimal =
            if (queues.isEmpty()) {
                ZERO
            } else {
                (queues.filter { it.creditorId == creditorId && it.type == THIRD }
                    .map { (it.principalAmount ?: ZERO).plus(it.percentAmount ?: ZERO).plus(it.stateDutyAmount ?: ZERO) }
                    .firstOrNull() ?: ZERO).let { if (isRound) it.setScale(0, RoundingMode.DOWN) else it }
            }
    }
}
