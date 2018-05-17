package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import java.util.*

@Entity(
        tableName = "notifications",
        foreignKeys = [(ForeignKey(entity = Interview::class, parentColumns = ["id"], childColumns = ["interviewId"], onDelete = ForeignKey.CASCADE))]
)
data class Notifications(
        @PrimaryKey val id: UUID,
        var interviewId: UUID,
        var months: List<Int>,
        var dates: List<Int>,
        var days: List<Int>,
        var hours: List<Int>,
        var minutes: List<Int>
) {

    companion object {
        fun fromBundle(bundle: Bundle) : Notifications {
            return Notifications(
                    UUID.fromString(bundle.getString("id")),
                    UUID.fromString(bundle.getString("interviewId")),
                    bundle.getIntArray("months").asList(),
                    bundle.getIntArray("dates").asList(),
                    bundle.getIntArray("days").asList(),
                    bundle.getIntArray("hours").asList(),
                    bundle.getIntArray("minutes").asList()
            )
        }
    }

    fun toBundle() : Bundle {
        UUID.randomUUID()
        val bundle = Bundle()
        bundle.putString("id", id.toString())
        bundle.putString("interviewId", interviewId.toString())
        bundle.putIntArray("months", months.toIntArray())
        bundle.putIntArray("dates", dates.toIntArray())
        bundle.putIntArray("days", days.toIntArray())
        bundle.putIntArray("hours", hours.toIntArray())
        bundle.putIntArray("minutes", minutes.toIntArray())
        return bundle
    }

    fun findNextOccurrenceAfter (calendar: Calendar) : Calendar {
        val result = calendar.clone() as Calendar
        result.set(Calendar.SECOND, 0)
        result.set(Calendar.MILLISECOND, 0)
        do {
            findNextAfter(result, minutes, Calendar.MINUTE, Calendar.HOUR)
            findNextAfter(result, hours, Calendar.HOUR, Calendar.DATE)
            findNextAfter(result, dates, Calendar.DAY_OF_MONTH, Calendar.MONTH)
            findNextAfter(result, months, Calendar.MONTH, Calendar.YEAR)
        } while (!isCorrectWeekday(result))
        return result
    }

    private fun findNextAfter (calendar: Calendar, list: List<Int>, field: Int, nextField: Int) {
        val f = calendar.get(field)
        val sorted = list.sorted()
        var found = sorted.firstOrNull { it > f }
        if (found == null) {
            found = sorted.first()
            calendar.add(nextField, 1)
        }
        calendar.set(field, found)
    }

    private fun isCorrectWeekday (calendar: Calendar) : Boolean {
        return calendar.get(Calendar.DAY_OF_WEEK) in days
    }

}