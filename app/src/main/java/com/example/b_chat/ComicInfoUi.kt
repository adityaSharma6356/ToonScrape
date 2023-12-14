package com.example.b_chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun ComicInfoUi(mainViewModel: MainViewModel, api: Scrapper, mainNavController: NavHostController){
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(9, 9, 9))) {
            Image(
                painter = rememberAsyncImagePainter(model = mainViewModel.comicInfo.image),
                contentDescription = "cover",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .blur(10.dp),
                colorFilter = ColorFilter.tint(Color(0, 0, 0, 122), blendMode = BlendMode.Darken)
            )
            Text(
                text = mainViewModel.comicInfo.title,
                color = Color(255, 232, 170, 255),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(230.dp, 260.dp, 10.dp, 0.dp),
                fontFamily = FontFamily.SansSerif,
                maxLines = 4
            )
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(modifier = Modifier
                        .padding(30.dp, 60.dp, 20.dp, 0.dp)
                        .size(180.dp, 270.dp), elevation = 10.dp, shape = RoundedCornerShape(20.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = mainViewModel.comicInfo.image, filterQuality = FilterQuality.High),
                            contentDescription = "cover",
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(modifier = Modifier
                        .padding(top = 70.dp)
                        .weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                tint = Color.Yellow,
                                painter = painterResource(id = R.drawable.star),
                                contentDescription = "star",
                                modifier = Modifier
                                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                                    .size(15.dp)
                            )
                            Text(
                                text = mainViewModel.comicInfo.rating,
                                color = Color(255, 232, 170, 255),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(3.dp, 0.dp, 10.dp, 0.dp),
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color(
                                    199,
                                    199,
                                    199,
                                    255
                                )
                                )) {
                                    append("Author: ")
                                }
                                withStyle(style = SpanStyle(color = Color.White)) {
                                    append(mainViewModel.comicInfo.author)
                                }
                            },
                            color = Color(255, 255, 255, 255),
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color(
                                    199,
                                    199,
                                    199,
                                    255
                                )
                                )) {
                                    append("Art by: ")
                                }
                                withStyle(style = SpanStyle(color = Color.White)) {
                                    append(mainViewModel.comicInfo.artist)
                                }
                            },
                            color = Color(255, 255, 255, 255),
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color(
                                    199,
                                    199,
                                    199,
                                    255
                                )
                                )) {
                                    append("Posted On: ")
                                }
                                withStyle(style = SpanStyle(color = Color.White)) {
                                    append(mainViewModel.comicInfo.postedOn)
                                }
                            },
                            fontSize = 14.sp,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.White
                        )) {
                            append("Genres\n")
                        }
                        withStyle(style = SpanStyle(color = Color(189, 189, 189, 255))) {
                            append(mainViewModel.comicInfo.genre)
                        }
                    },
                    fontSize = 15.sp,
                    modifier = Modifier.padding(20.dp, 30.dp, 20.dp, 0.dp),
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = Color.White
                        )) {
                            append("Synopsis\n")
                        }
                        withStyle(style = SpanStyle(color = Color(189, 189, 189, 255))) {
                            append(mainViewModel.comicInfo.synopsis)
                        }
                    },
                    fontSize = 15.sp,
                    modifier = Modifier.padding(20.dp, 30.dp, 20.dp, 0.dp),
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.SansSerif
                )
                Row(modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    val width = (LocalConfiguration.current.screenWidthDp/2)-40
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(width.dp)
                            .height(50.dp)
                            .background(Color(31, 31, 31, 255), RoundedCornerShape(10.dp))
                            .clickable {
                                mainViewModel.getAsura(
                                    api,
                                    mainViewModel.comicInfo.chapters.last().link,
                                    mainNavController
                                )
                            }
                    ) {
                        Text(
                            text = "Read " + mainViewModel.comicInfo.chapters.last().name,
                            color = Color(206, 206, 206, 255),
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(width.dp)
                            .height(50.dp)
                            .background(Color(31, 31, 31, 255), RoundedCornerShape(10.dp))
                            .padding(5.dp)
                            .clickable {
                                mainViewModel.getAsura(
                                    api,
                                    mainViewModel.comicInfo.chapters.first().link,
                                    mainNavController
                                )
                            }
                    ) {
                        Text(
                            text = "Read " + mainViewModel.comicInfo.chapters.first().name,
                            color = Color(206, 206, 206, 255),
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 17.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .padding(10.dp, 20.dp)
                        .background(Color(31, 31, 31, 255), RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                   ) {
                    mainViewModel.comicInfo.chapters.forEach {
                        Column(modifier = Modifier.fillMaxWidth().clickable {
                            mainViewModel.getAsura(
                            api,
                            it.link,
                            mainNavController
                        ) }) {
                            Text(
                                text = it.name,
                                color = Color(206, 206, 206, 255),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                                modifier = Modifier
                                    .padding(10.dp, 10.dp, 10.dp, 5.dp)
                            )
                            Text(
                                text = it.time,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.SansSerif,
                                modifier = Modifier
                                    .padding(10.dp, 0.dp, 10.dp, 10.dp)
                            )
                            Spacer(modifier = Modifier.align(CenterHorizontally).height(1.dp).fillMaxWidth().background(Color(9,9,9)
                            ))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }
}














