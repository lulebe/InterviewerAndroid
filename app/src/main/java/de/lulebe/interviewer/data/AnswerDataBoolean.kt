package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "answer_data_boolean",
        foreignKeys = [(ForeignKey(entity = Answer::class, parentColumns = ["id"], childColumns = ["answerId"], onDelete = ForeignKey.CASCADE))]
)
data class AnswerDataBoolean (
        @PrimaryKey var id: UUID,
        var answerId: UUID,
        var data: Boolean
) {
    @Dao
    interface AnswerDataBooleanDao {

        @Insert
        fun createAnswerDataBoolean(answerDataBoolean: AnswerDataBoolean)

        @Update
        fun updateAnswerDataBoolean(answerDataBoolean: AnswerDataBoolean)

        @Delete
        fun deleteAnswerDataBoolean(answerDataBoolean: AnswerDataBoolean)

        @Query("SELECT * FROM answer_data_boolean WHERE answerId=:answerId")
        fun getDataForAnswer(answerId: UUID) : AnswerDataBoolean?

        @Query("SELECT * FROM answers INNER JOIN answer_data_boolean ON answer_data_boolean.answerId=answers.id WHERE questionId=:questionId")
        fun getAnswersForQuestion(questionId: UUID) : List<AnswerWithBooleanData>

    }

    data class AnswerWithBooleanData(
            var data: Boolean
    ) : AnswerWithData()
}