package com.example.s9firebase2023.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s9firebase2023.R
import com.example.s9firebase2023.model.PokemonModel
import com.squareup.picasso.Picasso

class PokemonAdapter(private var pokemonList: List<PokemonModel>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.bind(pokemon)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(pokemon)
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun setData(pokemonList: List<PokemonModel>) {
        this.pokemonList = pokemonList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(pokemon: PokemonModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonImageView: ImageView = itemView.findViewById(R.id.pokemonImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val urlTextView: TextView = itemView.findViewById(R.id.urlTextView)

        fun bind(pokemon: PokemonModel) {
            nameTextView.text = pokemon.name
            urlTextView.text = pokemon.imageUrl

            // Utiliza una biblioteca como Picasso para cargar la imagen del Pokémon en el ImageView
            Picasso.get().load(pokemon.imageUrl).into(pokemonImageView)
        }
    }
}