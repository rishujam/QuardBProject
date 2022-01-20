package com.ev.quardbproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ev.quardbproject.ApiData.RetrofitInstance
import com.ev.quardbproject.databinding.MovieFragmentBinding
import com.ev.quardbproject.datamodels.MovieItem
import com.ev.quardbproject.datamodels.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieFragment: Fragment(),MovieAdapter.OnItemClickListener {

    private var _binding:MovieFragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var list:List<MovieItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= MovieFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()

        binding.etSearch.setOnClickListener {
            val searchFragment = SearchFragment()
            (activity as MainActivity).setCurrentFragment(searchFragment)
        }
    }

    private fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            list = RetrofitInstance.api.getAllMovies()
            withContext(Dispatchers.Main){
                rvSetup(list)
            }
        }
    }

    private fun rvSetup(list:List<MovieItem>){
        val adapter = MovieAdapter(list,this)
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = GridLayoutManager(context,2)
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt("ID",list[position].show.id)
        bundle.putString("NAME",list[position].show.name)
        val movieDetailFragment = MovieDetail()
        movieDetailFragment.arguments = bundle
        (activity as MainActivity).setCurrentFragmentBack(movieDetailFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }
}