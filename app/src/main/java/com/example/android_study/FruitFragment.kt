package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FruitFragment: Fragment() {

    private lateinit var fruitRecyclerView: RecyclerView
    private lateinit var adapter: FruitAdapter

    private val fruitData = arrayOf("apple","banana","orange")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recyclerview,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fruitRecyclerView = view.findViewById(R.id.fruitRecyclerView)
        fruitRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FruitAdapter(fruitData)
        fruitRecyclerView.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}