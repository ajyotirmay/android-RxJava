package appjoe.wordpress.com.testdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static appjoe.wordpress.com.testdemo.Tab1.EXTRA_URL;

public class ViewImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_image);
        ImageView imageView = findViewById(R.id.fullscreenImage);

        Intent intent = getIntent();

        if (intent != null) {
            String imageUrl = intent.getStringExtra(EXTRA_URL);
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
            } else {
                Toast.makeText(this, "Unable to get image", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
