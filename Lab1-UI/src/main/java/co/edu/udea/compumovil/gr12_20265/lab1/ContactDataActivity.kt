package co.edu.udea.compumovil.gr12_20265.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr12_20265.lab1.ui.theme.Labs20265Gr12Theme

class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Labs20265Gr12Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ContactDataScreen()
                }
            }
        }
    }
}

private const val TAG = "ContactData"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDataScreen() {
    // Variables de estado
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pais by rememberSaveable { mutableStateOf("") }
    var ciudad by rememberSaveable { mutableStateOf("") }

    // Estados de error
    var errorTelefono by rememberSaveable { mutableStateOf(value = false) }
    var errorEmail by rememberSaveable { mutableStateOf(value = false) }
    var errorPais by rememberSaveable { mutableStateOf(value = false) }

    // Listas para Autocompletado
    val paisesLatam = androidx.compose.ui.res.stringArrayResource(R.array.paises_latam).toList()
    
    // Cargamos ciudades iniciales desde recursos y luego actualizamos desde API
    val ciudadesIniciales = androidx.compose.ui.res.stringArrayResource(R.array.ciudades_colombia).toList()
    var ciudadesColombia by remember { mutableStateOf(ciudadesIniciales) }

    // Efecto para cargar ciudades desde la API
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.cityService.getCities()
            if (response.isNotEmpty()) {
                ciudadesColombia = response.map { it.name }.sorted()
                Log.d(TAG, "Ciudades cargadas desde API: ${ciudadesColombia.size}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cargando ciudades desde API, usando locales", e)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(R.string.titulo_info_contacto),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Teléfono (Teclado telefónico)
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it; errorTelefono = false },
            label = { Text(stringResource(R.string.label_telefono)) },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            isError = errorTelefono,
            supportingText = { if (errorTelefono) Text(stringResource(R.string.error_campo_obligatorio)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email (Teclado email)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorEmail = false },
            label = { Text(stringResource(R.string.label_email)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = errorEmail,
            supportingText = { if (errorEmail) Text(stringResource(R.string.error_campo_obligatorio)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // País (Autocomplete)
        AutoCompleteField(
            value = pais,
            onValueChange = { pais = it; errorPais = false },
            label = stringResource(R.string.label_pais),
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            options = paisesLatam,
            isError = errorPais,
            errorMessage = stringResource(R.string.error_campo_obligatorio)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ciudad (Autocomplete)
        AutoCompleteField(
            value = ciudad,
            onValueChange = { ciudad = it },
            label = stringResource(R.string.label_ciudad),
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
            options = ciudadesColombia
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dirección (Sin sugerencias, teclado normal)
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text(stringResource(R.string.label_direccion)) },
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrectEnabled = false, // Sin sugerencias
                imeAction = ImeAction.Done // Último campo
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                errorTelefono = telefono.isBlank()
                errorEmail = email.isBlank()
                errorPais = pais.isBlank()

                if (!errorTelefono && !errorEmail && !errorPais) {
                    Log.d(TAG, "Información de contacto:")
                    Log.d(TAG, "Teléfono: $telefono")
                    if (direccion.isNotBlank()) Log.d(TAG, "Dirección: $direccion")
                    Log.d(TAG, "Email: $email")
                    Log.d(TAG, "País: $pais")
                    if (ciudad.isNotBlank()) Log.d(TAG, "Ciudad: $ciudad")
                } else {
                    Log.d(TAG, "Validación fallida: faltan campos obligatorios")
                }
            },
            modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.boton_siguiente))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
) {
    var expanded by remember { mutableStateOf(value = false) }
    // Filtramos las opciones según lo que el usuario vaya escribiendo
    val filteredOptions = options.filter { it.contains(value, ignoreCase = true) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            label = { Text(label) },
            leadingIcon = leadingIcon,
            isError = isError,
            supportingText = { if (isError) Text(errorMessage) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next, autoCorrectEnabled = false),
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        if (filteredOptions.isNotEmpty() && expanded) {
            ExposedDropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}