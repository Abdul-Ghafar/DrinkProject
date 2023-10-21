package com.example.drinksapp.ui.drinkdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.drinksapp.R
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.databinding.FragmentDrinkDetailsBinding
import com.example.drinksapp.presentation.ByNameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrinkDetailsFragment : Fragment(R.layout.fragment_drink_details) {
    private val viewModel by activityViewModels<ByNameViewModel>()

    private lateinit var cocktail: Cocktail

    private var isCocktailFavorited: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            DrinkDetailsFragmentArgs.fromBundle(it).also { args ->
                cocktail = args.drink
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDrinkDetailsBinding.bind(view)

        Glide.with(requireContext())
            .load(cocktail.image)
            .centerCrop()
            .into(binding.imgCocktail)

        binding.cocktailTitle.text = cocktail.name
        binding.cocktailDesc.text = cocktail.description

        fun updateButtonIcon() {
            val isCocktailFavorited = isCocktailFavorited ?: return

            binding.btnSaveOrDeleteCocktail.setImageResource(
                when {
                    isCocktailFavorited -> R.drawable.ic_baseline_delete_24
                    else -> R.drawable.ic_baseline_favorite_24
                }
            )
        }

        binding.btnSaveOrDeleteCocktail.setOnClickListener {
            val isCocktailFavorited = isCocktailFavorited ?: return@setOnClickListener

            viewModel.saveOrDeleteFavoriteCocktail(cocktail)
            this.isCocktailFavorited = !isCocktailFavorited
            updateButtonIcon()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            isCocktailFavorited = viewModel.isCocktailFavorite(cocktail)
            updateButtonIcon()
        }
    }
}