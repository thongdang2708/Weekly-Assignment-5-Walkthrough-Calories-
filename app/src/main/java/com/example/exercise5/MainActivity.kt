package com.example.exercise5

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.exercise5.ui.theme.Exercise5Theme
import kotlin.math.absoluteValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Exercise5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ColorieScreen();
                }
            }
        }
    }
}

@Composable
fun ColorieScreen () {
    var weightInput = remember {
        mutableStateOf("")
    }

    var weight = weightInput.value.toIntOrNull() ?: 0

    var male = remember {
        mutableStateOf(true)
    }

    var intensity = remember {
        mutableStateOf(1.3f)
    }

    var result = remember {
        mutableStateOf(0)
    }

    Column(
       modifier = Modifier.padding(8.dp),
       verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
       Heading(title = stringResource(R.string.calories))
       AgeField(weightInput = weightInput.value, onValueChange = {weightInput.value = it})
        GenderChoices(male = male.value, setGenderMale = { male.value = it})
        IntensityList(onClick = {intensity.value = it})
        Text(text = result.value.toString(), color = MaterialTheme.colors.secondary, fontWeight = FontWeight.Bold)
        Calculation(male = male.value, weight = weight, intensity = intensity.value, setResult ={result.value = it.absoluteValue} )
   }
}

@Composable
fun Heading (title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}


@Composable
fun GenderChoices (male: Boolean, setGenderMale: (Boolean) -> Unit) {
    Column(Modifier.selectableGroup()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = male, onClick = { setGenderMale(true) })
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = !male, onClick = { setGenderMale(false) })
            Text(text = "Female")
        }
    }
}

@Composable
fun AgeField (weightInput: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter weight")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun IntensityList (onClick: (Float) -> Unit) {

    var expanded = remember {
        mutableStateOf(false)
    }

    var selectedText = remember {
        mutableStateOf("Light")
    }

    var textFieldSize = remember {
        mutableStateOf(Size.Zero)
    }

    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very hard!")

    val icon = if (expanded.value)
        Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText.value,
            onValueChange = {selectedText.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize.value = coordinates.size.toSize()
                },
            label = { Text("Select intensity")},
            trailingIcon = {
                Icon(icon, "contentDescription", Modifier.clickable { expanded.value = !expanded.value })
            }
        )

        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }, modifier = Modifier.width(
            with(LocalDensity.current) {
                textFieldSize.value.width.toDp()
            }
        )) {
            items.forEach { label ->

                DropdownMenuItem(onClick = {
                    selectedText.value = label
                    var intensity : Float = when (label) {
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2f
                        "Very hard" -> 2.2f
                        else -> 0.0f
                    }
                    onClick(intensity)
                    expanded.value = false
                }) {
                    Text(text = label)
                }
            }
        }
    }


}

@Composable
fun Calculation (male: Boolean, weight: Int, intensity: Float, setResult: (Int) -> Unit) {
    println(weight);
    println(intensity);
    Button(
        onClick = {
            if (male) {
                setResult(((879 + 10.2 * weight) * intensity).toInt())
            } else {
                setResult(((795 + 7.18 * weight) * intensity).toInt())
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Calculate")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Exercise5Theme {
        ColorieScreen()
    }
}