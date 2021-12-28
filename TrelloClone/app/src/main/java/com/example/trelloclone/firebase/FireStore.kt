package com.example.trelloclone.firebase

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.Card
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.fragment.CardDetailFragment
import com.example.trelloclone.ui.fragment.MyCardsFragment
import com.example.trelloclone.ui.registration.SignUpFragment
import com.example.trelloclone.utils.AppLevelFunctions
import com.example.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Firestore {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(fragment: SignUpFragment, userInfo: User) {
        mFireStore
            .collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.userRegisteredSuccess()
            }.addOnFailureListener {
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun addCard(fragment: MyCardsFragment, cardInfo: Card) {
        mFireStore
            .collection(Constants.CARDS)
            .add(cardInfo)
            .addOnSuccessListener {
                fragment.cardAddedSuccess()
            }.addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    fun updateCardDetails(fragment: CardDetailFragment, cardInfo: Card) {
        mFireStore
            .collection(Constants.CARDS)
            .document(cardInfo.id)
            .set(cardInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.cardUpdatedSuccess()
            }.addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    fun getCardDetails(fragment: CardDetailFragment, cardId: String) {
        mFireStore
            .collection(Constants.CARDS)
            .document(cardId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.toString())
                val card = document.toObject(Card::class.java)!!
                card.id = document.id
                Log.d("CardDetails: ", card.toString())
                // TODO call a function from fragment which takes card as an argument, containing all it's data
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, e.message.toString())
            }
    }

    fun getAllCardsCreatedByUser(fragment: MyCardsFragment) {
        mFireStore
            .collection(Constants.CARDS)
            .whereEqualTo(Constants.CREATED_BY, AppLevelFunctions.getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())
                val cardsList: ArrayList<Card> = ArrayList()
                for (i in document.documents) {
                    val card = i.toObject(Card::class.java)!!
                    card.id = i.id
                    cardsList.add(card)
                }
                Log.d("MY ID: ", AppLevelFunctions.getCurrentUserID())
                Log.d("MyCards: ", cardsList.toString())
                // TODO fragment.addCardListToUI(cardsList)
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, e.message.toString())
            }
    }

    fun addBoard(fragment: Fragment, boardInfo: Board) {
        mFireStore
            .collection(Constants.BOARDS)
            .add(boardInfo)
            .addOnSuccessListener {
                // TODO: fragment.boardAddedSuccess()
            }.addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    fun updateBoard(fragment: Fragment, boardInfo: Board) {
        mFireStore
            .collection(Constants.BOARDS)
            .document(boardInfo.id)
            .set(boardInfo, SetOptions.merge())
            .addOnSuccessListener {
                // TODO: fragment.boardUpdatedSuccess()
            }.addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    fun getBoards(fragment: Fragment) {
        mFireStore
            .collection(Constants.BOARDS)
            .whereArrayContains("members", AppLevelFunctions.getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.id = i.id
                    boardList.add(board)
                }
                Log.d("MyBoards: ", boardList.toString())
                // TODO fragment.addBoardListToUI(boardsList)
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, e.message.toString())
            }
    }
}
