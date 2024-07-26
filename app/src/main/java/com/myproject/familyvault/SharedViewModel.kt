package com.myproject.familyvault

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var dataCollected = MutableLiveData<Boolean>()

}