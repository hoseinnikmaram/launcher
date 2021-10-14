package com.example.launcher.ui.MainFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.launcher.databinding.FragmentMainBinding
import com.example.launcher.util.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : Fragment() {
    private val mainViewModel by sharedViewModel<MainViewModel>()
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
            mainViewModel.getInstalledPackage(requireActivity()).observe(viewLifecycleOwner) {
                val subListSize = if (it.size < 8)
                    it.size
                 else
                    8
                binding.recyclerView.adapter = PackageAdapter(it.subList(0, subListSize)) { packageName ->
                    directOpenInstalledApp(
                        packageName = packageName,
                        packageManager = activity?.packageManager,
                        context = requireContext()
                    )
                }
                binding.isVisibleProgressBar = false
            }
            binding.mainLayout.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()) {
                override fun onSwipeTop() {
                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToPackageListFragment())

                }
            })

            mainViewModel.isShowDialog(requireActivity())
        }
        return binding.root
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
