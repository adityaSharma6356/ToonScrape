package com.example.b_chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainViewModel : ViewModel() {

    val chapterData = SnapshotStateList<String>()
    val latestRelease = SnapshotStateList<LatestRelease>()
    val popularToday = SnapshotStateList<PopularToday>()
    val topList = SnapshotStateList<TopContent>()
    var comicInfo by mutableStateOf(ComicInfo("","","","","","","","","","","","","","", listOf()))
    var prevLink = ""
    val prevChapter = ""
    var loading by mutableStateOf(false)



    fun getAsura(api: Scrapper, link: String, mainNavController: NavController){
        viewModelScope.launch {
            loading = true
            if(link!=prevChapter){
                prevLink =  link
                val fLink = link.substring(22)
                val response = api.getAsura(fLink)
                if(response.isSuccessful && response.body()!=null){
                    Log.d("asura", response.body().toString())
                    val data = Jsoup.parse(response.body())
                    val container = data.getElementById("readerarea")
                    val items = container.select("img")
                    Log.d("asura", items.toString())
                    val images = mutableListOf<String>()
                    items.forEach {
                        val link = it.attr("src")
                        images.add(link)
                    }
                    chapterData.clear()
                    chapterData.addAll(images)
                }
            }
            mainNavController.navigate(Screen.ChapterUi.route){
                launchSingleTop = true
            }
            loading = false

        }
    }

    fun getComicInfo(api: Scrapper, title:String, image:String, link: String, mainNavController: NavController){
        viewModelScope.launch {
            if(prevLink!=link){
                loading = true
                prevLink = link
                val fLink = link.substring(22)
                val response = api.getAsura(fLink)
                if (response.isSuccessful && response.body() != null) {
                    val data = Jsoup.parse(response.body())
                    val altTitle = data.select("div.wd-full:nth-child(3) > span:nth-child(2)").text()
                    val rating = data.select("div.rating").select("div.num").attr("content")
                    val synopsis = data.select(".entry-content.entry-content-single > p:nth-child(2)").text()
                    var author = ""
                    var artist = ""
                    var posted = ""
                    data.select("div.infox").select("div.fmed").forEach {
                        val tp = it.select("b").text()
                        if(tp=="Author"){
                            author = it.select("span").text()
                        }
                        if(tp=="Artist"){
                            artist = it.select("span").text()
                        }
                        if(tp=="Posted On"){
                            posted = it.select("span").text()
                        }
                    }
                    val fb = data.select(".fb").attr("href")
                    val wa = data.select(".twt").attr("href")
                    val tw = data.select(".wa").attr("href")
                    val pn = data.select(".pntrs").attr("href")
                    val genre = data.select(".mgen").text()
                    val foll = data.select(".bookmark").attr("data-id")
                    val chaptersL = mutableListOf<Chapter>()
                    data.select(".clstyle").select("div.chbox").forEach { chapter ->
                        val linknn = chapter.select("a").attr("href")
                        val titlenn = chapter.select("span.chapternum").text()
                        val datenn = chapter.select("span.chapterdate").text()
                        chaptersL.add(
                            Chapter(
                                titlenn,
                                linknn,
                                datenn
                            )
                        )
//                    Log.d("testasu", linknn)
                    }
                    comicInfo = ComicInfo(
                        title = title,
                        image = image,
                        rating = rating,
                        alternativeTitle = altTitle,
                        synopsis = synopsis,
                        fbLink = fb,
                        waLink = wa,
                        twLink = tw,
                        piLink = pn,
                        author = author,
                        artist = artist,
                        postedOn = posted,
                        genre = genre,
                        chapters = chaptersL,
                        followedBy = foll
                    )

                }
            }
            mainNavController.navigate(Screen.ComicInfo.route){
                popUpTo(mainNavController.graph.findStartDestination().id)
                launchSingleTop = true
            }
            loading = false
        }
    }
    fun popularToday(api: Scrapper) {
        viewModelScope.launch {
            loading = true
            val response = api.getAsura("")
            if (response.isSuccessful && response.body() != null) {
//                Log.d("asura1", response.body().toString())
                val data = Jsoup.parse(response.body())

                val slidtop = data.select("div.slidtop")
                val owlItems = slidtop.select("div.slide-item.full")
                val  topL = mutableListOf<TopContent>()
                Log.d("asura3", owlItems.toString())
                owlItems.forEach {
                    val imaged = it.select("img")
                    val image = imaged.attr("src")
                    val score = it.select("span.fa.fa-star").text()
                    val title = it.select("span.ellipsis").text()
                    val link = it.select("a").attr("href")
                    var genre = ""
                    it.select("div.extra-category").forEach { genr ->
                        genre+=genr.text()
                    }
                    val summary = it.select("div.excerpt").select("p").text()
                    if (image.isNotBlank() && title.isNotBlank() && link.isNotBlank() && score.isNotBlank()) {
                        topL.add(
                            TopContent(
                                title,
                                link,
                                image,
                                score.toFloat(),
                                genre,
                                summary
                            )
                        )
                    }
                }
                topList.clear()
                topList.addAll(topL)




                val container = data.select("div.bixbox.hothome")
                val info = container.select("div.bsx")
                val tpList = mutableListOf<PopularToday>()
                info.forEach {
                    val imaged = it.select("img")
                    val image = imaged.attr("src")
                    val score = it.select("div.numscore").text()
                    val title = it.select("a").attr("title")
                    val link = it.select("a").attr("href")
                    val latestChapter = it.select("div.epxs").text()
                    Log.d("asura1", latestChapter.toString())
                    if (image.isNotBlank() && title.isNotBlank() && link.isNotBlank() && score.isNotBlank()) {
                        tpList.add(
                            PopularToday(
                                title,
                                link,
                                image,
                                score.toFloat(),
                                latestChapter
                            )
                        )
                    }
                }
                popularToday.clear()
                popularToday.addAll(tpList)
                val lcontainer = data.select("div.bixbox")
                val linfo = lcontainer.select("div.utao.styletwo")
                val ltpList = mutableListOf<LatestRelease>()
                linfo.forEach {
                    val imaged = it.select("img")
                    val image = imaged.attr("src")
                    val title = it.select("a.series").attr("title")
                    val link = it.select("a.series").attr("href")
                    val chapters = mutableListOf<Chapter>()

                    val li = it.select("li")
                    li.forEach { chapter ->
                            chapters.add(Chapter(
                                chapter.select("a").text(),
                                chapter.select("a").attr("href"),
                                chapter.select("span").text()
                            ))
                    }
//                        Log.d("asura1", title.toString())
                    if(image.isNotBlank() && title.isNotBlank() && link.isNotBlank() && chapters.isNotEmpty()){
                        ltpList.add(
                            LatestRelease(
                                title,
                                link,
                                image,
                                chapters
                            )
                        )
//                            Log.d("asura1", ltpList.last().toString())
                    }
                }
                latestRelease.clear()
                latestRelease.addAll(ltpList)
//                    latestRelease.forEach { Log.d("asura1", it.toString()) }
            }
            loading = false
        }
    }
}

data class ComicInfo(
    val title:String,
    val image:String,
    val rating:String,
    val alternativeTitle:String,
    val synopsis:String,
    val fbLink:String,
    val waLink:String,
    val followedBy:String,
    val twLink:String,
    val piLink:String,
    val author:String,
    val artist:String,
    val postedOn:String,
    val genre:String,
    val chapters:List<Chapter>
)


data class LatestRelease(
    val title: String,
    val link: String,
    val image: String,
    val newChapters: List<Chapter>,
)

data class Chapter(
    val name: String,
    val link: String,
    val time: String
)

data class PopularToday(
    val title:String,
    val link: String,
    val image:String,
    val score: Float,
    val latestChapter:String,
)
data class TopContent(
    val title:String,
    val link: String,
    val image:String,
    val score: Float,
    val genre: String,
    val summary:String
)
