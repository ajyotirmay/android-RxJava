package appjoe.wordpress.com.testdemo

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    var status: Int = 0

    fun readPrefs() {
        val prefs = this.getSharedPreferences("com.wordpress.appjoe.prefs", Context.MODE_PRIVATE)
        status = prefs.getInt("status", 0)
    }

    fun writePrefs() {
        val prefs = this.getSharedPreferences("com.wordpress.appjoe.prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("status", 1)
        editor.apply()
    }

    fun launchMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        readPrefs()
        if (status == 1) {
            launchMain()
        }

    }

    fun signup(v: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    fun errorMessage() {
        Toast.makeText(this, "Invalid id or password", Toast.LENGTH_SHORT).show()
    }

    fun login(v: View) {
        val email = findViewById<EditText>(R.id.uname)
        val passwd = findViewById<EditText>(R.id.passwd)
        val emailid = email.text.toString()

        if (Patterns.EMAIL_ADDRESS.matcher(emailid).matches()
                && email.text.toString().isNotEmpty()
                && passwd.text.toString().isNotEmpty()) {
            val prefs = this.getSharedPreferences("com.wordpress.appjoe.prefs", Context.MODE_PRIVATE)
            val email_val = prefs.getString("email", null)
            val passwd_val = prefs.getString("passwd", null)

            if (emailid == email_val && passwd.text.toString() == passwd_val) {
                writePrefs()
                launchMain()
            } else {
                errorMessage()
            }
        } else {
            errorMessage()
        }


    }
}
