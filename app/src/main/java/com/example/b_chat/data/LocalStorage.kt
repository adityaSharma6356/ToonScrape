package com.example.b_chat.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorage(
    private val context:Context,
    private val store: SharedStorage = SharedStorage()
) {
    fun getComicsData(): AppData = store.getData(context)

    fun storePopularToday(data: AppData) {
        val temp = store.getData(context)
        temp.popularTodayData.clear()
        temp.popularTodayData.addAll(data.popularTodayData)
        store.saveData(context, temp)
    }
    fun storeSubscriptions(data: AppData) {
        val temp = store.getData(context)
        temp.subscribedComicsData.clear()
        temp.subscribedComicsData.addAll(data.subscribedComicsData)
        store.saveData(context, temp)
    }
    fun storeLatestUpdates(data: AppData) {
        val temp = store.getData(context)
        temp.latestUpdatesData.clear()
        temp.latestUpdatesData.addAll(data.latestUpdatesData)
        store.saveData(context, temp)
    }
    fun storeTrendingData(data: AppData) {
        val temp = store.getData(context)
        temp.trendingComicsData.clear()
        temp.trendingComicsData.addAll(data.trendingComicsData)
        store.saveData(context, temp)
    }
    fun readChapter(chapterLink:String) {
        val temp = store.getData(context)
        temp.chaptersRead.remove(chapterLink)
        temp.chaptersRead.add(chapterLink)
        store.saveData(context, temp)
    }

    fun subscribe(comic:ComicsData){
        val temp = store.getData(context)
        temp.subscribedComicsHash.remove(comic.link)
        temp.subscribedComicsHash.add(comic.link)
        temp.subscribedComicsData.remove(comic)
        temp.subscribedComicsData.add(comic)
        store.saveData(context, temp)
    }

    fun unSubscribe(comic: ComicsData){
        val temp = store.getData(context)
        temp.subscribedComicsHash.remove(comic.link)
        temp.subscribedComicsData.remove(comic)
        store.saveData(context, temp)
    }

}


class SharedStorage{
    private val key1 = "toonscrape_shared_storage"
    private val key2 = "toonscrape_shared_storage_data"
    fun saveData(context: Context, data:AppData){
        val editor = context.getSharedPreferences(key1, Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(data)
        editor.putString(key2, json)
        editor.apply()
    }

    fun getData(context: Context): AppData {
        val gson = Gson()
        val json = context.getSharedPreferences(key1, Context.MODE_PRIVATE).getString(key2, null)
        val type = object : TypeToken<AppData>() {}.type
        return gson.fromJson(json, type) ?: AppData()
    }
}