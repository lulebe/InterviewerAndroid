package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_media",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataMedia (
        @PrimaryKey var id: UUID,
        var questionId: UUID
) : QuestionData {
    override fun insertInto(db: AppDatabase) {
        db.questionDataMediaDao().createQuestionDataMedia(this)
    }
    @Dao
    interface QuestionDataMediaDao {
        @Insert
        fun createQuestionDataMedia(questionDataMedia: QuestionDataMedia)
    }
}