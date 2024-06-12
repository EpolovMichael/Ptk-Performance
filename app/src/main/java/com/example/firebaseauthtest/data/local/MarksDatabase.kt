package com.example.firebaseauthtest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Mark::class], version = 1)
abstract class MarksDatabase : RoomDatabase() {
    abstract fun markDao(): MarkDao
}