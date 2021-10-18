package com.example.launcher.ui

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
import com.example.launcher.databinding.FragmentPackageListBinding
import com.example.launcher.model.PackageModel
import com.example.launcher.ui.MainFragment.MainViewModel
import com.example.launcher.ui.MainFragment.PackageAdapter
import com.example.launcher.ui.MainFragment.SearchEditText
import com.example.launcher.ui.MainFragment.packageList
import com.example.launcher.util.directOpenInstalledApp
import com.example.launcher.util.hideKeyboardFrom
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PackageListFragment : Fragment() {
    lateinit var binding: FragmentPackageListBinding
    private val mainViewModel: MainViewModel by sharedViewModel()
    private val packages: MutableState<List<PackageModel>> = mutableStateOf(ArrayList())
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
        mainViewModel.getInstalledPackage(requireActivity())
            .observe(viewLifecycleOwner) { packages.value = it }
        return view
    }
}