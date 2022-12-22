package com.quanticheart.storyblok

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quanticheart.storyblok.conn.ConnStoryblok
import com.quanticheart.storyblok.conn.model.BottomMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val conn = ConnStoryblok()
    val list = MutableLiveData<List<BottomMenu>>()

    fun loadBottomMenu() {
        CoroutineScope(Dispatchers.IO).launch {
            conn.getBottonMenu {
                Log.e("TEST", "$it")
                list.postValue(it)
            }
        }
    }
}