package com.maeschdev.drohnenapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maeschdev.drohnenapp.ui.theme.DrohnenAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrohnenAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ControlPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
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
                    .fillMaxHeight(0.75f)
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                sendData("Up", "192.168.178.145", 5000)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_upward),
                            contentDescription = "Up",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.25f))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard_arrow_up),
                            contentDescription = "Forward",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_rotate_left),
                            contentDescription = "Rotate Left",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_rotate_right),
                            contentDescription = "Rotate Right",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.25f))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard_arrow_left),
                            contentDescription = "Left",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard_arrow_right),
                            contentDescription = "Right",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_downward),
                            contentDescription = "Down",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.25f))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard_arrow_down),
                            contentDescription = "Backward",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
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