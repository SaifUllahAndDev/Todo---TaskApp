package com.example.todoapp.viewModelLayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun PriorityChips(
    priority : Priority? ,
    insertPriority: (Priority) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth() ,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Priority.entries.forEach { priorityType ->
            FilterChip(
                selected = priority == priorityType ,
                onClick = {
                    insertPriority(priorityType)
                } ,
                label = {
                    Text(
                        text = priorityType.name.lowercase().replaceFirstChar { it.uppercase() }
                    )
                }
            )
        }
    }
}