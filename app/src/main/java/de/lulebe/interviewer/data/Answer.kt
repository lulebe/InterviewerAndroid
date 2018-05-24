package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(
        tableName = "answers",
        foreignKeys = [ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE)]
)
data class Answer (
        @PrimaryKey var id: UUID,
        var questionId: UUID,
        var createdAt: Calendar,
        var validSince: Calendar,
        var validUntil: Calendar,
        var success: Boolean?
)