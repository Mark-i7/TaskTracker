package com.example.trelloclone.model

class Card ( id : Int,
             val cardName : String,
             val dueDate : String,
             val imageId : Int,
             viewType : Int) : BaseClass(id, viewType)
