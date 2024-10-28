package com.example.scroll

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Tarea>
    private lateinit var taskAdapter: TareaAdapter
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
        taskAdapter = TareaAdapter(
            taskList,
            { position -> // Manejar el clic en el icono de "tick"
                removeTask(position)
            }
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
        val newTaskText = inputTask.text.toString()
        if (!TextUtils.isEmpty(newTaskText)) {
            // Crear y añadir la nueva tarea a la lista
            val newTask = Tarea(newTaskText, taskList.size) // Al añadir, asignamos la posición inicial
            taskList.add(newTask)
            taskAdapter.notifyItemInserted(taskList.size - 1)
            inputTask.setText("") // Limpiar el campo de entrada
        }
    }

    private fun removeTask(position: Int) {
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        taskAdapter.notifyItemRangeChanged(position, taskList.size)
    }
}
