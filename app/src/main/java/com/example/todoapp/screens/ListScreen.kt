package com.example.todoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.datalayer.Task
import com.example.todoapp.viewModelLayer.Priority
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ListScreen(
    list : List<Task>,
    onEdit : (Task) -> Unit,
    onDelete : (Task) -> Unit ,
    onCheckedChange : (task : Task) -> Unit
) {

    LazyColumn {
        items(list) { task ->
            val formattedDate = task.dueDate?.let {
                SimpleDateFormat("EEE, MMM d , yyyy" , Locale.getDefault()).format(Date(it))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp) ,
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onEdit(task) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit ,
                            contentDescription = "Edit Task"
                        )
                    }
                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Text(
                            text = formattedDate ?: "No Due Date",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp).background(
                            color = when(task.priority){
                                    Priority.LOW -> Color(0xFFFFA500)
                                    Priority.MEDIUM -> Color(0xFF4CAF50)
                                    Priority.HIGH -> Color.Red
                                    else -> Color.Transparent
                                } ,
                            shape = CircleShape
                        )
                    )
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { isChecked ->
                            onCheckedChange(task.copy(isDone = isChecked))
                        }
                    )
                    Button(
                        onClick = { onDelete(task) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete ,
                            contentDescription = "Delete Task"
                        )
                    }
                }
            }
        }
    }
}