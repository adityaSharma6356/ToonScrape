package com.example.b_chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(){
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(50.dp))
        var genreData by remember { mutableStateOf<Genre?>(null) }
        if(genreData!=null){

        } else {
            CustomTextField(
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        null,
                        tint = Color(194, 194, 194, 255)
                    )
                },
                trailingIcon = null,
                modifier = Modifier
                    .padding(20.dp, 0.dp)
                    .height(40.dp)
                , fontSize = 13.sp,
                placeholderText = "Search Title"
            )

            val width = ((LocalConfiguration.current.screenWidthDp/4)-20)/2
            Text(
                text = "Top Genres",
                color = Color.White,
                fontSize = 17.sp,
                modifier = Modifier.padding(20.dp, 30.dp, 5.dp, 0.dp),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
                columns = GridCells.Fixed(4)){
                itemsIndexed(genreList){ index, item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(5.dp)) {
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .clickable { genreData = item }
                            .background(Color(31, 31, 31, 255))
                            .padding(20.dp)
                            ,
                            contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.name,
                                tint = Color.White,
                                modifier = Modifier.size(width.dp)
                            )
                        }
                        Text(
                            text = item.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(0.dp, 10.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Thin
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(modifier = modifier
        .background(
            Color(31, 31, 31, 255),
            RoundedCornerShape(5.dp)
        )
        .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        cursorBrush = SolidColor(Color.White),
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = fontSize,
            fontFamily = FontFamily.SansSerif
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = Color(163, 163, 163, 255),
                            fontSize = fontSize,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    innerTextField()
                }
                if(text.isNotBlank()){
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = "cancel",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp).clickable { text="" }
                    )
                }
            }
        }
    )
}

data class Genre(
    val name:String,
    val link:String,
    val icon: Int
)

val genreList = listOf(
    Genre("Action", "https://asuratoon.com/manga/?genre%5B%5D=Action&order=update", R.drawable.action),
    Genre("Regression", "https://asuratoon.com/manga/?genre%5B%5D=Regression&order=update",R.drawable.regression),
    Genre("Isekai", "https://asuratoon.com/manga/?genre%5B%5D=Isekai&order=update", R.drawable.isekai),
    Genre("Fantasy", "https://asuratoon.com/manga/?genre%5B%5D=Fantasy&order=update", R.drawable.fantasy),
    Genre("Comedy", "https://asuratoon.com/manga/?genre%5B%5D=Comedy&order=update", R.drawable.comedy),
    Genre("Murim", "https://asuratoon.com/manga/?genre%5B%5D=Murim&order=update", R.drawable.murim),
    Genre("Drama", "https://asuratoon.com/manga/?genre%5B%5D=Drama&order=update", R.drawable.drama),
    Genre("Mystery", "https://asuratoon.com/manga/?genre%5B%5D=Mystery&order=update",R.drawable.mystery),
    Genre("Romance", "https://asuratoon.com/manga/?genre%5B%5D=Romance&order=update",R.drawable.romance),
    Genre("Sci-fi", "https://asuratoon.com/manga/?genre%5B%5D=Sci-fi&order=update",R.drawable.sci_fi),
    Genre("Slice of Life", "https://asuratoon.com/manga/?genre%5B%5D=Slice%20of%20Life&order=update",R.drawable.slice_of_life),
    Genre("Sports", "https://asuratoon.com/manga/?genre%5B%5D=Sports&order=update",R.drawable.sports),
    Genre("Super Hero", "https://asuratoon.com/manga/?genre%5B%5D=Super%20Hero&order=update",R.drawable.superhero),
    Genre("Supernatural", "https://asuratoon.com/manga/?genre%5B%5D=Supernatural&order=update",R.drawable.supernatural),
    Genre("Thriller", "https://asuratoon.com/manga/?genre%5B%5D=Thriller&order=update",R.drawable.thriller),
    Genre("Historical", "https://asuratoon.com/manga/?genre%5B%5D=Historical&order=update", R.drawable.historical),
)
