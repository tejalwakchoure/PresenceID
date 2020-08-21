package com.example.presenceid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class StudentDashboard extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private ImageView imageView;
    Button upload;
    TextView hello, marked, showName;
    FirebaseAuth firebaseAuth;
    String name="";
    FirebaseUser user;
    FirebaseFirestore db;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Registration reg = new Registration();
        upload = findViewById(R.id.StudentUpload);
        hello = findViewById(R.id.helloS);
        marked = findViewById(R.id.marked);
        imageView = findViewById(R.id.CameraS);
        showName = findViewById(R.id.showName);

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        user = firebaseAuth.getCurrentUser();


        //String email = getIntent().getExtras().getString("email");
        //String name = email.substring(email.indexOf('@'));
        name = user.getEmail().substring(0, user.getEmail().indexOf('@'));
        hello.setText("Hello " + name);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = df.format(c);

        df = new SimpleDateFormat("HH:mm");
        final String currentTime = df.format(c);

        Map<String,String> courseTimes = new HashMap<String, String>();
        courseTimes.put("cr1", "09:00");

            upload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent rec = new Intent(StudentDashboard.this, CameraActivity.class);
                    rec.putExtra("name", name);
                    startActivityForResult(rec, REQUEST_CODE);
                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data!=null) {
            Bitmap photo = (Bitmap) data.getExtras().get("photo");
            //String results = data.getStringExtra("results");
            boolean finalresult = data.getBooleanExtra("finalresult",false);
            imageView.setImageBitmap(photo);
            showName.setText(Boolean.toString(finalresult));
            if(finalresult) {
                marked.setText("Your attendance has been marked for the date " + currentDate);
                upload.setVisibility(View.GONE);
                addNewattendee();
            }
            else
                marked.setText("Unable to recognize face. Please try again");
        }
    }

    private void addNewattendee() {
        Map<String, Object> newAttendee = new HashMap<>();
        newAttendee.put("NAME", name);
        newAttendee.put("Email", user.getEmail());
        newAttendee.put("Date",currentDate);

        db.collection("attendees").document(name).set(newAttendee, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(StudentDashboard.this, "Added in Database",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentDashboard.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });


    };
}