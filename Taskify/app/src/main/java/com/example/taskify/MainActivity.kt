package com.example.taskify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskify.navigation.AppNavigation
import com.example.taskify.ui.theme.TaskifyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskifyTheme {
                AppNavigation() // Lanza el sistema de rutas de la app
            }
        }
    }
}

// Preview para ver la app completa desde Android Studio
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewApp() {
    TaskifyTheme {
        AppNavigation()
    }
}
