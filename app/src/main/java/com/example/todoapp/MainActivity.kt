package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.todoapp.datalayer.TaskDatabase
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.screens.MainScreen
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.viewModelLayer.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = TaskDatabase.getDatabase(applicationContext)
        val repo = TaskRepository(db.taskDao())
        val viewModel = TaskViewModel(repo)
        setContent {
            ToDoAppTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
