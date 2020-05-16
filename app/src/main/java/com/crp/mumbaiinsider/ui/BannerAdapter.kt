package com.crp.mumbaiinsider.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.crp.mumbaiinsider.databinding.BannerItemBinding
import com.crp.mumbaiinsider.model.Banner

class BannerAdapter(private val list: List<Banner>) :
    RecyclerView.Adapter<BannerAdapter.BannerView>() {
    class BannerView(private val bannerItemBinding: BannerItemBinding) :
        RecyclerView.ViewHolder(bannerItemBinding.root) {
        fun bind(banner: Banner) {
            bannerItemBinding.bannerIv.load(banner.vertical_cover_image) { crossfade(true) }
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