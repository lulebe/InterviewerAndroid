package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Bundle
import java.util.*

@Entity(
        tableName = "schedules",
        foreignKeys = [(ForeignKey(entity = Interview::class, parentColumns = ["id"], childColumns = ["interviewId"], onDelete = ForeignKey.CASCADE))]
)
data class Schedule (
        @PrimaryKey val id: UUID,
        var interviewId: UUID,
        var intervalType: IntervalType,
        var x: Int,
        var startDate: Calendar
) {
    companion object {

        fun createDefaultDaily(interviewId: UUID) : Schedule {
            val cal = Calendar.getInstance()
            cal.timeInMillis = 0
            return Schedule(
                    UUID.randomUUID(),
                    interviewId,
                    IntervalType.DAYS,
                    1,
                    cal
            )
        }

        fun fromBundle(bundle: Bundle) : Schedule {
            val cal = Calendar.getInstance()
            cal.timeInMillis = bundle.getLong("startDate")
            return Schedule(
                    UUID.fromString(bundle.getString("id")),
                    UUID.fromString(bundle.getString("interviewId")),
                    IntervalType.values()[bundle.getInt("intervalType")],
                    bundle.getInt("x"),
                    cal
            )
        }
    }

    fun toBundle() : Bundle {
        val bundle = Bundle()
        bundle.putString("id", id.toString())
        bundle.putString("interviewId", interviewId.toString())
        bundle.putInt("intervalType", intervalType.ordinal)
        bundle.putInt("x", x)
        bundle.putLong("startDate", startDate.timeInMillis)
        return bundle
    }
}