package com.example.android_study

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter: TodoAdapter
    private var todoIdCounter = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewTodos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(mutableListOf())
        recyclerView.adapter = todoAdapter

        val editTextTask: EditText = findViewById(R.id.editTextTask)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)

        buttonAdd.setOnClickListener {
            val taskText = editTextTask.text.toString().trim()
            if (!TextUtils.isEmpty(taskText)) {
                val newTodo = Todo (
                    id = todoIdCounter++,
                    task = taskText
                )
                todoAdapter.addTodo(newTodo)
                editTextTask.text.clear()
            } else {
                editTextTask.error = "タスクを入力してください"
            }

        }
    }
}

class TodoAdapter(private val todoList: MutableList<Todo>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textViewTask)
        val checkBoxCompleted: CheckBox = view.findViewById(R.id.checkBoxCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return  ViewHolder(textView)
    }

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.textView.text = todo.task
        holder.checkBoxCompleted.isChecked = todo.isCompleted

        holder.checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
            todo.isCompleted = isChecked
        }
    }

    fun addTodo(todo: Todo) {
        todoList.add(todo)
        notifyItemInserted(todoList.size - 1)
    }
}

data class Todo(
    val id: Int,
    var task: String,
    var isCompleted: Boolean = false
)