package com.example.roomdbclassdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(Team::class)], version = 1)
abstract class TeamDatabase: RoomDatabase() {
    abstract fun teamDao(): TeamDao // access to DAO

    companion object {

        fun getInstance(context: Context): TeamDatabase {
            synchronized(this) { // single threaded access

                val teamDB = Room.databaseBuilder(
                    context.applicationContext, // Downed by this application
                    TeamDatabase::class.java, // DB class
                    "team_database") // DB name
                    .build()

                return teamDB
            }
        }
    }
}