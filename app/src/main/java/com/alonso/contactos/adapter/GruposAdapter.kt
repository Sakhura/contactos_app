package com.alonso.contactos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alonso.contactos.databinding.ItemContactoGrupoBinding
import com.alonso.contactos.model.Contacto

class GruposAdapter(
    private val onContactoClick: (Contacto) -> Unit,
    private val onRemoverContacto: (Contacto) -> Unit
) : ListAdapter<Contacto, GruposAdapter.ContactoGrupoViewHolder>(ContactoDiffCallback()) {

    inner class ContactoGrupoViewHolder(private val binding: ItemContactoGrupoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contacto: Contacto) {
            binding.tvNombre.text = contacto.nombre
            binding.tvTelefono.text = contacto.telefono
            binding.tvEmail.text = contacto.email

            // Click en toda la tarjeta
            itemView.setOnClickListener {
                onContactoClick(contacto)
            }

            // Botón para remover del grupo
            binding.btnRemover.setOnClickListener {
                onRemoverContacto(contacto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoGrupoViewHolder {
        val binding = ItemContactoGrupoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactoGrupoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactoGrupoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ContactoDiffCallback : DiffUtil.ItemCallback<Contacto>() {
    override fun areItemsTheSame(oldItem: Contacto, newItem: Contacto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contacto, newItem: Contacto): Boolean {
        return oldItem == newItem
    }
}