package com.pzbapps.squiggly.common.presentation

const val HOME_GRAPH = "home_graph"
const val AUTH_GRAPH = "auth_graph"

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")
    object AddNoteScreen : Screens("add_note_screen")
    object EditNoteScreen : Screens("edit_note_screen/{id}/{screen}?query={query}") {
        fun editNoteWithId(id: Int, screen: String): String {
            return "edit_note_screen/$id/$screen "
        }

        fun editNoteWithSearchQuery(id: Int, screen: String, query: String): String {
            return "edit_note_screen/$id/$screen?query=$query"
        }
    }

    object LoginScreen : Screens("login_screen")
    object SignUpScreen : Screens("signup_screen")

    object SplashScreen : Screens("splash_screen")
    object SettingsScreen : Screens("settings_screen")
    object ArchiveScreen : Screens("archive_screen")

    object LockedNotesScreen : Screens("locked_notes_screen")

    object SearchScreen : Screens("search_screen/{queryText}/{screen}") {
        fun searchNoteWIthScreen(queryText: String, screen: String): String {
            return "search_screen/$queryText/$screen"
        }
    }

    object AboutUsScreen : Screens("about_us")

    object NotebookMainScreen : Screens("notebook_main_screen/{title}") {
        fun notebookWithTitle(title: String): String {
            return "notebook_main_screen/$title"
        }
    }

    object CheckboxMainScreen : Screens("checkbox_main_screen")

    object BulletPointMainScreen : Screens("bullet_point_screen")

    object TrashBinScreen : Screens("trash_bin_screen")

    object DeleteTrashScreen : Screens("delete_trash_screen/{id}") {
        fun deleteTrashScreenWithId(id: Int): String {
            return "delete_trash_screen/$id"
        }
    }

    object AddNoteInNotebookScreen : Screens("add_note_in_notebook_screen/{notebookName}") {
        fun addNoteBookWIthName(notebookName: String): String {
            return "add_note_in_notebook_screen/$notebookName"
        }
    }

    object AddNoteInLockedScreen : Screens("add_note_in_locked_screen")
    object BackupAndRestoreScreen : Screens("backup_and_restore_screen")
    object CheckboxNotebookMainScreen : Screens("checkbox_notebook_main_screen/{notebook}") {
        fun checkboxNotebookMainScreenWithNotebook(notebook: String): String {
            return "checkbox_notebook_main_screen/$notebook"
        }
    }

    object BulletPointsNotebook : Screens("bullet_points_notebook/{notebook}") {
        fun bulletPointsWithNotebook(notebook: String): String {
            return "bullet_points_notebook/$notebook"
        }
    }

    object CheckBoxLockedNotesMainScreen : Screens("checkbox_locked_notes_main_screen")
    object BulletPointsLockedNotesMainScreen : Screens("bullet_points_locked_notes_main_screen")
    object PrivacyPolicy : Screens("privacy_policy")
    object FeedbackScreen : Screens("feedback_screen")
    object ReportBugScreen : Screens("report_bug_screen")
}