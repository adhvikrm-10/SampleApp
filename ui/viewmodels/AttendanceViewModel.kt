package com.example.smartcampusattendance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampusattendance.data.dao.AttendanceDao
import com.example.smartcampusattendance.data.entities.AttendanceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel(private val attendanceDao: AttendanceDao) : ViewModel() {
    private val _selectedClassAttendance = MutableStateFlow<Flow<List<AttendanceEntity>>?>(null)
    val selectedClassAttendance: StateFlow<Flow<List<AttendanceEntity>>?> = _selectedClassAttendance

    private val _attendanceStats = MutableStateFlow<AttendanceStats?>(null)
    val attendanceStats: StateFlow<AttendanceStats?> = _attendanceStats

    fun selectClassAttendance(classId: Int) {
        _selectedClassAttendance.value = attendanceDao.getAttendanceByClass(classId)
        updateStats(classId)
    }

    fun markAttendance(classId: Int, date: String, isPresent: Boolean) {
        viewModelScope.launch {
            val attendanceEntity = AttendanceEntity(
                classId = classId,
                date = date,
                isPresent = isPresent
            )
            attendanceDao.insertAttendance(attendanceEntity)
            updateStats(classId)
        }
    }

    private fun updateStats(classId: Int) {
        viewModelScope.launch {
            val presentCount = attendanceDao.getPresentCount(classId)
            val totalCount = attendanceDao.getTotalCount(classId)
            val percentage = if (totalCount > 0) (presentCount * 100) / totalCount else 0
            _attendanceStats.value = AttendanceStats(presentCount, totalCount, percentage)
        }
    }

    fun deleteAttendance(attendanceEntity: AttendanceEntity, classId: Int) {
        viewModelScope.launch {
            attendanceDao.deleteAttendance(attendanceEntity)
            updateStats(classId)
        }
    }
}

data class AttendanceStats(
    val presentCount: Int,
    val totalCount: Int,
    val percentage: Int
)
