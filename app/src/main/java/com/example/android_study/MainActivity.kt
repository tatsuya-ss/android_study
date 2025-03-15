package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonListAdapter
    private val pokemonList = mutableListOf<PokemonResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.pokemonRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonListAdapter(pokemonList)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            for (id in 1..10) {
                try {
                    val pokemon = RetrofitInstance.api.getPokemon(id)
                    pokemonList.add(pokemon)
                    adapter.notifyItemInserted(pokemonList.size -1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

class PokemonListAdapter(private val pokemonList: MutableList<PokemonResponse>): RecyclerView.Adapter<PokemonListAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pokeName: TextView = view.findViewById(R.id.pokeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val pokeText = LayoutInflater.from(parent.context)
            .inflate(R.layout.poke_name_row, parent, false)
        return ViewHolder(pokeText)
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokeName.text = pokemon.name
    }

}

data class PokemonResponse(val name: String)

interface PokeAPIService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonResponse
}

object RetrofitInstance {
    val api: PokeAPIService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeAPIService::class.java)

    }
}

