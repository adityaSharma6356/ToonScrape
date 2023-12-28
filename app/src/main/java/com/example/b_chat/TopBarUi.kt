package com.example.b_chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun TopBarUi(){
    val state = rememberScrollState()
    val aniColor by remember { derivedStateOf { if(state.value>0) Color(255, 255, 255, 57) else Color.Transparent } }
    val topColor by animateColorAsState(targetValue = aniColor)
    Row(verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .zIndex(1f)
            .fillMaxWidth()
            .background(topColor)
            .padding(bottom = 20.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.menu_icon),
            contentDescription = "menu",
            modifier = Modifier
                .padding(top = 60.dp, start = 30.dp)
                .size(25.dp)
                .drawBehind {
                    drawCircle(color = Color(255, 255, 255, 57), radius = 35f,)
                },
            tint = Color.White
        )
        Text(
            text = "Web2Read",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
        )
        Icon(
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "search",
            modifier = Modifier
                .padding(top = 60.dp, end = 30.dp)
                .size(25.dp)
                .drawBehind {
                    drawCircle(color = Color(255, 255, 255, 57), radius = 35f,)
                },
            tint = Color.White
        )
    }
}