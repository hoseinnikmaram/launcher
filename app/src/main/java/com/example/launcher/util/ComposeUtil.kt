package com.boomino.launcher.ui.MainFragment

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.rememberImagePainter
import com.boomino.launcher.R
import com.boomino.launcher.model.PackageModel


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
    packages: SnapshotStateList<PackageModel>,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    if (packages.isNullOrEmpty()) {
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



