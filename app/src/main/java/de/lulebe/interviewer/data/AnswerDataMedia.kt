package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "answer_data_media",
        foreignKeys = [(ForeignKey(entity = Answer::class, parentColumns = ["id"], childColumns = ["answerId"], onDelete = ForeignKey.CASCADE))]
)
data class AnswerDataMedia(
        @PrimaryKey var id: UUID,
        var answerId: UUID,
        var data: String,
        var contentType: ContentType
)