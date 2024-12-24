package net.tution.thilini.tution.Activities.Home_Pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import net.tution.thilini.tution.Activities.Viewers.View_Attendance_Class_Wise_Count;
import net.tution.thilini.tution.Activities.Viewers.View_Students;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Home_Attendance extends AppCompatActivity {

    String TAG = "Home_Attendance";

    Button butGenerateQRCode, butScan, butAttendanceClassWiseCount;

    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_attendance);

        setTitle("Attendance Home");

        initiate_Fields();
        initiate_Listeners();
    }

    private void initiate_Listeners() {
        butGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("qrrequest", true);

                Intent intent = new Intent(Home_Attendance.this, View_Students.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        butScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Attendance.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });

        butAttendanceClassWiseCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Attendance.this, View_Attendance_Class_Wise_Count.class);
                startActivity(intent);
            }
        });

    }

    private void initiate_Fields() {
        butGenerateQRCode = findViewById(R.id.butGenerateQRCode);
        butScan = findViewById(R.id.ButScan);
        butAttendanceClassWiseCount = findViewById(R.id.butAttendanceClassWiseCount);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "Results Error");
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data != null) {
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Toast.makeText(Home_Attendance.this, result, Toast.LENGTH_LONG).show();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                DatabaseReference studentAttendance = databaseReference.child("attendance").child(result);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                studentAttendance.child(String.valueOf(calendar.getTimeInMillis())).setValue("1");
            } else
                Log.d(TAG, "Results Error");
        }
    }

}
