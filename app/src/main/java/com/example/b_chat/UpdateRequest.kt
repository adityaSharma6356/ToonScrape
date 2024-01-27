package com.example.b_chat

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun UpdateRequest(mainViewModel: MainViewModel, modifier: Modifier) {
    val url by remember { mutableStateOf("https://sourceforge.net/projects/web2read/") }
    Column(
        modifier = modifier.background(MainTheme.lightBackground, RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon_circle_light),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(0.2f)
        )
        Text(
            text = "Update Required",
            color = Color.White,
            fontSize = 21.nonScaledSp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp),
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = "Exciting New Features Await! To unlock a world of improvements, bug fixes, and innovative features, please update to the latest version.",
            color = Color.White,
            fontSize = 16.nonScaledSp,
            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 20.dp).padding(horizontal = 30.dp),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )

        val context = LocalContext.current
        Button(
            onClick = { openWebsiteInBrowser(url, context) },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MainTheme.navColor),
            modifier = Modifier
                .padding(bottom = 5.dp)
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Update Now",
                color = Color.White,
                fontSize = 19.nonScaledSp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontFamily = FontFamily.SansSerif,
            )
        }
        Button(
            onClick = { mainViewModel.updateAvailable = false },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .padding(top = 5.dp)
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Skip Version",
                color = MainTheme.lightBackground,
                fontSize = 19.nonScaledSp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontFamily = FontFamily.SansSerif,
            )
        }
    }
}

fun openWebsiteInBrowser(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}