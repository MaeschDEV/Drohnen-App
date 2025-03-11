package com.maeschdev.drohnenapp

import android.app.Application
import android.content.res.Configuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    var stopped by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
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
                Row(modifier = Modifier.weight(1f)) {
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        0,
                        1,
                        painterResource(R.drawable.ic_arrow_upward),
                        "Up",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.25f)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        onToggleStopButtonPressed(stopped)
                                        stopped = !stopped
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(if (!stopped) R.drawable.ic_stop_circle else R.drawable.ic_play_circle),
                            contentDescription = "Start / Stop",
                            modifier = Modifier
                                .size(75.dp)
                                .background(MaterialTheme.colorScheme.errorContainer),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        2,
                        1,
                        painterResource(R.drawable.ic_keyboard_arrow_up),
                        "Forward",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
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
                        "Rotate Left",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        1,
                        2,
                        painterResource(R.drawable.ic_rotate_right),
                        "Rotate Right",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.weight(0.25f))
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        3,
                        1,
                        painterResource(R.drawable.ic_keyboard_arrow_left),
                        "Left",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f),
                        3,
                        2,
                        painterResource(R.drawable.ic_keyboard_arrow_right),
                        "Right",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Row(modifier = Modifier.weight(1f)) {
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        0,
                        2,
                        painterResource(R.drawable.ic_arrow_downward),
                        "Down",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.weight(0.25f))
                    SendDataButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        2,
                        2,
                        painterResource(R.drawable.ic_keyboard_arrow_down),
                        "Backward",
                        if (!stopped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun SendDataButton(modifier: Modifier = Modifier, dataType: Int, value: Int, painter: Painter, contentDescription: String, background: Color, foreground: Color){
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
                .background(background),
            tint = foreground
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var ipAddress by remember { mutableStateOf(viewModel.ipAddress) }
    var port by remember { mutableStateOf(viewModel.port) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
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
                    .verticalScroll(rememberScrollState())
                    .then(
                        if (isLandscape) Modifier.padding(64.dp, 16.dp, 16.dp, 16.dp)
                        else Modifier.padding(16.dp)
                    )
            ) {
                TextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                        viewModel.saveText1(it)
                        IP_ADDRESS = it
                    },
                    label = { Text(stringResource(R.string.ip_address)) },
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
                    label = { Text(stringResource( R.string.port)) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    }
}

@Preview(
    name = "Portrait"
)
@Composable
fun AppNavigatorPreview() {
    val dummyNavController = rememberNavController()

    DrohnenAppTheme {
        ControlPage(dummyNavController)
    }
}

@Preview(
    name = "Landscape",
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"
)
@Composable
fun AppNavigatorPreviewLS() {
    val dummyNavController = rememberNavController()

    DrohnenAppTheme {
        ControlPage(dummyNavController)
    }
}