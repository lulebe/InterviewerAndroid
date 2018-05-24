package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_number",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataNumber (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var min: Float?,
        var max: Float?,
        var successThreshold: Float?,
        var successType: SuccessType?
) : QuestionData {

    override fun insertInto(db: AppDatabase) {
        db.questionDataNumberDao().createQuestionDataNumber(this)
    }

    @Dao
    interface QuestionDataNumberDao {
        @Insert
        fun createQuestionDataNumber(questionDataNumber: QuestionDataNumber)
    }

    enum class SuccessType {
        gt, lt, eq
    }
}