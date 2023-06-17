package com.example.trek_tour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Karjat_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karjat_details);

        Button BookTrek=findViewById(R.id.Book);
        BookTrek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Karjat_details.this,acc.class);
                startActivity(intent);
                finish();
            }
        });
    }
}