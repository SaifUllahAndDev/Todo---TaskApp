package com.example.todoapp.datalayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database( entities = [Task::class] , version = 3 )
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao

    companion object {

        @Volatile
        private var INSTANCE : TaskDatabase? = null

        fun getDatabase(context: Context) : TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TaskDatabase::class.java ,
                    "TaskDatabase.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}