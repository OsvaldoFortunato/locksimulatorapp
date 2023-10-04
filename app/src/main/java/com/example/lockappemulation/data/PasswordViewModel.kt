package com.example.lockappemulation.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class PasswordViewModel (context:Context){

    private var androidViewModel: AndroidViewModel = AndroidViewModel(context.applicationContext as Application)
    private var passwordDao : PasswordDao = LockDatabase.getInstance(context).passwordDao

    fun getCount() : Deferred<Long>{

        val count = androidViewModel.viewModelScope.async(Dispatchers.IO){
            val count = passwordDao.countTotal()

            return@async count
        }
        return count
    }

    fun insert(password: Password) : Deferred<Boolean>{
        val result = androidViewModel.viewModelScope.async(Dispatchers.IO){
            try {
                passwordDao.insert(password)
                return@async true
            }
            catch (e:Exception){
                return@async false
            }
        }
        return result
    }

    fun getPassword():Deferred<List<Password>>{
        val result =  androidViewModel.viewModelScope.async (Dispatchers.IO){
            return@async passwordDao.getAll()
        }
        return result
    }
}