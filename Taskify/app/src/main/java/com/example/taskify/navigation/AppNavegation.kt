package com.example.taskify.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskify.screens.PantallaAlarmas
import com.example.taskify.screens.PantallaCalendario
import com.example.taskify.screens.PantallaTareas

/**
 * Define el sistema de navegación de la app usando Navigation Compose.
 * Crea el NavController y mapea cada ruta string ("tareas", "alarmas", "calendario")
 * a su pantalla correspondiente. Las animaciones están desactivadas.
 */
@Composable
fun AppNavigation() {

    // Controlador de navegación que recuerda el estado del back stack
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "tareas",       // Pantalla inicial al abrir la app
        enterTransition = { EnterTransition.None },   // Sin animación de entrada
        exitTransition = { ExitTransition.None },     // Sin animación de salida
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        // Ruta "tareas" → muestra la lista de tareas pendientes
        composable("tareas") {
            PantallaTareas(navController = navController)
        }

        // Ruta "alarmas" → muestra la lista de alarmas configuradas
        composable("alarmas") {
            PantallaAlarmas(navController = navController)
        }

        // Ruta "calendario" → muestra la vista de calendario con tareas del día
        composable("calendario") {
            PantallaCalendario(navController = navController)
        }

    }

}