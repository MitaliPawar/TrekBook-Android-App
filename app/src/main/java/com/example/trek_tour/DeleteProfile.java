package com.example.trek_tour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteProfile extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private TextView editTextUserPwd;
    private TextView textViewAuthenticated;
    private ProgressBar progressBar;
    private String userPwd;
    private Button buttonAuthentication,buttonDeleteUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        getSupportActionBar().setTitle("Delete Your Profile");
        progressBar=findViewById(R.id.progressbar);
        editTextUserPwd=findViewById(R.id.text_delete_user_pwd);
        textViewAuthenticated=findViewById(R.id.text_delete_user_authenticated);
        buttonDeleteUser=findViewById(R.id.button_delete_user);
        buttonAuthentication=findViewById(R.id.button_authenticate_user);

        //Disable Delete User Button until user is authenticated
        buttonDeleteUser.setEnabled(false);

        authProfile=FirebaseAuth.getInstance();
        firebaseUser= authProfile.getCurrentUser();
        if(firebaseUser.equals("")){
            Toast.makeText(DeleteProfile.this, "Something went wrong !"+
                    "USer Details are not available at the moment", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(DeleteProfile.this,UserProfile.class);
            startActivity(intent);
            finish();
        }else
        {
            reAuthenticateUser(firebaseUser);
        }

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwd=editTextUserPwd.getText().toString();

                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(DeleteProfile.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    editTextUserPwd.setError("Please enter your current password to authenticate");
                    editTextUserPwd.requestFocus();
                }
                else
                {
                   progressBar.setVisibility(view.VISIBLE);
                   //ReAuthenticate User Now
                    AuthCredential credential= EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                //Disable editText for Password.
                                editTextUserPwd.setEnabled(false);

                                //Enable Delete User Button. Disable Authenticate Button
                                buttonAuthentication.setEnabled(false);
                                buttonDeleteUser.setEnabled(true);

                                //Set TextView to show User is authenticated/ Verified
                                textViewAuthenticated.setText("You are authenticated/Verified."+"You can change your password now");
                                Toast.makeText(DeleteProfile.this, "Password has been Verified."+"You can delete your profile now." +
                                        "Be careful,this action is irreversible", Toast.LENGTH_SHORT).show();

                                //update color of change Password Button
                                buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfile.this,R.color.purple_200));

                                buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showAlertDialog();
                                    }
                                });
                            }else{
                                try{
                                    throw task.getException();
                                }
                                catch(Exception e){
                                    Toast.makeText(DeleteProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void showAlertDialog() {
            //setup the Alert Builder
            AlertDialog.Builder builder=new AlertDialog.Builder(DeleteProfile.this);
            builder.setTitle("Delete User and Related Data");
            builder.setMessage("Do You Really want to delete your profile and related data? This action is irreversible.");

            // Open Email Apps if User clicks/taps Continue button
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    deleteUser(firebaseUser);
                }
            });

            //Return to User Profile Activity if User presses Cancel Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               Intent intent=new Intent(DeleteProfile.this,UserProfile.class);
               startActivity(intent);
               finish();
            }
        });

            //Create AlertDialog
            AlertDialog alertDialog= builder.create();

            //

            //show the AlertDialog
            alertDialog.show();
        }

    private void deleteUser(FirebaseUser firebaseUser) {
    }


    //Creating ActionBar Menu
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
            Intent intent=new Intent(DeleteProfile.this,UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.menu_update_profile) {
            Intent  intent=new Intent(DeleteProfile.this,UpdateP.class);
            startActivity(intent);
            //finish();
        }else if(id==R.id.menu_settings){
            Toast.makeText(DeleteProfile.this, "menu_settings", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_DeleteProfile){
            Intent  intent=new Intent(DeleteProfile.this,DeleteProfile.class);
            startActivity(intent);
        }else if(id==R.id.menu_Logout){
            authProfile.signOut();
            Toast.makeText(DeleteProfile.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(DeleteProfile.this,MainActivity.class);

            //Clear stack to prevent user coming back to DashBoard on back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();//close DashBoard
        }else{
            Toast.makeText(DeleteProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}