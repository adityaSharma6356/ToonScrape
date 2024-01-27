package com.example.b_chat

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.b_chat.data.ComicsData
import java.lang.Integer.min

@Composable
fun VerticalComicListView(
    list: SnapshotStateList<ComicsData>,
    heading: String,
    onClick: (ComicsData) -> Unit
){
    if(list.isNotEmpty()){
        LazyColumn(modifier = Modifier
            .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            if(heading.isNotBlank()){
                item {
                    Text(
                        text = heading,
                        color = Color.White,
                        fontSize = 20.nonScaledSp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .padding(20.dp, 50.dp, 0.dp, 5.dp),
                        fontFamily = FontFamily.SansSerif,
                    )
                }
            }
            itemsIndexed(list,key = { it, _ ->
                list[it].link
            }){ _, item ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clickable {
                        onClick(item)
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
                    Column(modifier = Modifier
                        .padding(top = 25.dp)
                        .weight(1f)
                        .align(Alignment.Top)) {
                        Text(
                            text = item.name,
                            color = Color(255, 255, 255, 255),
                            fontSize = 15.nonScaledSp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp, 0.dp, 15.dp, 5.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                        val count = min(item.chaptersData.size-1, 2)
                        Log.d("sndjoasjd", count.toString())
                        for(i in 0..count) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = item.chaptersData[i].name,
                                    color = Color(255, 255, 255, 153),
                                    fontSize = 13.nonScaledSp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    text = item.chaptersData[i].time,
                                    color = Color(255, 255, 255, 153),
                                    fontSize = 13.nonScaledSp,
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
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
        }
    }
}
