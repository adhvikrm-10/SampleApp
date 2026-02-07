package com.example.smartcampusattendance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampusattendance.data.dao.ClassDao
import com.example.smartcampusattendance.data.entities.ClassEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClassViewModel(private val classDao: ClassDao) : ViewModel() {
    val classes: Flow<List<ClassEntity>> = classDao.getAllClasses()

    private val _addClassState = MutableStateFlow<AddClassState>(AddClassState.Idle)
    val addClassState: StateFlow<AddClassState> = _addClassState

    fun addClass(className: String, instructor: String, schedule: String) {
        viewModelScope.launch {
            try {
                _addClassState.value = AddClassState.Loading
                val classEntity = ClassEntity(
                    className = className,
                    instructor = instructor,
                    schedule = schedule
                )
                classDao.insertClass(classEntity)
                _addClassState.value = AddClassState.Success
            } catch (e: Exception) {
                _addClassState.value = AddClassState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            classDao.deleteClass(classEntity)
        }
    }

    fun resetAddClassState() {
        _addClassState.value = AddClassState.Idle
    }
}

sealed class AddClassState {
    object Idle : AddClassState()
    object Loading : AddClassState()
    object Success : AddClassState()
    data class Error(val message: String) : AddClassState()
}
