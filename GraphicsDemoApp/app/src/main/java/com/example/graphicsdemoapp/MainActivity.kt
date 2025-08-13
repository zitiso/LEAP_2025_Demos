package com.example.graphicsdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.graphicsdemoapp.ui.theme.GraphicsDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphicsDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Draw()
                }
            }
        }
    }
}

@Composable
fun Draw() {
    Column {
        Text("Hello, Graphics!",
            modifier = Modifier
                //  .fillMaxSize()
                .drawBehind {
                    drawRect(color = Color.Yellow, size = Size(300f, 50f))
                }
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 10.dp, color = Color.Yellow)
        )
        {
            drawRect(
                color = Color.LightGray,
                size = Size(50f, 150f)
            )

            drawLine(
                Color.Red,
                Offset(50f, 50f),
                Offset(400f, 600f),
                strokeWidth = 25f
            )
            drawLine(
                Brush.verticalGradient(
                    listOf(Color.Red, Color.Blue)
                ),
                Offset(1000f, 300f),
                Offset(1000f, 600f),
                strokeWidth = 100f,
                cap = StrokeCap.Round,
            )

            drawCircle(Color.Red)

            drawCircle(
                Color.Blue,
                radius = size.minDimension / 3.3f,
                alpha = 0.5f // blend with red circle and produce purple
            )
            drawCircle(
                Color.Blue,
                radius = size.minDimension / 8.0f,
                alpha = 1f // overwrite red and blue circles
            )
            drawPoints(
                listOf(
                    Offset(50f, 350f),
                    Offset(100f, 345f),
                    Offset(150f, 310f),
                    Offset(200f, 200f),
                    Offset(250f, 280f),
                    Offset(300f, 140f),
                    Offset(350f, 100f),
                    Offset(400f, 180f),
                    Offset(450f, 220f),
                    Offset(500f, 330f)
                ),
                PointMode.Points,
                Color.Red,
                strokeWidth = 30.0f,
                cap = StrokeCap.Round
            )
        }

    }
}