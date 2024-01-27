package com.example.b_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.b_chat.data.ComicsData

@Composable
fun PopularTodayUi(
    list: List<ComicsData>,
    onClick: (ComicsData) -> Unit,
    onClickMore: () -> Unit
){
    val width = LocalConfiguration.current.screenWidthDp/3
    val height = width*1.5
    Box(modifier = Modifier.background(MainTheme.background)){
        Column(modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height.dp)
            .background(MainTheme.background)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Popular Today",
                    color = Color.White,
                    fontSize = 19.nonScaledSp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(20.dp, 20.dp, 0.dp, 10.dp),
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "See all",
                    color = MainTheme.navColor,
                    fontSize = 15.nonScaledSp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(0.dp, 20.dp, 20.dp, 10.dp).clickable { onClickMore() },
                    fontFamily = FontFamily.SansSerif
                )
            }


            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)){
                list.forEachIndexed { it, _ ->
                    Column(modifier = Modifier
                        .padding(0.dp, 5.dp, 0.dp, 15.dp)
                        .width((width).dp)
                        .clickable {
                            onClick(list[it])
                        },
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .height(height.dp)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(10.dp))) {
                            AsyncImage(
                                model = list[it].image,
                                contentDescription = "image",
                                filterQuality = FilterQuality.None,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = list[it].chaptersData[0].name,
                                color = MainTheme.mainColor,
                                modifier = Modifier
                                    .padding(10.dp, 3.dp, 0.dp, 3.dp),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 13.nonScaledSp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = list[it].rating,
                                color = Color(255, 236, 180, 255),
                                modifier = Modifier
                                    .padding(10.dp, 3.dp),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 13.nonScaledSp,
                                maxLines = 1,
                            )

                        }

                        Text(
                            text = list[it].name,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp, 10.dp, 15.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.nonScaledSp,
                            maxLines = 2,
                        )
                    }
                }
            }
        }
    }
}

