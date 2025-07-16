package com.example.todoapp.viewModelLayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarAndFilterChips(
    searchValue : String ,
    onSearchValueChange : (String) -> Unit ,
    filterType : FilterType ,
    insertFilter : (FilterType) -> Unit
) {
    TextField(
        value = searchValue ,
        onValueChange = onSearchValueChange ,
        placeholder = {
            Text(
                text = "Search"
            )
        }
    )
    Row(
        modifier = Modifier.fillMaxWidth() ,
        horizontalArrangement = Arrangement.Center
    ) {
        FilterType.entries.forEach { filter ->
            FilterChip(
                selected = filterType == filter ,
                onClick = { insertFilter(filter) },
                label = {
                    Text(filter.name)
                } ,
                modifier = Modifier.padding(4.dp)

            )
        }
    }
}