package com.example.b_chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.Interaction
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    mainViewModel: MainViewModel,

){
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(50.dp))
        var showSearchResult by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current
        val source = remember {
            MutableInteractionSource()
        }
        SearchTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .fillMaxWidth()
                .height(40.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions {
                if (value.isNotBlank()) {
                    showSearchResult = value
                    mainViewModel.addSearchItem(value, context)
                    focusManager.clearFocus()
                }
            },
            trailingIcon = {
                if (value.isNotBlank()) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = "cancel",
                        tint = Color.White,
                        modifier = Modifier
                            .size(15.dp)
                            .clickable { value = "" }
                    )
                }
            },
            enabled = true,
            interactionSource = source
        )
        var openPast by remember {
            mutableStateOf(false)
        }
        if(source.collectIsPressedAsState().value){
            openPast = true
        }
        if(mainViewModel.savedSearches.isNotEmpty() && openPast){
            BackHandler {
                focusManager.clearFocus()
                openPast = false
            }
            Column(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                mainViewModel.savedSearches.forEach{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(20.dp, 5.dp)
                            .fillMaxWidth()
                            .clickable {
                                value = it
                                showSearchResult = it
                            }
                            .padding(10.dp, 5.dp)
                    ) {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.cancel),
                            contentDescription = "remove",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(12.dp)
                                .clickable {
                                    mainViewModel.removeSearchItem(
                                        it,
                                        context
                                    )
                                }
                        )
                    }
                }
            }
        } else {
            var genreData by remember { mutableStateOf<Genre?>(null) }
            val width = ((LocalConfiguration.current.screenWidthDp / 4) - 20) / 2
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
                columns = GridCells.Fixed(4)
            ) {
                itemsIndexed(genreList) { index, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .clickable { genreData = item }
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
                            fontSize = 12.sp,
                            modifier = Modifier.padding(0.dp, 10.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Thin
                        )
                    }
                }
            }

        }

//        if(genreData!=null){
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    painter = painterResource(id = R.drawable.left_icon),
//                    contentDescription = "back",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .padding(start = 20.dp, end = 10.dp)
//                        .size(30.dp)
//                        .clickable { genreData = null }
//                )
//                Text(
//                    text = genreData?.name?: "",
//                    color = Color.White,
//                    fontSize = 17.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.ExtraBold
//                )
//            }
//        } else {
//            if(showSearchResult.isNotBlank()){
//                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top=15.dp)) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.left_icon),
//                        contentDescription = "back",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .padding(start = 20.dp, end = 10.dp, top = 0.dp)
//                            .size(30.dp)
//                            .clickable { showSearchResult = "" }
//                    )
//                    Text(
//                        text = showSearchResult,
//                        color = Color.White,
//                        fontSize = 17.sp,
//                        maxLines = 1,
//                        fontFamily = FontFamily.SansSerif,
//                        fontWeight = FontWeight.ExtraBold
//                    )
//                }
//
//            } else {


//            }


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
    interactionSource: MutableInteractionSource
){
    BasicTextField(
        textStyle = TextStyle(
            color = Color.White
        ),
        cursorBrush = Brush.horizontalGradient(listOf(Color.White, Color.White)),
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.border(1.dp, Color.White, RoundedCornerShape(5.dp)),
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
