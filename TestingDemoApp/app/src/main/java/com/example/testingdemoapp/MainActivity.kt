package com.example.testingdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.testingdemoapp.ui.theme.TestingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Testing()
                }
            }
        }
    }

    // this function is testing in a unit test, in ExampleUnitTest
    companion object {
        fun add2(a: Int, b: Int) = a + b
    }
}
@Composable
fun Testing( vm :AdditionViewModel = AdditionViewModel() ) {

    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(10.dp)
    ) {

        OutlinedTextField(
            value = a.toString(),
            onValueChange = { a = it },
            modifier = Modifier.width(80.dp).padding(10.dp).testTag("text-a")
        )

        OutlinedTextField(
            value = b.toString(),
            onValueChange = { b = it },
            modifier = Modifier.width(80.dp).padding(10.dp).testTag("text-b")
        )

        Button(
            onClick = { c = vm.add(a.toInt(),b.toInt()).toString() },
            modifier = Modifier.width(100.dp).padding(10.dp).testTag("button-add")
        ) {Text("ADD")}

        OutlinedTextField(
            value = c.toString(),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.width(80.dp).padding(10.dp).testTag("text-c")
        )

    }
}

class AdditionViewModel : ViewModel() {

    fun add( a :Int, b :Int ) : Int { return a + b }

}


