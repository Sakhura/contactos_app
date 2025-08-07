package com.alonso.contactos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alonso.contactos.model.Contacto

@Dao
interface ContactoDao {
    @Query("SELECT * FROM contactos ORDER BY nombre ASC")
    fun obtenerContactos(): LiveData<List<Contacto>>

    @Query("SELECT * FROM contactos WHERE nombre LIKE :query")
    fun buscarContactos(query: String): LiveData<List<Contacto>>

    @Insert
    suspend fun insertar(contacto: Contacto)

    @Query("SELECT COUNT(*) FROM categorias WHERE id = :categoriaId")
    suspend fun existeCategoria(categoriaId: Int): Int

    @Update
    suspend fun actualizar(contacto: Contacto)

    @Delete
    suspend fun eliminar(contacto: Contacto)
}