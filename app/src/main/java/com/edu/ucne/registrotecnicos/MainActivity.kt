package com.edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room

import com.edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import com.edu.ucne.registrotecnicos.data.repository.TipoTecnicoRepository
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoViewModel
import com.edu.ucne.registrotecnicos.presentation.tipoTecnico.TipoTecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.tipoTecnico.TipoTecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.tipoTecnico.TipoTecnicoViewModel
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val tipoTecnicoRepository = TipoTecnicoRepository(tecnicoDb.tipoTecnicoDao())
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TecnicoList)
                {
                    composable<Screen.TecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository, 0, tipoTecnicoRepository) },
                            onVerTecnico = {
                                navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                            },
                            navController = navController,
                            onAgregarTecnico = {
                                navController.navigate(Screen.Tecnico(0))
                            }
                        )
                    }

                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(viewModel = viewModel {
                            TecnicoViewModel(
                                repository,
                                args.tecnicoId,
                                tipoTecnicoRepository
                            )
                        },
                            navController = navController)
                    }

                    composable<Screen.TipoTecnico> {
                        val args = it.toRoute<Screen.TipoTecnico>()
                        TipoTecnicoScreen(viewModel = viewModel{
                            TipoTecnicoViewModel(
                                tipoTecnicoRepository,
                                args.tipoTecnicoId
                            ) },
                            navController = navController )
                    }

                    composable<Screen.TipoTecnicoList> {
                        TipoTecnicoListScreen(
                            viewModel = viewModel{TipoTecnicoViewModel(tipoTecnicoRepository,0)},
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
        }
    }
}


sealed class Screen() {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    object TipoTecnicoList: Screen()

    @Serializable
    data class TipoTecnico(val tipoTecnicoId: Int) : Screen()
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    RegistroTecnicosTheme {

    }
}