package com.boomino.launcher.ui.MainFragment

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.rememberImagePainter
import com.boomino.launcher.R
import com.boomino.launcher.model.PackageModel
import com.boomino.launcher.util.*
import kotlinx.coroutines.delay
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun showMainPageContent(
    page: Int,
    packages: SnapshotStateList<PackageModel>,
    packageManager: PackageManager?,
    context: Context,
    view: View
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        showTime()
        showDate()
        Spacer(modifier = Modifier.height(20.dp))
        SearchEditText(context = context) {
            hideKeyboardFrom(context, view)
            actionSearch(it, context)
        }
        Spacer(modifier = Modifier.weight(1.0f))
        packageList(isItemDefault = true, packages = packages, onClick = { packageName ->
            directOpenInstalledApp(
                packageName = packageName,
                packageManager = packageManager,
                context = context
            )
        }
        ) { packageName ->
            openInformationApp(context, packageName)
        }
    }
}

@Composable
fun showTime() {
    var currentTime by remember {
        mutableStateOf(
            SimpleDateFormat(
                "HH:mm",
                Locale.US
            ).format(Date())
        )
    }
    LaunchedEffect(currentTime) {
        while (true) {
            delay(1000L)
            currentTime = SimpleDateFormat("HH:mm", Locale.US).format(Date())
        }
    }
    Text(
        modifier = Modifier.height(80.dp),
        text = currentTime.normalizePersianDigits(),
        style = TextStyle(
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.iran_sans)),
            textAlign = TextAlign.Center,
            fontSize = 60.sp,
        ),

        )
}

@Composable
fun showDate() {
    val persianDate = PersianDate()
    val persianDateFormat = PersianDateFormat("d F Y")
    val date = persianDateFormat.format(persianDate) ?: ""
    Text(
        text = date,
        style = TextStyle(
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.iran_sans)),
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )
    )
}


@Composable
fun SearchEditText(
    colorText: Color = Color.Black,
    colorBackground: Color = Color.White,
    context: Context,
    onClear: () -> Unit = {},
    onClick: (String) -> Unit
) {

    var textFieldState by remember {
        mutableStateOf("")
    }
    if (textFieldState.isEmpty()) {
        onClear()
    }
    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(color = colorBackground, RoundedCornerShape(25.dp)),
            value = textFieldState,
            onValueChange = {
                textFieldState = it
            },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .background(
                            colorBackground,
                            RoundedCornerShape(25.dp)
                        )
                        .padding(4.dp)
                        .height(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Box(Modifier.weight(1f)) {
                        if (textFieldState.isEmpty()) {
                            Text(
                                text = context.getString(R.string.search_text),
                                color = Color.Gray,
                            )
                        }
                        innerTextField()
                    }
                    if (textFieldState.isNotEmpty()) {
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
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onClick(textFieldState) }),
            textStyle = TextStyle(
                color = colorText,
                fontFamily = FontFamily(Font(R.font.iran_sans))
            ),
            maxLines = 1,
            singleLine = true,

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
    isDefault: Boolean = false,
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
        if (!isDefault) {
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

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun packageList(
    isSearch: Boolean = false,
    packages: SnapshotStateList<PackageModel>,
    isItemDefault: Boolean = false,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    if (isSearch && packages.isNullOrEmpty()) {
        Text(
            text = "جستوجو بدون نتیجه است",
            maxLines = 1,
            style = TextStyle(
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.iran_sans)),
                textAlign = TextAlign.Center
            )
        )
    } else if (packages.isNullOrEmpty()) {
        CircularProgressIndicator(color = Color.White)
    } else {
        var selectedIndex by remember { mutableStateOf(-1) }
        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            content = {
                items(packages.size) { index ->
                    Box(contentAlignment = Alignment.Center) {
                        addItems(
                            packages,
                            index,
                            isItemDefault,
                            onClick = { onClick(it) },
                            onLongClick = { selectedIndex = it })
                        if (selectedIndex == index)
                            showPopupMenu(selected = { selectedIndex = it },
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
    isItemDefault: Boolean = false,
    onClick: (String) -> Unit,
    onLongClick: (Int) -> Unit
) {
    packageItem(packageModel = packages[index], isDefault = isItemDefault, onClick = {
        onClick(it)
    },
        onLongClick = {
            onLongClick(index)
        }
    )
}

@Composable
fun showPopupMenu(selected: (Int) -> Unit, onClick: () -> Unit) {
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



