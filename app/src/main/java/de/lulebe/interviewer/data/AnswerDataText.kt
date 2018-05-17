package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "answer_data_text",
        foreignKeys = [(ForeignKey(entity = Answer::class, parentColumns = ["id"], childColumns = ["answerId"]))]
)
data class AnswerDataText (
        @PrimaryKey var id: UUID,
        var answerId: UUID,
        var data: String
)