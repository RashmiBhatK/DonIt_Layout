package com.android.navada.donit.activities;
import com.android.navada.donit.R;

import android.content.Intent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;



public class FirstActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ImageView img = (ImageView) findViewById(R.id.donit);
        //img.setTranslationX(-2000f);
        img.animate().alpha(1f).setDuration(2000);

        img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent mainIntent = new Intent(FirstActivity.this,SignInActivity.class);
                startActivity(mainIntent);
            }
        });

    }
}