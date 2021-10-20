package com.boomino.launcher.ui.MainFragment

import android.R.attr.bitmap
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.boomino.launcher.R
import com.boomino.launcher.model.PackageModel
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun SearchEditText(context: Context, onClick: (String) -> Unit) {

    var textFieldState by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = textFieldState,
        onValueChange = {
            textFieldState = it
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Gray
        ),

        trailingIcon = {
            IconButton(
                onClick = {
                    onClick(textFieldState)
                }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onClick(textFieldState) }),
        textStyle = TextStyle(color = Color.Black, fontFamily = FontFamily(Font(R.font.iran_sans))),
        maxLines = 1,
        singleLine = true,
        placeholder = {
            Text(
                text = context.getString(R.string.search_text),
            )
        }

    )

}
@Composable
fun showIcon(){
    GlideImage(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp),
        alignment = Alignment.BottomCenter,
        imageModel = R.drawable.ic_arrow_up
    )
}

@Composable
fun packageItem(packageModel: PackageModel, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .padding(vertical = 12.dp)
            .clickable(onClick = { onClick(packageModel.packageName) }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeByteArray(packageModel.icon, 0, packageModel.icon.size, options)
        GlideImage(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp),
            imageModel = bitmap,
            requestOptions = RequestOptions()
                .override(256, 256)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop(),
            contentScale = ContentScale.Crop,
            placeHolder = ImageBitmap.imageResource(R.drawable.placeholder)
        )
        Text(
            text = packageModel.label,
            maxLines = 1,
            style = TextStyle(
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.iran_sans)),
                textAlign = TextAlign.Center
            )
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun packageList(packages: List<PackageModel>, onClick: (String) -> Unit) {
    if (packages.isNullOrEmpty()) {
        CircularProgressIndicator(color = Color.White)
    } else {
        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            content = {
                items(packages.size) { index ->
                    packageItem(packageModel = packages[index]) {
                        onClick(it)
                    }
                }
            }
        )

    }
}



