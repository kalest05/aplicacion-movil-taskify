package com.example.taskify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskify.data.RepositorioAlarmas
import com.example.taskify.data.RepositorioTareas
import com.example.taskify.model.Alarma
import com.example.taskify.model.DiaSemana
import com.example.taskify.model.Tareas
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * ViewModel para la pantalla de tareas. Actúa como intermediario entre
 * la UI y los repositorios: expone la lista de tareas como estado observable
 * y contiene toda la lógica de negocio (agregar, eliminar, recordatorios).
 */
class TaskViewModel : ViewModel() {

    // Acceso al repositorio de tareas
    private val repositorio = RepositorioTareas()

    // Acceso al repositorio de alarmas (para crear/eliminar alarmas vinculadas)
    private val repositorioAlarmas = RepositorioAlarmas()

    /**
     * Lista de tareas observable por la UI. Cuando cambia, Compose re-dibuja
     * automáticamente los componentes que la usan. Solo se puede modificar
     * internamente gracias al "private set".
     */
    var listaTareas: List<Tareas> by mutableStateOf(repositorio.obtenerTareas().toList())
        private set

    /**
     * Sincroniza listaTareas con el estado actual del Database.
     * Se llama al entrar a la pantalla con LaunchedEffect para reflejar
     * cambios que pudo hacer otra pantalla (como AlarmaViewModel).
     */
    fun refrescarTareas() {
        listaTareas = repositorio.obtenerTareas().toList()
    }

    /**
     * Agrega una nueva tarea con un id único autogenerado.
     * Si la tarea tiene recordatorio activo, también crea una alarma vinculada.
     * Al final actualiza listaTareas para que la UI refleje el cambio.
     */
    fun agregarTarea(tarea: Tareas) {
        val todasLasTareas = repositorio.obtenerTareas()
        val nuevoId = (todasLasTareas.maxOfOrNull { it.id } ?: 0) + 1 // Id = máximo actual + 1
        val nuevaTarea = tarea.copy(id = nuevoId)

        repositorio.agregarTarea(nuevaTarea)

        if (nuevaTarea.recordatorio) {
            crearAlarmaParaTarea(nuevaTarea)
        }

        listaTareas = repositorio.obtenerTareas().toList()
    }

    /**
     * Elimina una tarea y también su alarma vinculada si existe.
     * Busca la alarma por tareaId para mantener los datos consistentes.
     */
    fun eliminarTarea(tarea: Tareas) {
        repositorio.eliminarTarea(tarea)

        // Eliminar alarma vinculada si existe
        val alarmaAsociada = repositorioAlarmas.obtenerAlarmas().find { it.tareaId == tarea.id }
        if (alarmaAsociada != null) {
            repositorioAlarmas.eliminarAlarma(alarmaAsociada)
        }

        listaTareas = repositorio.obtenerTareas().toList()
    }

    /**
     * Cambia el estado de completada de una tarea sin guardarla en el repositorio.
     * Solo actualiza la copia local en listaTareas.
     */
    fun cambiarEstadoTarea(tarea: Tareas, estado: Boolean) {
        val index = listaTareas.indexOf(tarea)
        if (index != -1) {
            val nuevaLista = listaTareas.toMutableList()
            nuevaLista[index] = tarea.copy(completada = estado)
            listaTareas = nuevaLista
        }
    }

    /**
     * Activa o desactiva el recordatorio de una tarea y sincroniza la alarma vinculada.
     * Si se activa y no existe alarma, la crea. Si se desactiva, la pone inactiva.
     * Guarda el cambio en el repositorio para que persista durante la sesión.
     */
    fun cambiarRecordatorioTarea(tarea: Tareas, estado: Boolean) {
        val tareaActualizada = tarea.copy(recordatorio = estado)
        repositorio.actualizarTarea(tareaActualizada)

        val alarmaAsociada = repositorioAlarmas.obtenerAlarmas().find { it.tareaId == tarea.id }

        if (estado) {
            if (alarmaAsociada == null) {
                crearAlarmaParaTarea(tareaActualizada) // Crear alarma nueva
            } else {
                repositorioAlarmas.actualizarAlarma(alarmaAsociada.copy(activa = true))
            }
        } else {
            alarmaAsociada?.let {
                repositorioAlarmas.actualizarAlarma(it.copy(activa = false))
            }
        }

        listaTareas = repositorio.obtenerTareas().toList()
    }

    /**
     * Crea una alarma automáticamente a partir de los datos de una tarea.
     * Calcula el día de la semana desde la fecha de la tarea y lo asigna
     * como el único día activo. Vincula la alarma con la tarea mediante tareaId.
     */
    private fun crearAlarmaParaTarea(tarea: Tareas) {
        val todasLasAlarmas = repositorioAlarmas.obtenerAlarmas()
        val nuevoIdAlarma = (todasLasAlarmas.maxOfOrNull { it.id } ?: 0) + 1
        val diaSemana = obtenerDiaSemanaDeFecha(tarea.fecha)

        val nuevaAlarma = Alarma(
            id = nuevoIdAlarma,
            hora = tarea.hora,
            descripcion = "Recordatorio: ${tarea.titulo}",
            dias = if (diaSemana != null) listOf(diaSemana) else DiaSemana.entries,
            activa = true,
            tareaId = tarea.id // Vincula la alarma con la tarea
        )
        repositorioAlarmas.agregarAlarma(nuevaAlarma)
    }

    /**
     * Convierte una fecha en string "dd/MM/yyyy" al DiaSemana correspondiente.
     * Usa Calendar para extraer el día de la semana del objeto Date.
     * Retorna null si la fecha tiene formato inválido o no se puede analizar.
     */
    private fun obtenerDiaSemanaDeFecha(fecha: String): DiaSemana? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse(fecha)
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                when (cal.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> DiaSemana.LUNES
                    Calendar.TUESDAY -> DiaSemana.MARTES
                    Calendar.WEDNESDAY -> DiaSemana.MIERCOLES
                    Calendar.THURSDAY -> DiaSemana.JUEVES
                    Calendar.FRIDAY -> DiaSemana.VIERNES
                    Calendar.SATURDAY -> DiaSemana.SABADO
                    Calendar.SUNDAY -> DiaSemana.DOMINGO
                    else -> null
                }
            } else null
        } catch (e: Exception) {
            null // Si falla el análisis, retorna null sin bloquearse
        }
    }
}