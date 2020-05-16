package com.crp.mumbaiinsider.ui

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crp.mumbaiinsider.databinding.ActivityMainBinding
import com.crp.mumbaiinsider.model.Banner
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
    }

    private fun getData() {
        mainViewModel.getMainApiResponse()
        mainViewModel.mainLiveData.observe(this@MainActivity, Observer { state ->
            when (state) {
                is State.Loading -> {
                    mainBinding.shimmerFL.startShimmer()
                }
                is State.Success -> {
                    Log.e("Data", state.data.popular.toString())

                    mainBinding.shimmerFL.stopShimmer()
                    mainBinding.shimmerFL.visibility = GONE
                    state.data.banners?.let {
                        setBanner(it)
                    }

                }
                is State.Error -> {
                }

            }
        })
    }

    private fun setBanner(list: List<Banner>) {
        mainBinding.viewPager2.adapter = BannerAdapter(list)
    }
}