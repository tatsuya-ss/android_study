package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_study.databinding.FragmentPokeListBinding
import kotlinx.coroutines.launch

class PokeListFragment: Fragment() {

    // _binding は nullable で、onDestroyView でクリアするための変数
    private var _binding: FragmentPokeListBinding? = null
    // 非nullな binding は get() でアクセス
    private val binding get() = _binding!!

    private lateinit var adapter: PokeAdapter
    private val pokemonList = mutableListOf<PokemonResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Fragment 用のレイアウトをインフレートしてバインディングを初期化 */
        _binding = FragmentPokeListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pokeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PokeAdapter(pokemonList)
        binding.pokeRecyclerView.adapter = adapter

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