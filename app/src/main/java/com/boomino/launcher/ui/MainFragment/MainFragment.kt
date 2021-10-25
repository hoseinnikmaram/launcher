package com.boomino.launcher.ui.MainFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.boomino.launcher.model.PackageModel
import com.boomino.launcher.util.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel>()
    //private val packages: MutableState<List<PackageModel>> = mutableStateOf(ArrayList())
    val packages = mutableStateListOf<PackageModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(80.dp))
                    showTime()
                    showDate()
                    Spacer(modifier = Modifier.height(20.dp))
                    SearchEditText(context=requireContext()) {
                        hideKeyboardFrom(requireContext(), requireView())
                        actionSearch(it)
                    }

                    Spacer(modifier = Modifier.weight(1.0f))
                    packageList(packages = packages, onClick = { packageName ->
                        directOpenInstalledApp(
                            packageName = packageName,
                            packageManager = activity?.packageManager,
                            context = requireContext()
                        )
                    }
                    ) { packageName ->
                        openInformationApp(requireContext(), packageName)
                    }
                }

            }
        }
        view.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToPackageListFragment())
            }

            override fun onLongClick() {
                super.onLongClick()
                startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))

            }
        })

        mainViewModel.responsePackageDefaultList.observe(viewLifecycleOwner) {
            packages.clear()
            packages.addAll(it)
        }
        return view
    }


    private fun actionSearch(textSearch: String) {
        val text = textSearch.trim().replace(" ", "%20")
        if (text.isEmpty())
            return
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(URL_ZAREBIN + text))
        startActivity(browserIntent)
    }


}
