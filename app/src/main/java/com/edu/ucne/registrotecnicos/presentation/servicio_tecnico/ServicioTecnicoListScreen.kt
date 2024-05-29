package com.edu.ucne.registrotecnicos.presentation.servicio_tecnico

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
//import com.edu.ucne.registrotecnicos.Screen
import com.edu.ucne.registrotecnicos.data.local.entities.ServicioTecnicoEntity
import com.edu.ucne.registrotecnicos.presentation.components.BotonAgregarFlotanteTexto
import com.edu.ucne.registrotecnicos.presentation.components.TopAppBar
import com.edu.ucne.registrotecnicos.presentation.navigation.Screen
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch


@Composable
fun ServicioTecnicoListScreen(
    viewModel: ServicioTecnicoViewModel,
    onVerServicio: (ServicioTecnicoEntity) -> Unit,
    navController: NavHostController,
    onAgregarServicio: () -> Unit
) {
    val servicios by viewModel.servicios.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    ServicioTecnicoListBody(
        servicios = servicios,
        onVerServicio = onVerServicio,
        onDeleteServicio = { servicio ->
            scope.launch { viewModel.deleteServicio(servicio) }
        },
        navController = navController,
        onAgregarServicio = onAgregarServicio
    )
}


@Composable
fun ServicioTecnicoListBody(
    servicios: List<ServicioTecnicoEntity>,
    onVerServicio: (ServicioTecnicoEntity) -> Unit,
    onDeleteServicio: (ServicioTecnicoEntity) -> Unit,
    navController: NavHostController,
    onAgregarServicio: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
                Text(
                    text = "Servicios",
                    modifier = Modifier.padding(16.dp)
                )
                Divider()

                NavigationDrawerItem(
                    label = { Text(text = "Tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Tipos de tecnicos"
                        )
                    }
                )

                Divider()

                NavigationDrawerItem(
                    label = { Text(text = "Tipos de tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Tipos de tecnicos"
                        )
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Servicios") },
                    selected = false,
                    onClick = { navController.navigate(Screen.ServicioList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Servicios"
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Servicios",
                    onTopBarClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            floatingActionButton = {
                BotonAgregarFlotanteTexto(
                    title = "Agregar Servicio",
                    onAgregarServicio
                )
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
                    items(servicios) { servicio ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerServicio(servicio) }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = servicio.servicioTecnicoId.toString(),
                                modifier = Modifier.weight(0.10f)
                            )
                            Text(
                                text = servicio.fecha.toString(),
                                modifier = Modifier.weight(0.40f)
                            )
                            Text(
                                text = servicio.cliente.toString(),
                                modifier = Modifier.weight(0.30f)
                            )
                            Text(
                                text = servicio.total.toString(),
                                modifier = Modifier.weight(0.30f)
                            )
                            IconButton(onClick = { onDeleteServicio(servicio) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar Servicio"
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
private fun ServicioTecnicoListPreview() {
    val servicios = listOf(
        ServicioTecnicoEntity(
            cliente = "Rosario Lopez",
            descripcion = "Quiero un servicio para arreglar el internet",
            total = 14500.45
        )
    )
    RegistroTecnicosTheme {
        ServicioTecnicoListBody(
            servicios = servicios,
            onVerServicio = {},
            onDeleteServicio = {},
            navController = rememberNavController(),
            onAgregarServicio = {}
        )
    }
}