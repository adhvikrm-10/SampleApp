package com.example.smartcampusattendance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampusattendance.data.entities.ClassEntity
import com.example.smartcampusattendance.ui.viewmodels.ClassViewModel

@Composable
fun HomeScreen(
    classViewModel: ClassViewModel,
    onAddClassClick: () -> Unit,
    onClassClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val classes by classViewModel.classes.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClassClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Class")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "My Classes",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (classes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text("No classes added yet. Tap + to add a class.")
                }
            } else {
                LazyColumn {
                    items(classes) { classEntity ->
                        ClassCard(
                            classEntity = classEntity,
                            onCardClick = { onClassClick(classEntity.id) },
                            onDeleteClick = { classViewModel.deleteClass(classEntity) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClassCard(
    classEntity: ClassEntity,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classEntity.className,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Instructor: ${classEntity.instructor}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Schedule: ${classEntity.schedule}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Class",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
