package com.quanticheart.storyblok.ui.email.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quanticheart.storyblok.conn.ConnStoryblok
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmailDetailsViewModel : ViewModel() {

    private val conn = ConnStoryblok()
    val md = MutableLiveData<String>()

    fun loadLink(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            conn.getLinkDetails(id) {
                md.postValue(it)
            }
        }
    }

}