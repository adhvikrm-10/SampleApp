package com.example.smartcampusattendance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.smartcampusattendance.data.database.AppDatabase
import com.example.smartcampusattendance.ui.navigation.NavGraph
import com.example.smartcampusattendance.ui.theme.SmartCampusAttendanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "attendance_db"
        ).build()

        val classDao = db.classDao()
        val attendanceDao = db.attendanceDao()

        setContent {
            SmartCampusAttendanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        classDao = classDao,
                        attendanceDao = attendanceDao
                    )
                }
            }
        }
    }
}
