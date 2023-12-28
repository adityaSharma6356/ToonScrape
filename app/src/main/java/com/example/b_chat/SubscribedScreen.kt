package com.example.b_chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import java.lang.Integer.min

@Composable
fun SubscribesScreen(
    mainViewModel: MainViewModel,
    api: Scrapper,
    mainNavController: NavHostController
){
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Text(
            text = "Subscriptions",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(20.dp, 50.dp, 0.dp, 5.dp)
                .align(Alignment.Start),
            fontFamily = FontFamily.SansSerif,
        )

        mainViewModel.subscribedComics.forEachIndexed { index, item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clickable {
                    mainViewModel.getComicInfo(
                        api,
                        item.title,
                        item.image,
                        item.link,
                        mainNavController
                    )
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = "image",
                    modifier = Modifier
                        .padding(20.dp, 20.dp, 10.dp, 20.dp)
                        .width(100.dp),
                    filterQuality = FilterQuality.None,
                    contentScale = ContentScale.FillHeight
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        color = Color(255, 255, 255, 255),
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(5.dp, 0.dp, 15.dp, 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                    val count = min(item.chapters.size, 2)
                    for(i in 0..count) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = item.chapters[i].name,
                                color = Color(255, 255, 255, 153),
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                text = item.chapters[i].time,
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
