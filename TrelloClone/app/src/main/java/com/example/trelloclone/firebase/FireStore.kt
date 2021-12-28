package com.example.trelloclone.firebase

import android.util.Log
import com.example.trelloclone.models.Card
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.fragment.CardDetailFragment
import com.example.trelloclone.ui.fragment.MyCardsFragment
import com.example.trelloclone.ui.registration.SignUpFragment
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
            .document(cardInfo.id.toString())
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
                // TODO call a function from fragment which takes card as an argument, containing all it's data
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, e.message.toString())
            }
    }

    fun getAllCardsCreatedByUser(fragment: MyCardsFragment) {
        mFireStore
            .collection(Constants.CARDS)
            .whereArrayContains(Constants.CREATED_BY, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())
                val cardsList: ArrayList<Card> = ArrayList()
                for(i in document.documents){
                    val card = i.toObject(Card::class.java)!!
                    card.id = i.id
                    cardsList.add(card)
                }

                // TODO fragment.addCardListToUI(cardsList)
            }
            .addOnFailureListener { e->
                Log.e(fragment.javaClass.simpleName, e.message.toString())
            }
    }
}
