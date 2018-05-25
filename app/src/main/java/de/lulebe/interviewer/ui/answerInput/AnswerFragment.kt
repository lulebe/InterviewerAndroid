package de.lulebe.interviewer.ui.answerInput

import java.util.*

interface AnswerFragment {
    fun setArgs(questionId: UUID, interviewId: UUID, time: Calendar?)
    fun submit()
}