package com.crp.mumbaiinsider.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.crp.mumbaiinsider.databinding.FeaturedViewItemBinding
import com.crp.mumbaiinsider.model.Featured

class FeaturedEventAdapter(
    private val list: List<Featured>,
    val adapterOnClick: (Featured) -> Unit
) :
    RecyclerView.Adapter<FeaturedEventAdapter.FeaturedEventView>() {

    inner class FeaturedEventView(private val featuredViewItemBinding: FeaturedViewItemBinding) :
        RecyclerView.ViewHolder(featuredViewItemBinding.root) {
        fun bind(featured: Featured) {
            featuredViewItemBinding.eventLogoIV.load(featured.vertical_cover_image) {
                crossfade(true)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
            featuredViewItemBinding.eventTitleTV.text = featured.name
            featuredViewItemBinding.eventVenueTV.text = featured.venue_name
            featuredViewItemBinding.eventTimeTV.text = featured.venue_date_string
            featuredViewItemBinding.eventPriceTV.text = featured.price_display_string

            featuredViewItemBinding.root.setOnClickListener {
                adapterOnClick(featured)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FeaturedEventView(
        FeaturedViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: FeaturedEventView, position: Int) =
        holder.bind(list[position])
}