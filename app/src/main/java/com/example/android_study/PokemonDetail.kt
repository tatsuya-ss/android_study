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
        const val EXTRA_POKEMON_WEIGHT = "EXTRA_POKEMON_WEIGHT"
        const val EXTRA_POKEMON_HEIGHT = "EXTRA_POKEMON_HEIGHT"
        const val EXTRA_POKEMON_TYPES = "EXTRA_POKEMON_TYPES"  // カンマ区切り文字列などで渡す
    }

    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_POKEMON_NAME) ?: "名前なし"
        val imageURL = intent.getStringExtra(EXTRA_POKEMON_IMAGE_URL)
        val weight = intent.getIntExtra(EXTRA_POKEMON_WEIGHT, 0)
        val height = intent.getIntExtra(EXTRA_POKEMON_HEIGHT, 0)
        val types = intent.getStringExtra(EXTRA_POKEMON_TYPES) ?: "Unknown"

        binding.detailPokeTextView.text = name
        binding.detailPokeHeightTextView.text = "体重：$weight"
        binding.detailPokeWeightTextView.text = "高さ：$height"
        binding.detailPokeTypesTextView.text = "タイプ：$types"
        Glide.with(this)
            .load(imageURL)
            .into(binding.detailPokeImageView)
    }
}