package com.edu.ucne.registrotecnicos.presentation.tipoTecnico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.edu.ucne.registrotecnicos.Screen
import com.edu.ucne.registrotecnicos.data.local.entities.TipoTecnicoEntity
import com.edu.ucne.registrotecnicos.presentation.components.TopAppBar
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch

var descripcionTipoTecnicoVacia by mutableStateOf(false)
var descripcionTipoTecnicoGuardada by mutableStateOf(false)

@Composable
fun TipoTecnicoScreen(
    viewModel: TipoTecnicoViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tiposTecnicos by viewModel.tiposTecnicos.collectAsStateWithLifecycle()

    TipoTecnicoBody(
        uiState = uiState,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        onSaveTipoTecnico = {
            viewModel.saveTipoTecnico()
        },
        tiposTecnicos = tiposTecnicos,
        navController = navController
    )
}

@Composable
fun TipoTecnicoBody(
    uiState: TipoTecnicoUIState,
    onDescripcionChanged: (String) -> Unit,
    onSaveTipoTecnico: () -> Unit,
    tiposTecnicos: List<TipoTecnicoEntity>,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
                Text(text = "Registro de tipos de tecnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Tipos de tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Tipos de Tecnicos"
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ) {}
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Tipo de tecnicos",
                onTopBarClick = {
                    scope.launch {
                        drawerState.open()
                    }
                })
        }
    ) { innerPadding ->
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
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = onDescripcionChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(onClick = {
                            descripcionTipoTecnicoVacia = false
                            descripcionTipoTecnicoGuardada = false
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button2"
                            )
                            Text(text = "Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if(ValidarTipoTecnico(
                                    TipoTecnicoEntity(
                                        tipoTecnicoId = uiState.tipoTecnicoId,
                                        descripcion = uiState.descripcion.toString()
                                    ),
                                    lTiposTecnicos = tiposTecnicos
                                )){
                                    onSaveTipoTecnico()
                                    navController.navigate(Screen.TipoTecnicoList)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button2"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}


fun ValidarTipoTecnico(
    tipoTecnico: TipoTecnicoEntity?,
    lTiposTecnicos: List<TipoTecnicoEntity>?
): Boolean {
    descripcionTipoTecnicoVacia =
        tipoTecnico?.descripcion.isNullOrEmpty() || tipoTecnico?.descripcion?.isBlank() ?: false
    descripcionTipoTecnicoGuardada =
        lTiposTecnicos!!.any { it.descripcion.toString().uppercase() == tipoTecnico?.descripcion?.uppercase() && it.tipoTecnicoId != tipoTecnico.tipoTecnicoId }

    return !descripcionTipoTecnicoVacia && !descripcionTipoTecnicoGuardada
}


@Preview
@Composable
private fun TipoTecnicoPreview() {
    RegistroTecnicosTheme {
        TipoTecnicoBody(
            uiState = TipoTecnicoUIState(),
            onSaveTipoTecnico = {},
            onDescripcionChanged = {},
            navController = rememberNavController(),
            tiposTecnicos = emptyList()
        )
    }
}