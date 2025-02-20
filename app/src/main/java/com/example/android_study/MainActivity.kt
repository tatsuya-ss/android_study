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

        val dataset = arrayOf("January", "February", "March")
        val customAdapter = CustomAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview1)//recyclerviewという名前をつけてエラーが起こる。
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
}

// 今回の場合であれば１種類のViewHolderなので、
class CustomAdapter(private val dataSet: Array<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         * こいつがセル
         * どこに何を表示させるかを決定させる
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView

            init {
                // Define click listener for the ViewHolder's View
                textView = view.findViewById(R.id.textView)
            }
        }

        // Create new views (invoked by the layout manager)
    //どの種類のレイアウトを使用するかを指定する
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_1, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
    // 使うViewHolder
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.textView.text = dataSet[position]
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size
}

class SomeViewAdapter() : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    //🟦 この条件３つを使うとRecyclerViewを動かすことができる。
    //🟥ここが何をしているか
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    // 🟥ここが何をしているか
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}