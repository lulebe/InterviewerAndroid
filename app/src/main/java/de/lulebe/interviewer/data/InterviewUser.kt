package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
        tableName = "interview_users",
        foreignKeys = [
            (ForeignKey(entity = Interview::class, parentColumns = ["id"], childColumns = ["interviewId"], onDelete = ForeignKey.CASCADE))
        ]
)
data class InterviewUser (
        @PrimaryKey var id: UUID,
        var userId: UUID,
        var userName: String,
        var interviewId: UUID,
        var admin: Boolean
)