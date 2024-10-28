package com.example.scroll

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(
    private val tareas: MutableList<Tarea>,
    private val onTickClick: (Int) -> Unit // Manejamos el clic en el icono de "tick"
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea_layout, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tareaActual = tareas[position]
        holder.tareaTextView.text = tareaActual.texto

        holder.tickImageView.setOnClickListener {
            onTickClick(position) // Manejar el clic en el icono de "tick"
        }

        // Funcionalidad para editar la tarea
        holder.tareaTextView.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Modificar Tarea")

            // Inflar el diseño del diálogo
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_edit_tarea, null)
            builder.setView(dialogView)

            // Enlazar el EditText
            val editTextTarea = dialogView.findViewById<EditText>(R.id.editTextTarea)
            editTextTarea.setText(tareaActual.texto) // Prellenar con el texto actual

            // Configurar los botones del diálogo
            builder.setPositiveButton("Actualizar") { dialog, which ->
                tareaActual.texto = editTextTarea.text.toString() // Actualizar el texto de la tarea
                notifyItemChanged(position) // Notificar que el item ha cambiado
            }
            builder.setNegativeButton("Cancelar", null)

            builder.show() // Mostrar el diálogo
        }
    }

    override fun getItemCount(): Int = tareas.size

    inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tareaTextView: TextView = itemView.findViewById(R.id.tareaTexto)
        val tickImageView: ImageView = itemView.findViewById(R.id.tick)
    }
}
