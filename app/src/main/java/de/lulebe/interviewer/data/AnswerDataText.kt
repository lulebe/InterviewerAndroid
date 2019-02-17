package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "answer_data_text",
        foreignKeys = [(ForeignKey(entity = Answer::class, parentColumns = ["id"], childColumns = ["answerId"], onDelete = ForeignKey.CASCADE))]
)
data class AnswerDataText (
        @PrimaryKey var id: UUID,
        var answerId: UUID,
        var data: String
) {
    @Dao
    interface AnswerDataTextDao {

        @Insert
        fun createAnswerDataText(answerDataText: AnswerDataText)

        @Update
        fun updateAnswerDataText(answerDataText: AnswerDataText)

        @Delete
        fun deleteAnswerDataText(answerDataText: AnswerDataText)

        @Query("SELECT * FROM answer_data_text WHERE answerId=:answerId")
        fun getDataForAnswer(answerId: UUID) : AnswerDataText?

        @Query("SELECT * FROM answers INNER JOIN answer_data_text ON answer_data_text.answerId=answers.id WHERE questionId=:questionId")
        fun getAnswersForQuestion(questionId: UUID) : List<AnswerWithTextData>

    }

    data class AnswerWithTextData(
            var data: String
    ) : AnswerWithData()
}