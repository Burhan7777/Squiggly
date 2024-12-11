package com.pzbapps.squiggly.common.presentation

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.add_note_feature.data.repository.InsertNoteRepository
import com.pzbapps.squiggly.add_note_feature.domain.model.AddNote
import com.pzbapps.squiggly.add_note_feature.domain.model.GetNoteBook
import com.pzbapps.squiggly.add_note_feature.domain.usecase.AddNoteUseCase
import com.pzbapps.squiggly.add_note_feature.domain.usecase.AddNotebookUseCase
import com.pzbapps.squiggly.add_note_feature.domain.usecase.GetNoteBookUseCase
import com.pzbapps.squiggly.archive_notes_feature.domain.GetArchiveNotesUseCase
import com.pzbapps.squiggly.common.data.Model.NoteBook
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.domain.utils.GetResult
import com.pzbapps.squiggly.edit_note_feature.data.repository.EditNoteRepository
import com.pzbapps.squiggly.edit_note_feature.domain.usecase.*
import com.pzbapps.squiggly.login_and_signup_feature.domain.usecase.AuthenticationSignInUseCase
import com.pzbapps.squiggly.login_and_signup_feature.domain.usecase.AuthenticationSignUpUseCase
import com.pzbapps.squiggly.login_and_signup_feature.domain.usecase.SignInUserCase
import com.pzbapps.squiggly.login_and_signup_feature.domain.usecase.SignUpUserCase
import com.pzbapps.squiggly.main_screen.data.repository.NoteRepository
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.main_screen.domain.usecase.GetNotesUseCase
import com.pzbapps.squiggly.notebook_main_screen.data.NotebookRepository
import com.pzbapps.squiggly.notebook_main_screen.domain.GetNotebookNotesUseCase
import com.pzbapps.squiggly.search_main_screen_feature.domain.usecase.GetArchiveSearchResultUseCase
import com.pzbapps.squiggly.search_main_screen_feature.domain.usecase.GetSearchResultUseCase
import com.pzbapps.squiggly.settings_feature.screen.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val insertNoteRepository: InsertNoteRepository,
    private val noteRepository: NoteRepository,
    private val editNoteRepository: EditNoteRepository,
    private val notebookRepository: NotebookRepository,
    private val settingsRepository: SettingsRepository,
    private val authenticationSignUpUseCase: AuthenticationSignUpUseCase,
    private val signUpUserCase: SignUpUserCase,
    private val authenticationSignInUseCase: AuthenticationSignInUseCase,
    private val signInUserCase: SignInUserCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getArchiveNotesUseCase: GetArchiveNotesUseCase,
    private val archiveNoteUseCase: ArchiveNoteUseCase,
    private val unArchiveNoteUseCase: UnArchiveNoteUseCase,
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val getArchiveSearchResultUseCase: GetArchiveSearchResultUseCase,
    private val addNotebookUseCase: AddNotebookUseCase,
    private val getNotebookUseCase: GetNoteBookUseCase,
    private val getNotebookNotesUseCase: GetNotebookNotesUseCase,
    private var application: Application
) : AndroidViewModel(application) {

    var listOfNotes = mutableStateListOf<Note>()
        private set

    var listOfNotesLiveData = MutableLiveData<ArrayList<Note>>()

    var listOfNotesByNotebook = mutableStateListOf<Note>()
        private set

    var listOfNotesByDataModified = MutableLiveData<ArrayList<Note>>()

    var listOfNotesByNotebookLiveData = MutableLiveData<ArrayList<Note>>()

    var listOfNoteBooks = mutableStateListOf<NoteBook>()

    var notebooks =
        mutableStateListOf<String>() // These are the notebooks displayed in the drop down menu in the "ADD NOTE" screen

    var getNote = mutableStateOf(Note())
        private set

    var getNoteById = mutableStateOf<Note>(Note())

    var getNoteByIdLivData = MutableLiveData<Note>()
    var getNoteByIdLivData2 = MutableLiveData<Note>()


    var getResultFromSignUp: MutableLiveData<String> = MutableLiveData<String>()
        private set

    var getResultFromSignIn: MutableLiveData<String> = MutableLiveData()
        private set

    var addNoteToCloud = MutableLiveData<GetResult>()
        private set

    var getResultToShowNotes = MutableLiveData<GetResult>()
        private set

    var getListOfNotesToShow: MutableLiveData<SnapshotStateList<AddNote>> =
        MutableLiveData()
        private set

    var getResultFromEditNote = MutableLiveData<GetResult>()
        private set

    var getNoteDetailsToEdit = MutableLiveData<AddNote>()
        private set

    var getResultFromUpdateNote = MutableLiveData<GetResult>()
        private set

    var pairNotes: Pair<MutableLiveData<GetResult>, SnapshotStateList<AddNote>?> =
        Pair(getResultToShowNotes, getListOfNotesToShow.value)

    var getResultFromDeleteNote = MutableLiveData<GetResult>()
        private set

    var getResultFromArchivedNotes = MutableLiveData<GetResult>()
        private set

    var getArchivedNotes: MutableLiveData<SnapshotStateList<AddNote>> =
        MutableLiveData()
        private set

    var getResultFromUnArchiveNotes = MutableLiveData<GetResult>()
        private set

    var getSearchResult = MutableLiveData<ArrayList<AddNote>>()
        private set

    var getArchiveSearchResult = MutableLiveData<ArrayList<AddNote>>()
        private set

    var getNotebookNotes = MutableLiveData<ArrayList<AddNote?>>()
        private set

    val getNoteBooks = MutableLiveData<ArrayList<GetNoteBook?>>()

    val getNoteBookByName = MutableLiveData<NoteBook>()

    var generatedNoteId = MutableLiveData<Long>()

    var listOfLockedNotebooksNote =
        mutableStateListOf<Note>() // THIS IS STORED IN VIEWMODEL BECAUSE AFTER OPENING THE NOTE AND MOVING BACK IT WILL BE EMPTY

    var showLockedNotes =
        mutableStateOf(false)

    var timeInString = mutableStateOf("")

    /*  var getListOfNotesToShow = mutableListOf<AddNote>()
          private set
  */
    val prefs: SharedPreferences =
        application.getSharedPreferences(Constant.LIST_PREFERENCE, Context.MODE_PRIVATE)
    val name = prefs.getBoolean(Constant.LIST_OR_GRID, false)

    var showGridOrLinearNotes = mutableStateOf(name)

    var dateCreatedOldestFirst = mutableStateOf(false)
    var dateModifiedNewestFirst = mutableStateOf(false)

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            generatedNoteId.postValue(insertNoteRepository.insertNote(note))
        }
    }

    var _listOfNotesFlow = MutableStateFlow<ArrayList<Note>>(arrayListOf())
    var listOFNotesFLow: StateFlow<ArrayList<Note>> = _listOfNotesFlow
    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfNotes = noteRepository.getAllNotes().toMutableStateList()
            listOfNotesLiveData.postValue(noteRepository.getAllNotes().toCollection(ArrayList()))
            _listOfNotesFlow.value = noteRepository.getAllNotes().toCollection(ArrayList())

        }
    }

    fun getAllNotesByDateModified() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfNotesByDataModified.postValue(
                noteRepository.getAllNotesByDateModified().toCollection(ArrayList())
            )
        }
    }


    fun getAllNotesByNotebook(notebook: String) {
        viewModelScope.launch(Dispatchers.IO) {
            listOfNotesByNotebook = noteRepository.getNotesByNoteBook(notebook).toMutableStateList()
            listOfNotesByNotebookLiveData.postValue(
                noteRepository.getNotesByNoteBook(notebook).toCollection(ArrayList())
            )
        }
    }

    private val _getNoteByIdFlow = MutableStateFlow<Note>(Note())
    val getNoteByIdFlow: StateFlow<Note?> = _getNoteByIdFlow

    fun getNoteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getNoteById.value = editNoteRepository.getNotesById(id)
            //getNoteByIdLivData.postValue(editNoteRepository.getNotesById(id))
            _getNoteByIdFlow.value = editNoteRepository.getNotesById(id)
            getNoteByIdLivData2.postValue(editNoteRepository.getNotesById(id))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            editNoteRepository.updateNote(note)
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            editNoteRepository.deleteNote(id)
        }
    }

    fun deleteTrashNote(deletedNote: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            editNoteRepository.deleteTrashNote(deletedNote)
        }
    }

    fun authenticationSignUp(
        email: String,
        password: String,
        repeatedPassword: String,
        isChecked: Boolean
    ): String {
        return authenticationSignUpUseCase.checkAuth(
            email,
            password,
            repeatedPassword,
            isChecked
        )
    }

    fun authenticationSignIn(
        email: String,
        password: String
    ): String {
        return authenticationSignInUseCase.authenticateSignIn(email, password)
    }

    fun signInUser(email: String, password: String) {
        getResultFromSignIn = signInUserCase.signInUser(email, password)
    }

    fun signUpUser(email: String, password: String) {
        getResultFromSignUp = signUpUserCase.signUpUser(email, password)
    }

    fun addNoteToCloud(addNote: AddNote) {
        addNoteToCloud = addNoteUseCase.addNote(addNote)
    }

    fun getNotesToShow() {
        viewModelScope.launch {
            pairNotes = getNotesUseCase.getNotes()
            getResultToShowNotes = pairNotes.first
            getListOfNotesToShow.value = pairNotes.second
            Log.i("model", getListOfNotesToShow.value.toString())
        }
    }

    fun getNoteToEdit(noteId: String) {
        viewModelScope.launch {
            var pair = editNoteUseCase.getNoteToEdit(noteId)
            getResultFromEditNote.value = pair.first
            getNoteDetailsToEdit.value = pair.second
        }
    }

    fun updateNote(noteId: String, map: HashMap<String, Any>) {
        getResultFromUpdateNote = updateNoteUseCase.updateNote(noteId, map)
        Log.i("result", getResultFromUpdateNote.value.toString())
    }

    fun deleteNote(noteId: String) {
        getResultFromDeleteNote = deleteNoteUseCase.deleteNote(noteId)
    }

    fun getArchivedNotes() {
        viewModelScope.launch {
            var pair = getArchiveNotesUseCase.getArchivedNotes()
            getResultFromArchivedNotes = pair.first
            getArchivedNotes.value = pair.second
            Log.i("model", getListOfNotesToShow.value.toString())
        }
    }

    fun archiveNotes(notesId: String, map: HashMap<String, Any>) {
        getResultFromArchivedNotes = archiveNoteUseCase.archiveNote(notesId, map)
    }

    fun unArchiveNotes(notesId: String, map: HashMap<String, Any>) {
        getResultFromUnArchiveNotes = unArchiveNoteUseCase.unarchiveNote(notesId, map)
    }

    fun getSearchResult(searchQuery: String) {
        viewModelScope.launch {
            getSearchResult.value = getSearchResultUseCase.getSearchResult(searchQuery)
        }

    }

    fun getArchiveSearchResult(searchQuery: String) {
        viewModelScope.launch {
            getArchiveSearchResult.value =
                getArchiveSearchResultUseCase.getArchiveSearchResult(searchQuery)
        }
    }

    fun addNoteBook(notebook: NoteBook) {
        viewModelScope.launch(Dispatchers.IO) {
            insertNoteRepository.addNoteBook(notebook)
        }
    }

    fun getAllNotebooks() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfNoteBooks = insertNoteRepository.getAllNoteBooks().toMutableStateList()
            withContext(Dispatchers.Main) {
                notebooks.clear()
                notebooks.add("Add Notebook")
                for (i in listOfNoteBooks) {
                    notebooks.add(i.name)
                }
            }
        }
    }

    fun convertAllNotebooksIntoArrayList() {

    }

    fun getNoteBook() {
        viewModelScope.launch {
            getNoteBooks.value = getNotebookUseCase.getNoteBook()
        }
    }

    fun getNotebookNote(notebook: String) {
        viewModelScope.launch {
            getNotebookNotes.value = getNotebookNotesUseCase.getNotebookNote(notebook)
        }
    }

    fun deleteNotebook(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notebookRepository.deleteNotebook(name)
        }
    }

    fun updateNotebook(notebook: NoteBook) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateNoteBook(notebook)
        }
    }

    fun getNotebookByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getNoteBookByName.postValue(settingsRepository.getNotebookByName(name))
        }
    }

    fun addTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            insertNoteRepository.addTag(tag)
        }
    }


}