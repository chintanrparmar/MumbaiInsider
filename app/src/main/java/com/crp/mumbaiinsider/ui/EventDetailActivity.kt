package com.crp.mumbaiinsider.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import com.crp.mumbaiinsider.databinding.ActivityEventDetailBinding
import com.crp.mumbaiinsider.model.Featured
import com.crp.mumbaiinsider.network.State
import com.crp.mumbaiinsider.utils.Helper
import com.crp.mumbaiinsider.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.no_internet_layout.view.*
import org.koin.android.ext.android.inject

class EventDetailActivity : AppCompatActivity() {
    private lateinit var activityEventDetailBinding: ActivityEventDetailBinding

    private val mainViewModel: MainViewModel by inject()
    private var idString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEventDetailBinding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(activityEventDetailBinding.root)

        intent.getStringExtra("_id").let { idString = it.toString() }

        onClickEvent()
        loadData()
    }

    private fun onClickEvent() {
        activityEventDetailBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
        activityEventDetailBinding.noInternetCl.retryBt.setOnClickListener { loadData() }
    }// onClick Listener

    private fun loadData() {
        if (Helper.isConnectingToInternet(this)) {
            activityEventDetailBinding.noInternetCl.visibility = View.GONE
            activityEventDetailBinding.shimmerFL.visibility = View.VISIBLE
            getData()
        } else {
            networkError()
        }
    } //checking connection and loading data. if no internet will display no internet layout.


    private fun getData() {
        mainViewModel.getMainApiResponse()
        mainViewModel.mainLiveData.observe(this@EventDetailActivity, Observer { state ->
            when (state) {
                is State.Loading -> {
                    activityEventDetailBinding.shimmerFL.startShimmer()
                }
                is State.Success -> {
                    stopLoading()
                    setData(state.data.featured?.filter { it._id == idString })
                }
                is State.Error -> {
                }
            }
        })
    } //Fetching data and observing state through viewmodel

    private fun setData(featuredList: List<Featured>?) {

        val featured = featuredList?.get(0)

        featured?.let {
            activityEventDetailBinding.eventImageIV.load(it.horizontal_cover_image)
//            activityEventDetailBinding.catLogoIv.load(it.category_id?.icon_img)
            activityEventDetailBinding.eventTitleTV.text = it.name
            activityEventDetailBinding.eventPriceTV.text = it.price_display_string
            activityEventDetailBinding.eventTimeTV.text = it.venue_date_string
            activityEventDetailBinding.eventVenueTV.text = it.venue_name
            activityEventDetailBinding.eventCatTV.text = it.category_id?.name
        }
    } // Set Single item data

    private fun stopLoading() {
        activityEventDetailBinding.shimmerFL.stopShimmer()
        activityEventDetailBinding.shimmerFL.visibility = View.GONE
        activityEventDetailBinding.dataLayout.visibility = View.VISIBLE
    }

    private fun networkError() {
        activityEventDetailBinding.shimmerFL.stopShimmer()
        activityEventDetailBinding.shimmerFL.visibility = View.GONE
        activityEventDetailBinding.dataLayout.visibility = View.GONE
        activityEventDetailBinding.noInternetCl.visibility = View.VISIBLE
    }


}