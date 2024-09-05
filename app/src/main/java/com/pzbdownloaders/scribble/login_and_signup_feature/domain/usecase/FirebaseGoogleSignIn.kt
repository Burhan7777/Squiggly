import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pzbdownloaders.scribble.R
import com.pzbdownloaders.scribble.common.domain.utils.Constant
import com.pzbdownloaders.scribble.common.presentation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun googleSignInButton(navHostController: NavHostController, context: Context) {
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    val credentialManager = CredentialManager.create(context)

    // Generate a nonce and hash it with sha-256
    // Providing a nonce is optional but recommended
//        val rawNonce = UUID.randomUUID().toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
//        val bytes = rawNonce.toString().toByteArray()
//        val md = MessageDigest.getInstance("SHA-256")
//        val digest = md.digest(bytes)
//        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in


    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(context.getString(R.string.default_web_client_id_1))
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    coroutineScope.launch(Dispatchers.Main) {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            var googleAuthProvider = GoogleAuthProvider.getCredential(googleIdToken, null)


            var firebase = Firebase.auth
            firebase.signInWithCredential(googleAuthProvider).addOnSuccessListener { user ->
                var calendar = Calendar.getInstance()
                var timeInSeconds = calendar.timeInMillis / 1000L
                var remainingTime = timeInSeconds + 86400
                val dateFormatter = SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm")
                val startingTIme: String = dateFormatter.format(Date(timeInSeconds * 1000L))
                var firestore = Firebase.firestore

                var docs = firestore.collection("Users").document(user.user?.uid!!)
                var document = docs.get()
                document.addOnSuccessListener {
                    if (it.exists()) {
                        var details = it.data
                        var ongoing = details!!["trialOngoing"] as Boolean
                        if (!ongoing) {
                            Toast.makeText(
                                context,
                                "Your trial period is over",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Toast.makeText(
                                context,
                                "Your 10 days trial continues",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        var hashmap: HashMap<String, Any> = HashMap()
                        hashmap["userId"] = user.user!!.uid
                        hashmap["emailId"] = user.user!!.email!!
                        hashmap["startingTime"] = timeInSeconds
                        hashmap["remainingTime"] = remainingTime
                        hashmap["dateWhenTrialPeriodStarted"] = startingTIme
                        hashmap["trialOngoing"] = true
                        docs.set(hashmap).addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Premium activated for 10 days",
                                Toast.LENGTH_SHORT
                            ).show()
                            val sharedPreferences = context.getSharedPreferences(
                                Constant.SHARED_PREP_NAME,
                                Context.MODE_PRIVATE
                            )
                            val editor = sharedPreferences.edit()
                            editor.apply {
                                putString(Constant.USER_KEY, Constant.USER_VALUE)
                            }.apply()
                            navHostController.navigate(Screens.HomeScreen.route)
                            // checkTrialPeriod(uid, activity, navController)
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Premium failed to activate. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            // Handle successful sign-in
        } catch (e: GetCredentialException) {
            // Handle GetCredentialException thrown by `credentialManager.getCredential()`
        } catch (e: GoogleIdTokenParsingException) {
            // Handle GoogleIdTokenParsingException thrown by `GoogleIdTokenCredential.createFrom()`
        } catch (e: Exception) {
            // Handle unknown exceptions
        }
    }

}