package com.louis.gourmandism.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
import com.louis.gourmandism.MainActivity
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.ActivityLoginBinding
import com.louis.gourmandism.extension.getVmFactory

private const val RC_SIGN_IN = 20



class LoginActivity : AppCompatActivity() {

    private val TAG = this.javaClass.name
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userPhoto : Uri
    private var userUid = ""
    private var userName = ""

    val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
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
            signInButtonEnable(false)
            signIn(mGoogleSignInClient)
        }

        viewModel.profile.observe(this, Observer {
            if(it != null){
                startActivity(Intent(this, MainActivity::class.java))
            } else{
                viewModel.createUser(userUid, userName, userPhoto)
            }
        })

        viewModel.createStatus.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
        })

    }

    private fun signIn(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                signInButtonEnable(true)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, intent to main activity with the signed-in user's information
                val user = auth.currentUser
                if (user != null) {
                    UserManager.userToken = user.uid
                    userUid = user.uid
                    userName = user.displayName.toString()
                    user.photoUrl?.let {
                        userPhoto = it
                    }
                    viewModel.getProfile(user.uid)
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG,"signInWithCredential:failure", task.exception)
                signInButtonEnable(true)
            }
        }
    }

    private fun signInButtonEnable(status: Boolean){
        binding.signInButton.isEnabled = status
    }

}