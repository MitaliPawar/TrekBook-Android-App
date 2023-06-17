package com.example.trek_tour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UpdateEmail extends AppCompatActivity {
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
    }
    //Creating ActionBAr Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.menu_refresh){
            //Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }  else if (id == R.id.menu_user_profile) {
            Intent intent=new Intent(UpdateEmail.this,UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.menu_update_profile) {
            Intent  intent=new Intent(UpdateEmail.this,UpdateP.class);
            startActivity(intent);
        }else if(id==R.id.menu_settings){
            Toast.makeText(UpdateEmail.this, "menu_settings", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_DeleteProfile){
            Intent  intent=new Intent(UpdateEmail.this,DeleteProfile.class);
            startActivity(intent);
        }else if(id==R.id.menu_Logout){
            authProfile.signOut();
            Toast.makeText(UpdateEmail.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UpdateEmail.this,MainActivity.class);

            //Clear stack to prevent user coming back to DashBoard on back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();//close DashBoard
        }else{
            Toast.makeText(UpdateEmail.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}