package co.edu.udea.compumovil.gr12_20265.lab1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr12_20265.lab1.ui.theme.Labs20265Gr12Theme
import java.util.Calendar

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Labs20265Gr12Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    PersonalDataScreen()
                }
            }
        }
    }
}

private const val TAG = "PersonalData"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen() {
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("Hombre") }
    var fechaNacimiento by rememberSaveable { mutableStateOf("") }
    var gradoEscolaridad by rememberSaveable { mutableStateOf("") }
    var otroEscolaridadTexto by rememberSaveable { mutableStateOf("") }

    var errorNombres by rememberSaveable { mutableStateOf(value = false) }
    var errorApellidos by rememberSaveable { mutableStateOf(value = false) }
    var errorFecha by rememberSaveable { mutableStateOf(value = false) }
    var errorOtro by rememberSaveable { mutableStateOf(value = false) }

    val context = LocalContext.current
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                        cal.timeInMillis = millis
                        val day = cal.get(java.util.Calendar.DAY_OF_MONTH)
                        val month = cal.get(java.util.Calendar.MONTH) + 1
                        val year = cal.get(java.util.Calendar.YEAR)
                        fechaNacimiento = "$day/$month/$year"
                        errorFecha = false
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    var menuExpandido by rememberSaveable { mutableStateOf(value = false) }
    val otroLabel = stringResource(R.string.escolaridad_otro)
    val opcionesEscolaridad = listOf(
        stringResource(R.string.escolaridad_primaria),
        stringResource(R.string.escolaridad_secundaria),
        stringResource(R.string.escolaridad_universitaria),
        otroLabel,
    )

    val hombreLabel = stringResource(R.string.sexo_hombre)
    val mujerLabel = stringResource(R.string.sexo_mujer)

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(R.string.titulo_info_personal),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CampoNombres(nombres, errorNombres, { nombres = it; errorNombres = false }, Modifier.weight(1f))
                CampoApellidos(apellidos, errorApellidos, { apellidos = it; errorApellidos = false }, Modifier.weight(1f))
            }
        } else {
            CampoNombres(nombres, errorNombres, { nombres = it; errorNombres = false }, Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            CampoApellidos(apellidos, errorApellidos, { apellidos = it; errorApellidos = false }, Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountBox, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(stringResource(R.string.label_sexo), style = MaterialTheme.typography.bodyLarge)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(selected = sexo == "Hombre", onClick = { sexo = "Hombre" })
            Text(hombreLabel)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = sexo == "Mujer", onClick = { sexo = "Mujer" })
            Text(mujerLabel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = stringResource(R.string.label_fecha_nacimiento),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = fechaNacimiento.ifEmpty { stringResource(R.string.fecha_placeholder) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Button(onClick = { showDatePicker = true }) {
                    Text(stringResource(R.string.boton_cambiar), maxLines = 1)
                }
            }
            if (errorFecha) {
                Text(
                    text = stringResource(R.string.error_campo_obligatorio),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = !menuExpandido }
        ) {
            OutlinedTextField(
                value = gradoEscolaridad,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_grado_escolaridad)) },
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                opcionesEscolaridad.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            gradoEscolaridad = opcion
                            menuExpandido = false
                            if (opcion != otroLabel) otroEscolaridadTexto = ""
                        }
                    )
                }
            }
        }

        if (gradoEscolaridad == otroLabel) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = otroEscolaridadTexto,
                onValueChange = { 
                    otroEscolaridadTexto = it
                    errorOtro = false
                },
                label = { Text(stringResource(R.string.label_especifique_escolaridad)) },
                isError = errorOtro,
                supportingText = { if (errorOtro) Text(stringResource(R.string.error_campo_obligatorio)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                errorNombres = nombres.isBlank()
                errorApellidos = apellidos.isBlank()
                errorFecha = fechaNacimiento.isBlank()
                errorOtro = (gradoEscolaridad == otroLabel) && otroEscolaridadTexto.isBlank()

                if (!errorNombres && !errorApellidos && !errorFecha && !errorOtro) {
                    Log.d(TAG, "Información personal:")
                    Log.d(TAG, "$nombres $apellidos")
                    Log.d(TAG, if (sexo == "Hombre") "Masculino" else "Femenino")
                    Log.d(TAG, "Nació el $fechaNacimiento")
                    if (gradoEscolaridad.isNotBlank()) {
                        if ((gradoEscolaridad == otroLabel) && otroEscolaridadTexto.isNotBlank()) {
                            Log.d(TAG, otroEscolaridadTexto)
                        } else {
                            Log.d(TAG, gradoEscolaridad)
                        }
                    }

                    // NAVEGACIÓN A LA SEGUNDA PANTALLA
                    val intent = Intent(context, ContactDataActivity::class.java)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.boton_siguiente))
        }
    }
}

@Composable
fun CampoNombres(nombres: String, errorNombres: Boolean, onNombresChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = nombres,
        onValueChange = onNombresChange,
        label = { Text(stringResource(R.string.label_nombres)) },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        isError = errorNombres,
        supportingText = { if (errorNombres) Text(stringResource(R.string.error_campo_obligatorio)) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Words, autoCorrectEnabled = false, imeAction = ImeAction.Next)
    )
}

@Composable
fun CampoApellidos(apellidos: String, errorApellidos: Boolean, onApellidosChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = apellidos,
        onValueChange = onApellidosChange,
        label = { Text(stringResource(R.string.label_apellidos)) },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        isError = errorApellidos,
        supportingText = { if (errorApellidos) Text(stringResource(R.string.error_campo_obligatorio)) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Words, autoCorrectEnabled = false, imeAction = ImeAction.Next)
    )
}