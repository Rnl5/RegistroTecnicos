package com.edu.ucne.registrotecnicos.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Combobox(
    items: List<T>,
    selectedItem: T?,
    selectedItemString: (T?) -> String,
    onItemSelected: (T?) -> Unit,
    label: String = "",
    itemTemplate: @Composable (T) -> Unit,
    isErrored: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(selectedItem ?: "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItemString(selectedItem),
            onValueChange = { },
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            isError = isErrored,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {itemTemplate(item)},
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                        textFieldValue = item.toString()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun vistaT(
) {
    val items = listOf(
        Tecnico(nombres = "Pedro", descripcion = "Redes"),
        Tecnico(nombres = "Juan", descripcion = "Base de datos"),
        Tecnico(nombres = "Lopez", descripcion = "Programacion" ),
    )

    var selectedItem by remember { mutableStateOf<Tecnico?>(null) }
    Combobox(
        items = items,
        label = "un label",

        selectedItemString = { it?.let { tecnico ->
            "${tecnico.nombres} ${tecnico.descripcion}"
        } ?: ""},
        selectedItem = selectedItem,
        onItemSelected = {
            selectedItem = it
        },
        itemTemplate = { Text(it.nombres) },
        isErrored = false
    )
}

data class Tecnico(
    val nombres: String,
    val descripcion: String
)