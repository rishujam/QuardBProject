package com.ev.quardbproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ev.quardbproject.databinding.ItemMovieBinding
import com.ev.quardbproject.datamodels.MovieItem
import com.bumptech.glide.Glide


class MovieAdapter(
    val movies:List<MovieItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    inner class MovieViewHolder (val binding:ItemMovieBinding):RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.itemMovie.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val posi =adapterPosition
            if(posi!=RecyclerView.NO_POSITION){
                listener.onItemClick(posi)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(holder.binding.root).load(movies[position].show.image.medium).into(holder.binding.thumbnail)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}