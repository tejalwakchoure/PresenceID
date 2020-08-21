package com.example.presenceid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button student, teacher;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        student = findViewById(R.id.StudentButton);
        teacher = findViewById(R.id.TeacherButton);
        register = findViewById(R.id.Register);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentlogin = new Intent(MainActivity.this, StudentLogin.class);
                startActivity(studentlogin);
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacherlogin = new Intent(MainActivity.this, TeacherLogin.class);
                startActivity(teacherlogin);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(MainActivity.this, Registration.class);
                startActivity(reg);
            }
        });
    }
}
