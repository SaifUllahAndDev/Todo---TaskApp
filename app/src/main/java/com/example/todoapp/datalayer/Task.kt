package com.example.todoapp.datalayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.viewModelLayer.Priority


@Entity(tableName = "TasksTable")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0 ,
    val title : String ,
    val isDone : Boolean = false ,
    val dueDate : Long? = null ,
    val priority: Priority? = Priority.LOW
)
