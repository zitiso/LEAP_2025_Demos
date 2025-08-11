package com.example.restclassdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.restclassdemoapp.ui.theme.RESTClassDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val teamViewModel = TeamViewModel()
        setContent {
            RESTClassDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        TeamView(teamViewModel)
                        Spacer(
                            modifier = Modifier
                                .padding(top = 5.dp, bottom = 5.dp)
                                .background(Color.Blue)
                                .height(5.dp)
                                .fillMaxWidth()
                        )
                        InsertTeamView(teamViewModel)
                    }
                }
            }
        }
    }
}
