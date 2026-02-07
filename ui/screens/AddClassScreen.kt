package com.example.smartcampusattendance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampusattendance.ui.viewmodels.AddClassState
import com.example.smartcampusattendance.ui.viewmodels.ClassViewModel

@Composable
fun AddClassScreen(
    classViewModel: ClassViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var className by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }

    val addClassState by classViewModel.addClassState.collectAsState()

    LaunchedEffect(addClassState) {
        if (addClassState is AddClassState.Success) {
            className = ""
            instructor = ""
            schedule = ""
            classViewModel.resetAddClassState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Class") },
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
            TextField(
                value = className,
                onValueChange = { className = it },
                label = { Text("Class Name") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = instructor,
                onValueChange = { instructor = it },
                label = { Text("Instructor Name") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = schedule,
                onValueChange = { schedule = it },
                label = { Text("Schedule (e.g., Mon, Wed, Fri)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (className.isNotBlank() && instructor.isNotBlank() && schedule.isNotBlank()) {
                        classViewModel.addClass(className, instructor, schedule)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = addClassState !is AddClassState.Loading
            ) {
                if (addClassState is AddClassState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Add Class")
                }
            }

            if (addClassState is AddClassState.Error) {
                Text(
                    text = (addClassState as AddClassState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
