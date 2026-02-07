package com.example.smartcampusattendance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartcampusattendance.data.dao.AttendanceDao
import com.example.smartcampusattendance.data.dao.ClassDao
import com.example.smartcampusattendance.ui.screens.AddClassScreen
import com.example.smartcampusattendance.ui.screens.AttendanceScreen
import com.example.smartcampusattendance.ui.screens.HomeScreen
import com.example.smartcampusattendance.ui.viewmodels.AttendanceViewModel
import com.example.smartcampusattendance.ui.viewmodels.ClassViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    classDao: ClassDao,
    attendanceDao: AttendanceDao
) {
    val classViewModel: ClassViewModel = remember { ClassViewModel(classDao) }
    val attendanceViewModel: AttendanceViewModel = remember { AttendanceViewModel(attendanceDao) }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                classViewModel = classViewModel,
                onAddClassClick = { navController.navigate("add_class") },
                onClassClick = { classId ->
                    navController.navigate("attendance/$classId/Class")
                }
            )
        }

        composable("add_class") {
            AddClassScreen(
                classViewModel = classViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("attendance/{classId}/{className}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull() ?: 0
            val className = backStackEntry.arguments?.getString("className") ?: "Class"

            AttendanceScreen(
                classId = classId,
                className = className,
                attendanceViewModel = attendanceViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
