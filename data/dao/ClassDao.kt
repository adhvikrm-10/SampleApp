package com.example.smartcampusattendance.data.dao

import androidx.room.*
import com.example.smartcampusattendance.data.entities.ClassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity): Long

    @Query("SELECT * FROM classes ORDER BY createdDate DESC")
    fun getAllClasses(): Flow<List<ClassEntity>>

    @Query("SELECT * FROM classes WHERE id = :classId")
    suspend fun getClassById(classId: Int): ClassEntity?

    @Delete
    suspend fun deleteClass(classEntity: ClassEntity)

    @Update
    suspend fun updateClass(classEntity: ClassEntity)
}
