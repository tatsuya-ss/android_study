package com.example.android_study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_study.databinding.ActivityMainBinding
import com.example.android_study.databinding.ItemPokemonBinding
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

// MainActivityクラス
// ポケモンリストを表示するメインの画面を実装
class MainActivity : AppCompatActivity() {
    // View Binding用の変数
    private lateinit var binding: ActivityMainBinding
    // ViewModelのインスタンスをActivity単位で管理
    private val viewModel: PokemonViewModel by viewModels()
    // RecyclerViewのアダプターインスタンス
    private val adapter = PokemonListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // レイアウトの初期化
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerViewのセットアップ
        setupRecyclerView()
        // ViewModelの監視を開始
        observeViewModel()
        // 初回データ取得
        viewModel.fetchPokemonList()
    }

    // RecyclerViewの設定を行うメソッド
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            // アダプターの設定
            adapter = this@MainActivity.adapter
            // 縦方向のリストとして表示
            layoutManager = LinearLayoutManager(this@MainActivity)

            // スクロール検知のリスナーを追加
            // 無限スクロールの実装
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    // 現在表示されているアイテム数
                    val visibleItemCount = layoutManager.childCount
                    // 全アイテム数
                    val totalItemCount = layoutManager.itemCount
                    // 最初に表示されているアイテムの位置
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    // ローディング中でなく、最後のページでもない場合
                    if (!viewModel.isLoading.value!! && !viewModel.isLastPage) {
                        // リストの末尾に近づいたら次のデータを読み込む
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            viewModel.fetchPokemonList()
                        }
                    }
                }
            })
        }
    }

    // ViewModelの監視設定
    private fun observeViewModel() {
        // ポケモンリストの監視
        viewModel.pokemonList.observe(this) { pokemonList ->
            adapter.submitList(pokemonList.toList())
        }

        // ローディング状態の監視
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        // エラー状態の監視
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// PokemonViewModelクラス
// ポケモンリストのデータと状態を管理
class PokemonViewModel : ViewModel() {
    // リポジトリのインスタンス
    private val repository = PokemonRepository()

    // ページング用の変数
    private var currentPage = 0
    private val pageSize = 20
    // 最後のページかどうかのフラグ
    var isLastPage = false
        private set

    // ポケモンリストを保持するLiveData
    private val _pokemonList = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    // ローディング状態を保持するLiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // エラー状態を保持するLiveData
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // ポケモンリストを取得するメソッド
    fun fetchPokemonList() {
        // ローディング中または最後のページの場合は何もしない
        if (isLoading.value == true || isLastPage) return

        // コルーチンを使用して非同期処理
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // オフセットを計算
                val offset = currentPage * pageSize
                // APIからデータを取得
                val response = repository.getPokemonList(offset, pageSize)

                // 既存のリストと新しいデータを結合
                val currentList = _pokemonList.value.orEmpty()
                val newList = currentList + response.results
                _pokemonList.value = newList

                // ページ管理の更新
                currentPage++
                isLastPage = response.results.isEmpty() || offset + pageSize >= response.count
                _error.value = null
            } catch (e: Exception) {
                // エラーハンドリング
                _error.value = "ポケモンリストの取得に失敗しました: ${e.message}"
            } finally {
                // ローディング状態を解除
                _isLoading.value = false
            }
        }
    }
}

// PokemonRepositoryクラス
// APIとの通信を担当
class PokemonRepository {
    // Retrofitクライアントのインスタンス
    private val api = RetrofitClient.pokemonApi

    // ポケモンリストを取得する関数
    suspend fun getPokemonList(offset: Int, limit: Int): PokemonListResponse {
        return try {
            api.getPokemonList(offset, limit)
        } catch (e: Exception) {
            throw Exception("ネットワークエラー: ${e.message}")
        }
    }
}

// RetrofitClientオブジェクト
// シングルトンとしてRetrofitの設定を管理
object RetrofitClient {
    // PokeAPIのベースURL
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // HTTPログ出力の設定
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClientの設定
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Retrofitインスタンスの設定
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API interfaceの実装を作成
    val pokemonApi: PokemonApi = retrofit.create(PokemonApi::class.java)
}

// APIインターフェース
interface PokemonApi {
    // ポケモンリストを取得するエンドポイント
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponse
}

// APIレスポンスのデータクラス
data class PokemonListResponse(
    val count: Int,
    val results: List<Pokemon>
)

// ポケモンデータクラス
data class Pokemon(
    val name: String,
    val url: String
) {
    // ポケモンの画像URLを生成するメソッド
    fun getImageUrl(): String {
        val id = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }
}
// PokemonListAdapterクラス
// RecyclerViewのアダプター実装
class PokemonListAdapter : ListAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    // ViewHolderの生成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    // ViewHolderにデータをバインド
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ViewHolderクラス
    class PokemonViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // ポケモンデータをViewにバインド
        fun bind(pokemon: Pokemon) {
            // ポケモンの名前を表示（先頭文字を大文字に）
            binding.pokemonName.text = pokemon.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            // Glideを使用して画像を読み込み
            Glide.with(binding.root.context)
                .load(pokemon.getImageUrl())
                .centerCrop()
                .into(binding.pokemonImage)
        }
    }
}

// リスト更新の差分計算を行うDiffUtilクラス
class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
    // アイテムが同じかどうかの判定（ID比較）
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.name == newItem.name
    }

    // アイテムの内容が同じかどうかの判定（完全一致比較）
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }
}