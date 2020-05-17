package com.crp.mumbaiinsider.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crp.mumbaiinsider.model.Featured
import com.crp.mumbaiinsider.model.MainResponse
import com.crp.mumbaiinsider.network.InsiderAPI
import com.crp.mumbaiinsider.network.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val insiderAPI: InsiderAPI by inject()
    private val _mainLiveData = MutableLiveData<State<MainResponse>>()
    val mainLiveData: LiveData<State<MainResponse>> get() = _mainLiveData

    fun getMainApiResponse() {
        _mainLiveData.value = State.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val mainResponse = insiderAPI.getApiResponse()
            withContext(Main) {

                mainResponse.let { mainData ->
                    mainData.featured?.let {
                        if (it.isNotEmpty()) {
                            _mainLiveData.value = State.success(mainResponse)
                        } else {
                            _mainLiveData.value = State.error("No Data Available")
                        }
                    }

                } ?: run {
                    _mainLiveData.value = State.error("Something went wrong")
                }


            }
        }
    }


}