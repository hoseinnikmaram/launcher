package com.boomino.launcher.ui.PackageListFragment

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.boomino.launcher.model.PackageModel
import com.boomino.launcher.ui.MainFragment.MainViewModel
import com.boomino.launcher.ui.MainFragment.packageList
import com.boomino.launcher.util.directOpenInstalledApp
import com.boomino.launcher.util.openInformationApp
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PackageListFragment : Fragment() {
    private val mainViewModel: MainViewModel by sharedViewModel()
        //private val packages: MutableState<List<PackageModel>> = mutableStateOf(ArrayList())
        val packages = mutableStateListOf<PackageModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    packageList(packages, onClick = { packageName ->
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
        mainViewModel.responsePackageList
            .observe(requireActivity()) { packages.addAll(it) }
        return view
    }
}