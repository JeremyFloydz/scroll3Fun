package com.example.scroll

import android.os.Bundle
import android.widget.Button
import android.media.MediaPlayer
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Tarea>
    private lateinit var taskAdapter: TareaController
    private lateinit var inputTask: EditText
    private lateinit var addButton: Button
    private lateinit var tasksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar la lista de tareas
        taskList = mutableListOf()

        // Enlazar los elementos de la interfaz
        inputTask = findViewById(R.id.tareaInput)
        addButton = findViewById(R.id.btnAnadirTarea)
        tasksRecyclerView = findViewById(R.id.recyclerViewTareas)

        // Configurar el adaptador
        taskAdapter = TareaController(
            taskList,
            { tarea -> removeTask(tarea) } // Cambiado para recibir Tarea
        )

        // Configurar el RecyclerView
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = taskAdapter

        // Configurar el botón para añadir tareas
        addButton.setOnClickListener {
            createTask()
        }
    }

    private fun createTask() {
        val newTaskText = inputTask.text.toString().trim() // Eliminar espacios en blanco
        if (newTaskText.isNotEmpty()) {
            // Crear y añadir la nueva tarea a la lista
            val newTask = Tarea(newTaskText) // Al añadir, no es necesario la posición
            taskList.add(newTask)
            taskAdapter.notifyItemInserted(taskList.size - 1)
            inputTask.setText("") // Limpiar el campo de entrada
        } else {
            // Mostrar un mensaje si el campo está vacío
            Toast.makeText(this, "No se puede añadir una tarea vacía", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeTask(tarea: Tarea) {
        val position = taskList.indexOf(tarea) // Obtener la posición de la tarea
        if (position != -1) {
            playSound() // Reproducir el sonido antes de borrar
            taskList.removeAt(position)
            taskAdapter.notifyItemRemoved(position)
            taskAdapter.notifyItemRangeChanged(position, taskList.size)
        }
    }

    private fun playSound() {
        // Crea una instancia de MediaPlayer
        val mediaPlayer = MediaPlayer.create(this, R.raw.sonido) // Nombre del archivo sin la extensión
        mediaPlayer.start() // Inicia la reproducción

        // Configura un listener para liberar recursos una vez que termine de sonar
        mediaPlayer.setOnCompletionListener {
            it.release() // Libera los recursos del MediaPlayer
        }
    }
}
