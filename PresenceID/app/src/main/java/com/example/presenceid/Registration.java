package com.example.presenceid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageView;
    EditText regEmail, name, id, pwd;
    CheckBox cr1;//, cr2;
    RadioButton student, teacher;
   // RadioGroup radioGroup;
    Button upload, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        firebaseAuth = FirebaseAuth.getInstance();
    //radioGroup = findViewById(R.id.teacher);
        regEmail = findViewById(R.id.RegEmail);
        name = findViewById(R.id.Name);
        id = findViewById(R.id.ID);
        pwd = findViewById(R.id.RegPwd);
        cr1 = findViewById(R.id.course1);
      //  cr2 = findViewById(R.id.course2);
        student = findViewById(R.id.RegStud);
        teacher = findViewById(R.id.RegTeach);
        upload = findViewById(R.id.UploadPhoto);
        register = findViewById(R.id.Register);
        imageView = findViewById(R.id.Camera);

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //check if all fields are accurate
                //If yes,
               registerUser();
            }
        });

    }
    private void registerUser()
    {
        String email = regEmail.getText().toString().trim();
        String password = pwd.getText().toString().trim();
        String names = name.getText().toString().trim();

        if(TextUtils.isEmpty(email))
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
        if(TextUtils.isEmpty(names)){
            Toast.makeText(this, "Please Enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        //if validations are ok
        // we will first show a progress bar
        //  progressDialog.setMessage("Registering User");
        // progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //user Registered successfully
                            Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Registration.this,"could not register",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) resultIntent.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    /*public boolean isRegcr1(String email){
        if(cr1.isChecked())
            return true;
        return false;
    }

    public boolean isTeacher(String email)
    {
        if(teacher.isChecked())
            return true;
        return false;
    }

    public boolean isStudent(String email)
    {
        if(student.isChecked())
            return true;
        return false;
    }*/




}