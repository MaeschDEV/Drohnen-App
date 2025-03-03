package com.maeschdev.drohnenapp

import android.app.Application
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.maeschdev.drohnenapp.ui.theme.DrohnenAppTheme
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrohnenAppTheme {
                val viewModel: MainViewModel = viewModel()
                AppNavigator(viewModel)
            }
        }
        startSendingCommands()
    }
}

@Composable
fun AppNavigator(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navGraph = remember(navController){
        navController.createGraph(startDestination = "controlPage"){
            composable("controlPage") { ControlPage(navController) }
            composable("settingsPage") { SettingsPage(navController, viewModel) }
        }
    }

    NavHost(navController = navController, graph = navGraph)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlPage(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Drohnen-App")
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("settingsPage") }
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            0,
                            1,
                            painterResource(R.drawable.ic_arrow_upward),
                            "Up"
                        )
                        Spacer(modifier = Modifier.weight(0.25f))
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            2,
                            1,
                            painterResource(R.drawable.ic_keyboard_arrow_up),
                            "Forward"
                        )
                    }
                    Row(modifier = Modifier.weight(1f)) {
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),
                            1,
                            1,
                            painterResource(R.drawable.ic_rotate_left),
                            "Rotate Left"
                        )
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),
                            1,
                            -1,
                            painterResource(R.drawable.ic_rotate_right),
                            "Rotate Right"
                        )
                        Spacer(modifier = Modifier.weight(0.25f))
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),
                            3,
                            1,
                            painterResource(R.drawable.ic_keyboard_arrow_left),
                            "Left"
                        )
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),
                            3,
                            -1,
                            painterResource(R.drawable.ic_keyboard_arrow_right),
                            "Right"
                        )
                    }
                    Row(modifier = Modifier.weight(1f)) {
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            0,
                            -1,
                            painterResource(R.drawable.ic_arrow_downward),
                            "Down"
                        )
                        Spacer(modifier = Modifier.weight(0.25f))
                        SendDataButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            2,
                            -1,
                            painterResource(R.drawable.ic_keyboard_arrow_down),
                            "Backward"
                        )
                    }
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

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    var ipAddress by mutableStateOf("")
        private set

    var port by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferences.savedText1.collect { loadedText ->
                ipAddress = loadedText
                IP_ADDRESS = loadedText
            }
        }

        viewModelScope.launch {
            userPreferences.savedText2.collect { loadedText ->
                port = loadedText
                PORT = loadedText
            }
        }
    }

    fun saveText1(newText: String){
        ipAddress = newText
        IP_ADDRESS = newText
        viewModelScope.launch {
            userPreferences.saveText1(newText)
        }
    }

    fun saveText2(newText: String){
        port = newText
        PORT = newText
        viewModelScope.launch {
            userPreferences.saveText2(newText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavController, viewModel: MainViewModel) {
    var ipAddress by remember { mutableStateOf(viewModel.ipAddress) }
    var port by remember { mutableStateOf(viewModel.port) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Einstellungen")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("controlPage") }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                        viewModel.saveText1(it)
                        IP_ADDRESS = it
                    },
                    label = { Text("IP Adresse") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(25.dp))
                TextField(
                    value = port,
                    onValueChange = {
                        port = it
                        viewModel.saveText2(it)
                        PORT = it
                    },
                    label = { Text("Port") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
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
        SettingsPage(rememberNavController(), viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    DrohnenAppTheme {
        SettingsPage(rememberNavController(), viewModel())
    }
}