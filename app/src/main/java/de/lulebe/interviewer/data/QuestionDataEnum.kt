package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "question_data_enum",
        foreignKeys = [(ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"]))]
)
data class QuestionDataEnum (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var option: String,
        var success: Boolean
)