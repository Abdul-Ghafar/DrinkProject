package com.example.drinksapp.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drinksapp.R
import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.databinding.FavoriteFragmentBinding
import com.example.drinksapp.presentation.ByNameViewModel
import com.example.drinksapp.utils.show
import com.example.drinksapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.favorite_fragment),
    FavoritesAdapter.OnCocktailClickListener {
    private val viewModel by activityViewModels<ByNameViewModel>()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesAdapter = FavoritesAdapter(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FavoriteFragmentBinding.bind(view)

        binding.rvFavourite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavourite.adapter = favoritesAdapter

        viewModel.getFavoriteCocktails().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.root.show()
                        return@Observer
                    }
                    favoritesAdapter.setCocktailList(result.data)
                }
                is Resource.Failure -> {
                    showToast("An error occurred ${result.exception}")
                }
            }
        })
    }

    override fun onCocktailClick(cocktail: Cocktail, position: Int) {
        findNavController().navigate(FavoritesFragmentDirections.actionFavoritosFragmentToDetailFragment(cocktail))
    }

    override fun onCocktaiDeleteClick(cocktail: Cocktail, position: Int) {
        viewModel.deleteFavoriteCocktail(cocktail)
    }
}