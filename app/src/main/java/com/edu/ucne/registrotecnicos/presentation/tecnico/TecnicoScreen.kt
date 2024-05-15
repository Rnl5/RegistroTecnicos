package com.edu.ucne.registrotecnicos.presentation.tecnico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme



var nombreTecnicoVacio by  mutableStateOf(false)
var nombreTecnicoSinSimbolos by  mutableStateOf(false)
var nombreTecnicoGuardado by mutableStateOf(false)
var sueldoHoraVacio by  mutableStateOf(false)

@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel,
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    TecnicoBody(
        onSaveTecnico = {tecnico ->
            viewModel.saveTecnico(tecnico)
        },
        tecnicos = tecnicos
    )
}


@Composable
fun TecnicoBody(
    tecnicos: List<TecnicoEntity>,
    onSaveTecnico: (TecnicoEntity) -> Unit
) {
    var tecnicoId by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var sueldoHora by remember { mutableStateOf("") }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Nombres") },
                value = nombres,
                onValueChange = { nombres = it },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreTecnicoVacio || nombreTecnicoSinSimbolos || nombreTecnicoVacio
            )

            OutlinedTextField(
                label = { Text(text = "Sueldo a pagar") },
                value = sueldoHora,
                onValueChange = { sueldoHora = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = sueldoHoraVacio
            )

            Spacer(modifier = Modifier.padding(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        tecnicoId = ""
                        nombres = ""
                        sueldoHora = ""
                        nombreTecnicoVacio = false
                        nombreTecnicoSinSimbolos = false
                        nombreTecnicoGuardado = false
                        sueldoHoraVacio = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "new button"
                    )
                    Text(text = "Nuevo")
                }

                OutlinedButton(
                    onClick = {
                        if (Validar(
                                TecnicoEntity(
                                    tecnicoId = tecnicoId.toIntOrNull(),
                                    nombres = nombres,
                                    sueldoHora = sueldoHora.toDoubleOrNull()
                                ),
                            lTecnicos = tecnicos
                            )
                        ) {
                            onSaveTecnico(
                                TecnicoEntity(
                                    tecnicoId = tecnicoId.toIntOrNull(),
                                    nombres = nombres,
                                    sueldoHora = sueldoHora.toDoubleOrNull()
                                )
                            )

                            tecnicoId = ""
                            nombres = ""
                            sueldoHora = ""
                        }
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "save button"
                    )
                    Text(text = "Guardar")
                }
            }

        }
    }
}


fun Validar(tecnico: TecnicoEntity?, lTecnicos:List<TecnicoEntity>?): Boolean {
    nombreTecnicoVacio = tecnico?.nombres.isNullOrEmpty() || tecnico?.nombres?.isBlank() ?: false
    sueldoHoraVacio = (tecnico?.sueldoHora ?: 0.0) <= 0.0
    nombreTecnicoSinSimbolos = tecnico?.nombres?.contains(Regex("[^a-zA-Z ]+")) ?: false
    nombreTecnicoGuardado = lTecnicos!!.any { it.nombres.uppercase() == tecnico?.nombres?.uppercase() && it.tecnicoId != tecnico.tecnicoId }

    return !nombreTecnicoVacio && !sueldoHoraVacio  && !nombreTecnicoSinSimbolos && !nombreTecnicoGuardado
}


@Preview
@Composable
private fun TecnicoPreview(){
    RegistroTecnicosTheme {
        TecnicoBody(
            onSaveTecnico = {},
            tecnicos = emptyList()
        )
    }
}