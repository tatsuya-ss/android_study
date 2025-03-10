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
import com.google.gson.annotations.SerializedName
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
                    adapter.notifyItemInserted(pokemonList.size - 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

class PokemonListAdapter(private val pokeList: MutableList<PokemonResponse>): RecyclerView.Adapter<PokemonListAdapter.ViewHolder>(){

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.pokeImageView)
        val textView: TextView = view.findViewById(R.id.pokeTextView)
    }

    //🟥エラー起きるかも
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_row, parent, false)
        return  ViewHolder(textView)
    }

    override fun getItemCount(): Int = pokeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokeList[position]
        holder.textView.text = pokemon.name.replaceFirstChar { it.uppercase() }
        Glide.with(holder.itemView.context)
            .load(pokemon.sprites.frontDefault)
            .into(holder.imageView)

    }
}

interface PokeAPIService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonResponse
}

data class PokemonResponse(
    val name: String,
    val sprites: Sprites
)

data class Sprites(
    @SerializedName("front_defaults")
    val frontDefault: String?
)

object RetrofitInstance {
    val api: PokeAPIService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeAPIService::class.java)
    }
}