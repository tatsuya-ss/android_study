package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fruits = arrayOf("apple","banana","orange")
        val adapter = FruitAdapter(fruits)
        val recyclerView: RecyclerView = findViewById(R.id.fruitsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}

class FruitAdapter(private val fruitsData: Array<String>): RecyclerView.Adapter<FruitAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_row,parent,false)
        return ViewHolder(textLayout)
    }

    override fun getItemCount(): Int = fruitsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitsData[position]
        holder.textView.text = fruit
    }

}