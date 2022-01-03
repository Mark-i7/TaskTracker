package com.example.trelloclone.models

open class BaseClass(
    var id: String,
    var viewType: Int
) {
    override fun toString(): String {
        return "BaseClass(id='$id', viewType=$viewType)"
    }
}

