package com.example.trelloclone.firebase

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.trelloclone.MainActivity
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.Card
import com.example.trelloclone.models.TaskList
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.cards.CardDetailFragment
import com.example.trelloclone.ui.cards.MyCardsFragment
import com.example.trelloclone.ui.home.HomeFragment
import com.example.trelloclone.ui.login.LoginFragment
import com.example.trelloclone.ui.members.MembersFragment
import com.example.trelloclone.ui.registration.SignUpFragment
import com.example.trelloclone.utils.AppLevelFunctions
import com.example.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Firestore {

    private val mFireStore = FirebaseFirestore.getInstance()


    /**
     * USER RELATED DB FUNCTIONS
     */

    /**
     * Function to manage the registration of a new user
     * uses basic firebaseauthentication method
     */
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

    /**
     * Creating a user id for the registration
     */
    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun loadUserData(caller: Any){
        mFireStore
            .collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                Log.d("LOGGEDINUSER", loggedInUser.toString())

                when (caller) {
                    is MainActivity -> caller.updateNavigationUserDetails(loggedInUser)
                    is LoginFragment -> {
                        val mainActivity: MainActivity = caller.activity as MainActivity
                        mainActivity.updateNavigationUserDetails(loggedInUser)
                    }
                    is HomeFragment -> {
                        val mainActivity: MainActivity = caller.activity as MainActivity
                        mainActivity.updateNavigationUserDetails(loggedInUser)
                    }
                }
            }
            .addOnFailureListener {
                Log.e(caller.javaClass.simpleName, "Error getting documents")
            }
    }

    fun updateUserPhoto(imageUri: String) {
        mFireStore
            .collection(Constants.USERS)
            .document(getCurrentUserId())
            .update("image", imageUri)
            .addOnSuccessListener {
                Log.d("Pic updated", "SUCCESS")
            }
            .addOnFailureListener {
                Log.d("FAILURE", "Pic not updated")
            }
    }


    fun getMemberDetails(fragment: MembersFragment, email: String) {
        mFireStore
            .collection(Constants.USERS)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { document ->
                if(document.documents.size > 0) {
                    val user = document.documents[0].toObject(User::class.java)!!
                    fragment.memberDetails(user)
                }
            }
            .addOnFailureListener {
                Log.e("ERROR", "Couldn't get details for member with given email")
            }
    }

    fun assignMemberToBoard(fragment: MembersFragment, board: Board, user: User) {
        mFireStore
            .collection(Constants.BOARDS)
            .document(board.id)
            .update("members", board.members)
            .addOnSuccessListener {
                fragment.memberAssignSuccess(user)
            }
            .addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, it.message.toString())
            }
    }

    /**
     * CARD RELATED DB FUNCTIONS
     */

    /**
     * Adding a new card in the database
     */
    fun addCard(cardInfo: Card) {
        mFireStore
            .collection(Constants.CARDS)
            .add(cardInfo)
            .addOnSuccessListener {
                Log.i("addCard", "card added successfully")
            }.addOnFailureListener {
                Log.e("addCard", "error")
            }
    }

    /**
     * Function to update the details of a card based on id
     */
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

    /**
     * Getting the cards created by the logged in user
     * They can be viewed in the MyCards section of the app
     */
    fun getAllCardsCreatedByUser() : ArrayList<Card> {
        val cardsList: ArrayList<Card> = ArrayList()
        mFireStore
            .collection(Constants.CARDS)
            .whereEqualTo(Constants.CREATED_BY, AppLevelFunctions.getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                for(i in document.documents) {
                    val card = i.toObject(Card::class.java)!!
                    card.id = i.id
                    cardsList.add(card)
                }
            }
            .addOnFailureListener { e ->
                Log.i("tag", e.message.toString())
            }
        return cardsList
    }

    /**
     * Function to delete one particular card from the database
     */
    fun deleteCard(card: Card){
        mFireStore
            .collection(Constants.CARDS)
            .document(card.id)
            .delete()
            .addOnSuccessListener {
                Log.i("Card_Del", "Card deleted")
            }
            .addOnFailureListener {
                Log.i("Card_Del","Some error occurred")
            }
    }


    /**
     * BOARD RELATED DB FUNCTIONS
     */

    /**
     * Function to add a new board created by the user in to the db with generated id
     */
    fun addBoard(boardInfo: Board) {
        mFireStore
            .collection(Constants.BOARDS)
            .add(boardInfo)
            .addOnSuccessListener {
                // TODO: fragment.boardAddedSuccess()
            }.addOnFailureListener {
                Log.e("addBoard", "Error writing documents")
            }
    }

    /**
     * Function to update a board's details
     * Based on the board id
     */
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

    /**
     * Function to get the boards for the current logged in user
     * shows all the boards which he is member in
     */
    fun getBoards(callback : FirebaseCallbackBoards) {
        val boardList: ArrayList<Board> = ArrayList()
        mFireStore
            .collection(Constants.BOARDS)
            .whereArrayContains("members", AppLevelFunctions.getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i("getBoards", document.documents.toString())
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.id = i.id
                    boardList.add(board)
                }
                Log.d("MyBoards: ", boardList.toString())
                callback.onResponse(boardList)
            }
            .addOnFailureListener { e ->
                Log.e("getBoards", e.message.toString())
            }
    }

    /**
     * Function to delete one particular board from the database by id
     */
    fun deleteBoard(board: Board){
        mFireStore
            .collection(Constants.BOARDS)
            .document(board.id)
            .delete()
            .addOnSuccessListener {
                Log.i("Board_Del", "Board deleted")
            }
            .addOnFailureListener {
                Log.i("Board_Del","Some error occurred")
            }
    }


    /**
     * TASKLIST RELATED DB FUNCTIONS
     */

    /**
     * Function to add a new list in the db
     */
    fun addList(list:TaskList)  {
        mFireStore
            .collection(Constants.LIST)
            .add(list)
            .addOnSuccessListener {
                Log.i("List_Add", "List added successfully")
            }
            .addOnFailureListener {
                Log.i("List_Add","Some error occurred")
            }
    }

    /**
     * Function to rename a list in DB
     */
    fun renameList(list: TaskList) {
        mFireStore
            .collection(Constants.LIST)
            .document(list.id)
            .update("listName", list.listName)
            .addOnSuccessListener {
                Log.i("List_Rename", "List renamed successfully")
            }
            .addOnFailureListener {
                Log.i("List_Rename", "Some error occurred")
            }
    }

    /**
     * Function to delete a list from db
     */
    fun deleteList(list: TaskList) {
        mFireStore
            .collection(Constants.LIST)
            .document(list.id)
            .delete()
            .addOnSuccessListener {
                Log.i("List_Del", "List deleted")
            }
            .addOnFailureListener {
                Log.i("List_Del","Some error occurred")
            }
    }

    /**
     * Function to get lists for one specific boardId
     */
    fun getListsForBoard(boardId: String, callback : FirebaseCallbackTasks) {
        val taskList: ArrayList<TaskList> = ArrayList()
        mFireStore
            .collection(Constants.LIST)
            .whereEqualTo(Constants.BOARD_ID, boardId)
            .get()
            .addOnSuccessListener { document ->
                for(i in document.documents) {
                    val list = i.toObject(TaskList::class.java)!!
                    list.id = i.id
                    taskList.add(list)
                }
                Log.i("taskList", taskList.toString())
                callback.onResponse(taskList)
            }
            .addOnFailureListener { e ->
                Log.i("tag", e.message.toString())
            }
    }
}

interface FirebaseCallbackTasks {
    fun onResponse(list : ArrayList<TaskList>)
}

interface FirebaseCallbackBoards {
    fun onResponse(list : ArrayList<Board>)
}


