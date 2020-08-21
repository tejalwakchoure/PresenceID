package com.example.presenceid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherDashboard extends AppCompatActivity {

    EditText date,time;
    Spinner courses;
    Button enter;
    TextView textView;
    private static final String ATTENDEES_PATH = "attendees.txt";
    FirebaseAuth firebaseAuth;
    ListView attendeesl;
    FirebaseFirestore db;
    String[] students;
    List<String> students_list;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        courses = findViewById(R.id.spinner1);
        String[] items = new String[]{"Course1"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        courses.setAdapter(adapter);

        String course = courses.getSelectedItem().toString();

        date = (EditText) findViewById(R.id.editText);
        time = (EditText) findViewById(R.id.editText2);
        textView = (TextView) findViewById(R.id.textView);
        attendeesl = findViewById(R.id.attendees);
        attendeesl.setVisibility(View.GONE);


        students = new String[] {
                "Attendance"
        };
        students_list = new ArrayList<String>(Arrays.asList(students));
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, students_list);

        db = FirebaseFirestore.getInstance();
        attendeesl.setAdapter(arrayAdapter);

        //  String name = getIntent().getExtras().getString("name");
        textView.setText("Hello "+user.getEmail().substring(0, user.getEmail().indexOf('@')));

        enter = (Button) findViewById(R.id.button2);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadData();
                courses.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                time.setVisibility(View.GONE);
                enter.setVisibility(View.GONE);
            }
        });
    }

    private void ReadData() {

        final List<String> list = new ArrayList<>();
        final StringBuilder fields = new StringBuilder("");
        db.collection("attendees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    //List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //list.add(document.getId());
                        DocumentReference attendee = db.collection("attendees").document(document.getId());
                        attendee.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    students_list.add(doc.get("NAME").toString()+"  "+doc.get("Date").toString());
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                    Log.d("test","name , date displayed");
                    attendeesl.setVisibility(View.VISIBLE);
                } else {
                    Log.d("test", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
