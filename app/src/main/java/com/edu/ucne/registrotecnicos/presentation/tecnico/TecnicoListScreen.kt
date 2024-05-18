package com.edu.ucne.registrotecnicos.presentation.tecnico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.presentation.components.TopAppBar
import com.edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
//    onDeleteTecnico: @Composable (TecnicoEntity) -> Unit
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }

    val scope = rememberCoroutineScope()

    TecnicoListBody(
        tecnicos = tecnicos,
        onVerTecnico = { tecnico ->
            tecnicoSeleccionado = tecnico
            onVerTecnico(tecnico)
        },
        onDeleteTecnico = { tecnico ->
            scope.launch {
                viewModel.deleteTecnico(tecnico)
            }
        }
    )
}

@Composable
fun TecnicoListBody(
    tecnicos: List<TecnicoEntity>,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onDeleteTecnico: (TecnicoEntity) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = "Tecnicos") }) { innerPadding ->
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
                items(tecnicos) { tecnico ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerTecnico(tecnico) }
                            .padding(16.dp)
                    ) {
                        Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                        Text(text = tecnico.nombres, modifier = Modifier.weight(0.40f))
                        Text(text = tecnico.sueldoHora.toString(), modifier = Modifier.weight(0.40f))

                        IconButton(
                            onClick = { onDeleteTecnico(tecnico) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar tecnico"
                            )
                        }
                    }
                }
            }
        }
    }
}

//     {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            items(tecnicos) { tecnico ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onVerTecnico(tecnico) }
//                        .padding(16.dp)
//                ) {
//                    Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
//                    Text(text = tecnico.nombres, modifier = Modifier.weight(0.40f))
//                    Text(text = tecnico.sueldoHora.toString(), modifier = Modifier.weight(0.40f))
//
//
//                    IconButton(
//                        onClick = {onDeleteTecnico(tecnico)}
//                    ) {
//                       Icon(
//                           imageVector = Icons.Default.Delete,
//                           contentDescription = "Eliminar tecnico")
//                    }
//                }
//            }
//        }
//    }
//}


@Preview
@Composable
private fun TecnicoListPreview() {
    val tecnicos = listOf(
        TecnicoEntity(
            nombres = "Ronell Jesus",
            sueldoHora = 27950.0
        )
    )
    RegistroTecnicosTheme {
        TecnicoListBody(tecnicos = tecnicos, onVerTecnico = {}) {

        }
    }
}