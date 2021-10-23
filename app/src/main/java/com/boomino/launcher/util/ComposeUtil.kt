package com.boomino.launcher.ui.MainFragment

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.rememberImagePainter
import com.boomino.launcher.R
import com.boomino.launcher.model.PackageModel


@Composable
fun SearchEditText(colorText: Color = Color.Black,colorBackground: Color = Color.White,context: Context,onClear: () -> Unit={} ,onClick: (String) -> Unit) {

    var textFieldState by remember {
        mutableStateOf("")
    }
    if (textFieldState.isEmpty()){
        onClear()
    }
    Box(modifier = Modifier.padding(horizontal = 8.dp)){
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .height(55.dp)
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
            value = textFieldState,
            onValueChange = {
                textFieldState = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorBackground,
                focusedIndicatorColor = colorBackground,
                unfocusedIndicatorColor = colorBackground
            ),

            leadingIcon = {
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
            trailingIcon={
                if (textFieldState.isNotEmpty()){
                    IconButton(
                        onClick = {
                            textFieldState = ""
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onClick(textFieldState) }),
            textStyle = TextStyle(color = colorText, fontFamily = FontFamily(Font(R.font.iran_sans))),
            maxLines = 1,
            singleLine = true,
            placeholder = {
                Text(
                    text = context.getString(R.string.search_text),
                    color = Color.Gray,)
            }

        )

    }

}

@Composable
fun showIcon() {
    Image(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp),
        alignment = Alignment.BottomCenter,
        painter = rememberImagePainter(
            data = R.drawable.ic_arrow_up,
            builder = {
                crossfade(true)
            }
        ),
        contentDescription = null,
    )
}

@ExperimentalFoundationApi
@Composable
fun packageItem(
    packageModel: PackageModel,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .width(60.dp)
            .padding(vertical = 12.dp)

            .combinedClickable(
                onLongClick = { onLongClick(packageModel.packageName) },
                onClick = { onClick(packageModel.packageName) }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val options = BitmapFactory.Options()
        val bitmap =
            BitmapFactory.decodeByteArray(packageModel.icon, 0, packageModel.icon.size, options)
        Image(
            painter = rememberImagePainter(
                data = bitmap,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
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
fun packageList(
    isSearch: Boolean = false,
    packages: SnapshotStateList<PackageModel>,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    if (isSearch && packages.isNullOrEmpty()){
        Text(
            text = "جستوجو بدون نتیجه است",
            maxLines = 1,
            style = TextStyle(
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.iran_sans)),
                textAlign = TextAlign.Center
            )
        )
    }
    else if (packages.isNullOrEmpty()) {
        CircularProgressIndicator(color = Color.White)
    } else {
        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            content = {
                items(packages.size) { index ->
                    Box(contentAlignment = Alignment.Center) {

                        addItems(
                            packages,
                            index,
                            onClick = { onClick(it) },
                            onLongClick = { selectedIndex = it })
                        if (selectedIndex == index)
                            showDropdown(selected = { selectedIndex = it },
                                onClick = { onLongClick(packages[index].packageName) })
                    }
                }
            }
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun addItems(
    packages: SnapshotStateList<PackageModel>,
    index: Int,
    onClick: (String) -> Unit,
    onLongClick: (Int) -> Unit
) {
    packageItem(packageModel = packages[index], {
        onClick(it)
    },
        onLongClick = {
            onLongClick(index)
        }
    )
}

@Composable
fun showDropdown(selected: (Int) -> Unit, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(true) }
    if (expanded) {
        Popup(
            alignment = Alignment.TopCenter,
            onDismissRequest = {
                selected(-1)
                expanded = false
            },
        )
        {
            Box(
                Modifier
                    .size(120.dp, 50.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
            )

            {
                DropdownMenuItem(onClick = {
                    selected(-1)
                    expanded = false
                    onClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Text(stringResource(R.string.info_text))
                }
            }

        }
    }
}



