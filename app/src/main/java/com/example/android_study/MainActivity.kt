package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_study.databinding.FruitRowBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fruits = arrayOf("apple","banana","orange")
        val adapter = FruitAdapter(fruits)

        val recyclerView: RecyclerView = findViewById(R.id.FruitsRecyclerVew)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}

class FruitAdapter(private val fruits: Array<String>): RecyclerView.Adapter<FruitAdapter.ViewHolder>() {
    class ViewHolder(val binding: FruitRowBinding): RecyclerView.ViewHolder(binding.root)

    // ViewHolderにレイアウトと結びつける
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val fruitBinding = FruitRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(fruitBinding)
    }

    override fun getItemCount(): Int = fruits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruits[position]
        holder.binding.fruitTextView.text = fruit
    }

}