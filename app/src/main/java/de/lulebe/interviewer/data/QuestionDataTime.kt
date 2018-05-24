package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_time",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataTime (
        @PrimaryKey var id: UUID,
        var questionId: UUID
) : QuestionData {

    override fun insertInto(db: AppDatabase) {
        db.questionDataTimeDao().createQuestionDataTime(this)
    }

    @Dao
    interface QuestionDataTimeDao {
        @Insert
        fun createQuestionDataTime(questionDataTime: QuestionDataTime)
    }

}