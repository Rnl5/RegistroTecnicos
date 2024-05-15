package com.edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room

import com.edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoListScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoScreen
import com.edu.ucne.registrotecnicos.presentation.tecnico.TecnicoViewModel
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

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
                Surface {
                    val viewModel: TecnicoViewModel = viewModel(
                        factory = TecnicoViewModel.provideFactory(repository)
                    )
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(8.dp)
                        ) {
                            TecnicoScreen(viewModel = viewModel)

                            TecnicoListScreen(
                                viewModel = viewModel,
                                onDeleteTecnico = { tecnico ->
                                    LaunchedEffect(Unit) {
                                        viewModel.deleteTecnico(tecnico)
                                    }
                                },
                                onVerTecnico = {}
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview(){
    RegistroTecnicosTheme {

    }
}