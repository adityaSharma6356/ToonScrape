package com.example.b_chat

import android.os.Build
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ReadingUi(mainViewModel: MainViewModel, window: Window){
    val context = LocalContext.current
    val state = rememberScrollState()
    val source = MutableInteractionSource()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state),
        horizontalAlignment = Alignment.CenterHorizontally) {
        mainViewModel.chapterData.forEach{ link ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .placeholder(R.drawable.loading)
                    .data(link)
                    .build()
                ,
                contentDescription = "image",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}