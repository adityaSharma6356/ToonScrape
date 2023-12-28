package com.example.b_chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.b_chat.ui.theme.BChatTheme
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window , false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asuratoon.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(Scrapper::class.java)

        val mainViewModel = viewModels<MainViewModel>().value
        mainViewModel.popularToday(api)
//        mainViewModel.subscribedComics.addAll(mainViewModel.getList(this))
//        mainViewModel.subscribedComics.forEach { Log.d("subslog", it.title) }
        setContent {
            BChatTheme {
                Box(modifier = Modifier.background(Color(9, 9, 9, 255))) {
                    val navController = rememberNavController()
                    if(mainViewModel.loading){
                        AlertDialog(
                            text = {
                                Image(painter = painterResource(id = R.drawable.loading), contentDescription = "loading", modifier = Modifier
                                    .clip(
                                        CircleShape
                                    )
                                    .size(200.dp), contentScale = ContentScale.Crop)

                            },
                            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false),
                            containerColor = Color.Transparent,
                            confirmButton = {},
                            onDismissRequest = {})
                    }
                    val mainNavController = rememberNavController()
                    NavHost(navController = mainNavController, startDestination = Screen.Main.route){
                        composable(Screen.Main.route){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val windowInsetsController = window.insetsController
                                windowInsetsController?.show(WindowInsetsCompat.Type.navigationBars())
                            }

                            NavHost(navController = navController, startDestination = Screen.Home.route){
                                composable(Screen.Home.route){
                                    val state = rememberScrollState()
                                    val showName by remember { derivedStateOf { state.value>0 } }
                                    AnimatedVisibility(modifier = Modifier.fillMaxWidth().height(90.dp).zIndex(1f),visible = showName, enter = fadeIn(), exit = fadeOut(),
                                    ) {
                                        Box(modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(20, 20, 20, 255)),
                                            contentAlignment = Alignment.BottomStart
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.app_name),
                                                fontSize = 20.sp,
                                                color = Color.White,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(10.dp)
                                            )
//                                            Spacer(
//                                                modifier = Modifier
//                                                    .fillMaxWidth()
//                                                    .height(0.5.dp)
//                                                    .background(
//                                                        Color.White
//                                                    )
//                                            )
                                        }
                                    }
                                    Column(modifier = Modifier
                                        .background(Color.Transparent)
                                        .fillMaxSize()
                                        .verticalScroll(state)
                                    ) {
                                        if(mainViewModel.topList.isNotEmpty()){
                                            TopUi(mainViewModel, mainNavController, api)
                                        }
                                        PopularTodayUi(mainViewModel, mainNavController, api)
                                        UpdatesUi(mainViewModel,mainNavController, api)
                                        Spacer(modifier = Modifier.height(90.dp))
                                    }
                                }
                                composable(Screen.Search.route){
                                    SearchScreen()
                                }
                                composable(Screen.Subscribed.route){
                                    SubscribesScreen(mainViewModel, api, mainNavController)
                                }
                            }
                            BottomBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
                        }
                        composable(Screen.ComicInfo.route){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val windowInsetsController = window.insetsController
                                windowInsetsController?.show(WindowInsetsCompat.Type.navigationBars())
                            }
                            ComicInfoUi(mainViewModel = mainViewModel, api, mainNavController)
                        }
                        composable(Screen.ChapterUi.route){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val windowInsetsController = window.insetsController
                                windowInsetsController?.let {
                                    it.hide(WindowInsetsCompat.Type.navigationBars())
                                    it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                                }
                            }
                            ReadingUi(mainViewModel = mainViewModel, api)
                        }
                    }
                    //bottom nav

                }
            }
        }
    }
}


interface Scrapper {

    @GET
    suspend fun getAsura(@Url url: String): Response<String>
}











