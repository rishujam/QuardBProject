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

        binding.back.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }
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
        val summary = movie.show.summary
        binding.tvSummary.text = summary.substring(3,summary.length-4)
        val rating = movie.show.rating.toString()
        binding.tvRating.text = rating.substring(0,6)+" "+rating.substring(rating.length-4,rating.length-1)
        binding.date.text = movie.show.premiered
        val gen = movie.show.genres.toString()
        binding.gen.text = gen.substring(1,gen.length-2)
        binding.lang.text = movie.show.language
        binding.timings.text = movie.show.schedule.time+"  "+ movie.show.schedule.days.toString().split("]")[0].substring(1)
        Glide.with(binding.root).load(movie.show.image.original).into(binding.poster)
    }
}