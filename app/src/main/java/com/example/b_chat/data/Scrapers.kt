package com.example.b_chat.data

import android.util.Log
import com.example.b_chat.Resource
import com.example.b_chat.Scrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import retrofit2.Response
import java.util.concurrent.CancellationException

class MainScraper(
    val storage: LocalStorage,
    private val api: Scrapper,
    private val apiSF: Scrapper,
) {

    private val app_version = 0.2

    suspend fun searchScraper(query:String, byGenre:Boolean = false): List<ComicsData> {
        val scraperLink = if(byGenre) {
            query.substring(22)
        } else {
            "?s="+query.addPlusForSpaces()
        }
        val response = api.getAsura(scraperLink)
        if(response.isSuccessful && response.body()!=null){
            val data = Jsoup.parse(response.body())
            val list = data.select("div.listupd")
            val returnList = mutableListOf<ComicsData>()
            list.select("div.bs").forEach {  comic ->
                val link = comic.select("a").attr("href")
                val type = comic.select("span.type").text()
                val img = comic.select("img").attr("src")
                val name = comic.select("div.tt").text()
                val rating = comic.select("div.numscore").text()
                val chapter = comic.select("div.epxs").text()
                returnList.add(
                    ComicsData(
                    link = link,
                    name = name,
                    image = img,
                    type = type,
                    rating = rating,
                    chaptersData = listOf(ChapterData(name = chapter))
                )
                )
            }
            Log.d("searchScraper", returnList.toString())
            if(returnList.size==1 && returnList[0].link.isBlank()){
                return listOf()
            }
            return returnList
        }  else {
            return listOf()
        }
    }
    suspend fun chapterScraper(comic: ComicsData, chapterIndex: Int): Resource<ComicsData> {
        val current = comic.chaptersData[chapterIndex]
        val fLink = current.link.substring(22)
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
                images.add(
                    link
                )
            }
            images.remove(images.first())
            storage.readChapter(current.link)
            comic.chaptersData[chapterIndex].read = true
            comic.chaptersData[chapterIndex].pages = images
            return Resource.Success(comic, "Success")
        } else {
            return Resource.Error("API Error", null)
        }
    }
    suspend fun comicInfoScraper(comicToSearch: ComicsData): Resource<ComicsData> {
        val link = comicToSearch.link
        val dataToCheck = storage.getComicsData()
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
            val genre = mutableListOf<String>()
            data.select(".mgen").forEach { genr ->
                genre.add(genr.text())
            }
            val foll = data.select(".bookmark").attr("data-id")
            val chaptersL = mutableListOf<ChapterData>()
            data.select(".clstyle").select("div.chbox").forEach { chapter ->
                val linknn = chapter.select("a").attr("href")
                val titlenn = chapter.select("span.chapternum").text()
                val datenn = chapter.select("span.chapterdate").text()
                chaptersL.add(
                    ChapterData(
                        name = titlenn,
                        link = linknn,
                        time = datenn,
                        read = dataToCheck.chaptersRead.contains(linknn)
                    )
                )
            }
            val comicInfo = ComicsData(
                name = comicToSearch.name,
                image = comicToSearch.image,
                rating = rating,
                alternativeTitle = altTitle,
                synopsis = synopsis,
                fbLink = fb,
                waLink = wa,
                twLink = tw,
                piLink = pn,
                author = author,
                art = artist,
                postedOn = posted,
                genre = genre,
                chaptersData = chaptersL,
                followedBy = foll,
                link = link
            )
            return Resource.Success(comicInfo, "success")
        } else {
            return Resource.Error("api error", null)
        }
    }
    suspend fun initialScraper(): Flow<Resource<AppData>>{
        return flow {
            emit(Resource.Loading(null, true))
            val dataToEmit = storage.getComicsData()

            emit(Resource.Success(dataToEmit, "From Local Storage"))
            val response:Response<String> = try {
                api.getAsura("")
            } catch (ce: CancellationException) {
                Log.e("ScraperResult", ce.message.toString())
                emit(Resource.Error("Unknown error occurred", null))
                Response.success(null)
            } catch (e: Exception) {
                Log.e("ScraperResult", e.message.toString())
                emit(Resource.Error("Unknown error occurred", null))
                Response.success(null)
            }

            if (response.isSuccessful && response.body() != null) {
                val data = Jsoup.parse(response.body())
                val slidtop = data.select("div.slidtop")
                val owlItems = slidtop.select("div.slide-item.full")
                val  topL = mutableListOf<ComicsData>()
                owlItems.forEach {
                    val image =  it.select("img").attr("src")
                    val score = it.select("span.fa.fa-star").text()
                    val title = it.select("span.ellipsis").text()
                    val link = it.select("a").attr("href")
                    val genre = mutableListOf<String>()
                    it.select("div.extra-category").forEach { genr ->
                        genre.add(genr.text())
                    }
                    val summary = it.select("div.excerpt").select("p").text()
                    if (image.isNotBlank() && title.isNotBlank() && link.isNotBlank() && score.isNotBlank()) {
                        topL.add(
                            ComicsData(
                                name = title,
                                link = link,
                                image = image,
                                rating = score,
                                genre = genre,
                                synopsis = summary
                            )
                        )
                    }
                }
                dataToEmit.trendingComicsData.clear()
                dataToEmit.trendingComicsData.addAll(topL)
                storage.storeTrendingData(dataToEmit)
                emit(Resource.Success(dataToEmit, "Updated Trending Comics"))
                emit(Resource.Loading(null, false))

                val container = data.select("div.bixbox.hothome")
                val info = container.select("div.bsx")
                val tpList = mutableListOf<ComicsData>()
                info.forEach {
                    val imaged = it.select("img")
                    val image = imaged.attr("src")
                    val score = it.select("div.numscore").text()
                    val title = it.select("a").attr("title")
                    val link = it.select("a").attr("href")
                    val latestChapter = ChapterData(name = it.select("div.epxs").text())
                    Log.d("asura1", latestChapter.toString())
                    if (image.isNotBlank() && title.isNotBlank() && link.isNotBlank() && score.isNotBlank()) {
                        tpList.add(
                            ComicsData(
                                name =  title,
                                link = link,
                                image = image,
                                rating = score,
                                chaptersData = listOf(latestChapter)
                            )
                        )
                    }
                }
                dataToEmit.popularTodayData.clear()
                dataToEmit.popularTodayData.addAll(tpList)
                storage.storePopularToday(dataToEmit)
                emit(Resource.Success(dataToEmit, "Updated Popular Today List"))

                val lcontainer = data.select("div.bixbox")
                val linfo = lcontainer.select("div.utao.styletwo")
                val ltpList = mutableListOf<ComicsData>()
                linfo.forEach {
                    val imaged = it.select("img")
                    val image = imaged.attr("src")
                    val title = it.select("a.series").attr("title")
                    val link = it.select("a.series").attr("href")
                    val chapters = mutableListOf<ChapterData>()
                    val li = it.select("li")
                    li.forEach { chapter ->
                        chapters.add(
                            ChapterData(
                                name = chapter.select("a").text(),
                                link = chapter.select("a").attr("href"),
                                time = chapter.select("span").text()
                            )
                        )
                    }
                    if(image.isNotBlank() && title.isNotBlank() && link.isNotBlank() && chapters.isNotEmpty()){
                        ltpList.add(
                            ComicsData(
                                name = title,
                                link = link,
                                image = image,
                                chaptersData = chapters
                            )
                        )
                    }
                }
                dataToEmit.latestUpdatesData.clear()
                dataToEmit.latestUpdatesData.addAll(ltpList)
                storage.storeLatestUpdates(dataToEmit)
                emit(Resource.Success(dataToEmit, "Updated Latest Release List"))

                emit(Resource.Loading(null, false))
            }
        }
    }

    suspend fun isUpdateAvailable():Boolean{
        val response = apiSF.getAsura("/toonscrape_version")
//        Log.d("magictext", response.body().toString())

        return if (response.isSuccessful && response.body() != null) {
            val latestVersion = response.body().toString().toDouble()
            Log.d("magictext", latestVersion.toString())
            latestVersion>app_version
    //            Log.d("magictext", response.toString())
        } else {
            false
        }
    }
}

fun String.addPlusForSpaces():String{
    return this.replace(" ", "+")
}