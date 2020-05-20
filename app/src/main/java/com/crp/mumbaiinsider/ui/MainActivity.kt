package com.crp.mumbaiinsider.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.lifecycle.Observer
import com.crp.mumbaiinsider.R
import com.crp.mumbaiinsider.databinding.ActivityMainBinding
import com.crp.mumbaiinsider.model.Banner
import com.crp.mumbaiinsider.model.Featured
import com.crp.mumbaiinsider.network.State
import com.crp.mumbaiinsider.ui.adapter.BannerAdapter
import com.crp.mumbaiinsider.ui.adapter.FeaturedEventAdapter
import com.crp.mumbaiinsider.utils.Helper
import com.crp.mumbaiinsider.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.no_internet_layout.view.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        sharedPreferences =
            getSharedPreferences(getString(R.string.theme_mode), Context.MODE_PRIVATE)

        onClickEvent()
        loadData()
    }

    private fun loadData() {
        if (Helper.isConnectingToInternet(this)) {
            mainBinding.noInternetCl.visibility = GONE
            mainBinding.shimmerFL.visibility = VISIBLE
            getData()
        } else {
            networkError()
        }
    } //checking connection and loading data. if no internet will display no internet layout.

    private fun onClickEvent() {

        mainBinding.noInternetCl.noInternetView.retryBt.setOnClickListener {
            loadData()
        }
        mainBinding.viewAllFeatured.setOnClickListener {
            goToList("")
        }
        mainBinding.seeAll.setOnClickListener {
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

        mainBinding.changeThemeIv.setOnClickListener { switchTheme() }
    } //All OnClickListener

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
                    state.data.featured?.let {
                        setFeaturedEvents(it)
                        setCategoriesEvents(it)
                    }

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
    } //Fetching data and observing state through viewmodel

    private fun stopLoading() {
        mainBinding.shimmerFL.stopShimmer()
        mainBinding.shimmerFL.visibility = GONE
        mainBinding.dataLayout.visibility = VISIBLE
    }

    private fun networkError() {
        mainBinding.shimmerFL.stopShimmer()
        mainBinding.shimmerFL.visibility = GONE
        mainBinding.dataLayout.visibility = GONE
        mainBinding.noInternetCl.visibility = VISIBLE
    }

    private fun setBanner(list: List<Banner>) {

        var distinctList = list.distinctBy { it.map_link }
        if (distinctList.size > 5) {
            distinctList = distinctList.take(5)
        }

        mainBinding.viewPager2.apply {
            this.adapter =
                BannerAdapter(distinctList) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                }
            mainBinding.indicatorVP.setViewPager(this)
        }
    } //Get Distinct Limited Items to Set in List

    private fun setFeaturedEvents(list: List<Featured>) {
        mainBinding.featuredRV.adapter =
            FeaturedEventAdapter(list.take(6)) {
                openDetailPage(it)
            }
    } //Display limited featured items in List as we have see all option

    private fun setCategoriesEvents(list: List<Featured>) {
        mainBinding.comedyRV.adapter = getFilterListAdapter(list, "Comedy")
        mainBinding.healthRV.adapter = getFilterListAdapter(list, "Health and Fitness")
        mainBinding.workshopRV.adapter = getFilterListAdapter(list, "Workshops")
        mainBinding.talksRV.adapter = getFilterListAdapter(list, "Talks")
    } //set different category list


    private fun getFilterListAdapter(
        list: List<Featured>,
        filterStr: String
    ): FeaturedEventAdapter {
        return FeaturedEventAdapter(list.filter { it.category_id?.name == filterStr }) {
            openDetailPage(it)
        }
    } //generic function to return each category adapter

    private fun openDetailPage(featured: Featured) {
        val intent = Intent(this@MainActivity, EventDetailActivity::class.java)
        intent.putExtra("_id", featured._id)
        startActivity(intent)
    } // open detail page with passing ID of current item.

    private fun goToList(string: String) {
        val intent = Intent(this@MainActivity, EventListActivity::class.java)
        intent.putExtra("filter", string)
        startActivity(intent)
    } // to see all event when tapped on see all button

    private fun switchTheme() {
        val currentTheme = sharedPreferences.getInt(getString(R.string.theme_mode), 0)

        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPreferences.edit {
                putInt(
                    getString(R.string.theme_mode),
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPreferences.edit {
                putInt(
                    getString(R.string.theme_mode),
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
        recreate()

    }// switch theme function to be triggered by user.
}