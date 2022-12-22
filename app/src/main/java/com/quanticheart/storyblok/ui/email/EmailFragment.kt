package com.quanticheart.storyblok.ui.email

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.quanticheart.storyblok.databinding.FragmentEmailBinding
import com.quanticheart.storyblok.ui.email.details.EmailDetailsActivity

class EmailFragment : Fragment() {

    private var _binding: FragmentEmailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this)[EmailViewModel::class.java]

        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val ad = EmailAdapter(binding.textHome)
        ad.setOnClickListener {
            activity?.startActivity(Intent(activity, EmailDetailsActivity::class.java).apply {
                putExtra("id", it)
            })
        }
        viewModel.list.observe(viewLifecycleOwner) {
            ad.setData(it)
        }
        viewModel.loadLinks()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}