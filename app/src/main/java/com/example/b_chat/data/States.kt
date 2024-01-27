package com.example.b_chat.data

data class AppData(
    var currentComic : ComicsData = ComicsData(),
    var trendingComicsData: MutableList<ComicsData> = mutableListOf(),
    var popularTodayData: MutableList<ComicsData> = mutableListOf(),
    var latestUpdatesData: MutableList<ComicsData> = mutableListOf(),
    var subscribedComicsData: MutableList<ComicsData> = mutableListOf(),
    var subscribedComicsHash: HashSet<String> = hashSetOf(),
    var chaptersRead: HashSet<String> = hashSetOf()
)

data class ComicsData(
    val link: String = "",
    val image: String = "",
    val name: String = "",
    val author: String = "",
    val art: String = "",
    val postedOn: String = "",
    val subscribed: Boolean = false,
    val rating: String = "",
    val genre: List<String> = listOf(),
    val synopsis: String = "",
    val chaptersData: List<ChapterData> = listOf(),
    val alternativeTitle:String = "",
    val fbLink:String = "",
    val twLink:String = "",
    val waLink:String = "",
    val piLink:String = "",
    val followedBy:String = "",
    val type:String = ""
)

data class ChapterData(
    val name: String = "",
    val link: String = "",
    val time: String = "",
    var read: Boolean = false,
    var pages: List<String> = listOf()
)















