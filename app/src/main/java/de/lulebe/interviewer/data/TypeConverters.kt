package de.lulebe.interviewer.data

import android.arch.persistence.room.TypeConverter
import java.util.*


class TypeConverters {

    @TypeConverter
    fun UUIDtoString (uuid: UUID) = uuid.toString()
    @TypeConverter
    fun StringtoUUID (string: String) = UUID.fromString(string)

    @TypeConverter
    fun CalendarToLong (calendar: Calendar) = calendar.timeInMillis
    @TypeConverter
    fun LongToCalendar (long: Long) : Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = long
        return cal
    }

    @TypeConverter
    fun AnswerTypeToInt (answerType: AnswerType) = answerType.ordinal
    @TypeConverter
    fun IntToAnswerType (int: Int) = AnswerType.values()[int]

    @TypeConverter
    fun ContentTypeToInt (contentType: ContentType) = contentType.ordinal
    @TypeConverter
    fun IntToContentType (int: Int) = ContentType.values()[int]

    @TypeConverter
    fun ColorSchemeToInt (colorScheme: ColorScheme) = colorScheme.ordinal
    @TypeConverter
    fun IntToColorScheme (int: Int) = ColorScheme.values()[int]

    @TypeConverter
    fun IntervalTypeToInt (intervalType: IntervalType) = intervalType.ordinal
    @TypeConverter
    fun IntToIntervalType (int: Int) = IntervalType.values()[int]

    @TypeConverter
    fun QuestionDataNumberSuccessTypeToInt (successType: QuestionDataNumber.SuccessType) = successType.ordinal
    @TypeConverter
    fun IntToQuestionDataNumberSuccessType (int: Int) = QuestionDataNumber.SuccessType.values()[int]

    @TypeConverter
    fun IntListToString (intList: List<Int>) : String {
        if (intList.isEmpty()) return ""
        return intList.joinToString(",")
    }
    @TypeConverter
    fun StringToIntList (string: String) : List<Int> {
        if (string.isEmpty()) return emptyList()
        return string.split(",").map { it.toInt() }
    }

}