package com.crp.mumbaiinsider.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crp.mumbaiinsider.databinding.ActivityMainBinding
import com.crp.mumbaiinsider.model.Banner
import com.crp.mumbaiinsider.model.Featured
import com.crp.mumbaiinsider.network.State
import com.crp.mumbaiinsider.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        getData()
        onClickEvent()
    }

    private fun onClickEvent() {
        mainBinding.viewAllFeatured.setOnClickListener {
            goToList("")
        }
        mainBinding.comedyCp.setOnClickListener {
            goToList("Comedy")
        }
        mainBinding.workshopCp.setOnClickListener {
            goToList("Workshops")
        }
        mainBinding.talksCp.setOnClickListener {
            goToList("Talks")
        }
        mainBinding.healthCp.setOnClickListener {
            goToList("Health and Fitness")
        }
    }

    private fun getData() {
        mainViewModel.getMainApiResponse()
        mainViewModel.mainLiveData.observe(this@MainActivity, Observer { state ->
            when (state) {
                is State.Loading -> {
                    mainBinding.shimmerFL.startShimmer()
                }
                is State.Success -> {
                    stopLoading()
                    state.data.banners?.let { setBanner(it) }
                    state.data.featured?.let { setFeaturedEvents(it) }

                }
                is State.Error -> {
                }

            }
        })
    }

    private fun stopLoading() {
        mainBinding.shimmerFL.stopShimmer()
        mainBinding.shimmerFL.visibility = GONE
        mainBinding.dataLayout.visibility = VISIBLE
    }

    private fun setBanner(list: List<Banner>) {

        var distinctList = list.distinctBy { it.map_link }
        if (distinctList.size > 5) {
            distinctList = distinctList.take(5)
        }


        mainBinding.viewPager2.apply {
            this.adapter = BannerAdapter(distinctList) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
            mainBinding.indicatorVP.setViewPager(this)
        }
    }

    private fun setFeaturedEvents(list: List<Featured>) {
        mainBinding.featuredRV.adapter = FeaturedEventAdapter(list.take(6)) { openDetailPage(it) }
    }

    private fun openDetailPage(featured: Featured) {

    }

    private fun goToList(string: String) {
        val intent = Intent(this@MainActivity, EventListActivity::class.java)
        intent.putExtra("filter", string)
        startActivity(intent)
    }
}