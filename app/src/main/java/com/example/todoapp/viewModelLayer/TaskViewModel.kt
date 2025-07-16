package com.example.todoapp.viewModelLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.datalayer.Task
import com.example.todoapp.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val allTasks  = repository.allTasks.stateIn(
        scope = viewModelScope ,
        started = SharingStarted.WhileSubscribed(5000) ,
        initialValue = emptyList()
    )

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery

    fun updateSearchQuery( query : String ) {
        _searchQuery.value = query
    }

    val _filterType = MutableStateFlow(FilterType.ALL)
    val filterType : StateFlow<FilterType> = _filterType

    val filteredTasks = combine( allTasks , filterType , searchQuery ) { tasks , filter , query ->
        val filteredbyStatus = when(filter) {
            FilterType.ALL -> tasks
            FilterType.COMPLETE -> tasks.filter { it.isDone }
            FilterType.INCOMPLETE -> tasks.filter { !it.isDone }
        }
        filteredbyStatus.filter { tasks ->
            tasks.title.contains(query , ignoreCase = true)
        }
    }.stateIn(
        scope = viewModelScope ,
        started = SharingStarted.WhileSubscribed(5000) ,
        initialValue = emptyList()
    )

    fun insertFilter( filter : FilterType ) {
        _filterType.value = filter
    }

}