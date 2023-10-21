package com.example.drinksapp.ui.favorites

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.drinksapp.R
import com.example.drinksapp.core.BaseViewHolder
import com.example.drinksapp.data.model.Cocktail
import com.example.drinksapp.databinding.RowDrinksBinding
import kotlinx.android.synthetic.main.row_drinks.view.*


class FavoritesAdapter(
    private val context: Context,
    private val itemClickListener: OnCocktailClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var cocktailList = listOf<Cocktail>()

    interface OnCocktailClickListener {
        fun onCocktailClick(cocktail: Cocktail, position: Int)

        fun onCocktaiDeleteClick(cocktail: Cocktail, position: Int)
    }

    fun setCocktailList(cocktailList: List<Cocktail>) {
        this.cocktailList = cocktailList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = RowDrinksBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = FavouriteViewHolder(itemBinding)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION }
                ?: return@setOnClickListener

            itemClickListener.onCocktailClick(cocktailList[position], position)
        }

        holder.itemView.imgFav.setOnClickListener{
            val position = holder.adapterPosition.takeIf { it != NO_POSITION }

            itemClickListener.onCocktaiDeleteClick(cocktailList[position!!], position)

        }

        return holder
    }

    override fun getItemCount(): Int = cocktailList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is FavouriteViewHolder -> holder.bind(cocktailList[position])
        }
    }

    private inner class FavouriteViewHolder(
        private val binding: RowDrinksBinding
    ) : BaseViewHolder<Cocktail>(binding.root) {
        override fun bind(item: Cocktail): Unit = with(binding) {

            checkBox2.isChecked = item.hasAlcohol.equals("Alcoholic", ignoreCase = true)

            txtTitulo.text = item.name
            txtDescripcion.text = item.description


            Glide.with(context)
                .load(item.image.trim())
                .placeholder(R.drawable.baseline_img_24)
                .error(R.drawable.baseline_broken_image_24)

                .addListener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imgCocktail)


        }
    }
}