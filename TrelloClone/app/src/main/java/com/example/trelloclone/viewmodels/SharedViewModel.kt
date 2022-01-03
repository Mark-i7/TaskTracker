package com.example.trelloclone.viewmodels
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trelloclone.firebase.*
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.Card
import com.example.trelloclone.models.TaskList
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.cards.CardDetailFragment
import kotlin.collections.ArrayList


class SharedViewModel(private val fireStore: Firestore) : ViewModel() {

    var cards: MutableLiveData<ArrayList<Card>> = MutableLiveData()
    var boards: MutableLiveData<ArrayList<Board>> = MutableLiveData()
    var taskLists : MutableLiveData<ArrayList<TaskList>> = MutableLiveData()
    var currentCardId: String = ""
    var currentBoardId: String = ""
    var currentAssignedMembersList: ArrayList<User> = arrayListOf()

    init {
        getAllCardsCreatedByUser()
        getAllBoardsCreatedByUser()
    }

    /** Card related operations */

    fun addCard(cardInfo: Card) {
        fireStore.addCard(cardInfo)
        cards.value!!.add(cardInfo)
    }

    fun updateCardDetails(fragment: CardDetailFragment, cardInfo: Card) {
        fireStore.updateCardDetails(fragment, cardInfo)
        /** Local update */
        val currentCard = getCurrentCard()
        currentCard.image = cardInfo.image
        currentCard.startTime = cardInfo.startTime
        currentCard.startDate = cardInfo.startDate
        currentCard.dueTime = cardInfo.dueTime
        currentCard.dueDate = cardInfo.dueDate
        currentCard.details = cardInfo.details
        currentCard.description = cardInfo.description
    }

    fun getCurrentCard(): Card {
        return cards.value!!.filter { it.id == currentCardId }[0]
    }

    private fun getAllCardsCreatedByUser() {
        cards.value = fireStore.getAllCardsCreatedByUser()
    }

    fun deleteCard(card: Card){
        fireStore.deleteCard(card)
    }

    fun appendCardList(board: Board) {
        fireStore.getAllCardsByBoard(board.id, object: FirebaseCallbackCards {
            override fun onResponse(list: ArrayList<Card>) {
                list.forEach {
                    if (!cards.value!!.contains(it)) {
                        cards.value!!.add(it)
                    }
                }
            }
        })
    }
    /** Board related operations */

    fun addBoard(boardInfo: Board){
        fireStore.addBoard(boardInfo)
        boards.value!!.add(boardInfo)
    }

    private fun updateBoard(fragment: Fragment, boardInfo: Board){
        fireStore.updateBoard(fragment, boardInfo)
    }

    fun getAllBoardsCreatedByUser(){
        fireStore.getBoards(object : FirebaseCallbackBoards{
            override fun onResponse(list: ArrayList<Board>) {
                boards.value = list
                list.forEach {
                    appendCardList(it)
                }
            }
        })

    }

    fun getCurrentBoard(): Board {
        val currentBoard = boards.value!!.filter { it.id == currentBoardId }[0]
        currentBoard.members.map {
            (fireStore.getUserById(it, object: FirebaseCallbackUser {
                override fun onResponse(usr: User) {
                    if (!currentAssignedMembersList.contains(usr)) {
                        currentAssignedMembersList.add(usr)
                    }
                }
            }))
        }
        return currentBoard
    }

    fun deleteBoard(board: Board){
       fireStore.deleteBoard(board)
    }

    /** List related operations */

    fun addList(list: TaskList){
        fireStore.addList(list)
        taskLists.value!!.add(list)
    }

    private fun renameList(list: TaskList){
        fireStore.renameList(list)
    }

    private fun deleteList(list: TaskList){
        fireStore.deleteList(list)
    }

    fun getListsForBoard(boardId: String){
        fireStore.getListsForBoard(boardId, object : FirebaseCallbackTasks {
            override fun onResponse(list: ArrayList<TaskList>) {
                taskLists.value = list
            }
        })
    }
}