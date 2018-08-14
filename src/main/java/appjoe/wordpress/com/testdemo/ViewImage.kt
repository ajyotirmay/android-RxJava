package appjoe.wordpress.com.testdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import appjoe.wordpress.com.testdemo.Tab1.Companion.EXTRA_URL
import com.squareup.picasso.Picasso

class ViewImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_view_image)
        val imageView = findViewById<ImageView>(R.id.fullscreenImage)

        val intent = intent

        if (intent != null) {
            val imageUrl = intent.getStringExtra(EXTRA_URL)
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).fit().centerInside().into(imageView)
            } else {
                Toast.makeText(this, "Unable to get image", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
