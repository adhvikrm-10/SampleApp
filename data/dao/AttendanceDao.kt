package com.example.smartcampusattendance.data.dao

import androidx.room.*
import com.example.smartcampusattendance.data.entities.AttendanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendanceEntity: AttendanceEntity): Long

    @Query("SELECT * FROM attendance WHERE classId = :classId ORDER BY date DESC")
    fun getAttendanceByClass(classId: Int): Flow<List<AttendanceEntity>>

    @Query("SELECT COUNT(*) FROM attendance WHERE classId = :classId AND isPresent = 1")
    suspend fun getPresentCount(classId: Int): Int

    @Query("SELECT COUNT(*) FROM attendance WHERE classId = :classId")
    suspend fun getTotalCount(classId: Int): Int

    @Delete
    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity)

    @Update
    suspend fun updateAttendance(attendanceEntity: AttendanceEntity)
}
