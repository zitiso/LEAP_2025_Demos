package com.example.animationdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animationdemoapp.ui.theme.AnimationDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

// visibility animation

        var visible by remember { mutableStateOf(false) }

        Button(onClick = { visible = !visible }) { Text("Visibily Animation") }

        AnimatedVisibility(visible) { Text("hello out there!") }

        // colour animation

        var animate by remember { mutableStateOf(true) }
        val color by animateColorAsState(if (animate) Color.Red else Color.Blue)

        Column(modifier = Modifier.drawBehind { drawRect(color) }.padding(10.dp)) {
            Button(onClick = { animate = !animate }) {
                Text("Colour Animation")
            }
        }
        // resize animation
        var expanded by remember { mutableStateOf(false) }
        Button(onClick = { expanded = !expanded }) { Text("Resize Animation") }
        Box(
            modifier = Modifier.background(Color.LightGray)
                .animateContentSize()
                .height(if (expanded) 200.dp else 100.dp)
                .fillMaxWidth()
        )

// transition animation
        val infiniteTransition = rememberInfiniteTransition()

        val scale by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        )
        Text(text = "Echo",
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }

        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimationDemoAppTheme {
        Greeting("Android")
    }
}