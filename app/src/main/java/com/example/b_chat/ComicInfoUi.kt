package com.example.b_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComicInfoUi(mainViewModel: MainViewModel, api: Scrapper, mainNavController: NavHostController){
    val state = rememberScrollState()
    val showName by remember { derivedStateOf { state.value>400 } }
    AnimatedVisibility(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .zIndex(1f),visible = showName, enter = fadeIn(), exit = fadeOut(),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MainTheme.background),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = mainViewModel.comicInfo.title,
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(10.dp).basicMarquee(),
                maxLines = 1
            )
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state)) {
        val context = LocalContext.current
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MainTheme.background)) {
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
                    Column() {
                        Card(modifier = Modifier
                            .padding(30.dp, 60.dp, 20.dp, 0.dp)
                            .size(180.dp, 270.dp), elevation = CardDefaults.elevatedCardElevation(10.dp), shape = RoundedCornerShape(20.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(model = mainViewModel.comicInfo.image, filterQuality = FilterQuality.High),
                                contentDescription = "cover",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        val context = LocalContext.current
                        var subText by remember { mutableStateOf("Subscribe  ") }
                        var painterIcon by remember { mutableStateOf(R.drawable.add_icon) }

                        if(mainViewModel.subscribedComics.contains(mainViewModel.comicInfo)){
                            painterIcon = R.drawable.done_icon
                            subText = "Subscribed  "
                        } else {
                            painterIcon = R.drawable.add_icon
                            subText = "Subscribe  "
                        }
                        Box(contentAlignment = Center, modifier = Modifier
                            .padding(30.dp, 20.dp, 10.dp, 10.dp)
                            .width(180.dp)
                            .background(Color(255, 232, 170, 255), RoundedCornerShape(50))
                            .clickable {
                                if (mainViewModel.subscribedComics.contains(mainViewModel.comicInfo)) {
                                    mainViewModel.unSubscribe(mainViewModel.comicInfo, context)
                                } else {
                                    mainViewModel.subscribe(mainViewModel.comicInfo, context)
                                }
                            }
                            .padding(20.dp, 5.dp)
                        ) {
                            Row {
                                Text(
                                    text = subText,
                                    color = MainTheme.background,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                )
                                Icon(painter = painterResource(id = painterIcon), contentDescription = null, modifier = Modifier.size(25.dp), tint = Color(9,9,9))
                            }
                        }
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
                    modifier = Modifier.padding(20.dp, 20.dp, 20.dp, 0.dp),
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.SansSerif
                )
                if(mainViewModel.comicInfo.synopsis.isNotBlank()){
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
                }
                Row(modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    val width = (LocalConfiguration.current.screenWidthDp/2)-40
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(width.dp)
                            .heightIn(min = 50.dp)
                            .background(MainTheme.lightBackground, RoundedCornerShape(10.dp))
                            .padding(5.dp)
                            .clickable {
                                mainViewModel.getAsura(
                                    api,
                                    mainViewModel.comicInfo.chapters.lastIndex,
                                    mainNavController,
                                    context
                                )
                            }
                    ) {
                        Text(
                            text = "Read " + mainViewModel.comicInfo.chapters.last().name,
                            color = Color(206, 206, 206, 255),
                            fontWeight = FontWeight.ExtraBold,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(width.dp)
                            .heightIn(min = 50.dp)
                            .background(MainTheme.lightBackground, RoundedCornerShape(10.dp))
                            .padding(5.dp)
                            .clickable {
                                mainViewModel.getAsura(
                                    api,
                                    0,
                                    mainNavController,
                                    context
                                )
                            }
                    ) {
                        Text(
                            text = "Read " + mainViewModel.comicInfo.chapters.first().name,
                            color = Color(206, 206, 206, 255),
                            fontWeight = FontWeight.ExtraBold,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 17.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .padding(10.dp, 20.dp)
                        .background(MainTheme.lightBackground, RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                   ) {
                    mainViewModel.comicInfo.chapters.forEachIndexed { index, it ->
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                mainViewModel.getAsura(
                                    api,
                                    index,
                                    mainNavController,
                                    context
                                )
                            }) {
                            Text(
                                text = it.name,
                                color = if(it.read) Color(55, 65, 100, 255) else  Color(206, 206, 206, 255),
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
                            Spacer(modifier = Modifier
                                .align(CenterHorizontally)
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(
                                    Color(9, 9, 9)
                                ))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}














