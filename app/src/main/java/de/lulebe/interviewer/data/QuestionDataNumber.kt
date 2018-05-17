package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "question_data_number",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"]))]
)
data class QuestionDataNumber (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var min: Int?,
        var max: Int?,
        var successThreshold: Int?,
        var successType: SuccessType?
) {
    enum class SuccessType {
        gt, lt, eq
    }
}