package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_study.databinding.ActivityMainBinding
import com.example.android_study.databinding.PokemonRowBinding
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokeAPIListAdapter
    private val pokemonList = mutableListOf<PokemonResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
//        recyclerView = findViewById(R.id.pokeRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
        binding.pokeRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokeAPIListAdapter(pokemonList)
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
}

class PokeAPIListAdapter(private val pokemonList: MutableList<PokemonResponse>): RecyclerView.Adapter<PokeAPIListAdapter.ViewHolder>() {
//    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        val pokeName: TextView = view.findViewById(R.id.textView)
//        val pokemonImage: ImageView = view.findViewById(R.id.imageView)
//    }

    class ViewHolder(val binding: PokemonRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val pokeText = LayoutInflater.from(parent.context)
//            .inflate(R.layout.pokemon_row, parent, false)
//        return ViewHolder(pokeText)
        val binding = PokemonRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
//        holder.pokeName.text = pokemon.name
        holder.binding.textView.text = pokemon.name

        Glide.with(holder.itemView.context)
            .load(pokemon.sprites.frontDefault)
            .into(holder.binding.imageView)
    }

}

data class PokemonResponse(
    val name: String,
    val sprites: Sprites
)

data class Sprites (
    @SerializedName("front_default")
    val frontDefault: String?
)

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