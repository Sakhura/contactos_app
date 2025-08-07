package com.alonso.contactos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alonso.contactos.model.Categoria
import com.alonso.contactos.model.Contacto

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias")
    fun obtenerCategorias(): LiveData<List<Categoria>>

    @Insert
    suspend fun insertar(categoria: Categoria)

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Categoria?
}
