package net.tution.thilini.tution.Activities.Viewers;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.DataStructures.StudentPayment;
import net.tution.thilini.tution.ListAdapters.Class_Adapter;
import net.tution.thilini.tution.ListAdapters.Payments_Adapter;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class View_Payments_Class_Wise extends AppCompatActivity {

    String TAG = "View_Payments_Class_Wise";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference DBRef_Classes, DBRef_Students, DBRef_StudentsAssignedClasses, DBRef_Payments;

    ArrayList<Class> classesArrayList = new ArrayList<>();

    ArrayList<Student> studentsArrayList = new ArrayList<>();

    ArrayList<StudentPayment> studentsPaymentsArrayList = new ArrayList<>();
    long[] yearMonthsInMills = new long[12];

    ListView lvClasses;
    EditText etYear;
    Button butShow, butDumpCSV;
    ListView lvPayments;

    int selectedClass = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payments_class_wise);

        initiate_Fields();
        initiate_Listeners();

        loadClassesFromFireBase();
        loadStudentsFromFireBase();

    }

    private void initiate_Fields() {
        lvClasses = findViewById(R.id.lvClasses);
        etYear = findViewById(R.id.etYear);
        butShow = findViewById(R.id.butShow);
        butDumpCSV = findViewById(R.id.butDumpCSV);
        lvPayments = findViewById(R.id.lvStudentsAttendance);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DBRef_Classes = firebaseDatabase.getReference().child("classes");
        DBRef_Students = firebaseDatabase.getReference().child("students");
        DBRef_StudentsAssignedClasses = firebaseDatabase.getReference().child("class_student");
        DBRef_Payments = firebaseDatabase.getReference().child("payments");

    }

    private void initiate_Listeners() {
        butShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPayments();
            }
        });

        lvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = position;
                loadPayments();
            }
        });
        butDumpCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dumpPaymentsToCSV();
            }
        });
    }

    private void loadClassesFromFireBase() {
        DBRef_Classes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classesArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Class aClass = childSnapshot.getValue(Class.class);
                    classesArrayList.add(aClass);
                }
                Class_Adapter class_adapter = new Class_Adapter(classesArrayList, getBaseContext());
                lvClasses.setAdapter(class_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadStudentsFromFireBase() {
        DBRef_Students.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Student student = childSnapshot.getValue(Student.class);
                    studentsArrayList.add(student);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAttendanceToList() {

        boolean isComplete = true;
        for (StudentPayment studentPayment : studentsPaymentsArrayList) {
            if (studentPayment.getPayments() == null)
                isComplete = false;
        }

        Log.d(TAG, "Loading Payments : is complete : " + String.valueOf(isComplete));
        Log.d(TAG, "Loading Payments : Student Count : " + String.valueOf(studentsPaymentsArrayList.size()));
        if (isComplete) {
            Payments_Adapter adapter = new Payments_Adapter(studentsPaymentsArrayList, this);
            lvPayments.setAdapter(adapter);
        }
    }

    private void insertStudentTo_StudentsPaymentsList(String studentID) {
        for (Student student : studentsArrayList) {
            if (student.getId().equals(studentID)) {
                StudentPayment studentPayment = new StudentPayment();
                studentPayment.setStudent(student);
                studentsPaymentsArrayList.add(studentPayment);
                loadPaymentsFromFireBase(studentID, studentsPaymentsArrayList.size() - 1);
                break;
            }
        }
    }

    private void loadPaymentsFromFireBase(String studentID, final int position) {
        DBRef_Payments.child(studentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long[] payments = new long[12];
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for (int j = 0; j < 12; j++) {
                        if (yearMonthsInMills[j] == Long.valueOf(childSnapshot.getKey())) {
                            payments[j] = 1;
                        }
                    }
                }
                studentsPaymentsArrayList.get(position).setPayments(payments);
                Log.d(TAG, studentsPaymentsArrayList.get(position).getStudent().getFull_name() + " : " + Arrays.toString(studentsPaymentsArrayList.get(position).getPayments()));
                loadAttendanceToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadYearMonths() {
        Calendar calendar = calendarWithoutTime();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, Integer.valueOf(etYear.getText().toString()));

        for (int i = 0; i < 12; i++) {
            yearMonthsInMills[i] = calendar.getTimeInMillis();
            calendar.add(Calendar.MONTH, 1);
        }
        Log.d(TAG, "Dates Consider : " + Arrays.toString(yearMonthsInMills));
    }

    private Calendar calendarWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    private void loadPayments() {
        if (!etYear.getText().toString().equals("") && selectedClass != -1) {
            Calendar calendar = calendarWithoutTime();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.YEAR, Integer.valueOf(etYear.getText().toString()));

            loadYearMonths();
            studentsPaymentsArrayList.clear();
            loadAttendanceToList();

            DBRef_StudentsAssignedClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.child("classID").getValue(String.class).equals(classesArrayList.get(selectedClass).getId())) {
                            Log.d(TAG, "Classes_Students : " + child.toString());
                            String studentID = child.child("studentID").getValue(String.class);
                            insertStudentTo_StudentsPaymentsList(studentID);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void dumpPaymentsToCSV() {
        String filename = "payments.csv";

        String fileContents = "Student Name";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyy");
        for (int i = 0; i < yearMonthsInMills.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(yearMonthsInMills[i]);
            fileContents += ("," + simpleDateFormat.format(calendar.getTime()));
        }
        fileContents += "\n";

        for (int i = 0; i < studentsPaymentsArrayList.size(); i++) {
            fileContents += studentsPaymentsArrayList.get(i).getStudent().getFull_name();
            for (int j = 0; j < yearMonthsInMills.length; j++) {
                fileContents += ("," + studentsPaymentsArrayList.get(i).getPayments()[j]);
            }
            fileContents += "\n";
        }

        String path = Environment.getExternalStorageDirectory().getPath() + "/" + filename;
        Log.d(TAG, path);
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Toast.makeText(getBaseContext(), "CSV Saved...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }
}

