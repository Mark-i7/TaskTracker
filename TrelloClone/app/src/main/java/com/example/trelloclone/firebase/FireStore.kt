package com.example.trelloclone.firebase

import android.util.Log
import com.example.trelloclone.models.Card
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.cards.CardDetailFragment
import com.example.trelloclone.ui.cards.MyCardsFragment
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
            .document(cardInfo.id)
            .set(cardInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.cardUpdatedSuccess()
            }.addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    fun getAllCardsCreatedByUser() : ArrayList<Card> {
        val cardsList: ArrayList<Card> = ArrayList()
        mFireStore
            .collection(Constants.CARDS)
            .whereEqualTo(Constants.CREATED_BY, "fylXMPm624SCKjRzxGcjlCG7NG33")
            .get()
            .addOnSuccessListener { document ->
                Log.i("tag", document.documents.toString())
                for(i in document.documents){
                    val card = i.toObject(Card::class.java)!!
                    card.id = i.id
                    cardsList.add(card)
                }
                Log.d("MY ID: ", "fylXMPm624SCKjRzxGcjlCG7NG33")
                Log.d("MyCards1: ", cardsList.toString())
            }
            .addOnFailureListener { e->
                Log.e("tag", e.message.toString())
            }
        Log.d("MyCards2: ", cardsList.toString())
        return cardsList
    }
}
