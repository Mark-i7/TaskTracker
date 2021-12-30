package com.example.trelloclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.example.trelloclone.firebase.Firestore

class ViewModelFactory(private val fireStore: Firestore) : Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(fireStore) as T
    }
}