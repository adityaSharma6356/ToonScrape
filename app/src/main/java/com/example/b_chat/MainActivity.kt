package com.example.b_chat

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
        setContent {
            BChatTheme {
                Box(modifier = Modifier.background(Color(9, 9, 9, 255))) {
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
                            backgroundColor = Color.Transparent,
                            onDismissRequest = { /*TODO*/ },buttons = {})
                    }
                    val mainNavController = rememberNavController()
                    NavHost(navController = mainNavController, startDestination = Screen.Main.route){
                        composable(Screen.Main.route){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val windowInsetsController = window.insetsController
                                windowInsetsController?.show(WindowInsetsCompat.Type.navigationBars())
                            }

                            if(mainViewModel.topList.isNotEmpty()){
                                Image(
                                    painter = rememberAsyncImagePainter(model = mainViewModel.topList[0].image),
                                    contentDescription = "image",
                                    modifier = Modifier
                                        .blur(10.dp)
                                        .fillMaxWidth()
                                        .height(350.dp),
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tint(Color(0, 0, 0, 144), blendMode = BlendMode.Darken)
                                )
                            }
                            Column(modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                            ) {
                                if(mainViewModel.topList.isNotEmpty()){
                                    TopUi(mainViewModel, mainNavController, api)
                                }
                                PopularTodayUi(mainViewModel, mainNavController, api)
                                UpdatesUi(mainViewModel,mainNavController, api)
                            }
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

                            ReadingUi(mainViewModel = mainViewModel, window)
                        }
                    }
                }
            }
        }
    }
}


interface Scrapper {

    @GET
    suspend fun getAsura(@Url url: String): Response<String>
}











