package com.droidrbi.flopp.fragments.list

import android.app.Application
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
import com.droidrbi.flopp.database.DatabaseUtil.Companion.convertToMovieList
import com.droidrbi.flopp.database.DatabaseUtil.Companion.convertToResultList
import com.droidrbi.flopp.database.MovieDatabase
import com.droidrbi.flopp.database.MovieDatabaseDao
import com.droidrbi.flopp.database.model.Movie
import com.droidrbi.flopp.databinding.FragmentListBinding
import com.droidrbi.flopp.network.MovieApi
import com.droidrbi.flopp.network.NetworkUtil.Companion.isConnectedToNetwork
import com.droidrbi.flopp.network.models.PopularMovies
import com.droidrbi.flopp.network.models.Result
import kotlinx.coroutines.*
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
    private lateinit var _application: Application
    private lateinit var _dataSource: MovieDatabaseDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _listBinding = FragmentListBinding.inflate(inflater, container, false)
        _application = requireNotNull(this.activity).application

        _dataSource = MovieDatabase.getDatabase(_application).movieDatabaseDao()

        showData()
        return _listBinding.root
    }

    private fun showData() {
        if (isConnectedToNetwork(context)) {
            getMovies()
        } else {
            updateMovieList()

            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_LONG
            ).show()

        }
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
                    saveMoviesInDatabase()
                }
            })
    }

    private fun updateMovieList() {
        uiScope.launch {
            _dataset = fetchMoviesFromDB()
            _adapter = MovieListAdapter(_dataset, this@ListFragment)
            _listBinding.movieListView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = _adapter

            }
        }
    }

    private fun saveMoviesInDatabase() {
        uiScope.launch {
            insertMovies(convertToMovieList(_dataset))
        }
    }

    private suspend fun insertMovies(movieList: List<Movie>) {
        withContext(Dispatchers.IO) {
            _dataSource.insertPopularMovies(movieList)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModelJob.cancel()
    }

    private suspend fun fetchMoviesFromDB(): List<Result> {
        return withContext(Dispatchers.IO) {
            val movieList = _dataSource.getAllMovies()
            convertToResultList(movieList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onItemClick(movie: Result) {
        navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment(movie))
    }
}