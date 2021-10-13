package com.example.launcher.ui.MainFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.launcher.R
import com.example.launcher.databinding.FragmentMainBinding
import com.example.launcher.model.PackageModel
import com.example.launcher.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModel()
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentMainBinding.inflate(inflater, container, false)
            binding.editSearch.apply {
                setOnEnterListener {
                    hideKeyboardFrom(requireContext(), requireView())
                    actionSearch(text.toString())
                }
            }
            binding.icSearch.setOnClickListener { actionSearch(binding.editSearch.text.toString()) }
            binding.recyclerView.adapter = PackageAdapter(getInstalledPackage()){
                directOpenInstalledApp(packageName = it)
            }
            binding.mainLayout.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
                override fun onSwipeTop() {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToPackageListFragment())

                }
            })

                val isShowDialog = defaultCache()[KEY_IS_SHOW_DIALOG,false]
                if (isShowDialog == false){
                    defaultCache()[KEY_IS_SHOW_DIALOG] = true
                    showDialog()
                }
            }
        return binding.root
    }

    private fun showDialog() {
        if (AssistantUtil.getCurrentAssistWithReflection(requireContext()) != KEY_PACKAGE_NAME_ZAREBIN) {
            requireActivity().showDialog(
                getString(R.string.assistant_title),
                getString(R.string.assistant_description),
                positiveCallback = {
                    startActivity(Intent(Settings.ACTION_VOICE_INPUT_SETTINGS))
                })
        }
        if (checkBrowser(activity?.packageManager).toString() != KEY_PACKAGE_NAME_ZAREBIN) {
            requireActivity().showDialog(
                getString(R.string.browser_title),
                getString(R.string.browser_description),
                positiveCallback = {
                    startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
                })
        }
    }

    private fun actionSearch(textSearch: String) {
        val text = textSearch.trim().replace(" ", "%20")
        if (text.isEmpty())
            return
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://zarebin.ir/search?q=$text"))
        startActivity(browserIntent)
    }

    private fun getInstalledPackage(): List<PackageModel> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = requireActivity().packageManager.queryIntentActivities(mainIntent, 0)
        return pkgAppsList.map {
            PackageModel(
                icon = it.loadIcon(requireActivity().packageManager),
                label = it.loadLabel(requireActivity().packageManager).toString(),
                packageName = it.activityInfo.packageName
            )
        }.subList(0,8)
    }
    private fun directOpenInstalledApp(packageName: String) {
        val pm = activity?.packageManager
        val intent = pm?.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent);
        }
    }
}
