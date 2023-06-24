package com.example.s9firebase2023.ui.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s9firebase2023.R
import com.example.s9firebase2023.adapter.PokemonAdapter
import com.example.s9firebase2023.model.PokemonModel
import com.example.s9firebase2023.model.PokemonModelDetails
import com.example.s9firebase2023.model.PokemonModelResponse
import com.example.s9firebase2023.service.PokeApiService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView


class PokemonFragment : Fragment() , PokemonAdapter.OnItemClickListener {
    private lateinit var pokemonAdapter: PokemonAdapter
    private var pokemonList: MutableList<PokemonModel> = mutableListOf()
    private var offset: Int = 0
    private var totalItems: Int = 0
    private lateinit var etSearchPokemon: EditText
    private lateinit var btnSearchPokemon: Button
    private var filteredList: MutableList<PokemonModel> = mutableListOf()
    private var isSearchMode = false
    private var scrollListener: RecyclerView.OnScrollListener? = null
    private var loadingDialog: ProgressDialog? = null
    private val handler = Handler()
    private var LIMIT_ROWS = 10

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearchPokemon = view.findViewById(R.id.etSearchPokemon)
        btnSearchPokemon = view.findViewById(R.id.btnSearchPokemon)

        btnSearchPokemon.setOnClickListener {
            val searchTerm = etSearchPokemon.text.toString()
            searchPokemon(searchTerm)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPokemon)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pokemonAdapter = PokemonAdapter(pokemonList)
        recyclerView.adapter = pokemonAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pokeApiService = retrofit.create(PokeApiService::class.java)

        fetchPokemonList(pokeApiService)
        pokemonAdapter.setOnItemClickListener(this)

    }

    private fun searchPokemon(searchTerm: String) {
        filteredList = pokemonList.filter { pokemon ->
            pokemon.name.contains(searchTerm, ignoreCase = true)
        } as MutableList<PokemonModel>

        pokemonAdapter.setData(filteredList)
        isSearchMode = true
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.rvPokemon)
        recyclerView.clearOnScrollListeners()
    }

    private fun fetchPokemonList(pokeApiService: PokeApiService) {
        showLoadingDialog()
        pokeApiService.getPokemonList(offset, LIMIT_ROWS)
            .enqueue(object : Callback<PokemonModelResponse> {
                override fun onResponse(
                    call: Call<PokemonModelResponse>,
                    response: Response<PokemonModelResponse>
                ) {
                    hideLoadingDialog()
                    if (response.isSuccessful) {
                        val pokemonResponse = response.body()
                        val newPokemonList = pokemonResponse?.results ?: emptyList()
                        totalItems = pokemonResponse?.count ?: 0

                        newPokemonList.forEach { pokemon ->
                            pokeApiService.getPokemonDetails(pokemon.name)
                                .enqueue(object : Callback<PokemonModelDetails> {
                                    override fun onResponse(
                                        call: Call<PokemonModelDetails>,
                                        response: Response<PokemonModelDetails>
                                    ) {
                                        if (response.isSuccessful) {
                                            val pokemonDetails = response.body()
                                            val imageUrl =
                                                pokemonDetails?.sprites?.frontDefault ?: ""

                                            val abilities = pokemonDetails?.abilities?.map { it.ability.name }?.toList() ?: emptyList()


                                            val pokemonModel =
                                                PokemonModel(pokemonDetails?.name ?: "", imageUrl, abilities)
                                            pokemonList.add(pokemonModel)
                                            pokemonAdapter.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<PokemonModelDetails>,
                                        t: Throwable
                                    ) {
                                        // Handle error
                                    }
                                })
                        }

                        pokemonAdapter.setData(pokemonList)

                        if (!isSearchMode) {
                            val recyclerView =
                                requireView().findViewById<RecyclerView>(R.id.rvPokemon)
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                            scrollListener = object : RecyclerView.OnScrollListener() {
                                override fun onScrolled(
                                    recyclerView: RecyclerView,
                                    dx: Int,
                                    dy: Int
                                ) {
                                    super.onScrolled(recyclerView, dx, dy)

                                    val visibleItemCount = layoutManager.childCount
                                    val totalItemCount = layoutManager.itemCount
                                    val firstVisibleItemPosition =
                                        layoutManager.findFirstVisibleItemPosition()

                                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount < totalItems) {
                                        //offset += visibleItemCount
                                        Log.d(
                                            "Pokemon Scroll",
                                            "onScrolled called offset $offset"
                                        ) // Log de información

                                        offset += LIMIT_ROWS
                                        fetchPokemonList(pokeApiService)
                                    }
                                }
                            }

                            recyclerView.addOnScrollListener(scrollListener!!)
                        }
                    }
                }

                override fun onFailure(call: Call<PokemonModelResponse>, t: Throwable) {
                    // Handle error
                    hideLoadingDialog()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restablece los datos relevantes
        pokemonList.clear()
        offset = 0
        totalItems = 0

        // Limpia el adaptador
        pokemonAdapter.setData(emptyList())
        scrollListener?.let {
            val recyclerView = requireView().findViewById<RecyclerView>(R.id.rvPokemon)
            recyclerView.removeOnScrollListener(it)
        }
        hideLoadingDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon, container, false)
    }

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(requireContext())
            loadingDialog?.setMessage("Cargando Pokemones")
            loadingDialog?.setCancelable(false)
        }
        handler.postDelayed({
            hideLoadingDialog()
        }, 5000) // Pausa de 5 segundos (5000 milisegundos)
        loadingDialog?.show()

    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onItemClick(pokemon: PokemonModel) {
        showPokemonDialog(pokemon)
    }

    private fun showPokemonDialog(pokemon: PokemonModel) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pokemon, null)
        val dialogImageView: ImageView = dialogView.findViewById(R.id.dialogImageView)
        val dialogTextView: TextView = dialogView.findViewById(R.id.dialogTextView)
        val abilitiesTextView: TextView = dialogView.findViewById(R.id.abilitiesTitleTextView)

        Picasso.get().load(pokemon.imageUrl).into(dialogImageView)
        dialogTextView.text = pokemon.name

        val abilities = pokemon.abilities.joinToString(", ")
        abilitiesTextView.text = abilities

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setPositiveButton("Aceptar", null)
        alertDialogBuilder.create().show()
    }
}