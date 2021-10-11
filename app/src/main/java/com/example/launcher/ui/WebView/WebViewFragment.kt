package com.example.launcher.ui.WebView

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.example.launcher.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {
    lateinit var binding: FragmentWebViewBinding
    var pageError = false
    var url = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)

           //  url = WebViewFragmentArgs.fromBundle(requireArguments()).url
            binding!!.WebView.loadUrl(url)

            binding!!.WebView.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webViewClient = object : WebViewClient() {

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        if (!pageError)
                            setFinish()
                    }

                    override fun onReceivedError(
                        view: WebView,
                        errorCode: Int,
                        description: String,
                        failingUrl: String
                    ) {
                        setOffline()
                        pageError = true
                    }
                }
                setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                        //This is the filter
                        if (event!!.action != KeyEvent.ACTION_DOWN)
                            return true
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (canGoBack()) {
                                goBack()
                            } else {
                                requireActivity().onBackPressed()
                            }
                            return true;
                        }
                        return false; }
                })
            }
            binding!!.btnTryAgain.setOnClickListener {
                binding!!.WebView.loadUrl(url)
                pageError = false
                setLoading()
            }

        }
        return binding!!.root
    }

    private fun setLoading() {
            binding!!.progress.visibility = View.VISIBLE
            binding?.offlineContainer?.visibility = View.INVISIBLE
            binding?.WebView?.visibility = View.INVISIBLE

    }

    // handle visibility of offline screen
    fun setOffline() {
        binding!!.progress.visibility = View.GONE
        binding?.WebView?.visibility = View.GONE
        binding?.offlineContainer?.visibility = View.VISIBLE
    }

    private fun setFinish() {
        binding!!.progress.visibility = View.INVISIBLE
        binding?.offlineContainer?.visibility = View.INVISIBLE
        binding?.WebView?.visibility = View.VISIBLE
    }
}