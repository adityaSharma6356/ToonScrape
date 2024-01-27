package com.example.b_chat

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp

sealed class Screen(val route: String, val label: String, @DrawableRes val icon: Int, @DrawableRes val offIcon: Int) {
    object Main : Screen("main", "main", R.drawable.menu_icon, R.drawable.menu_icon)
    object ComicInfo : Screen("comic_info", "Info", R.drawable.menu_icon, R.drawable.menu_icon)
    object ChapterUi : Screen("chapter_reading_screen", "Chapter", R.drawable.menu_icon, R.drawable.menu_icon)
    object Home : Screen("home", "Home", R.drawable.home, R.drawable.home)
    object Search : Screen("search", "Search", R.drawable.search, R.drawable.search)
    object Subscribed : Screen("subscribed", "Subscriptions", R.drawable.subscriptions_icon, R.drawable.subscriptions_icon)
    object FullListScreen : Screen("main_full_list_screen", "Full List Screen", R.drawable.subscriptions_icon, R.drawable.subscriptions_icon)

}

class MainTheme {
    companion object {
        val background = Color(25, 26, 31, 255)
        val darkBackground = Color(13, 14, 17, 255)
        val lightBackground = Color(35, 37, 44, 255)
        val mainColor = Color(106, 139, 253, 255)
        val navColor = Color(217, 35, 47, 255)
    }
}

sealed class Resource<out T>(val status : Status , val data: T?, val message : String?) {
    class Success<T>(data : T?, message: String?) : Resource<T>(Status.SUCCESS, data, null)
    class Error<T>(message: String?, data: T?) : Resource<T>(Status.ERROR, data, message)
    class Loading<T>(data: T?, val isLoading: Boolean = true) : Resource<T>(Status.LOADING, null, null)
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp

//object SystemBarsCompat {
//    private val api: Api =
//        when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> Api31()
//            Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> Api30()
//            else -> Api()
//        }
//
//    fun hideSystemBars(window: Window, view: View, isImmersiveStickyMode: Boolean = false) =
//        api.hideSystemBars(window, view, isImmersiveStickyMode)
//
//    fun showSystemBars(window: Window, view: View) = api.showSystemBars(window, view)
//
//    fun areSystemBarsHidden(view: View): Boolean = api.areSystemBarsHidden(view)
//
//    @Suppress("DEPRECATION")
//    private open class Api {
//        open fun hideSystemBars(window: Window, view: View, isImmersiveStickyMode: Boolean = false) {
//            val flags = View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//
//            view.systemUiVisibility = if (isImmersiveStickyMode) {
//                flags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            } else {
//                flags or
//                        View.SYSTEM_UI_FLAG_IMMERSIVE or
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            }
//        }
//
//        open fun showSystemBars(window: Window, view: View) {
//            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        }
//
//        open fun areSystemBarsHidden(view: View) = view.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION != 0
//    }
//
//    @Suppress("DEPRECATION")
//    @RequiresApi(Build.VERSION_CODES.R)
//    private open class Api30 : Api() {
//
//        open val defaultSystemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
//
//        override fun hideSystemBars(window: Window, view: View, isImmersiveStickyMode: Boolean) {
//            window.setDecorFitsSystemWindows(false)
//            view.windowInsetsController?.let {
//                it.systemBarsBehavior =
//                    if (isImmersiveStickyMode) WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//                    else defaultSystemBarsBehavior
//                it.hide(WindowInsets.Type.systemBars())
//            }
//        }
//
//        override fun showSystemBars(window: Window, view: View) {
//            window.setDecorFitsSystemWindows(false)
//            view.windowInsetsController?.show(WindowInsets.Type.systemBars())
//        }
//
//        override fun areSystemBarsHidden(view: View) = !view.rootWindowInsets.isVisible(WindowInsets.Type.navigationBars())
//    }
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private class Api31 : Api30() {
//        override val defaultSystemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
//    }
//}