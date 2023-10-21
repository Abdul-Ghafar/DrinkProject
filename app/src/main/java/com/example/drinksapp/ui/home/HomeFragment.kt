package com.example.drinksapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drinksapp.R
import com.example.drinksapp.core.Resource
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.databinding.FragmentHomeBinding
import com.example.drinksapp.presentation.ByLetterViewModel
import com.example.drinksapp.presentation.ByNameViewModel
import com.example.drinksapp.utils.hide
import com.example.drinksapp.utils.onQueryTextChanged
import com.example.drinksapp.utils.show
import com.example.drinksapp.utils.showIf
import com.example.drinksapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    HomeAdapter.OnDrinkClickListener {
    private val viewModelName by activityViewModels<ByNameViewModel>()
    private val viewModelLetter by activityViewModels<ByLetterViewModel>()

    private lateinit var homeAdapter: HomeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        homeAdapter = HomeAdapter(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)

        binding.radioName.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                binding.searchView.setQuery(viewModelName.currentCocktailName.value.toString(), false)

                viewModelName.fetchCocktailList.observe(viewLifecycleOwner, Observer { result ->
                    Log.d("DDDD", "Observer fetchCocktailList:")
                    binding.progressBar.showIf { result is Resource.Loading }
                    when (result) {
                        is Resource.Loading -> {
                            binding.emptyContainer.root.hide()
                        }

                        is Resource.Success -> {
                            if (result.data.isEmpty()) {
                                binding.rvTragos.hide()
                                binding.emptyContainer.root.show()
                                return@Observer
                            }
                            binding.rvTragos.show()
                            homeAdapter.setCocktailList(result.data)
                            binding.emptyContainer.root.hide()
                        }

                        is Resource.Failure -> {
                            if(result.exception is IOException){
                                showToast("Error: Internet is Off")
                            }else {
                                showToast("Error: ${result.exception}")
                            }
                        }
                    }
                })
            }
        }

        binding.radioAlphabet.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                binding.searchView.setQuery(viewModelLetter.currentCocktailFirstLetter.value.toString(), false)

                viewModelLetter.fetchCocktailListByLetter.observe(viewLifecycleOwner, Observer { result ->
                    Log.d("DDDD", "Observer fetchCocktailListByLetter:")
                    binding.progressBar.showIf { result is Resource.Loading }

                    when (result) {
                        is Resource.Loading -> {
                            binding.emptyContainer.root.hide()
                        }

                        is Resource.Success -> {
                            if (result.data.isEmpty()) {
                                binding.rvTragos.hide()
                                binding.emptyContainer.root.show()
                                return@Observer
                            }
                            binding.rvTragos.show()
                            homeAdapter.setCocktailList(result.data)
                            binding.emptyContainer.root.hide()
                        }

                        is Resource.Failure -> {
                            if(result.exception is IOException){
                                showToast("Error: Internet is Off")
                            }else {
                                showToast("Error: ${result.exception}")
                            }
                        }
                    }
                })
            }
        }

        binding.searchView.setQuery(viewModelName.currentCocktailName.value.toString(),false)
        binding.radioName.isChecked=true

        binding.rvTragos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTragos.adapter = homeAdapter


        binding.searchView.onQueryTextChanged {
            if(binding.radioName.isChecked) {
                Log.d("DDDD", "onQueryTextByName:"+it)
                viewModelName.setCocktail(it)
            }else if(binding.radioAlphabet.isChecked) {
                Log.d("DDDD", "onQueryTextByLetter:"+it)
                if (it.length == 1) {
                    viewModelLetter.setCocktailFirstLetter(it)
                } else {
                    showToast("Only first letter is supported", Toast.LENGTH_SHORT)
                }
            }
            Log.d("DDDD", "onQueryTextChanged:"+it)
        }
    }

    override fun onCocktailClick(cocktail: Cocktail, position: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionMainFragmentToDrinkDetailFragment(
                cocktail
            )
        )
    }
}