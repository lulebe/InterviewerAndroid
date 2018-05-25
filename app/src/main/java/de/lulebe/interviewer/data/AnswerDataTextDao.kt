package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

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
}