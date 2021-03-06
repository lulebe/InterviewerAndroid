package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_duration",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataDuration (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var successDuration: Int,
        var successType: SuccessType
) : QuestionData {

    enum class SuccessType {
        gt, lt, eq
    }

    override fun insertInto(db: AppDatabase) {
        db.questionDataDurationDao().createQuestionDataDuration(this)
    }
    @Dao
    interface QuestionDataDurationDao {
        @Insert
        fun createQuestionDataDuration(questionDataDuration: QuestionDataDuration)
    }
}