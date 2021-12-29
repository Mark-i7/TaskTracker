package com.example.trelloclone.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.Card
import com.example.trelloclone.ui.cards.CardDetailFragment
import com.example.trelloclone.ui.cards.MyCardsFragment

class SharedViewModel(private val fireStore: Firestore) : ViewModel() {

    var cards: MutableLiveData<ArrayList<Card>> = MutableLiveData()
    var currentCardId: String = ""

    init {
        getAllCardsCreatedByUser()
        Log.i("cards", cards.value.toString())
    }

    private fun addCard(fragment: MyCardsFragment, cardInfo: Card) {
        fireStore.addCard(fragment, cardInfo)
    }

    fun updateCardDetails(fragment: CardDetailFragment, cardInfo: Card) {
        fireStore.updateCardDetails(fragment, cardInfo)
        /** Local update */
        val currentCard = getCurrentCard()
        currentCard.startTime = cardInfo.startTime
        currentCard.startDate = cardInfo.startDate
        currentCard.dueTime = cardInfo.dueTime
        currentCard.dueDate = cardInfo.dueDate
        currentCard.details = cardInfo.details
    }

    fun getCurrentCard(): Card {
        return cards.value!!.filter { it.id == currentCardId }[0]
    }

    private fun getAllCardsCreatedByUser() {
        cards.value = fireStore.getAllCardsCreatedByUser()
        Log.i("viewmodel", cards.value.toString())
    }
}