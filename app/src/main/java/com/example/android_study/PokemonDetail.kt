package com.example.android_study

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.android_study.databinding.ActivityPokemonDetailBinding

class PokemonDetail: AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON_NAME = "EXTRA_POKEMON_NAME"
        const val EXTRA_POKEMON_IMAGE_URL = "EXTRA_POKEMON_IMAGE_URL"
    }

    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_POKEMON_NAME) ?: "名前なし"
        val imageURL = intent.getStringExtra(EXTRA_POKEMON_IMAGE_URL)

        binding.detailPokeTextView.text = name
        Glide.with(this)
            .load(imageURL)
            .into(binding.detailPokeImageView)
    }
}