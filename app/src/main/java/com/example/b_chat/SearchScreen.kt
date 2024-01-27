package com.example.b_chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@Composable
fun SearchScreen(
    mainViewModel: MainViewModel,
    mainNavController: NavHostController,
    ){
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(50.dp))
        var colorChanger by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        val source = remember {
            MutableInteractionSource()
        }
        var borderColor by remember { mutableStateOf (Color.White) }
        if(source.collectIsPressedAsState().value){
            colorChanger = true
        }
        if(colorChanger){
            borderColor = MainTheme.navColor
            BackHandler {
                focusManager.clearFocus()
                colorChanger = false
                borderColor  = Color.White
            }
        }

        SearchTextField(
            value = mainViewModel.searchQuery,
            onValueChange = { mainViewModel.searchQuery = it },
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .fillMaxWidth()
                .height(40.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions {
                if (mainViewModel.searchQuery.isNotBlank()) {
                    mainViewModel.showSearchResult = mainViewModel.searchQuery
                    focusManager.clearFocus()
                    mainViewModel.search(mainViewModel.searchQuery)
                }
            },
            trailingIcon = {
                if (mainViewModel.searchQuery.isNotBlank()) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = "cancel",
                        tint = Color.White,
                        modifier = Modifier
                            .size(15.dp)
                            .clickable { mainViewModel.searchQuery = "" }
                    )
                }
            },
            enabled = true,
            interactionSource = source,
            borderColor = borderColor
        )
        val width = ((LocalConfiguration.current.screenWidthDp / 4) - 20) / 2
        if(mainViewModel.noResult){
            Text(
                text = "No matching result",
                color = MainTheme.navColor,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }
        if(mainViewModel.showSearchResult.isNotBlank() && !mainViewModel.noResult){
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = MainTheme.navColor
                        )
                        ) {
                            append("showing results for \"")
                        }
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append(mainViewModel.showSearchResult)
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = MainTheme.navColor
                        )
                        ) {
                            append("\"")
                        }
                    },
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(20.dp, 15.dp, 0.dp, 5.dp),
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "clear",
                    color = MainTheme.navColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(0.dp, 15.dp, 20.dp, 5.dp).clickable {
                        mainViewModel.showSearchResult = ""
                        mainViewModel.searchResult.clear() },
                    fontFamily = FontFamily.SansSerif
                )

            }
        }
        if(mainViewModel.loadingSearchResult){
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0F,
                targetValue = 360F,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing)
                ),
                label = "loading"
            )
            Icon(
                painter = painterResource(id = R.drawable.dotted_loading),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(0.1f)
                    .graphicsLayer {
                        rotationZ = angle
                    }
                    .align(Alignment.CenterHorizontally),
                tint = MainTheme.navColor
            )
        } else if(mainViewModel.searchResult.isEmpty()){
            Text(
                text = "Top Genres",
                color = Color.White,
                fontSize = 17.sp,
                modifier = Modifier.padding(20.dp, 30.dp, 5.dp, 0.dp),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                itemsIndexed(genreList) { _, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                mainViewModel.showSearchResult = item.name
                                mainViewModel.search(item.link, true)
                            }
                            .background(MainTheme.lightBackground)
                            .padding(20.dp),
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
                            fontSize = 12.nonScaledSp,
                            modifier = Modifier.padding(0.dp, 10.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Thin
                        )
                    }
                }
            }
        } else {
            val widthCustom = LocalConfiguration.current.screenWidthDp/3
            val height = widthCustom*1.5
            LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(bottom = 100.dp)){
                itemsIndexed(mainViewModel.searchResult){ _, comic ->
                    Row(modifier = Modifier.fillMaxWidth().clickable { mainViewModel.getComicInfo(comic, mainNavController) }) {
                        Card(modifier = Modifier
                            .padding(20.dp, 20.dp, 10.dp, 20.dp)
                            .size(widthCustom.dp, height.dp)) {
                            AsyncImage(
                                model = comic.image,
                                contentDescription = "image",
                                modifier = Modifier
                                    .width(width.dp)
                                    .height(height.dp),
                                filterQuality = FilterQuality.None,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier
                            .padding(top = 25.dp)
                            .weight(1f)
                            .align(Alignment.Top)) {
                            Text(
                                text = comic.name,
                                color = Color(255, 255, 255, 255),
                                fontSize = 17.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(5.dp, 0.dp, 15.dp, 5.dp),
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                text = comic.chaptersData.first().name,
                                color = Color(255, 255, 255, 153),
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(5.dp, 5.dp, 10.dp, 5.dp),
                                fontFamily = FontFamily.SansSerif
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    tint = Color.Yellow,
                                    painter = painterResource(id = R.drawable.star),
                                    contentDescription = "star",
                                    modifier = Modifier
                                        .padding(5.dp, 0.dp, 0.dp, 0.dp)
                                        .size(15.dp)
                                )
                                Text(
                                    text = comic.rating,
                                    color = Color(255, 232, 170, 255),
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(3.dp, 0.dp, 10.dp, 0.dp),
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                            Text(
                                text = comic.type.uppercase(),
                                color = Color.White,
                                fontSize = 10.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(5.dp, 10.dp, 10.dp, 5.dp)
                                    .drawBehind {
                                        drawRoundRect(
                                            MainTheme.navColor,
                                            cornerRadius = CornerRadius(10f, 10f)
                                        )
                                    }
                                    .padding(5.dp),
                                fontFamily = FontFamily.SansSerif
                            )

                        }
                    }


                }
            }
        }



    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier:Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    enabled:Boolean,
    interactionSource: MutableInteractionSource,
    borderColor: Color
){
    BasicTextField(
        textStyle = TextStyle(
            color = Color.White
        ),
        cursorBrush = Brush.horizontalGradient(listOf(Color.White, Color.White)),
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.border(1.dp, borderColor , RoundedCornerShape(5.dp)),
        visualTransformation = VisualTransformation.None,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        singleLine = true,
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MainTheme.background,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(5.dp),
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            placeholder = {
                Text(
                    "Search Title",
                    style = LocalTextStyle.current.copy(
                        color = Color(163, 163, 163, 255),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        )
    }
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
