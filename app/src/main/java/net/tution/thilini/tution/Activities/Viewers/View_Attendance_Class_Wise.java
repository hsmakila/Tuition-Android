package net.tution.thilini.tution.Activities.Viewers;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.DataStructures.StudentAttendance;
import net.tution.thilini.tution.ListAdapters.Student_Attendance_Adapter;
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

public class View_Attendance_Class_Wise extends AppCompatActivity {

    String TAG = "View_Attendance_Class_Wise";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference DBRef_Classes, DBRef_Students, DBRef_StudentsAssignedClasses, DBRef_Attendance;

    ArrayList<Class> classesArrayList = new ArrayList<>();
    ArrayList<String> classesStringList = new ArrayList<>();

    ArrayList<Student> studentsArrayList = new ArrayList<>();

    ArrayList<StudentAttendance> studentsAttendanceArrayList = new ArrayList<>();
    long[] monthDates = new long[5];

    ListView lvClasses;
    EditText etMonth, etYear;
    Button butShow, butDumpCSV;
    ListView lvStudentsAttendance;
    TextView tvDay1, tvDay2, tvDay3, tvDay4, tvDay5;

    int selectedClass = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendence_class_wise);

        initiate_Fields();
        initiate_Listeners();

        loadClassesFromFireBase();
        loadStudentsFromFireBase();

    }

    private void initiate_Fields() {
        lvClasses = findViewById(R.id.lvClasses);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);
        butShow = findViewById(R.id.butShow);
        butDumpCSV = findViewById(R.id.butDumpCSV);
        lvStudentsAttendance = findViewById(R.id.lvStudentsAttendance);

        tvDay1 = findViewById(R.id.tvDay1);
        tvDay2 = findViewById(R.id.tvDay2);
        tvDay3 = findViewById(R.id.tvDay3);
        tvDay4 = findViewById(R.id.tvDay4);
        tvDay5 = findViewById(R.id.tvDay5);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DBRef_Classes = firebaseDatabase.getReference().child("classes");
        DBRef_Students = firebaseDatabase.getReference().child("students");
        DBRef_StudentsAssignedClasses = firebaseDatabase.getReference().child("class_student");
        DBRef_Attendance = firebaseDatabase.getReference().child("attendance");

    }

    private void initiate_Listeners() {
        butShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAttendance();
            }
        });

        lvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = position;
                loadAttendance();
            }
        });
        butDumpCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dumpAttendanceToCSV();
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
                loadTeachersToSpinner();
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

    private void loadTeachersToSpinner() {
        classesStringList.clear();
        for (Class aClass : classesArrayList) {
            classesStringList.add(aClass.getSubject() + " for Grade : " + aClass.getClass_grade());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, classesStringList);
        lvClasses.setAdapter(adapter);
    }

    private void loadAttendanceToList() {

        boolean isComplete = true;
        for (StudentAttendance studentAttendance : studentsAttendanceArrayList) {
            if (studentAttendance.getAttendance() == null)
                isComplete = false;

        }
        Log.d(TAG, "Loading Attendance : is complete : " + String.valueOf(isComplete));
        Log.d(TAG, "Loading Attendance : Student Count : " + String.valueOf(studentsAttendanceArrayList.size()));
        if (isComplete) {
            Student_Attendance_Adapter adapter = new Student_Attendance_Adapter(studentsAttendanceArrayList, this);
            lvStudentsAttendance.setAdapter(adapter);
        }
    }

    private void insertStudentTo_StudentsAttendanceList(String studentID) {
        for (Student student : studentsArrayList) {
            if (student.getId().equals(studentID)) {
                StudentAttendance studentAttendance = new StudentAttendance();
                studentAttendance.setStudent(student);
                studentsAttendanceArrayList.add(studentAttendance);
                loadAttendancesFromFireBase(studentID, studentsAttendanceArrayList.size() - 1);
                break;
            }
        }
    }

    private void loadAttendancesFromFireBase(String studentID, final int position) {
        DBRef_Attendance.child(studentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long[] attendance = new long[5];
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for (int j = 0; j < 5; j++) {
                        if (monthDates[j] == Long.valueOf(childSnapshot.getKey())) {
                            attendance[j] = 1;
                        }
                    }
                }
                studentsAttendanceArrayList.get(position).setAttendance(attendance);
                Log.d(TAG, studentsAttendanceArrayList.get(position).getStudent().getFull_name() + " : " + Arrays.toString(studentsAttendanceArrayList.get(position).getAttendance()));
                loadAttendanceToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMonthDates() {
        Calendar calendar = calendarWithoutTime();
        switch (classesArrayList.get(selectedClass).getClass_day()) {
            case "Monday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case "Tuesday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case "Wednesday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case "Thursday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case "Friday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case "Saturday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case "Sunday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
        }
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        calendar.set(Calendar.MONTH, Integer.valueOf(etMonth.getText().toString()) - 1);
        calendar.set(Calendar.YEAR, Integer.valueOf(etYear.getText().toString()));

        int currentMonth = calendar.get(Calendar.MONTH);

        for (int i = 0; i < 5; i++)
            monthDates[i] = 0;

        int i = 0;
        while (currentMonth == calendar.get(Calendar.MONTH)) {
            monthDates[i] = calendar.getTimeInMillis();
            i++;
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        TextView[] tvDates = {tvDay1, tvDay2, tvDay3, tvDay4, tvDay5};
        for (int j = 0; j < 5; j++) {
            if (monthDates[j] != 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(monthDates[j]);
                tvDates[j].setText(simpleDateFormat.format(calendar1.getTime()));
            } else {
                tvDates[j].setText("");
            }

        }
        Log.d(TAG, "Dates Consider : " + Arrays.toString(monthDates));
    }

    private Calendar calendarWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    private void loadAttendance() {
        if (!etMonth.getText().toString().equals("") && !etYear.getText().toString().equals("") && selectedClass != -1) {
            Calendar calendar = calendarWithoutTime();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Integer.valueOf(etMonth.getText().toString()) - 1);
            calendar.set(Calendar.YEAR, Integer.valueOf(etYear.getText().toString()));
            calendar.add(Calendar.MONTH, 1);

            loadMonthDates();
            studentsAttendanceArrayList.clear();
            loadAttendanceToList();

            DBRef_StudentsAssignedClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.child("classID").getValue(String.class).equals(classesArrayList.get(selectedClass).getId())) {
                            Log.d(TAG, "Classes_Students : " + child.toString());
                            String studentID = child.child("studentID").getValue(String.class);
                            insertStudentTo_StudentsAttendanceList(studentID);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void dumpAttendanceToCSV() {
        String filename = "attendance.csv";

        String fileContents = "Student Name";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyy");
        for (int i = 0; i < monthDates.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(monthDates[i]);
            fileContents += ("," + simpleDateFormat.format(calendar.getTime()));
        }
        fileContents += "\n";

        for (int i = 0; i < studentsAttendanceArrayList.size(); i++) {
            fileContents += studentsAttendanceArrayList.get(i).getStudent().getFull_name();
            for (int j = 0; j < monthDates.length; j++) {
                fileContents += ("," + studentsAttendanceArrayList.get(i).getAttendance()[j]);
            }
            fileContents += "\n";
        }

        String path = Environment.getExternalStorageDirectory().getPath() + "/" + filename;
        Log.d(TAG, path);
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Toast.makeText(getBaseContext(),"CSV Saved...",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

}

