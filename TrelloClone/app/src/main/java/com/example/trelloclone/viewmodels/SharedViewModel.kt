package com.example.trelloclone.viewmodels
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.Card
import com.example.trelloclone.ui.cards.CardDetailFragment
import com.example.trelloclone.ui.cards.MyCardsFragment

class SharedViewModel(private val fireStore: Firestore) : ViewModel() {

    var cards: MutableLiveData<ArrayList<Card>> = MutableLiveData()
    var boards: MutableLiveData<ArrayList<Board>> = MutableLiveData()
    var currentCardId: String = ""
    var currentBoardId: String = ""

    init {
        getAllCardsCreatedByUser()
        //getAllBoardsCreatedByUser()
    }

    /** Card related operations */

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
    }

    /** Board related operations */
    private fun addBoard(fragment: Fragment, boardInfo: Board){
        fireStore.addBoard(fragment, boardInfo)
    }

    private fun updateBoard(fragment: Fragment, boardInfo: Board){
        fireStore.updateBoard(fragment, boardInfo)
    }

    private fun getAllBoardsCreatedByUser(){
        boards.value = fireStore.getBoards()
    }

    fun getCurrentBoard(): Board {
        return boards.value!!.filter { it.id == currentBoardId }[0]
    }

    /** List related operations */

}