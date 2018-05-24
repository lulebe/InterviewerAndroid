package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "questions",
        foreignKeys = [(ForeignKey(entity = Interview::class, parentColumns = ["id"], childColumns = ["interviewId"], onDelete = ForeignKey.CASCADE))]
)
data class Question (
        @PrimaryKey val id: UUID,
        var interviewId: UUID,
        var question: String,
        var answerType: AnswerType,
        var order: Int
)