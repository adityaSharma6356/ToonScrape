package com.example.b_chat

import androidx.annotation.DrawableRes

sealed class Screen(val route: String, val label: String, @DrawableRes val icon: Int, @DrawableRes val offIcon: Int) {
    object Main : Screen("main", "main", R.drawable.menu_icon, R.drawable.menu_icon)
    object ComicInfo : Screen("comic_info", "Info", R.drawable.menu_icon, R.drawable.menu_icon)
    object ChapterUi : Screen("chapter_reading_screen", "Chapter", R.drawable.menu_icon, R.drawable.menu_icon)
    object Home : Screen("home", "Home", R.drawable.home, R.drawable.home)
    object Search : Screen("search", "Search", R.drawable.search_icon, R.drawable.search_icon)
    object Subscribed : Screen("subscribed", "Subscriptions", R.drawable.subscribe, R.drawable.subscribe)

}