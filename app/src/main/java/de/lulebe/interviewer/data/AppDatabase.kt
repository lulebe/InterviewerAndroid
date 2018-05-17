package de.lulebe.interviewer.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(entities = [
    Interview::class,
    InterviewUser::class,
    Notifications::class,
    Question::class,
    Answer::class,
    AnswerDataText::class,
    AnswerDataMedia::class
], version = 1)
@TypeConverters(de.lulebe.interviewer.data.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun interviewDao() : InterviewDao
    abstract fun interviewUserDao() : InterviewUserDao

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