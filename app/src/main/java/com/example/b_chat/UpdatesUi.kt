package com.example.b_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.b_chat.data.ComicsData

@Composable
fun UpdatesUi(
    list: List<ComicsData>,
    onClick: (ComicsData) -> Unit,
    onClickMore: () -> Unit,
){
    Column(modifier = Modifier.fillMaxWidth().background(MainTheme.background), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Latest Updates",
                color = Color.White,
                fontSize = 19.nonScaledSp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 10.dp),
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "See all",
                color = MainTheme.navColor,
                fontSize = 15.nonScaledSp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 10.dp).clickable { onClickMore() },
                fontFamily = FontFamily.SansSerif
            )

        }
        if(list.isNotEmpty()){
            for(i in 0..minOf(list.size-1, 5)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clickable {
                        onClick(list[i])
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = list[i].image,
                        contentDescription = "image",
                        modifier = Modifier
                            .padding(20.dp, 10.dp, 10.dp, 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .width(110.dp),
                        filterQuality = FilterQuality.None,
                        contentScale = ContentScale.FillHeight
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = list[i].name,
                            color = Color(255, 255, 255, 255),
                            fontSize = 15.nonScaledSp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp, 0.dp, 15.dp, 5.dp),
                            fontFamily = FontFamily.SansSerif
                        )
                        list[i].chaptersData.forEach { chapter ->
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = chapter.name,
                                    color = Color(255, 255, 255, 153),
                                    fontSize = 13.nonScaledSp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    text = chapter.time,
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
    }
}