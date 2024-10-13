package com.example.homebankfront.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun MoreDropDownMenu(
    map: Map<String, () -> Unit>
) {

    val expanded = rememberSaveable { mutableStateOf(false) }

    Box {
        DropdownMenu(
            expanded = expanded.value,
            offset = DpOffset(x = (-20).dp, y = (-5).dp),
            onDismissRequest = { expanded.value = false }
        ) {
            map.forEach { (text, onClick) ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        expanded.value = false
                        onClick()
                    }
                )
            }
        }

        IconButton(
            onClick = { expanded.value = !expanded.value }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "",
            )
        }
    }
}