package com.ev.quardbproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ev.quardbproject.ApiData.RetrofitInstance
import com.ev.quardbproject.databinding.MovieDetailFragmentBinding
import com.ev.quardbproject.datamodels.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetail:Fragment() {

    private var _binding:MovieDetailFragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var id:String
    private lateinit var name:String
    private lateinit var movie:MovieItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieDetailFragmentBinding.inflate(inflater,container,false)
        id = arguments?.getString("ID").toString()
        name = arguments?.getString("NAME").toString()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    private fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            movie = RetrofitInstance.api.getMovie(name)[0]
            withContext(Dispatchers.Main){
                setupData(movie)
            }
        }
    }

    private fun setupData(movie:MovieItem){
        binding.title.text = movie.show.name
        binding.tvSummary.text = movie.show.summary
        binding.tvRating.text = movie.show.rating.toString()
        Glide.with(binding.root).load(movie.show.image.original).into(binding.poster)
    }
}