package com.crp.mumbaiinsider.di

import com.crp.mumbaiinsider.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
}