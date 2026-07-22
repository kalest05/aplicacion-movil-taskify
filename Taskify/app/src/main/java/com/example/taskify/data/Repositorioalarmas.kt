package com.example.taskify.data

import com.example.taskify.model.Alarma

/**
 * Capa intermedia entre Database y AlarmaViewModel.
 * Maneja todas las operaciones CRUD sobre la lista de alarmas.
 * Sigue el mismo patrón que RepositorioTareas para mantener consistencia.
 */
class RepositorioAlarmas {

    /**
     * Devuelve la lista completa de alarmas desde Database.
     * Retorna la referencia mutable directa, reflejando cambios en tiempo real.
     */
    fun obtenerAlarmas(): MutableList<Alarma> {
        return Database.alarmas
    }

    /**
     * Agrega una alarma nueva a la lista de Database.
     * alarma es el objeto Alarma ya construido con su id asignado.
     */
    fun agregarAlarma(alarma: Alarma) {
        Database.alarmas.add(alarma)
    }

    /**
     * Elimina una alarma de la lista comparando por referencia de objeto.
     * alarma es la alarma exacta que se desea eliminar.
     */
    fun eliminarAlarma(alarma: Alarma) {
        Database.alarmas.remove(alarma)
    }

    /**
     * Busca una alarma por su id y la reemplaza con la versión actualizada.
     * Usado para activar/desactivar o cambiar días sin eliminar y recrear.
     * alarmaActualizada es la alarma con el mismo id pero campos modificados.
     */
    fun actualizarAlarma(alarmaActualizada: Alarma) {
        val index = Database.alarmas.indexOfFirst { it.id == alarmaActualizada.id }
        if (index != -1) {
            Database.alarmas[index] = alarmaActualizada
        }
    }
}