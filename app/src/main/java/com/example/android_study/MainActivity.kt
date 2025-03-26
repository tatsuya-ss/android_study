package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frutFramelayout, FruitFragment())
                .commit()
        }
    }
}

class FruitAdapter(private val fruits: Array<String>): RecyclerView.Adapter<FruitAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val frutiText: TextView = view.findViewById(R.id.fruitTextView)
    }

    //ViewHolderとレイアウトを結びつける。inflateの引数はR.layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = fruits.size

    // ViewHolderに値をBindingする
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruits[position]
        holder.frutiText.text = fruit
    }


}
