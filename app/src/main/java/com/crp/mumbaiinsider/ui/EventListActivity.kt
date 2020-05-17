package com.crp.mumbaiinsider.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crp.mumbaiinsider.databinding.ActivityEventListBinding
import com.crp.mumbaiinsider.model.Featured
import com.crp.mumbaiinsider.network.State
import com.crp.mumbaiinsider.viewmodel.MainViewModel
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
        setListData()
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
        startActivity(Intent(this@EventListActivity, EventDetailActivity::class.java))
    }

    private fun stopLoading() {
        activityEventListBinding.shimmerFL.stopShimmer()
        activityEventListBinding.shimmerFL.visibility = View.GONE
    }

}