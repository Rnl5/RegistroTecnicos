package com.edu.ucne.registrotecnicos.presentation.tipoTecnico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.edu.ucne.registrotecnicos.Screen
import com.edu.ucne.registrotecnicos.data.local.entities.TipoTecnicoEntity
import com.edu.ucne.registrotecnicos.presentation.components.BotonAgregarFlotanteTexto
import com.edu.ucne.registrotecnicos.presentation.components.TopAppBar
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch

@Composable
fun TipoTecnicoListScreen(
    viewModel: TipoTecnicoViewModel,
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    navController: NavHostController,
    onAgregarTipoTecnico: () -> Unit
) {
    val tiposTecnicos by viewModel.tiposTecnicos.collectAsStateWithLifecycle()

    var tipoTecnicoSeleccionado by remember { mutableStateOf<TipoTecnicoEntity?>(null) }

    val scope = rememberCoroutineScope()

    TipoTecnicoListBody(
        tiposTecnicos = tiposTecnicos,
        onVerTipoTecnico = { tipoTecnico ->
            tipoTecnicoSeleccionado = tipoTecnico
            onVerTipoTecnico(tipoTecnico)
        },
        onDeleteTipoTecnico = { tipoTecnico ->
            scope.launch {
                viewModel.deleteTipoTecnico(tipoTecnico)
            }
        },
        navController = navController,
        onAgregarTipoTecnico = onAgregarTipoTecnico
    )
}

@Composable
fun TipoTecnicoListBody(
    tiposTecnicos: List<TipoTecnicoEntity>,
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    onDeleteTipoTecnico: (TipoTecnicoEntity) -> Unit,
    navController: NavHostController,
    onAgregarTipoTecnico: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                Modifier.requiredWidth(220.dp)
            ) {
                Text("Tecnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Técnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Tecnicos"
                        )
                    }
                )

                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Tipos de Técnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Tipos de Tecnicos"
                        )
                    }
                )
            }
        },
        drawerState = drawerState,
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Tipos de Tecnicos",
                    onTopBarClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    })
            },
        floatingActionButton = {
            BotonAgregarFlotanteTexto(title = "Agregar Tipo de Tecnico",
                onAgregarTipoTecnico)
        }

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(tiposTecnicos) { tipoTecnico ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerTipoTecnico(tipoTecnico) }
                                .padding(8.dp)
                        ) {
                            Text(
                                text = tipoTecnico.tipoTecnicoId.toString(),
                                modifier = Modifier.weight(0.10f)
                            )
                            Text(
                                text = tipoTecnico.descripcion.toString(),
                                modifier = Modifier.weight(0.80f)
                            )

                            IconButton(
                                onClick = { onDeleteTipoTecnico(tipoTecnico) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar tipo de tecnico"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

    @Preview
    @Composable
    private fun TipoTecnicoListPreview() {
        val tiposTecnicos = listOf(
            TipoTecnicoEntity(
                descripcion = "Esta es mi descripcion"
            )
        )

        RegistroTecnicosTheme {
            TipoTecnicoListBody(
                tiposTecnicos = tiposTecnicos,
                onVerTipoTecnico = {},
                navController = rememberNavController(),
                onDeleteTipoTecnico = {},
                onAgregarTipoTecnico = {}
            )
        }
    }