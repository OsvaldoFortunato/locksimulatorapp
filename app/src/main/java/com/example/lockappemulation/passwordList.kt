package com.example.lockappemulation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class passwordList {

    private var listPasswordList = MutableLiveData<ArrayList<String>>()

    fun geListLiveData(): LiveData<ArrayList<String>> {
        return listPasswordList
    }

    fun setPasswordList(password: ArrayList<String>) {
        listPasswordList.value = password as ArrayList<String>
    }

    fun resetPassword(){
        listPasswordList.value = ArrayList<String>()
    }
}