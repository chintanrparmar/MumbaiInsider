package com.crp.mumbaiinsider.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crp.mumbaiinsider.R
import com.crp.mumbaiinsider.databinding.ActivityEventListBinding
import com.crp.mumbaiinsider.model.Featured
import com.crp.mumbaiinsider.network.State
import com.crp.mumbaiinsider.utils.Helper
import com.crp.mumbaiinsider.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.no_internet_layout.view.*
import org.koin.android.ext.android.inject

class EventListActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()
    var filterString = ""
    private lateinit var activityEventListBinding: ActivityEventListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEventListBinding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(activityEventListBinding.root)

        filterString = intent?.getStringExtra("filter").toString()

        if (filterString != "") {
            activityEventListBinding.toolbar.title = filterString
        }

        activityEventListBinding.toolbar.setNavigationOnClickListener { onBackPressed() }

        onClickEvent()
        loadData()
    }

    private fun onClickEvent() {
        activityEventListBinding.noInternetCl.noInternetView.retryBt.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        if (Helper.isConnectingToInternet(this)) {
            activityEventListBinding.noInternetCl.visibility = View.GONE
            activityEventListBinding.shimmerFL.visibility = View.VISIBLE
            setListData()
        } else {
            networkError()
        }
    }


    private fun setListData() {
        mainViewModel.getMainApiResponse()
        mainViewModel.mainLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    activityEventListBinding.shimmerFL.startShimmer()
                }
                is State.Success -> {
                    stopLoading()
                    state.data.featured?.let { setData(it) }
                }
                is State.Error -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.something),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }


    private fun setData(list: List<Featured>) {
        var distinctList = list
        if (filterString != "") {
            distinctList = list.distinctBy { it.category_id?.name == filterString }
        }
        activityEventListBinding.eventListRV.adapter =
            FeaturedEventAdapter(distinctList) { openDetailPage(it) }
    }

    private fun openDetailPage(featured: Featured) {
        val intent = Intent(this@EventListActivity, EventDetailActivity::class.java)
        intent.putExtra("_id", featured._id)
        startActivity(intent)
    }

    private fun stopLoading() {
        activityEventListBinding.shimmerFL.stopShimmer()
        activityEventListBinding.shimmerFL.visibility = View.GONE
    }

    private fun networkError() {
        activityEventListBinding.shimmerFL.stopShimmer()
        activityEventListBinding.shimmerFL.visibility = View.GONE
        activityEventListBinding.noInternetCl.visibility = View.VISIBLE
    }

}