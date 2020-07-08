package com.droidrbi.flopp.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.droidrbi.flopp.databinding.FragmentDetailBinding
import com.droidrbi.flopp.network.models.Result


/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var _binding: FragmentDetailBinding
    private lateinit var _movie: Result

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val args = DetailFragmentArgs.fromBundle(requireArguments())
        _movie = args.movie
        _binding.movie = _movie
       /* val picasso = Picasso.get()
        val imageURI = "https://image.tmdb.org/t/p/w500${_movie.posterUrl}"
        picasso.load(imageURI)
            .placeholder(R.drawable.ic_broken_image)
            .into(_binding.thumbnail)*/
        return _binding.root
    }

}