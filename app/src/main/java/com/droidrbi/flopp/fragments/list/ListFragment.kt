package com.droidrbi.flopp.fragments.list

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidrbi.flopp.R
import com.droidrbi.flopp.databinding.FragmentListBinding
import com.droidrbi.flopp.network.models.Result

/**
 * A simple [Fragment] subclass.

 */
class ListFragment : Fragment(), MovieListAdapter.OnItemClickListener {

    private lateinit var navController: NavController
    private lateinit var _viewModel: MovieViewModel
    private lateinit var _viewModelFactory: MovieViewModelFactory
    private lateinit var _listBinding: FragmentListBinding
    private lateinit var _adapter: MovieListAdapter
    private lateinit var _application: Application


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _listBinding = FragmentListBinding.inflate(inflater, container, false)

        _listBinding.lifecycleOwner = viewLifecycleOwner

        _application = requireNotNull(this.activity).application

        _viewModelFactory = MovieViewModelFactory(_application)

        _viewModel = ViewModelProvider(this, _viewModelFactory)
            .get(MovieViewModel::class.java)
        _listBinding.viewModel = _viewModel

        _adapter = MovieListAdapter(this)
        _viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) {
                showNetworkUnavailableMessage()
            }
        })

        _listBinding.movieListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }
        return _listBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewModel.popularMovies.observe(viewLifecycleOwner, Observer<List<Result>> { movies ->
            movies.apply {
                _adapter.movieList = movies
            }
        })
    }

    private fun showNetworkUnavailableMessage() {
        Toast.makeText(
            context,
            getString(R.string.internet_not_connected),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onItemClick(movie: Result) {
        navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment(movie))
    }
}