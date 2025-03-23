package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_study.databinding.FragmentPokeListBinding
import kotlinx.coroutines.launch

class PokemonListFragment: Fragment() {
    private var _binding: FragmentPokeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PokemonListAdapter
    private val pokemonList = mutableListOf<PokemonResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokeListBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pokemonListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PokemonListAdapter(pokemonList) { pokemon ->
            //requireContext。Fragment にアタッチされているときに、常に有効なコンテキストを返します。
            Toast.makeText(requireContext(), " ${pokemon.name}がタップされました。", Toast.LENGTH_SHORT)
                .show()
        }

        binding.pokemonListRecyclerView.adapter = adapter

        lifecycleScope.launch {
            for (id in 1..10) {
                try {
                    val pokemon = RetrofitInstance.api.getPokemon(id)
                    pokemonList.add(pokemon)
                    adapter.notifyItemInserted(pokemonList.size - 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}