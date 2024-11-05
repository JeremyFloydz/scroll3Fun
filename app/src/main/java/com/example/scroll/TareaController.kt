package com.example.scroll

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareaController(
    private val tareas: MutableList<Tarea>,
    private val onTickClick: (Tarea) -> Unit // Cambiamos a recibir el objeto Tarea
) : RecyclerView.Adapter<TareaController.TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea_layout, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tareaActual = tareas[position]
        holder.tareaTextView.text = tareaActual.texto

        // Cambiar la imagen de la estrella según el estado de favorita
        holder.starImageView.setImageResource(
            if (tareaActual.esFavorita) R.drawable.estrella_solid_24
            else R.drawable.estrellavacia_24
        )

        // Manejar el clic en la imagen de la estrella
        holder.starImageView.setOnClickListener {
            // Cambiar el estado de favorita
            tareaActual.esFavorita = !tareaActual.esFavorita

            // Si se marca como favorita, mover al principio
            if (tareaActual.esFavorita) {
                // Mover la tarea al principio de la lista
                tareas.removeAt(position) // Eliminar de la posición actual
                tareas.add(0, tareaActual) // Añadir al principio
                notifyItemMoved(position, 0) // Notificar el cambio de posición
                notifyItemChanged(1) // Notificar que el segundo elemento ha cambiado (si existe)
            } else {
                notifyItemChanged(position) // Solo actualizar la imagen si no es favorita
            }

            // Actualizar la imagen de la estrella
            holder.starImageView.setImageResource(
                if (tareaActual.esFavorita) R.drawable.estrella_solid_24
                else R.drawable.estrellavacia_24
            )
        }

        // Manejar el clic en la imagen de "tick" para eliminar la tarea
        holder.tickImageView.setOnClickListener {
            // Pasar el objeto Tarea en lugar de la posición
            onTickClick(tareaActual)
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
        val starImageView: ImageView = itemView.findViewById(R.id.star) // Enlazamos el ImageView de la estrella
    }
}
