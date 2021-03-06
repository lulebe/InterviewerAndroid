package de.lulebe.interviewer.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface QuestionDao {

    @Insert
    fun createQuestion(question: Question)

    @Delete
    fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions WHERE id=:questionId")
    fun getQuestionById(questionId: UUID) : Question

    @Query("SELECT * FROM questions WHERE interviewId=:interviewId")
    fun getAllQuestionsForInterview(interviewId: UUID) : LiveData<List<Question>>

}