package com.example.advancedlayouts_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advancedlayouts_3.ui.theme.AdvancedLayouts_3Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvancedLayouts_3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    //tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScaffold_3()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold_3() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerBackgroundColor = colorResource(id = R.color.white)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyDrawer(
                onDrawerClose = {
                    scope.launch { drawerState.close() }
                },
                backgroundColor = drawerBackgroundColor
            )
        }
    ) {


        Scaffold(
            topBar = {
                //TopAppBar(
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.title)) },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.clickable {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    MyLayout()
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        "Copyright © 2025 Zitiso Inc.", textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { scope.launch { drawerState.open() } },
                    shape = MaterialTheme.shapes.small,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Text("?")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        )


    }
}

@Composable
fun MyDrawer(
    onDrawerClose: () -> Unit,
    backgroundColor: Color
) {
    Column(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_smiley_face_icon),
                contentDescription = "Smiley face",
                modifier = Modifier.fillMaxSize().padding(25.dp)
            )
        }
        Text(
            text = "Content for the drawer",
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onDrawerClose()
                }
        )
    }
}

@Composable
fun MyLayout() {

    var progress by remember { mutableFloatStateOf(0f) }
    var increment by remember { mutableFloatStateOf(0.1f) }

    val alphabet = arrayListOf("Alpha", "Baker", "Charlie", "Delta")

    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            for (letter in alphabet) {
                Button(
                    onClick = {
                        //updated to use increment assignment operator
                        progress += increment
                        if (progress > 1f) {
                            increment = -0.1f
                            progress = 1f
                        } else if (progress < 0f) {
                            increment = 0.1f
                            progress = 0f
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(5.dp)
                        .width(95.dp)
                ) {
                    Text(text = letter, textAlign = TextAlign.Center)
                }
            }
        }
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 40.dp,
            color = Color.Red,
            modifier = Modifier.width(200.dp)
        )
    }

}

