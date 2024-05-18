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
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoViewModel
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
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TecnicoList)
                {
                    composable<Screen.TecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository, 0) },

                            onVerTecnico = {
                                navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                            })
                    }

                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(viewModel = viewModel {
                            TecnicoViewModel(
                                repository,
                                args.tecnicoId
                            )
                        })
                    }
                }
            }
        }
    }
}


//                        val args = it.toRoute<Screen.Tecnico>()
//                        Box(modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center)
//                        {
//                            Text(text = "Estas viendo el ticket ${args.tecnicoId}")
//
//                            OutlinedButton(onClick = { navController.navigate(Screen.Tecnico)}) {
//
//                            }
//
//
//                        }

//TecnicoScreen(viewModel = viewModel)
//}
//}


//                Surface {
//                    val viewModel: TecnicoViewModel = viewModel(
//                        factory = TecnicoViewModel.provideFactory(repository)
//                    )
//                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(innerPadding)
//                                .padding(8.dp)
//                        ) {
//                            TecnicoScreen(viewModel = viewModel)
//
//                            TecnicoListScreen(
//                                viewModel = viewModel,
//                                onDeleteTecnico = { tecnico ->
//                                    LaunchedEffect(Unit) {
//                                        viewModel.deleteTecnico(tecnico)
//                                    }
//                                },
//                                onVerTecnico = {}
//                            )
//                        }
//                    }
//                }
//}
//}
//}
//}


sealed class Screen() {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    RegistroTecnicosTheme {

    }
}