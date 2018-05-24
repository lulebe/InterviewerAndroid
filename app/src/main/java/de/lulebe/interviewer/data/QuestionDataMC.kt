package de.lulebe.interviewer.data

import android.arch.persistence.room.*
import java.util.*

@Entity(
        tableName = "question_data_mc",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE))]
)
data class QuestionDataMC (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var option: String,
        var success: Boolean
) : QuestionData {
    override fun insertInto(db: AppDatabase) {
        db.questionDataMCDao().createQuestionDataMC(this)
    }
    @Dao
    interface QuestionDataMCDao {
        @Insert
        fun createQuestionDataMC(questionDataMC: QuestionDataMC)
    }
}