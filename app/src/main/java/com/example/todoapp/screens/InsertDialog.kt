package com.example.todoapp.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todoapp.viewModelLayer.Priority
import com.example.todoapp.viewModelLayer.PriorityChips
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun InsertDialog(
    initialText : String,
    initialDueDate : Long? = null ,
    initialPriority : Priority? ,
    onSave : (String , Long? , Priority ?) -> Unit ,
    onDismiss : () -> Unit
) {

    var priority = remember { mutableStateOf(initialPriority) }

    var text by remember { mutableStateOf(initialText) }

    val dueDate = remember { mutableStateOf(initialDueDate) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    dueDate.value?.let {
        calendar.timeInMillis = it
    }

    val datePickerDialog = DatePickerDialog(
        context ,
        { _ , year , month , dayOfMonth ->
            val setCalendar = Calendar.getInstance().apply {
                set( year,month,dayOfMonth ,0,0,0)
                set(Calendar.MILLISECOND , 0)
            }
            dueDate.value = setCalendar.timeInMillis
        } ,
        calendar.get(Calendar.YEAR) ,
        calendar.get(Calendar.MONTH) ,
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = onDismiss ,
        title = {
            Text(
                text = "Insert Task"
            )
        } ,
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text("Task")
                    }
                )
                Button(
                    onClick = {
                        datePickerDialog.show()
                    }
                ) {
                    Text(
                        text = dueDate.value?.let {
                            SimpleDateFormat(
                                "EEE, MMM d, yyyy",
                                Locale.getDefault()
                            ).format(Date(it))
                        } ?: "Pick a date"
                    )
                }
                Button(
                    onClick = {
                        dueDate.value = null
                    }
                ) {
                    Text(
                        text = "Clear Date"
                    )
                }
                Text(
                    text = "Priority :"
                )
                PriorityChips(
                    priority = priority.value ,
                    insertPriority = { selected ->
                        priority.value = selected
                    }
                )
            }
        } ,
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()){
                        onSave(text , dueDate.value , priority.value)
                        text = ""
                        dueDate.value = null
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check ,
                    contentDescription = "Save task"
                )
            }
        } ,
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close ,
                    contentDescription = "Dismiss"
                )
            }
        }
    )
}