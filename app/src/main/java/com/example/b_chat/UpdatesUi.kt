package com.example.b_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun UpdatesUi(mainViewModel: MainViewModel, mainNavController: NavHostController, api: Scrapper){
    Column(modifier = Modifier.fillMaxWidth().background(MainTheme.background), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Latest Updates",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(20.dp, 20.dp, 0.dp, 0.dp).align(Alignment.Start),
            fontFamily = FontFamily.SansSerif,
        )
        val context = LocalContext.current
        val size by remember { mutableStateOf(minOf(10, mainViewModel.latestRelease.size)) }
        mainViewModel.latestRelease.forEachIndexed { i, _ ->
            if(i<=10){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clickable {
                        mainViewModel.getComicInfo(
                            api,
                            mainViewModel.latestRelease[i].title,
                            mainViewModel.latestRelease[i].image,
                            mainViewModel.latestRelease[i].link,
                            mainNavController,
                            context
                        )
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = mainViewModel.latestRelease[i].image,
                        contentDescription = "image",
                        modifier = Modifier
                            .padding(20.dp, 20.dp, 10.dp, 20.dp)
                            .width(100.dp),
                        filterQuality = FilterQuality.None,
                        contentScale = ContentScale.FillHeight
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mainViewModel.latestRelease[i].title,
                            color = Color(255, 255, 255, 255),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp, 0.dp, 15.dp, 5.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                        mainViewModel.latestRelease[i].newChapters.forEach { chapter ->
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = chapter.name,
                                    color = Color(255, 255, 255, 153),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    text = chapter.time,
                                    color = Color(255, 255, 255, 153),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 10.dp),
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}