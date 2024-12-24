package net.tution.thilini.tution.Activities.Home_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.tution.thilini.tution.Activities.Editors.Assign_Classes_To_Student;
import net.tution.thilini.tution.Activities.Editors.Edit_Student;
import net.tution.thilini.tution.Activities.Viewers.View_Students;
import net.tution.thilini.tution.R;

public class Home_Student extends AppCompatActivity {

    Button butAddStudent, butSearchStudent, butViewAllStudents, butAssignClassForStudent, butViewClassesByStudent,butGenerateQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        setTitle("Student Home");

        initiate_Fields();
        initiate_Listeners();
    }

    private void initiate_Listeners() {
        butAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Student.this,Edit_Student.class);
                startActivity(intent);
            }
        });
        butSearchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Student.this,View_Students.class);
                startActivity(intent);
            }
        });
        butViewAllStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Student.this,View_Students.class);
                startActivity(intent);
            }
        });
        butAssignClassForStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Student.this,Assign_Classes_To_Student.class);
                startActivity(intent);
            }
        });
        butViewClassesByStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Student.this,Assign_Classes_To_Student.class);
                startActivity(intent);
            }
        });
        butGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("qrrequest",true);

                Intent intent = new Intent(Home_Student.this, View_Students.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    private void initiate_Fields() {
        butAddStudent = findViewById(R.id.butAddStudent);
        butSearchStudent = findViewById(R.id.butSearchStudent);
        butViewAllStudents = findViewById(R.id.butViewAllStudents);
        butAssignClassForStudent = findViewById(R.id.butAssignClassForStudent);
        butViewClassesByStudent = findViewById(R.id.butViewClassesByStudent);
        butGenerateQR=findViewById(R.id.butGenerateQR);
    }
}
