package com.example.launcher.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.launcher.R
import com.example.launcher.databinding.FragmentMainBinding
import com.example.launcher.databinding.FragmentPackageListBinding
import com.example.launcher.model.PackageModel
import com.example.launcher.ui.MainFragment.PackageAdapter
import com.example.launcher.util.liveDataFromValue
import com.example.launcher.util.runAsync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PackageListFragment : Fragment() {
    lateinit var binding: FragmentPackageListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentPackageListBinding.inflate(inflater, container, false)
            lifecycleScope.launch {
                binding.recyclerView.adapter = PackageAdapter(getInstalledPackage()){
                    directOpenInstalledApp(packageName = it)
                }
                binding.isVisibleProgressBar = false
            }

        }
        return binding.root
    }
    private fun directOpenInstalledApp(packageName: String) {
        val pm = activity?.packageManager
        val intent = pm?.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent);
        }
    }
     private suspend fun getInstalledPackage(): List<PackageModel> = withContext(Dispatchers.IO) {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = requireActivity().packageManager.queryIntentActivities(mainIntent, 0)
         pkgAppsList.map {
            PackageModel(
                icon = it.loadIcon(requireActivity().packageManager),
                label = it.loadLabel(requireActivity().packageManager).toString(),
                packageName = it.activityInfo.packageName
            )
        }
    }
}