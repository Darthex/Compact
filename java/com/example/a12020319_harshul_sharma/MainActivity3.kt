package com.example.a12020319_harshul_sharma

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class MainActivity3 : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var pb:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme
        setContentView(R.layout.login)

        val edtEmail:EditText = findViewById(R.id.email)
        val edtPassword:EditText = findViewById(R.id.password)
        val loginButton:Button = findViewById(R.id.login)
        val signupButton:TextView = findViewById(R.id.signup)
        val cl:ConstraintLayout = findViewById(R.id.constlayout)
        pb = findViewById(R.id.idPBLoading)
        //
        mAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {

            if(edtEmail.text.toString() != "" && edtPassword.text.toString() != "") {
                pb.visibility = View.VISIBLE
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                hideKeyboard(cl)
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                login(email, password)
                //get username from database and display on toast
                //Toast.makeText(this,"Welcome ${uname} !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Empty Fields", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }
    }

    private fun login(email:String, password:String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, MainActivity2::class.java)
                    finish()
                    pb.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    pb.visibility = View.GONE
                    Toast.makeText(this, "Please Check Your Credentials", Toast.LENGTH_SHORT).show()
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}