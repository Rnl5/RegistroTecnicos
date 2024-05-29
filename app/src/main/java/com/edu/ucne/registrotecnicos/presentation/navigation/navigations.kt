package com.edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.edu.ucne.registrotecnicos.presentation.servicio_tecnico.ServicioTecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.servicio_tecnico.ServicioTecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.servicio_tecnico.ServicioTecnicoViewModel
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoViewModel
import com.edu.ucne.registrotecnicos.presentation.tipoTecnico.TipoTecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.tipoTecnico.TipoTecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.tipoTecnico.TipoTecnicoViewModel
import androidx.navigation.compose.NavHost
import com.edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import com.edu.ucne.registrotecnicos.data.repository.ServicioTecnicoRepository
import com.edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import com.edu.ucne.registrotecnicos.data.repository.TipoTecnicoRepository
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme


private lateinit var tecnicoDb: TecnicoDb


@Composable
fun navegacion(
    tecnicoDb: TecnicoDb,
    repository: TecnicoRepository,
    tipoTecnicoRepository: TipoTecnicoRepository,
    servicioTecnicoRepository: ServicioTecnicoRepository
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.TecnicoList) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                viewModel = viewModel {
                    TecnicoViewModel(
                        repository,
                        0,
                        tipoTecnicoRepository
                    )
                },
                onVerTecnico = {
                    navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                },
                navController = navController,
                onAgregarTecnico = {
                    navController.navigate(Screen.Tecnico(0))
                }
            )
        }

        composable<Screen.ServicioList> {
            ServicioTecnicoListScreen(
                viewModel = viewModel {
                    ServicioTecnicoViewModel(
                        servicioTecnicoRepository,
                        0,
                        repository
                    )
                },
                onVerServicio = {
                    navController.navigate(Screen.Servicio(it.servicioTecnicoId ?: 0))
                },
                navController = navController,
                onAgregarServicio = {
                    navController.navigate(Screen.Servicio(0))
                }
            )
        }

        composable<Screen.Tecnico> {
            val args = it.toRoute<Screen.Tecnico>()
            TecnicoScreen(
                viewModel = viewModel {
                    TecnicoViewModel(
                        repository,
                        args.tecnicoId,
                        tipoTecnicoRepository
                    )
                },
                navController = navController
            )
        }

        composable<Screen.Servicio> {
            val args = it.toRoute<Screen.Servicio>()
            ServicioTecnicoScreen(viewModel = viewModel {
                ServicioTecnicoViewModel(
                    servicioTecnicoRepository,
                    args.servicioTecnicoId,
                    repository
                )
            },
                navController = navController)
        }

        composable<Screen.TipoTecnico> {
            val args = it.toRoute<Screen.TipoTecnico>()
            TipoTecnicoScreen(
                viewModel = viewModel {
                    TipoTecnicoViewModel(
                        tipoTecnicoRepository,
                        args.tipoTecnicoId
                    )
                },
                navController = navController
            )
        }

        composable<Screen.TipoTecnicoList> {
            TipoTecnicoListScreen(
                viewModel = viewModel {
                    TipoTecnicoViewModel(
                        tipoTecnicoRepository,
                        0
                    )
                },
                navController = navController,
                onVerTipoTecnico = {
                    navController.navigate(Screen.TipoTecnico(it.tipoTecnicoId ?: 0))
                },
                onAgregarTipoTecnico = {
                    navController.navigate(Screen.TipoTecnico(0))
                }
            )
        }
    }
}