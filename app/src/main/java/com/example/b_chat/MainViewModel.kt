package com.example.b_chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.b_chat.data.AppData
import com.example.b_chat.data.ComicsData
import com.example.b_chat.data.MainScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var showSplashScreen by mutableStateOf(true)
    var currentChapterIndex by mutableIntStateOf(0)

    var loading by mutableStateOf(false)

    var scraperOp : MainScraper? = null
    var displayData by mutableStateOf(AppData())
    var errorScreen by mutableStateOf(false)
    private var errorInLoading by mutableStateOf(false)
    var subbedOrNot by mutableStateOf("Subscribe  ")
    var fullScreenHeading by mutableStateOf("Popular Today")
    var subbedIcon by mutableIntStateOf(R.drawable.add_icon)
    private var updatingSubscriptions by mutableStateOf(false)
    var fullComicList = mutableStateListOf<ComicsData>()
    var searchResult = mutableStateListOf<ComicsData>()
    var loadingSearchResult by mutableStateOf(false)
    var noResult by mutableStateOf(false)
    var updateAvailable by mutableStateOf(false)
    var searchQuery by mutableStateOf("")
    var showSearchResult by mutableStateOf("")

    fun search(query:String, byGenre:Boolean = false){
        viewModelScope.launch(Dispatchers.IO) {
            loadingSearchResult = true
            searchResult.clear()
            scraperOp?.searchScraper(query, byGenre)?.let { searchResult.addAll(it) }
            noResult = searchResult.isEmpty()
            loadingSearchResult = false
        }
    }

    fun checkAppVersion(){
        viewModelScope.launch(Dispatchers.IO) {
            updateAvailable = scraperOp?.isUpdateAvailable() == true

        }
    }

    private fun updateSubscriptions(){
        viewModelScope.launch(Dispatchers.IO) {
            updatingSubscriptions = true
            val newList = mutableStateListOf<ComicsData>()
            displayData.subscribedComicsData.forEach { comic ->
                scraperOp?.comicInfoScraper(comic)?.let { result ->
                    result.data?.let {
                        newList.add(it)
                    }
                }
            }
            if(newList.size==displayData.subscribedComicsData.size){
                displayData.subscribedComicsData.forEach { Log.d("SubscriptionsUpdate", it.name) }
                displayData.subscribedComicsData = newList
                scraperOp?.storage?.storeSubscriptions(displayData)
            }
            updatingSubscriptions = false
        }
    }

    fun subscribeButtonClicked(){
        if (displayData.subscribedComicsHash.contains(displayData.currentComic.link)) {
            scraperOp?.storage?.unSubscribe(displayData.currentComic)
            subbedIcon = R.drawable.add_icon
            subbedOrNot = "Subscribe  "
        } else {
            scraperOp?.storage?.subscribe(displayData.currentComic)
            subbedIcon = R.drawable.done_icon
            subbedOrNot = "Subscribed  "
        }
        scraperOp?.storage?.getComicsData()?.let {
            displayData.subscribedComicsData = it.subscribedComicsData
            displayData.subscribedComicsHash = it.subscribedComicsHash
        }
    }

    fun getAsura(show: Int, mainNavController: NavController?){
        viewModelScope.launch {
            currentChapterIndex = show
            loading = true
            scraperOp?.chapterScraper(displayData.currentComic, show)?.let {
                when(it){
                    is Resource.Success -> {
                        displayData.currentComic = it.data ?: ComicsData()
                        Log.d("ScraperResult" , it.toString())
                        mainNavController?.navigate(Screen.ChapterUi.route){
//                            popUpTo(mainNavController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                        loading = false
                    }
                    is Resource.Error -> {
                        errorInLoading = true
                        loading = false
                    }
                    is Resource.Loading -> {
                        showSplashScreen = it.isLoading
                    }
                }
            }
        }
    }

    fun getComicInfo(comicToSearch: ComicsData, mainNavController: NavController){
        viewModelScope.launch {
            loading = true
            scraperOp?.comicInfoScraper(comicToSearch)?.let {
                when(it){
                    is Resource.Success -> {
                        displayData.currentComic = it.data ?: ComicsData()
                        if(displayData.subscribedComicsHash.contains(displayData.currentComic.link)){
                            subbedIcon = R.drawable.done_icon
                            subbedOrNot = "Subscribed  "
                        } else {
                            subbedIcon = R.drawable.add_icon
                            subbedOrNot = "Subscribe  "
                        }
                        Log.d("ScraperResultinfo" , it.data.toString())
                        mainNavController.navigate(Screen.ComicInfo.route){
//                            popUpTo(mainNavController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                        loading = false
                    }
                    is Resource.Error -> {
                        errorInLoading = true
                        loading = false
                    }
                    is Resource.Loading -> {
                        showSplashScreen = it.isLoading
                    }
                }
            }
        }
    }
    fun popularToday() {
        viewModelScope.launch {
            delay(5000)
            showSplashScreen = false
            updateSubscriptions()
        }
        viewModelScope.launch(Dispatchers.IO) {
            showSplashScreen = true
            scraperOp?.initialScraper()?.collect { result ->
                when(result) {
                    is Resource.Success -> {
                        displayData = result.data ?: displayData
                        Log.d("ScraperResult" , result.data?.popularTodayData.toString())
                    }
                    is Resource.Error -> {
                        errorScreen = true
                    }
                    is Resource.Loading -> {
                        showSplashScreen = result.isLoading
                    }
                }
            }
        }
    }
}

