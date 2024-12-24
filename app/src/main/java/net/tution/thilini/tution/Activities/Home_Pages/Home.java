package net.tution.thilini.tution.Activities.Home_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.tution.thilini.tution.Activities.Editors.Edit_Payments;
import net.tution.thilini.tution.MainActivity;
import net.tution.thilini.tution.R;

public class Home extends AppCompatActivity {

    String TAG = "Home";

    Button butTeacher, butStudent, butAttendance, butPayment, butReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        initiate_Fields();
        initiate_Listeners();


    }

    private void initiate_Listeners() {
        butStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Home_Student.class);
                startActivity(intent);
            }
        });
        butTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Home_Teacher.class);
                startActivity(intent);
            }
        });
        butAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Home_Attendance.class);
                startActivity(i);
            }
        });
        butPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Edit_Payments.class);
                startActivity(intent);
            }
        });
        butReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Home_Reports.class);
                startActivity(intent);
            }
        });
    }

    private void initiate_Fields() {
        butTeacher = findViewById(R.id.butTeacher);
        butStudent = findViewById(R.id.butStudent);
        butAttendance = findViewById(R.id.butAttendance);
        butPayment = findViewById(R.id.butPayment);
        butReports = findViewById(R.id.butReports);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            MainActivity.appStatus = MainActivity.AppStatus.EXIT_FROM_HOME;
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
