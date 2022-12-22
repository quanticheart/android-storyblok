package com.quanticheart.storyblok.ui.email.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.quanticheart.storyblok.databinding.ActivityEmailDetailsBinding

class EmailDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailDetailsBinding
    private lateinit var viewModel: EmailDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[EmailDetailsViewModel::class.java]
        viewModel.loadLink(intent.extras?.getString("id") ?: "")
        viewModel.md.observe(this) {
            binding.mdView.setMarkdown(it, true)
        }
    }
}
