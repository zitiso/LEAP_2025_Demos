package com.example.roomdbclassdemo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team (@PrimaryKey @NonNull val name: String, val city: String)