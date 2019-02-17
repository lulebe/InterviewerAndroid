package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_boolean",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataBoolean (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var successValue: Boolean
) : QuestionData {

    override fun insertInto(db: AppDatabase) {
        db.questionDataBooleanDao().createQuestionDataBoolean(this)
    }

    @Dao
    interface QuestionDataBooleanDao {
        @Insert
        fun createQuestionDataBoolean(questionDataBoolean: QuestionDataBoolean)

        @Query("SELECT * FROM question_data_boolean WHERE questionId=:questionId")
        fun getQuestionDataForQuestion(questionId: UUID) : QuestionDataBoolean
    }

}