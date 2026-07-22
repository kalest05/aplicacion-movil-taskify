package com.example.taskify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskify.data.RepositorioAlarmas
import com.example.taskify.data.RepositorioTareas
import com.example.taskify.model.Alarma
import com.example.taskify.model.DiaSemana

/**
 * ViewModel para la pantalla de alarmas. Expone la lista de alarmas
 * como estado observable y maneja las operaciones sobre ellas,
 * sincronizando con las tareas vinculadas cuando es necesario.
 */
class AlarmaViewModel : ViewModel() {

    // Acceso al repositorio de alarmas
    private val repositorio = RepositorioAlarmas()

    // Acceso al repositorio de tareas para sincronizar el estado de recordatorio
    private val repositorioTareas = RepositorioTareas()

    /**
     * Lista de alarmas observable por la UI. Solo se modifica internamente
     * con "private set" para evitar cambios accidentales desde la pantalla.
     */
    var listaAlarmas: List<Alarma> by mutableStateOf(repositorio.obtenerAlarmas().toList())
        private set

    /**
     * Sincroniza listaAlarmas con el Database actual.
     * Se llama al entrar a PantallaAlarmas para reflejar alarmas creadas
     * desde TaskViewModel.
     */
    fun refrescarAlarmas() {
        listaAlarmas = repositorio.obtenerAlarmas().toList()
    }

    /**
     * Agrega una alarma nueva con id autogenerado.
     * El id se calcula como el máximo id existente + 1 para evitar duplicados.
     */
    fun agregarAlarma(alarma: Alarma) {
        val nuevaAlarma = alarma.copy(id = (listaAlarmas.maxOfOrNull { it.id } ?: 0) + 1)
        repositorio.agregarAlarma(nuevaAlarma)
        listaAlarmas = repositorio.obtenerAlarmas().toList()
    }

    /**
     * Elimina una alarma y, si estaba vinculada a una tarea,
     * desactiva el campo recordatorio de esa tarea para mantener consistencia.
     */
    fun eliminarAlarma(alarma: Alarma) {
        repositorio.eliminarAlarma(alarma)

        // Desactivar recordatorio en la tarea vinculada si existe
        alarma.tareaId?.let { idTarea ->
            val tarea = repositorioTareas.obtenerTareas().find { it.id == idTarea }
            tarea?.let { repositorioTareas.actualizarTarea(it.copy(recordatorio = false)) }
        }

        listaAlarmas = repositorio.obtenerAlarmas().toList()
    }

    /**
     * Activa o desactiva una alarma y sincroniza el campo recordatorio
     * en la tarea vinculada para que ambas pantallas muestren el estado correcto.
     */
    fun cambiarEstadoAlarma(alarma: Alarma, activa: Boolean) {
        repositorio.actualizarAlarma(alarma.copy(activa = activa))

        // Sincronizar con la tarea vinculada
        alarma.tareaId?.let { idTarea ->
            val tarea = repositorioTareas.obtenerTareas().find { it.id == idTarea }
            tarea?.let { repositorioTareas.actualizarTarea(it.copy(recordatorio = activa)) }
        }

        listaAlarmas = repositorio.obtenerAlarmas().toList()
    }

    /**
     * Reemplaza los días activos de una alarma con una nueva lista.
     * Se llama desde TarjetaAlarma cuando el usuario toca un círculo de día
     * en alarmas manuales,las vinculadas a tareas no permiten cambiar días.
     */
    fun cambiarDiasAlarma(alarma: Alarma, nuevosDias: List<DiaSemana>) {
        repositorio.actualizarAlarma(alarma.copy(dias = nuevosDias))
        listaAlarmas = repositorio.obtenerAlarmas().toList()
    }
}