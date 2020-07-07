package com.droidrbi.flopp.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.droidrbi.flopp.R
import com.droidrbi.flopp.databinding.MovieCardLayoutBinding
import com.droidrbi.flopp.network.models.Result

class MovieListAdapter(
    private var itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MovieListAdapter.CollectibleViewHolder>() {

    /**
     * The videos that our Adapter will show
     */
    var movieList: List<Result> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }


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

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: CollectibleViewHolder, position: Int) {
        val item = movieList[position]
        holder.itemLayoutBinding.movie = item
        holder.bind(item, itemClickListener)
    }


    interface OnItemClickListener {
        fun onItemClick(movie: Result)
    }
}