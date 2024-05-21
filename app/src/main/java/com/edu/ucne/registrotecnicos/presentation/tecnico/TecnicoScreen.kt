package com.edu.ucne.registrotecnicos.presentation.tecnico

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.edu.ucne.registrotecnicos.Screen

import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.data.local.entities.TipoTecnicoEntity
import com.edu.ucne.registrotecnicos.presentation.components.Combobox
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch
import com.edu.ucne.registrotecnicos.presentation.components.TopAppBar


var nombreTecnicoVacio by mutableStateOf(false)
var nombreTecnicoSinSimbolos by mutableStateOf(false)
var nombreTecnicoGuardado by mutableStateOf(false)
var sueldoHoraVacio by mutableStateOf(false)
var tipoTecnicoNoVacio by mutableStateOf(false)

@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()

    val tiposTecnicos by viewModel.tipoTecnico.collectAsStateWithLifecycle()

    TecnicoBody(
        uiState = uiState,
        onNombresChanged = viewModel::onNombresChanged,
        onSueldoHoraChanged = viewModel::onSueldoHoraChanged,
        onSaveTecnico = {
            viewModel.saveTecnico()
        },
        tecnicos = tecnicos,
        onTipoTecnicoChanged = viewModel::onTipoTecnicoChanged,
        tipoTecnicos = tiposTecnicos,
        navController = navController
    )
}


@Composable
fun TecnicoBody(
    uiState: TecnicoUIState,
    onNombresChanged: (String) -> Unit,
    onSueldoHoraChanged: (String) -> Unit,
    onSaveTecnico: () -> Unit,
    tecnicos: List<TecnicoEntity>,
    navController: NavHostController,
    tipoTecnicos: List<TipoTecnicoEntity>,
    onTipoTecnicoChanged: (String) -> Unit,
) {
    var tecnicoSeleccionado by remember { mutableStateOf<TipoTecnicoEntity?>(null) }
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
                Text(text = "Registro de Tecnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Lista de tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Tecnicos"
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ) {}
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Tecnicos",
                onTopBarClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
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
                        value = uiState.nombres ?: "",
                        onValueChange = onNombresChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Sueldo por hora") },
                        value = uiState.sueldoHora.toString().replace("null", ""),
                        onValueChange = onSueldoHoraChanged,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Combobox(
                        label = "Tipos de tecnicos",
                        items = tipoTecnicos,
                        selectedItem = tecnicoSeleccionado,
                        selectedItemString = {
                            it?.let {
                                "${it.descripcion}"
                            } ?: ""
                        },
                        onItemSelected = {
                            onTipoTecnicoChanged(it?.descripcion ?: "")
                            tecnicoSeleccionado = it
                            uiState.tipoT = it?.descripcion
                        },
                        itemTemplate = { Text(text = it.descripcion ?: "") },
                        isErrored = tipoTecnicoNoVacio
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(onClick = {
                            tipoTecnicoNoVacio = false
                            nombreTecnicoSinSimbolos = false
                            nombreTecnicoVacio = false
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
                                            tecnicoId = uiState.tecnicoId,
                                            nombres = uiState.nombres,
                                            sueldoHora = uiState.sueldoHora,
                                            tipoT = uiState.tipoT
                                        ),
                                        lTecnicos = tecnicos
                                    )
                                ) {
                                    onSaveTecnico()
                                    navController.navigate(Screen.TecnicoList)
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
    }
}


fun Validar(tecnico: TecnicoEntity?, lTecnicos: List<TecnicoEntity>?): Boolean {
    nombreTecnicoVacio = tecnico?.nombres.isNullOrEmpty() || tecnico?.nombres?.isBlank() ?: false
    sueldoHoraVacio = (tecnico?.sueldoHora ?: 0.0) <= 0.0
    nombreTecnicoSinSimbolos = tecnico?.nombres?.contains(Regex("[^a-zA-Z ]+")) ?: false
    nombreTecnicoGuardado = lTecnicos!!.any {
        it.nombres.toString()
            .uppercase() == tecnico?.nombres?.uppercase() && it.tecnicoId != tecnico.tecnicoId
    }
    tipoTecnicoNoVacio = tecnico?.tipoT.isNullOrEmpty() || tecnico?.tipoT?.isBlank() ?: false

    return !nombreTecnicoVacio && !sueldoHoraVacio && !nombreTecnicoSinSimbolos && !nombreTecnicoGuardado && !tipoTecnicoNoVacio
}


@Preview
@Composable
private fun TecnicoPreview() {
    RegistroTecnicosTheme {
        TecnicoBody(
            uiState = TecnicoUIState(),
            onSaveTecnico = {},
            onNombresChanged = {},
            onSueldoHoraChanged = {},
            navController = rememberNavController(),
            tecnicos = emptyList(),
            tipoTecnicos = emptyList(),
            onTipoTecnicoChanged = {}
        )
    }
}