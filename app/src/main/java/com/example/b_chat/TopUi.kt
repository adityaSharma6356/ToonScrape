package com.example.b_chat

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopUi(mainViewModel: MainViewModel, mainNavController: NavHostController, api: Scrapper){
    val pagerState = rememberPagerState(0, 0f, pageCount = {
        mainViewModel.topList.size
    })
    LaunchedEffect(key1 = "Run"){
        var switch = true
        while(true){
            delay(5000)
            switch = if(pagerState.canScrollForward && switch){
                pagerState.animateScrollToPage(pagerState.currentPage+1)
                true
            } else if(pagerState.canScrollBackward){
                pagerState.animateScrollToPage(pagerState.currentPage-1)
                false
            } else {
                true
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth()) {

        val context = LocalContext.current
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier
//        .padding(top = 50.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(280.dp)
                .clickable {
                    mainViewModel.getComicInfo(
                        api,
                        mainViewModel.topList[it].title,
                        mainViewModel.topList[it].image,
                        mainViewModel.topList[it].link,
                        mainNavController,
                        context
                    )
                }) {
                val painter = rememberAsyncImagePainter(model = mainViewModel.topList[it].image)
                Image(
                    painter = painter,
                    contentDescription = "image",
                    modifier = Modifier
                        .blur(10.dp)
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(
                        Color(0, 0, 0, 144),
                        blendMode = BlendMode.Darken
                    )
                )
                Image(
                    painter = painter,
                    contentDescription = "image",
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 30.dp, 0.dp)
                        .width(120.dp)
                        .align(Alignment.CenterEnd),
                    contentScale = ContentScale.FillWidth,
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(250.dp)
                ) {
                    Text(
                        text = mainViewModel.topList[it].title,
                        color = Color.White,
                        fontSize = 20.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(20.dp, 0.dp, 10.dp, 0.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            tint = Color.Yellow,
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "star",
                            modifier = Modifier
                                .padding(20.dp, 0.dp, 0.dp, 0.dp)
                                .size(15.dp)
                        )
                        Text(
                            text = mainViewModel.topList[it].score.toString(),
                            color = Color(255, 232, 170, 255),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(3.dp, 0.dp, 10.dp, 0.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = mainViewModel.topList[it].genre,
                            color = Color(179, 179, 179, 255),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                    }

                    Text(
                        text = mainViewModel.topList[it].summary,
                        color = Color(207, 207, 207, 255),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(20.dp, 0.dp, 10.dp, 10.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }

            }
        }
        Row(modifier = Modifier
            .fillMaxWidth(0.3f)
            .padding(bottom = 10.dp)
            .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
            ) {
            for(i in 0 until mainViewModel.topList.size) {
                val width by remember { derivedStateOf { if(pagerState.currentPage==i) 10.dp else 5.dp }}
                Spacer(modifier = Modifier.padding(0.dp).animateContentSize().height(5.dp).width(width).background(Color(
                    218,
                    218,
                    218,
                    255
                ),
                    RoundedCornerShape(50)
                ))
            }
        }
    }
}












