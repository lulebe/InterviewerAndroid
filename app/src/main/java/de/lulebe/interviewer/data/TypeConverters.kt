package de.lulebe.interviewer.data

import android.arch.persistence.room.TypeConverter
import java.util.*


class TypeConverters {

    @TypeConverter
    fun UUIDtoString (uuid: UUID) = uuid.toString()
    @TypeConverter
    fun StringtoUUID (string: String) = UUID.fromString(string)

    @TypeConverter
    fun DateToLong (date: Date) = date.time
    @TypeConverter
    fun LongToDate (long: Long) = Date(long)

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
    fun IntListToString (intList: List<Int>) = intList.joinToString(",")
    @TypeConverter
    fun StringToIntList (string: String) = string.split(",").map { it.toInt() }

}