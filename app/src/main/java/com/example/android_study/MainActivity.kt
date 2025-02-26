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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val dataset = List(10) { "item ${ it + 1 }" }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view123)
        //LinearLayoutManager・・縦方向または横方向のレイアウトにする。デフォルトは縦方向。
        recyclerView.layoutManager = LinearLayoutManager(this)
        //画面にデータを移すためにadapterにMyAdapterをセットする必要がある。
        recyclerView.adapter = MyAdapter(dataset)
    }
}

class MyAdapter(private val dataset: List<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val view123 = view.findViewById<TextView>(R.id.textView)
    }

    //RecyclerView の各アイテム（リストの行）を作成するためのメソッド
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context) //RecyclerView の各アイテム（リストの行）を作成するためのメソッド
            .inflate(R.layout.item_row, parent, false)// XMLレイアウトをロードする
        return  ViewHolder(textView)
    }

    override fun getItemCount(): Int = dataset.size

    //ViewHolderに自動で上記で作成したViewHolderがセットされる。
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view123.text = dataset[position]
    }
}