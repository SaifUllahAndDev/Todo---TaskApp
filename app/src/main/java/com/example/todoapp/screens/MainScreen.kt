package com.example.todoapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.datalayer.Task
import com.example.todoapp.viewModelLayer.FilterType
import com.example.todoapp.viewModelLayer.Priority
import com.example.todoapp.viewModelLayer.SearchBarAndFilterChips
import com.example.todoapp.viewModelLayer.TaskViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen( viewModel: TaskViewModel) {

    val filteredTasks = viewModel.filteredTasks.collectAsState(emptyList())
    val filterType = viewModel.filterType.collectAsState(FilterType.ALL)

    val displayDialog = remember { mutableStateOf(false) }

    val taskToEdit = remember { mutableStateOf<Task?>(null) }

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val searchQuery = viewModel.searchQuery.collectAsState()

    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) } ,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ToDo -TaskApp"
                    )
                } ,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .clickable(
                    indication = null ,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }

        ){
            Column(
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally ,
            ) {
                SearchBarAndFilterChips(
                    searchValue = searchQuery.value,
                    onSearchValueChange = { query ->
                        viewModel.updateSearchQuery(query)
                    },
                    filterType = filterType.value ,
                    insertFilter = {
                        viewModel.insertFilter(it)
                    }
                )
                if (filteredTasks.value.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize() ,
                        verticalArrangement = Arrangement.Center ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\uD83C\uDF89 No tasks Found !" ,
                            fontSize = 32.sp ,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                } else{
                    ListScreen(
                        list = filteredTasks.value ,
                        onEdit = { task ->
                            taskToEdit.value = task
                            displayDialog.value = true
                        },
                        onCheckedChange = {
                            viewModel.updateTask(it)
                        } ,
                        onDelete = { task ->
                            viewModel.deleteTask(task)
                            coroutineScope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Task Deleted" ,
                                    actionLabel = "Undo" ,
                                    duration = SnackbarDuration.Short
                                )
                                if(result == SnackbarResult.ActionPerformed){
                                    viewModel.insertTask(task)
                                }
                            }
                        }
                    )
                }
            }

            if (displayDialog.value) {
                InsertDialog(
                    initialText = taskToEdit.value?.title ?: "",
                    initialDueDate = taskToEdit.value?.dueDate ,
                    initialPriority = taskToEdit.value?.priority ?: Priority.LOW ,
                    onSave = { title , date , priority ->
                        if (taskToEdit.value != null) {
                            viewModel.updateTask(taskToEdit.value!!.copy(title = title , dueDate = date , priority = priority))
                            displayDialog.value = false
                            taskToEdit.value = null
                        } else {
                            viewModel.insertTask(Task(title = title , dueDate = date , priority = priority))
                            displayDialog.value = false
                            taskToEdit.value = null
                        }
                    },
                    onDismiss = {
                        displayDialog.value = false
                        taskToEdit.value = null
                    }
                )
            }

            FloatingActionButton(
                onClick = {
                    displayDialog.value = true
                } ,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    }
}