package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Bundle
import java.util.*

@Entity(
        tableName = "notifications",
        foreignKeys = [(ForeignKey(entity = Interview::class, parentColumns = ["id"], childColumns = ["interviewId"], onDelete = ForeignKey.CASCADE))]
)
data class Notification(
        @PrimaryKey val id: UUID,
        var interviewId: UUID,
        var daysBefore: Int,
        var hours: Int,
        var minutes: Int
) {

    companion object {

        fun fromBundle(bundle: Bundle) : Notification {
            return Notification(
                    UUID.fromString(bundle.getString("id")),
                    UUID.fromString(bundle.getString("interviewId")),
                    bundle.getInt("daysBefore"),
                    bundle.getInt("hours"),
                    bundle.getInt("minutes")
            )
        }

    }

    fun toBundle() : Bundle {
        val bundle = Bundle()
        bundle.putString("id", id.toString())
        bundle.putString("interviewId", interviewId.toString())
        bundle.putInt("daysBefore", daysBefore)
        bundle.putInt("hours", hours)
        bundle.putInt("minutes", minutes)
        return bundle
    }

}