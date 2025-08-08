package com.example.myscaffoldapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch

//use older version of material for the Scaffold - version 3 is still considered experimental
//Add androidx.compose.material @1.6.8 (latest stable version)
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Icon
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.BottomAppBar
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.OutlinedTextField

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch

import com.example.myscaffoldapp.ui.theme.MyScaffoldAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScaffoldAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScaffold()
                }
            }
        }
    }
}

//Newer linting is looking for version structure - this annotation turns that off
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyScaffold() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { androidx.compose.material.Text("My Scaffold v2 App") },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                        contentDescription = "Menu",
                        modifier = Modifier.clickable {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    )
                },
                backgroundColor = androidx.compose.material.MaterialTheme.colors.primary
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                androidx.compose.material.Text("!!")
            }
        },
        drawerContent = { androidx.compose.material.Text(text = "Menu") },
        content = { ScreenPlay() },
        bottomBar = {
            BottomAppBar(
                backgroundColor = androidx.compose.material.MaterialTheme.colors.primary,
            ) {
                androidx.compose.material.Text(
                    "Playing with the Screen!", textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )

}

@Composable
fun ScreenPlay() {

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        var name by remember { mutableStateOf("") }
        OutlinedTextField(
            value = name,
            onValueChange = { it -> name = it },
            label = { Text("Username") },
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = name,
            modifier = Modifier
                .padding(start = 25.dp)
                .height(30.dp)
        )

        var checkStatus by remember {
            mutableStateOf(false)
        }
        Row {
            Checkbox(
                checked = checkStatus,
                onCheckedChange = { checkStatus = it })
            Text(
                text = "Checkbox",
                Modifier
                    .clickable { checkStatus = !checkStatus }
                    .padding(start = 0.dp, top = 12.dp, end = 5.dp)
            )
        }
        Row {
            Text(
                text = "On/Off",
                Modifier
                    .clickable { checkStatus = !checkStatus }
                    .padding(start = 10.dp, top = 12.dp)
            )
            Switch(
                checked = checkStatus,
                onCheckedChange = { checkStatus = it }
            )
        }


        val hotdogToppings = listOf("none", "Mustard", "Ketchup")

        val (selected, onOptionSelected) = remember {
            mutableStateOf(hotdogToppings[0])
        }
        Column(Modifier.padding(4.dp)) {
            Text(
                "Hotdog toppings",
                modifier = Modifier.padding(15.dp)
            )
            hotdogToppings.forEach { topping ->
                Row(modifier = Modifier.padding(4.dp)) {
                    RadioButton(
                        selected = selected == topping,
                        onClick = { onOptionSelected(topping) })
                    Text(
                        text = topping,
                        modifier = Modifier
                            .clickable { onOptionSelected(topping) }
                            .padding(top = 10.dp))
                }
            }
        }
        Column() {


            var sliderValue by remember {
                mutableStateOf(0f)
            }
            Text(
                text = sliderValue.toInt().toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                //steps = 10, //determines number of steps
                valueRange = -100f..100f,
                modifier = Modifier.padding(20.dp)
            )

        }


    }

}
