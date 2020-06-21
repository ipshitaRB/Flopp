package com.droidrbi.flopp.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.droidrbi.flopp.R
import com.droidrbi.flopp.databinding.MovieCardLayoutBinding
import com.droidrbi.flopp.network.models.Result
import com.squareup.picasso.Picasso

class MovieListAdapter(
    private val _dataset: List<Result>,
    private var itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MovieListAdapter.CollectibleViewHolder>() {

    class CollectibleViewHolder(var itemLayoutBinding: MovieCardLayoutBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root) {

        fun bind(movie: Result, clickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClick(movie)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectibleViewHolder {
        val itemLayoutBinding = DataBindingUtil.inflate<MovieCardLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.movie_card_layout,
            parent,
            false
        )

        return CollectibleViewHolder(itemLayoutBinding)
    }

    override fun getItemCount(): Int = _dataset.size

    override fun onBindViewHolder(holder: CollectibleViewHolder, position: Int) {
        val item = _dataset[position]
        val picasso = Picasso.get()
        holder.itemLayoutBinding.movie = item
        val imageURI = "https://image.tmdb.org/t/p/w500${item.posterUrl}"
        picasso.load(imageURI)
            .placeholder(R.drawable.ic_broken_image)
            .into(holder.itemLayoutBinding.thumbnailImageView)
        holder.bind(item, itemClickListener)
    }


    interface OnItemClickListener {
        fun onItemClick(movie: Result)
    }
}