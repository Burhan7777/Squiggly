package com.pzbapps.squiggly.add_note_feature.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pzbapps.squiggly.add_note_feature.domain.model.GetNoteBook
import kotlinx.coroutines.tasks.await

class GetNoteBookUseCase {

    suspend fun getNoteBook(): ArrayList<GetNoteBook?> {
        val listOfNoteBooks = ArrayList<GetNoteBook?>()
        val fireStore = Firebase.firestore
        val querySnapshot = fireStore.collection("Notebooks").whereEqualTo(
            "user_id",
            FirebaseAuth.getInstance().currentUser?.uid
        ).get().addOnSuccessListener {

        }.addOnFailureListener {

        }.await()
        for (i in querySnapshot.documents) {
            val notebook = i.toObject(GetNoteBook::class.java)
            listOfNoteBooks.add(notebook)
        }
        return listOfNoteBooks
    }
}