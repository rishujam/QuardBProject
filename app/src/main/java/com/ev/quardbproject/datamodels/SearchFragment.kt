package com.ev.quardbproject.datamodels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ev.quardbproject.ApiData.RetrofitInstance
import com.ev.quardbproject.MainActivity
import com.ev.quardbproject.MovieAdapter
import com.ev.quardbproject.MovieDetail
import com.ev.quardbproject.databinding.SearchFragmentBinding
import kotlinx.coroutines.*

class SearchFragment:Fragment() ,MovieAdapter.OnItemClickListener{

    private var _binding : SearchFragmentBinding? =null
    private val binding get() = _binding!!
    private lateinit var movieList:List<MovieItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = SearchFragmentBinding.inflate(inflater,container,false)

        var job: Job?=null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty() && binding.etSearch.hasFocus()){
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                movieList =RetrofitInstance.api.getMovie(editable.toString())
                                withContext(Dispatchers.Main){
                                    setupRv(movieList)
                                }
                            }catch (e:Exception){
                                Toast.makeText(context,"No Result: ${e.message}",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private fun setupRv(list:List<MovieItem>){
        val adapter = MovieAdapter(list,this)
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = GridLayoutManager(context,2)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt("ID",movieList[position].show.id)
        bundle.putString("NAME",movieList[position].show.name)
        val movieDetailFragment =MovieDetail()
        (activity as MainActivity).setCurrentFragmentBack(movieDetailFragment)
    }
}