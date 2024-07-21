package cl.fabiacosta.proyecto_servicios.ui.theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import cl.fabiacosta.proyecto_servicios.data.MeterReading
import cl.fabiacosta.proyecto_servicios.data.MeterReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private lateinit var meterReadingRepository: MeterReadingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meterReadingRepository = MeterReadingRepository.getInstance(this)

        lifecycleScope.launch(Dispatchers.IO) {
            meterReadingsDePrueba().forEach {
                meterReadingRepository.agregar(it)
            }
        }

        setContent {
            AppMeterReadings()
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AppMeterReadings() {
    val contexto = LocalContext.current
    var meterReadings by remember {
        mutableStateOf(emptyList<MeterReading>())
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            meterReadings = MeterReadingRepository.getInstance(contexto).obtenerTodos()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = innerPadding.calculateLeftPadding(LayoutDirection.Ltr))
        ) {
            ListaMeterReadingsUI(meterReadings)
        }
    }
}

@Composable
fun ListaMeterReadingsUI(
    meterReadings: List<MeterReading>
) {
    LazyColumn() {
        items(meterReadings) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.date.toString(),
                        style = TextStyle(
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Text(it.notes)
                        Text(
                            text = it.readingValue.toString(),
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Row() {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            }
            Divider()
        }
    }
}
