package appjoe.wordpress.com.testdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    fun register(v: View) {
        val email = findViewById<EditText>(R.id.uname_register)
        val password = findViewById<EditText>(R.id.passwd_register)
        val verifyPassword = findViewById<EditText>(R.id.passwd_register_verify)

        registerUser(email.text.toString(), password.text.toString(), verifyPassword.text.toString())
    }

    private fun registerUser(email: String, password: String, verifyPassword: String) {
        val prefsFilename = "com.wordpress.appjoe.prefs"

        fun writePrefs(email: String, password: String) {
            val prefs = this.getSharedPreferences(prefsFilename, 0)
            val editor = prefs.edit()
            editor.putString("email", email)
            editor.putString("passwd", password)
            // status 1: logged in
            editor.putInt("status", 1)
            editor.apply()
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.trim().isNotEmpty()) {
            if (password.isNotEmpty()) {
                if (password == verifyPassword) {
                    writePrefs(email, password)

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords can't be empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "email is not valid", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
