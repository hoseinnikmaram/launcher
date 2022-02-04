package com.nikmaram.launcher.ui.PackageListFragment

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.nikmaram.launcher.R
import com.nikmaram.launcher.model.PackageModel
import com.nikmaram.launcher.ui.MainFragment.MainViewModel
import com.nikmaram.launcher.ui.MainFragment.SearchEditText
import com.nikmaram.launcher.ui.MainFragment.packageList
import com.nikmaram.launcher.util.directOpenInstalledApp
import com.nikmaram.launcher.util.hideKeyboardFrom
import com.nikmaram.launcher.util.openInformationApp
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PackageListFragment : Fragment() {
    private val mainViewModel: MainViewModel by sharedViewModel()
    val packages = mutableStateListOf<PackageModel>()
    val isSearch =  mutableStateOf(false)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                Surface(
                    color = Color.Black,
                ) {
                    Column(
                        modifier = Modifier.absolutePadding(left = 8.dp,right = 8.dp,top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchEditText(
                            colorText=Color.White,
                            colorBackground = colorResource(R.color.grey_90),
                            context = requireContext(),
                            onClear = { getPackages() }) {
                            hideKeyboardFrom(requireContext(), requireView())
                            actionSearch(it)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        packageList(isSearch.value,packages, onClick = { packageName ->
                            directOpenInstalledApp(
                                packageName = packageName,
                                packageManager = activity?.packageManager,
                                context = requireContext()
                            )
                        },
                            onLongClick = { packageName ->
                                openInformationApp(requireContext(), packageName)
                            }
                        )
                    }

                }
            }
        }

        return view
    }

    private fun getPackages() {
        mainViewModel.getInstalledPackage()
            .observe(requireActivity()) {
                packages.clear()
                packages.addAll(it)
            }
    }

    private fun actionSearch(searchText: String) {
        mainViewModel.getInstallPackagesBySearch(searchText)
            .observe(viewLifecycleOwner) {
                isSearch.value = true
                packages.clear()
                packages.addAll(it)
            }
    }
}