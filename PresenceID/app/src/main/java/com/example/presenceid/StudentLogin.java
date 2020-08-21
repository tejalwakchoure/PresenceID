package com.example.presenceid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StudentLogin extends AppCompatActivity  {

    EditText email, pwd;
    Button signIn;
    FirebaseAuth firebaseAuth;

    Registration reg = new Registration();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_student);

        email = findViewById(R.id.StudentEmail);
        pwd = findViewById(R.id.StudentPwd);
        signIn = findViewById(R.id.StudentSignIn);
        firebaseAuth = FirebaseAuth.getInstance();

        //check if email exists
        //check if pwd exists
        //if yes,
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               userLogin();
            }
        });
    }
    private void userLogin()
    {
        String emailid = email.getText().toString().trim();
        String password = pwd.getText().toString().trim();
        if(TextUtils.isEmpty(emailid))
        {
            //email is empty
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }



        if(TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        //if email and password are correct then display the progressdialog
        //  progressDialog.setMessage("Logging In...Please Wait");
        // progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(emailid,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            //start the profile activity
                            startActivity(new Intent(getApplicationContext(),StudentDashboard.class));
                        }
                        else{
                            Toast.makeText(StudentLogin.this,"Login Error",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
