package net.tution.thilini.tution.Activities.Home_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.tution.thilini.tution.Activities.Editors.Assign_Class_To_Teacher;
import net.tution.thilini.tution.Activities.Editors.Edit_Teacher;
import net.tution.thilini.tution.Activities.Viewers.View_Classes_By_Teacher;
import net.tution.thilini.tution.Activities.Viewers.View_Teachers;
import net.tution.thilini.tution.R;

public class Home_Teacher extends AppCompatActivity {

    Button butAddTeacher, butSearchTeacher, butViewAllTeachers, butAssignClassForTeacher, butViewClassesByTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

        setTitle("Teacher Home");

        initiate_Fields();
        initiate_Listeners();
    }

    private void initiate_Listeners() {
        butAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Teacher.this, Edit_Teacher.class);
                startActivity(intent);
            }
        });
        butSearchTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Teacher.this, View_Teachers.class);
                startActivity(intent);
            }
        });
        butViewAllTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Teacher.this, View_Teachers.class);
                startActivity(intent);
            }
        });
        butAssignClassForTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Teacher.this, Assign_Class_To_Teacher.class);
                startActivity(intent);
            }
        });
        butViewClassesByTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Teacher.this, View_Classes_By_Teacher.class);
                startActivity(intent);
            }
        });
    }

    private void initiate_Fields() {
        butAddTeacher = findViewById(R.id.butAddTeacher);
        butSearchTeacher = findViewById(R.id.butSearchTeacher);
        butViewAllTeachers = findViewById(R.id.butViewAllTeachers);
        butAssignClassForTeacher = findViewById(R.id.butAssignClassForTeacher);
        butViewClassesByTeacher = findViewById(R.id.butViewClassesByTeacher);
    }

}
