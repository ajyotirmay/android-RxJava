package appjoe.wordpress.com.testdemo

import android.content.Context
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
        val passwd = findViewById<EditText>(R.id.passwd_register)
        val passwd_verify = findViewById<EditText>(R.id.passwd_register_verify)
        val PREFS_FILENAME = "com.wordpress.appjoe.prefs"
        val email_value = email?.text.toString()

        fun writePrefs(email: String, passwd: String) {
            val prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("email", email)
            editor.putString("passwd", passwd)
            // status 1: logged in
            editor.putInt("status", 1)
            editor.apply()
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email_value).matches() && email_value.trim().isNotEmpty()) {
            if (passwd.text.toString().isNotEmpty()) {
                if (passwd.text.toString() == passwd_verify.text.toString()) {

                    writePrefs(email.text.toString(), passwd.text.toString())

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
