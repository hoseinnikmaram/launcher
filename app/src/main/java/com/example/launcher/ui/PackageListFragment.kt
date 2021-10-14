package com.example.launcher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.launcher.databinding.FragmentPackageListBinding
import com.example.launcher.ui.MainFragment.MainViewModel
import com.example.launcher.ui.MainFragment.PackageAdapter
import com.example.launcher.util.directOpenInstalledApp
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PackageListFragment : Fragment() {
    lateinit var binding: FragmentPackageListBinding
    private val mainViewModel: MainViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentPackageListBinding.inflate(inflater, container, false)
            mainViewModel.getInstalledPackage(requireActivity()).observe(viewLifecycleOwner){
                binding.recyclerView.adapter = PackageAdapter(it) { packageName ->
                    directOpenInstalledApp(
                        packageName = packageName,
                        packageManager = activity?.packageManager,
                        context = requireContext()
                    )
                }
                binding.isVisibleProgressBar = false
            }


        }
        return binding.root
    }


}