package com.droidrbi.flopp.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidrbi.flopp.R
import com.droidrbi.flopp.databinding.FragmentListBinding
import com.droidrbi.flopp.network.MovieApi
import com.droidrbi.flopp.network.models.PopularMovies
import com.droidrbi.flopp.network.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.

 */
class ListFragment : Fragment(), MovieListAdapter.OnItemClickListener {

    private lateinit var navController: NavController

    private lateinit var _listBinding: FragmentListBinding
    private lateinit var _dataset: List<Result>
    private lateinit var _adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _listBinding = FragmentListBinding.inflate(inflater, container, false)
        getMovies()
        return _listBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onItemClick(movie: Result) {
        // uncomment after movie arg is added
        //navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment(movie))
    }

    private fun getMovies() {

        MovieApi.retrofitService.getMovies(getString(R.string.api_key)).enqueue(
            object : Callback<PopularMovies> {
                override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<PopularMovies>,
                    response: Response<PopularMovies>
                ) {
                    _dataset = response.body()!!.results
                    _adapter = MovieListAdapter(_dataset, this@ListFragment)
                    _listBinding.movieListView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = _adapter
                    }
                }
            })
    }
}