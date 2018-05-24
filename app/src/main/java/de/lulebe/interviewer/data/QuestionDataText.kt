package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_text",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataText (
        @PrimaryKey var id: UUID,
        var questionId: UUID
) : QuestionData {

    override fun insertInto(db: AppDatabase) {
        db.questionDataTextDao().createQuestionDataText(this)
    }

    @Dao
    interface QuestionDataTextDao {
        @Insert
        fun createQuestionDataText(questionDataText: QuestionDataText)
    }

}