package com.dmonster.dcash.view.newsDetail

import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val dataStoreModule: DataStoreModule
) : BaseViewModel() {
    val isLoadNews = MutableStateFlow(false)

    suspend fun isNotShowingNewsViewTutorial(): Boolean {
        return dataStoreModule.isNotShowingNewsViewTutorial.first()
    }
}