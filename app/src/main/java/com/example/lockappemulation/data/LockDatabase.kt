package com.example.lockappemulation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Password::class], version = 1)
abstract class LockDatabase : RoomDatabase() {

    abstract val passwordDao : PasswordDao

    companion object {

        @Volatile
        private var INSTANCE: LockDatabase? = null

        fun getInstance(context: Context): LockDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, LockDatabase::class.java, "lockdatabase"
                ).build()

                INSTANCE = instance
                return instance
            }

        }
    }
}