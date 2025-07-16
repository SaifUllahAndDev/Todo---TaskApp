package com.example.todoapp.repository

import com.example.todoapp.datalayer.Task
import com.example.todoapp.datalayer.TaskDao

class TaskRepository(
    private val taskDao: TaskDao
) {

    val allTasks = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

}