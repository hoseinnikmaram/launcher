package com.boomino.launcher.ui.MainFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.boomino.launcher.model.PackageModel
import com.boomino.launcher.util.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.ext.scope


class MainFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val defaultPackages = mutableStateListOf<PackageModel>()

    @OptIn(
        ExperimentalPagerApi::class,
        androidx.compose.foundation.ExperimentalFoundationApi::class
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                val pagerStateHorizontal = rememberPagerState()
                Column(
                    Modifier
                        .fillMaxSize()
                        .combinedClickable(onLongClick = { onLongClickPage(requireContext()) },
                            onClick = {})
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { change, dragAmount ->
                                if (dragAmount.toInt()< -25)
                                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToPackageListFragment())
                            }
                        }
                ) {
                    HorizontalPager(
                        count = 2,
                        state = pagerStateHorizontal,
                        reverseLayout = true,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                         showMainPageContent(
                                pagerStateHorizontal.currentPage,
                                defaultPackages,
                                activity?.packageManager,
                                requireContext(),
                                requireView()
                            )
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerStateHorizontal,
                        activeColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp),
                    )

                }
            }
        }

        mainViewModel.responsePackageDefaultList.observe(viewLifecycleOwner) {
            defaultPackages.clear()
            defaultPackages.addAll(it)
        }
        return view
    }

}
