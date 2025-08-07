package com.alonso.contactos.repository

import androidx.lifecycle.LiveData
import com.alonso.contactos.database.*
import com.alonso.contactos.model.*

class ContactosRepository(
    private val contactoDao: ContactoDao,
    private val categoriaDao: CategoriaDao,
    private val grupoDao: GrupoDao
) {

    val contactos = contactoDao.obtenerContactos()
    val categorias = categoriaDao.obtenerCategorias()
    val grupos = grupoDao.obtenerGrupos()

    companion object {
        private const val CATEGORIA_DEFECTO_ID = 1
        private const val CATEGORIA_DEFECTO_NOMBRE = "Holi"
    }

    // --- CRUD Contactos ---
    suspend fun insertarContacto(contacto: Contacto) {
        // Crear categoría por defecto si no existe
        crearCategoriaDefectoSiNoExiste()

        // Si el contacto no tiene categoría asignada, usar la por defecto
        val contactoConCategoria = if (contacto.categoriaId == 0) {
            contacto.copy(categoriaId = CATEGORIA_DEFECTO_ID)
        } else {
            // Verificar que la categoría existe
            val existe = contactoDao.existeCategoria(contacto.categoriaId)
            if (existe == 0) {
                // Si no existe, usar la categoría por defecto
                contacto.copy(categoriaId = CATEGORIA_DEFECTO_ID)
            } else {
                contacto
            }
        }

        contactoDao.insertar(contactoConCategoria)
    }

    private suspend fun crearCategoriaDefectoSiNoExiste() {
        val existe = contactoDao.existeCategoria(CATEGORIA_DEFECTO_ID)
        if (existe == 0) {
            val categoriaDefecto = Categoria(
                id = CATEGORIA_DEFECTO_ID,
                nombre = CATEGORIA_DEFECTO_NOMBRE
            )
            categoriaDao.insertar(categoriaDefecto)
        }
    }

    suspend fun actualizarContacto(contacto: Contacto) = contactoDao.actualizar(contacto)
    suspend fun eliminarContacto(contacto: Contacto) = contactoDao.eliminar(contacto)

    // --- CRUD Categorías ---
    suspend fun insertarCategoria(categoria: Categoria) = categoriaDao.insertar(categoria)

    // --- CRUD Grupos ---
    suspend fun insertarGrupo(grupo: Grupo) = grupoDao.insertarGrupo(grupo)
    suspend fun actualizarGrupo(grupo: Grupo) = grupoDao.actualizarGrupo(grupo)
    suspend fun eliminarGrupo(grupo: Grupo) = grupoDao.eliminarGrupo(grupo)

    // --- Buscar contactos ---
    fun buscarContactos(query: String) = contactoDao.buscarContactos("%$query%")

    // --- Relaciones Contacto-Grupo ---
    suspend fun asociarContactoAGrupo(crossRef: ContactoGrupoCrossRef) =
        grupoDao.insertarContactoGrupoCrossRef(crossRef)

    suspend fun removerContactoDeGrupo(crossRef: ContactoGrupoCrossRef) =
        grupoDao.eliminarContactoGrupoCrossRef(crossRef)

    // --- Consultas de relaciones ---
    fun obtenerGrupoConContactos(grupoId: Int): LiveData<GrupoConContactos> =
        grupoDao.obtenerGrupoConContactos(grupoId)

    fun obtenerContactoConGrupos(contactoId: Int): LiveData<ContactoConGrupos> =
        grupoDao.obtenerContactoConGrupos(contactoId)

    fun obtenerTodosLosGruposConContactos(): LiveData<List<GrupoConContactos>> =
        grupoDao.obtenerTodosLosGruposConContactos()

    // --- Consultas adicionales ---
    suspend fun contarContactosEnGrupo(grupoId: Int): Int =
        grupoDao.contarContactosEnGrupo(grupoId)

    fun obtenerContactosDeGrupo(grupoId: Int): LiveData<List<Contacto>> =
        grupoDao.obtenerContactosDeGrupo(grupoId)

    fun obtenerGruposDeContacto(contactoId: Int): LiveData<List<Grupo>> =
        grupoDao.obtenerGruposDeContacto(contactoId)
}