package com.example.b_chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.b_chat.data.ChapterData
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ReadingUi(
    mainViewModel: MainViewModel,
    data: ChapterData,
    currentChapterIndex: Int,

    ){
    val state = rememberScrollState()
    val source = MutableInteractionSource()
    var show by remember {  mutableStateOf(false) }

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
            if(currentChapterIndex < mainViewModel.displayData.currentComic.chaptersData.lastIndex){
                TextButton(onClick = {
                    scope.launch {
                        mainViewModel.getAsura(currentChapterIndex+1,null)
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
                text = data.name,
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

            if(currentChapterIndex>0){
                TextButton(onClick = {
                    scope.launch {
                        mainViewModel.getAsura(currentChapterIndex-1, null )
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
    val width = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = source, indication = null) {
                show = !show
            }
            .verticalScroll(state),
        horizontalAlignment = Alignment.CenterHorizontally) {
        data.pages.forEach {  page ->
            AsyncImage(
                model = page,
                contentDescription = "page",
                modifier = Modifier.width(width.dp),
                contentScale = ContentScale.FillWidth,
                error = painterResource(id = R.drawable.error_image),
                placeholder = painterResource(id = R.drawable.loading),
                )
        }
    }
}