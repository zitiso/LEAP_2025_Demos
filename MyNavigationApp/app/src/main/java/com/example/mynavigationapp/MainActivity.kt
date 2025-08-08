package com.example.mynavigationapp

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.mynavigationapp.ui.theme.MyNavigationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNavigationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNavHost()
                }
            }
        }
    }
}


@Composable
fun MyNavHost(
    //add dependency androidx.navigation navigation-runtime-ktx@2.7.7 navigation-compose@2.7.7
    navController: NavHostController = rememberNavController(),
    startDestination: String = "home"
) {
    Column {
        NavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            // build route graph here
            composable("home") {
                Home(
                    onNavigateToPageTwo = { navController.navigate("pageTwo") },
                    onNavigateToPageThree = { navController.navigate("pageThree") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("pageTwo") {
                PageTwo(
                    onNavigateToHome = { navController.navigate("home") },
                    onNavigateToPageThree = { navController.navigate("pageThree") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("pageThree") {
                PageThree(
                    navController
                )
            }
        }

    }
}

@Composable
fun Home(
    onNavigateToPageTwo: () -> Unit,
    onNavigateToPageThree: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column {
        Text("Home", fontSize = 30.sp)
        Spacer(
            modifier = Modifier.fillMaxHeight(.5f)
        )
        Row {
            Button(onClick = onNavigateToPageTwo) {Text("Page 2", fontSize = 30.sp)}
            Button(onClick = onNavigateToPageThree) {Text("Page 3", fontSize = 30.sp)}
            Button(onClick = onNavigateBack) { Text("Back", fontSize = 30.sp) }
        }
    }
}

@Composable
fun PageTwo(
    onNavigateToHome: () -> Unit,
    onNavigateToPageThree: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column {
        Text("Page 2", color = Color.Red, fontSize = 30.sp)
        Spacer(
            modifier = Modifier.fillMaxHeight(.5f)
        )
        Row {
            Button(onClick = onNavigateToHome) { Text("Home", fontSize = 30.sp) }
            Button(onClick = onNavigateToPageThree) { Text("Page 3", fontSize = 30.sp) }
            Button(onClick = onNavigateBack) { Text("Back", fontSize = 30.sp) }
        }
    }
}

@Composable
fun PageThree(navController: NavHostController) {
    Column {
        Text("Page 3", color = Color.Green, fontSize = 30.sp)
        Spacer(
            modifier = Modifier.fillMaxHeight(.5f)
        )
        Row {
            Button(onClick = {navController.navigate("home")}) { Text("Home", fontSize = 30.sp) }
            Button(onClick = {navController.navigate("PageTwo")}) { Text("Page 2", fontSize = 30.sp) }
            Button(onClick = {navController.popBackStack()}) { Text("Back", fontSize = 30.sp) }
        }
    }
}

