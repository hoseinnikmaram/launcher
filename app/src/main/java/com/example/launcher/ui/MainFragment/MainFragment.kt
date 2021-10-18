package com.example.launcher.ui.MainFragment

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.launcher.model.PackageModel
import com.example.launcher.util.OnSwipeTouchListener
import com.example.launcher.util.URL_ZAREBIN
import com.example.launcher.util.directOpenInstalledApp
import com.example.launcher.util.hideKeyboardFrom
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val packages: MutableState<List<PackageModel>> = mutableStateOf(ArrayList())
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
                    SearchEditText(requireContext()) {
                        hideKeyboardFrom(requireContext(), requireView())
                        actionSearch(it)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    packageList(packages.value) { packageName ->
                        directOpenInstalledApp(
                            packageName = packageName,
                            packageManager = activity?.packageManager,
                            context = requireContext()
                        )
                    }
                }

            }
        }
        view.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToPackageListFragment())

            }
        })
        mainViewModel.getInstalledPackage().observe(requireActivity()) {
            val subListSize = if (it.size < 8)
                it.size
            else
                8
            packages.value = it.subList(0, subListSize)
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
