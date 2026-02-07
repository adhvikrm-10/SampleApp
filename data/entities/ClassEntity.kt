package com.example.smartcampusattendance.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val className: String,
    val instructor: String,
    val schedule: String,  // e.g., "Mon, Wed, Fri"
    val createdDate: Long = System.currentTimeMillis()
)
