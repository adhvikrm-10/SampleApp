package com.example.smartcampusattendance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import com.example.smartcampusattendance.data.entities.AttendanceEntity
import com.example.smartcampusattendance.ui.viewmodels.AttendanceViewModel

@Composable
fun AttendanceScreen(
    classId: Int,
    className: String,
    attendanceViewModel: AttendanceViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(classId) {
        attendanceViewModel.selectClassAttendance(classId)
    }

    val selectedAttendance by attendanceViewModel.selectedClassAttendance.collectAsState()
    val stats by attendanceViewModel.attendanceStats.collectAsState()
    val attendanceList by selectedAttendance?.collectAsState(initial = emptyList())
        ?: remember { mutableStateOf(emptyList()) }

    var selectedDate by remember { mutableStateOf(getTodayDate()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(className) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Statistics Card
            if (stats != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Present", "${stats!!.presentCount}")
                        StatItem("Total", "${stats!!.totalCount}")
                        StatItem("Percentage", "${stats!!.percentage}%")
                    }
                }
            }

            // Date Picker
            TextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Mark Attendance Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        attendanceViewModel.markAttendance(classId, selectedDate, true)
                        selectedDate = getTodayDate()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Present")
                }
                Button(
                    onClick = {
                        attendanceViewModel.markAttendance(classId, selectedDate, false)
                        selectedDate = getTodayDate()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Absent")
                }
            }

            // Attendance History
            Text(
                "Attendance History",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (attendanceList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text("No attendance records yet")
                }
            } else {
                LazyColumn {
                    items(attendanceList) { record ->
                        AttendanceRecord(
                            record = record,
                            onDelete = {
                                attendanceViewModel.deleteAttendance(record, classId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun AttendanceRecord(record: AttendanceEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Date: ${record.date}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Status: ${if (record.isPresent) "Present" else "Absent"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (record.isPresent) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
            Button(
                onClick = onDelete,
                modifier = Modifier.size(width = 80.dp, height = 36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text("Delete", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

fun getTodayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date())
}
