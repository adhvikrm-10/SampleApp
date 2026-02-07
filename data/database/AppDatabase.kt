package com.example.smartcampusattendance.data.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartcampusattendance.data.dao.ClassDao
import com.example.smartcampusattendance.data.dao.AttendanceDao
import com.example.smartcampusattendance.data.entities.ClassEntity
import com.example.smartcampusattendance.data.entities.AttendanceEntity

@Database(
    entities = [ClassEntity::class, AttendanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
    abstract fun attendanceDao(): AttendanceDao
}
