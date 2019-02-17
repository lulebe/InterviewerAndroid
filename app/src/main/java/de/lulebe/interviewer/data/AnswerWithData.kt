package de.lulebe.interviewer.data

import java.util.*

abstract class AnswerWithData {
    var id: UUID = UUID.randomUUID()
    var questionId: UUID = UUID.randomUUID()
    var createdAt: Calendar = Calendar.getInstance()
    var validSince: Calendar = Calendar.getInstance()
    var validUntil: Calendar = Calendar.getInstance()
    var success: Boolean = true
}