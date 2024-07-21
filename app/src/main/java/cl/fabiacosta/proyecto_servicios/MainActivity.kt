package cl.fabiacosta.proyecto_servicios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.fabiacosta.proyecto_servicios.data.MeterReading
import cl.fabiacosta.proyecto_servicios.data.MeterReadingRepository
import cl.fabiacosta.proyecto_servicios.data.meterReadingsDePrueba
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
            val navController = rememberNavController()
            App(navController, meterReadingRepository)
        }
    }
}

@Composable
fun App(navController: NavHostController, meterReadingRepository: MeterReadingRepository) {
    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            MeterReadingsApp(onAddClick = {
                navController.navigate("formulario")
            })
        }
        composable("formulario") {
            MeterReadingForm(
                meterReadingRepository = meterReadingRepository,
                onSaveComplete = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MeterReadingsApp(onAddClick: () -> Unit) {
    val context = LocalContext.current
    var readings by remember { mutableStateOf(emptyList<MeterReading>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            readings = MeterReadingRepository.getInstance(context).obtenerTodos()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ReadingListUI(readings)
        }
    }
}

@Composable
fun ReadingListUI(readings: List<MeterReading>) {
    LazyColumn {
        items(readings) { reading ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                val icon = when (reading.meterType) {
                    "AGUA" -> R.drawable.agua
                    "LUZ" -> R.drawable.luz
                    "GAS" -> R.drawable.gas
                    else -> null
                }
                if (icon != null) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Meter type
                Text(
                    text = reading.meterType,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Meter ID
                Text(
                    text = reading.meterId,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Date
                Text(
                    text = reading.date.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Divider()
        }
    }
}

@Composable
fun MeterReadingForm(
    meterReadingRepository: MeterReadingRepository,
    onSaveComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val (meterId, setMeterId) = remember { mutableStateOf("") }
    val (selectedMeterType, setSelectedMeterType) = remember { mutableStateOf("AGUA") }
    val date = LocalDate.now()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro Medidor", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = meterId,
            onValueChange = setMeterId,
            label = { Text("Medidor") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date.toString(),
            onValueChange = { /* no-op */ },
            label = { Text("Fecha") },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text("Medidor de:", style = TextStyle(fontSize = 16.sp))
            RadioButtonGroup(
                options = listOf("AGUA", "LUZ", "GAS"),
                selectedOption = selectedMeterType,
                onOptionSelected = setSelectedMeterType
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                val reading = MeterReading(
                    id = 0, // Room will auto-generate the ID
                    meterId = meterId,
                    date = date,
                    meterType = selectedMeterType
                )
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        meterReadingRepository.agregar(reading)
                    }
                    onSaveComplete()
                }
            }) {
                Text("Registrar medición")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Text(text = option)
            }
        }
    }
}