package de.lulebe.interviewer.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(entities = [
    Interview::class,
    InterviewUser::class,
    Schedule::class,
    Notification::class,
    Question::class,
    QuestionDataMC::class,
    QuestionDataNumber::class,
    QuestionDataBoolean::class,
    QuestionDataText::class,
    QuestionDataTime::class,
    QuestionDataDuration::class,
    QuestionDataMedia::class,
    Answer::class,
    AnswerDataText::class,
    AnswerDataMedia::class
], version = 1)
@TypeConverters(de.lulebe.interviewer.data.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun interviewDao() : InterviewDao
    abstract fun interviewUserDao() : InterviewUserDao
    abstract fun questionDao() : QuestionDao
    abstract fun answerDao() : AnswerDao
    abstract fun scheduleDao() : ScheduleDao
    abstract fun notificationDao() : NotificationDao
    abstract fun questionDataMCDao() : QuestionDataMC.QuestionDataMCDao
    abstract fun questionDataNumberDao() : QuestionDataNumber.QuestionDataNumberDao
    abstract fun questionDataTextDao() : QuestionDataText.QuestionDataTextDao
    abstract fun questionDataTimeDao() : QuestionDataTime.QuestionDataTimeDao
    abstract fun questionDataDurationDao() : QuestionDataDuration.QuestionDataDurationDao
    abstract fun questionDataBooleanDao() : QuestionDataBoolean.QuestionDataBooleanDao
    abstract fun questionDataMediaDao() : QuestionDataMedia.QuestionDataMediaDao
    abstract fun anwserDataTextDao() : AnswerDataTextDao

    companion object {
        private var db : AppDatabase? = null
        fun getDatabase(ctx: Context) : AppDatabase {
            db?.let { return it }
            val newDb = Room.databaseBuilder(ctx, AppDatabase::class.java, "db").build()
            db = newDb
            return newDb
        }
    }

}