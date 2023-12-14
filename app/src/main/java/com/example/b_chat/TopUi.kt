package com.example.b_chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopUi(mainViewModel: MainViewModel, mainNavController: NavHostController, api: Scrapper){
    Box(modifier = Modifier
//        .padding(top = 50.dp)
        .fillMaxWidth()
        .background(Color.Transparent)
        .height(270.dp)
        .clickable {
            mainViewModel.getComicInfo(api,mainViewModel.topList[0].title,mainViewModel.topList[0].image,mainViewModel.topList[0].link ,mainNavController)

        }) {
        val painter = rememberAsyncImagePainter(model = mainViewModel.topList[0].image)
        Image(
            painter = painter,
            contentDescription = "image",
            modifier = Modifier
                .padding(0.dp, 65.dp, 30.dp, 0.dp)
                .width(120.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.FillWidth,
        )
        Column(
            Modifier
                .fillMaxHeight()
                .width(250.dp)) {

            Text(
                text = "  Trending  ",
                color = Color.Cyan,
                fontSize = 15.sp,
                maxLines = 1,
                modifier = Modifier.padding(20.dp, 50.dp, 10.dp, 0.dp).border(width=1.dp , color = Color.Cyan, shape = RoundedCornerShape(50), ),
            )
            Text(
                text = mainViewModel.topList[0].title,
                color = Color.White,
                fontSize = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(20.dp, 5.dp, 10.dp, 0.dp),
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
                    text = mainViewModel.topList[0].score.toString(),
                    color = Color(255, 232, 170, 255),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(3.dp, 0.dp, 10.dp, 0.dp),
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = mainViewModel.topList[0].genre,
                    color = Color(179, 179, 179, 255),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                    fontFamily = FontFamily.SansSerif
                )
            }

            Text(
                text = mainViewModel.topList[0].summary,
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












