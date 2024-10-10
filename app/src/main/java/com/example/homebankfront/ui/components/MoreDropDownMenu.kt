package com.example.homebankfront.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun MoreDropDownMenu(
    map: Map<String, () -> Unit>
) {
    Box {
        val expanded = rememberSaveable { mutableStateOf(false) }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            map.forEach { (text, onClick) ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = onClick
                )
            }
        }

        IconButton(
            onClick = {
                expanded.value = !expanded.value
            },
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "",
            )
        }
    }
}