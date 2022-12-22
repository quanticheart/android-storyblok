package com.quanticheart.storyblok.ui.email

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quanticheart.storyblok.conn.ConnStoryblok
import com.quanticheart.storyblok.conn.model.Links
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmailViewModel : ViewModel() {

    private val conn = ConnStoryblok()
    val list = MutableLiveData<List<Links>>()

    fun loadLinks() {
        CoroutineScope(Dispatchers.IO).launch {
            conn.getLinks {
                Log.e("TEST", "$it")
                list.postValue(it)
            }
        }
    }

}