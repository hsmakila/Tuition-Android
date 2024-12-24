package net.tution.thilini.tution.Activities.Home_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.tution.thilini.tution.Activities.Editors.Edit_Student;
import net.tution.thilini.tution.Activities.Viewers.View_Attendance_Class_Wise;
import net.tution.thilini.tution.Activities.Viewers.View_Payments_Class_Wise;
import net.tution.thilini.tution.Activities.Viewers.View_Students;
import net.tution.thilini.tution.R;

public class Home_Reports extends AppCompatActivity {

    Button butAttendance, butPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_reports);

        setTitle("Reports Home");

        initiate_Fields();
        initiate_Listeners();
    }

    private void initiate_Listeners() {
        butAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Reports.this,View_Attendance_Class_Wise.class);
                startActivity(intent);
            }
        });
        butPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Reports.this,View_Payments_Class_Wise.class);
                startActivity(intent);
            }
        });

    }

    private void initiate_Fields() {
        butAttendance = findViewById(R.id.butAttendance);
        butPayments = findViewById(R.id.butPayments);

    }
}
