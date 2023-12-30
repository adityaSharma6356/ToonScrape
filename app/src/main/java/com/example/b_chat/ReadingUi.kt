package com.example.b_chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ReadingUi(mainViewModel: MainViewModel, api: Scrapper){
    val state = rememberScrollState()
    val source = MutableInteractionSource()
    var show by remember {  mutableStateOf(false) }
    val context = LocalContext.current
    AnimatedVisibility(visible = show, enter = fadeIn(), exit = fadeOut(), modifier = Modifier
        .zIndex(1f)
        .fillMaxWidth()
        .height(110.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainTheme.background)
        ) {
            val scope = rememberCoroutineScope()
            if(mainViewModel.chapterData.index<mainViewModel.comicInfo.chapters.lastIndex){
                TextButton(onClick = {
                    scope.launch {
                        mainViewModel.getAsura(api, mainViewModel.chapterData.index+1,null, context )
                        show = false
                        state.scrollTo(0)
                    }
                }, modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomStart)) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_icon),
                        contentDescription = "previous",
                        tint = Color(
                            201,
                            201,
                            201,
                            255
                        ),
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "Previous", fontSize = 14.sp, color = Color.White, fontFamily = FontFamily.SansSerif)
                }
            }

            Text(
                text = mainViewModel.currentChapterName,
                fontSize = 14.sp,
                color = Color(
                    201,
                    201,
                    201,
                    255
                ),
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.BottomCenter)
            )

            if(mainViewModel.chapterData.index>0){
                TextButton(onClick = {
                    scope.launch {
                        mainViewModel.getAsura(api, mainViewModel.chapterData.index-1, null, context)
                        show = false
                        state.scrollTo(0)
                    }
                },modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)) {
                    Text(text = "Next", fontSize = 14.sp, color = Color.White, fontFamily = FontFamily.SansSerif)
                    Icon(
                        painter = painterResource(id = R.drawable.right_icon),
                        contentDescription = "next",
                        tint = Color(
                            201,
                            201,
                            201,
                            255
                        ),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state)
        .clickable(interactionSource = source, indication = null) {
            show = !show
        },
        horizontalAlignment = Alignment.CenterHorizontally) {
        val context = LocalContext.current
//        mainViewModel.addReadChapter(mainViewModel.comicInfo.chapters[mainViewModel.chapterData.index].link, context)
        mainViewModel.chapterData.pages.forEach{ link ->
            val painter = rememberAsyncImagePainter(
                model = link,
                error = painterResource(id = R.drawable.error_image),
                placeholder = painterResource(id =R.drawable.loading),
                contentScale = ContentScale.FillWidth,
            )
            Image(
                painter = painter,
                contentDescription = "image",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}