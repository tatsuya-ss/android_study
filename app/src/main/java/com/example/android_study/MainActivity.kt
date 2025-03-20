package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_study.databinding.PokeRowBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,true)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokeListFragment())
                .commit()
        }
    }
}

class PokeAdapter(private val pokemonList: MutableList<PokemonResponse>): RecyclerView.Adapter<PokeAdapter.ViewHolder>(){
    class ViewHolder(val binding: PokeRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokeRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.binding.pokeTextView.text = pokemon.name
        Glide.with(holder.itemView.context)
            .load(pokemon.sprites.frontDefault)
            .into(holder.binding.pokeImageView)
    }
}

data class PokemonResponse(
    val name: String,
    val sprites: Sprites
)

data class Sprites(
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