package com.example.b_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun PopularTodayUi(
    mainViewModel: MainViewModel,
    mainNavController: NavHostController,
    api: Scrapper
){
    Box(modifier = Modifier.background(Color(9,9,9))){
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color(24, 24, 24, 255), RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp))
        ) {
            Text(
                text = "Popular Today",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(10.dp, 15.dp, 0.dp, 0.dp),
                fontFamily = FontFamily.SansSerif
            )
            val width = LocalConfiguration.current.screenWidthDp/3
            val height = width*1.5
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    rememberScrollState()
                )){
                mainViewModel.popularToday.forEachIndexed { it, _ ->
                    Column(modifier = Modifier
                        .padding(0.dp, 5.dp, 0.dp, 15.dp)
                        .width((width).dp)
                        .clickable {
                            mainViewModel.getComicInfo(api,mainViewModel.popularToday[it].title,mainViewModel.popularToday[it].image,mainViewModel.popularToday[it].link,mainNavController )

                        },
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        AsyncImage(
                            model = mainViewModel.popularToday[it].image,
                            contentDescription = "image",
                            modifier = Modifier
                                .height(height.dp)
                                .padding(10.dp),
                            filterQuality = FilterQuality.None,
                            contentScale = ContentScale.FillHeight
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = mainViewModel.popularToday[it].latestChapter,
                                color = Color(207, 178, 255, 255),
                                modifier = Modifier
                                    .padding(10.dp, 3.dp),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = mainViewModel.popularToday[it].score.toString(),
                                color = Color(255, 236, 180, 255),
                                modifier = Modifier
                                    .padding(10.dp, 3.dp),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 13.sp,
                                maxLines = 1,
                            )

                        }

                        Text(
                            text = mainViewModel.popularToday[it].title,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.sp,
//                        textAlign = TextAlign.Center
                        )

                    }
                }
            }
        }
    }

}

