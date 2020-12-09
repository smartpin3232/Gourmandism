package com.louis.gourmandism

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.louis.gourmandism.databinding.ActivityLoginBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.UserManager

private const val RC_SIGN_IN = 20



class LoginActivity : AppCompatActivity() {
    private val TAG = this.javaClass.name
    private lateinit var binding: ActivityLoginBinding
    // FirebaseAuth
    private lateinit var auth: FirebaseAuth
    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        // Initialize Firebase Auth
        auth = Firebase.auth
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.signInButton.setOnClickListener{
            signIn(mGoogleSignInClient)
        }
    }
    private fun signIn(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            account.idToken?.let { firebaseAuthWithGoogle(it) }
            // Signed in successfully, show authenticated UI.
            val firebaseUser = Firebase.auth.currentUser
            firebaseUser?.let {
                // displayName, email, and profile photo Url
                // Check if user's email is verified: isEmailVerified
                UserManager.userToken = firebaseUser.uid

            }
            Log.d("firebaseUser", "name = ${firebaseUser?.displayName}, email = ${firebaseUser?.email}, uid = ${firebaseUser?.uid}")

            val intent = Intent(this, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean("loginStatus",true)
            intent.putExtra("bundle",bundle)
            startActivity(intent)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
    }
}