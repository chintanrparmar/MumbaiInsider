package com.crp.mumbaiinsider.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.crp.mumbaiinsider.R
import com.crp.mumbaiinsider.databinding.BannerItemBinding
import com.crp.mumbaiinsider.model.Banner

class BannerAdapter(private val list: List<Banner>, val adapterOnClick: (String) -> Unit) :
    RecyclerView.Adapter<BannerAdapter.BannerView>() {

    inner class BannerView(private val bannerItemBinding: BannerItemBinding) :
        RecyclerView.ViewHolder(bannerItemBinding.root) {
        fun bind(banner: Banner) {
            bannerItemBinding.bannerIv.load(banner.vertical_cover_image) {
                crossfade(true)
                memoryCachePolicy(CachePolicy.ENABLED)
                placeholder(R.drawable.default_banner)
            }
            bannerItemBinding.root.setOnClickListener {
                banner.map_link?.let { it1 -> adapterOnClick(it1) }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BannerView(
        BannerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BannerView, position: Int) = holder.bind(list[position])
}