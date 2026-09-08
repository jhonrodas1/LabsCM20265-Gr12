package co.edu.udea.compumovil.gr12_20265.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr12_20265.lab1.ui.theme.Labs20265Gr12Theme

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Labs20265Gr12Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
    // Variables de estado
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("Hombre") }
    var fechaNacimiento by rememberSaveable { mutableStateOf("") }
    var gradoEscolaridad by rememberSaveable { mutableStateOf("") }
    var otroEscolaridadTexto by rememberSaveable { mutableStateOf("") }

    // Estados de error para validación de campos obligatorios
    var errorNombres by rememberSaveable { mutableStateOf(false) }
    var errorApellidos by rememberSaveable { mutableStateOf(false) }
    var errorFecha by rememberSaveable { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
            errorFecha = false
        },
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH),
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

    // Variables para el Spinner (Grado de escolaridad)
    var menuExpandido by rememberSaveable { mutableStateOf(false) }
    val otroLabel = stringResource(R.string.escolaridad_otro)
    val opcionesEscolaridad = listOf(
        stringResource(R.string.escolaridad_primaria),
        stringResource(R.string.escolaridad_secundaria),
        stringResource(R.string.escolaridad_universitaria),
        otroLabel
    )

    val hombreLabel = stringResource(R.string.sexo_hombre)
    val mujerLabel = stringResource(R.string.sexo_mujer)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.titulo_info_personal),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nombres,
            onValueChange = {
                nombres = it
                if (it.isNotBlank()) errorNombres = false
            },
            label = { Text(stringResource(R.string.label_nombres)) },
            isError = errorNombres,
            supportingText = {
                if (errorNombres) Text(stringResource(R.string.error_campo_obligatorio))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = apellidos,
            onValueChange = {
                apellidos = it
                if (it.isNotBlank()) errorApellidos = false
            },
            label = { Text(stringResource(R.string.label_apellidos)) },
            isError = errorApellidos,
            supportingText = {
                if (errorApellidos) Text(stringResource(R.string.error_campo_obligatorio))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.label_sexo), style = MaterialTheme.typography.bodyLarge)
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(selected = sexo == "Hombre", onClick = { sexo = "Hombre" })
            Text(hombreLabel)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = sexo == "Mujer", onClick = { sexo = "Mujer" })
            Text(mujerLabel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // aqui tenemos la fecha de nacimiento
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.label_fecha_nacimiento),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (fechaNacimiento.isEmpty()) stringResource(R.string.fecha_placeholder) else fechaNacimiento,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Button(onClick = { datePickerDialog.show() }) {
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

        // escolaridad
        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = !menuExpandido }
        ) {
            OutlinedTextField(
                value = gradoEscolaridad,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_grado_escolaridad)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
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
                            // Si cambia a una opción distinta de "Otro", limpiamos el texto libre
                            if (opcion != otroLabel) {
                                otroEscolaridadTexto = ""
                            }
                        }
                    )
                }
            }
        }

        // lo ponemos por si el usuario elije la opcion otro, para que especifique si lo desea
        if (gradoEscolaridad == otroLabel) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = otroEscolaridadTexto,
                onValueChange = { otroEscolaridadTexto = it },
                label = { Text(stringResource(R.string.label_especifique_escolaridad)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // boton siguiente
        Button(
            onClick = {
                errorNombres = nombres.isBlank()
                errorApellidos = apellidos.isBlank()
                errorFecha = fechaNacimiento.isBlank()

                if (!errorNombres && !errorApellidos && !errorFecha) {
                    // Log de los datos ingresados
                    Log.d(TAG, "Información personal:")
                    Log.d(TAG, "$nombres $apellidos")
                    Log.d(TAG, if (sexo == "Hombre") "Masculino" else "Femenino")
                    Log.d(TAG, "Nació el $fechaNacimiento")
                    if (gradoEscolaridad.isNotBlank()) {
                        if (gradoEscolaridad == otroLabel && otroEscolaridadTexto.isNotBlank()) {
                            Log.d(TAG, "$gradoEscolaridad: $otroEscolaridadTexto")
                        } else {
                            Log.d(TAG, gradoEscolaridad)
                        }
                    }
                } else {
                    Log.d(TAG, "Validación fallida: faltan campos obligatorios")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.boton_siguiente))
        }
    }
}