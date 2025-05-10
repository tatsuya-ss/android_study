package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FruitsFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : FruitAdapter

    private val fruits = arrayOf("apple","banana", "orange")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fruit, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.FruitRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FruitAdapter(fruits)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}