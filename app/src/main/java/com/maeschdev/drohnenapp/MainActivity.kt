package com.maeschdev.drohnenapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maeschdev.drohnenapp.ui.theme.DrohnenAppTheme
import java.util.concurrent.CancellationException

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrohnenAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Drohnen-App")
                            },
                            actions = {
                                IconButton(
                                    onClick = { println("Pressed action button") }
                                ) {
                                    Icon(
                                        Icons.Rounded.Settings,
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    ControlPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        startSendingCommands()
    }
}

@Composable
fun ControlPage(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(1f), 0, 1, painterResource(R.drawable.ic_arrow_upward), "Up")
                    Spacer(modifier = Modifier.weight(0.25f))
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(1f), 2, 1, painterResource(R.drawable.ic_keyboard_arrow_up), "Forward")
                }
                Row(modifier = Modifier.weight(1f)) {
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(0.5f), 1, 1, painterResource(R.drawable.ic_rotate_left), "Rotate Left")
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(0.5f), 1, -1, painterResource(R.drawable.ic_rotate_right), "Rotate Right")
                    Spacer(modifier = Modifier.weight(0.25f))
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(0.5f), 3, 1, painterResource(R.drawable.ic_keyboard_arrow_left), "Left")
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(0.5f), 3, -1, painterResource(R.drawable.ic_keyboard_arrow_right), "Right")
                }
                Row(modifier = Modifier.weight(1f)) {
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(1f), 0, -1, painterResource(R.drawable.ic_arrow_downward), "Down")
                    Spacer(modifier = Modifier.weight(0.25f))
                    SendDataButton(modifier = Modifier.fillMaxHeight().weight(1f), 2, -1, painterResource(R.drawable.ic_keyboard_arrow_down), "Backward")
                }
            }
        }
    }
}

@Composable
fun SendDataButton(modifier: Modifier = Modifier, dataType: Int, value: Int, painter: Painter, contentDescription: String){
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onButtonPressed(dataType, value)
                        val released = try {
                            tryAwaitRelease()
                        } catch (c: CancellationException){
                            false
                        }
                        if (released){
                            onButtonReleased(dataType, value)
                        }
                        else{
                            onButtonReleased(dataType, value)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(75.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"
)
@Composable
fun LandscapePreview() {
    DrohnenAppTheme {
        ControlPage()
    }
}

@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    DrohnenAppTheme {
        ControlPage()
    }
}