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
        var intervalType: IntervalType
) {
    companion object {

        fun fromBundle(bundle: Bundle) : Schedule {
            val cal = Calendar.getInstance()
            cal.timeInMillis = bundle.getLong("startDate")
            return Schedule(
                    UUID.fromString(bundle.getString("id")),
                    UUID.fromString(bundle.getString("interviewId")),
                    IntervalType.values()[bundle.getInt("intervalType")]
            )
        }
    }

    fun toBundle() : Bundle {
        val bundle = Bundle()
        bundle.putString("id", id.toString())
        bundle.putString("interviewId", interviewId.toString())
        bundle.putInt("intervalType", intervalType.ordinal)
        return bundle
    }

    fun getIntervalForTime(calendar: Calendar) : Pair<Calendar, Calendar> {
        val calA = calendar.clone() as Calendar
        val calB = calendar.clone() as Calendar
        calA.set(Calendar.HOUR_OF_DAY, 0)
        calA.set(Calendar.MINUTE, 0)
        calA.set(Calendar.SECOND, 0)
        calA.set(Calendar.MILLISECOND, 0)
        calB.set(Calendar.HOUR_OF_DAY, 23)
        calB.set(Calendar.MINUTE, 59)
        calB.set(Calendar.SECOND, 59)
        calB.set(Calendar.MILLISECOND, 999)
        when (intervalType) {
            IntervalType.YEAR -> {
                calA.set(Calendar.DAY_OF_YEAR, 1)
                calB.set(Calendar.DAY_OF_YEAR, 1)
                calB.add(Calendar.YEAR, 1)
                calB.add(Calendar.DATE, -1)
            }
            IntervalType.MONTHS_SIX -> {
                calA.set(Calendar.DAY_OF_MONTH, 1)
                calB.set(Calendar.DAY_OF_MONTH, 1)
                calA.set(Calendar.MONTH, (calendar[Calendar.MONTH]-1) / 6 + 1)
                calB.set(Calendar.MONTH, (calendar[Calendar.MONTH]-1) / 6 + 1)
                calB.add(Calendar.MONTH, 6)
                calB.add(Calendar.DATE, -1)
            }
            IntervalType.MONTHS_THREE -> {
                calA.set(Calendar.DAY_OF_MONTH, 1)
                calB.set(Calendar.DAY_OF_MONTH, 1)
                calA.set(Calendar.MONTH, (calendar[Calendar.MONTH]-1) / 3 + 1)
                calB.set(Calendar.MONTH, (calendar[Calendar.MONTH]-1) / 3 + 1)
                calB.add(Calendar.MONTH, 3)
                calB.add(Calendar.DATE, -1)
            }
            IntervalType.MONTH -> {
                calA.set(Calendar.DAY_OF_MONTH, 1)
                calB.set(Calendar.DAY_OF_MONTH, 1)
                calB.add(Calendar.MONTH, 1)
                calB.add(Calendar.DATE, -1)
            }
            IntervalType.WEEK -> {
                calA.set(Calendar.DAY_OF_WEEK, 1)
                calB.set(Calendar.DAY_OF_WEEK, 7)
            }
            IntervalType.DAY -> {}
        }
        return Pair(calA, calB)
    }
}