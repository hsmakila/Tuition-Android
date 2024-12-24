package net.tution.thilini.tution.Activities.Editors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.ListAdapters.Class_Adapter;
import net.tution.thilini.tution.ListAdapters.Student_Adapter;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Assign_Classes_To_Student extends AppCompatActivity {

    String TAG = "Assign_Classes_To_Student";

    ListView lvStudentList, lvAllClassesList, lvAssignedClassList;
    Button butAssign, butRemove;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference DBReferenceClasses, DBReferenceStudents, DBReferenceClassVsStudent;

    int lvStudentsSelectedItemPosition = -1;
    int lvAllClassesSelectedItemPosition = -1;
    int lvAssignedClassesSelectedItemPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_classes_to_student);

        setTitle("Assign Classes to Student");

        init_Fields();
        init_Listeners();

        loadStudentsFromFireBase();

    }

    private void init_Fields() {
        lvStudentList = findViewById(R.id.lvStudentsList);
        lvAllClassesList = findViewById(R.id.lvAllClassList);
        lvAssignedClassList = findViewById(R.id.lvAssignedClassList);

        butAssign = findViewById(R.id.butAssign);
        butRemove = findViewById(R.id.butRemove);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DBReferenceClasses = firebaseDatabase.getReference().child("classes");
        DBReferenceStudents = firebaseDatabase.getReference().child("students");
        DBReferenceClassVsStudent = firebaseDatabase.getReference().child("class_student");
    }

    private void init_Listeners() {
        lvStudentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvStudentsSelectedItemPosition = position;
                updateUI();
            }
        });

        lvAllClassesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvAllClassesSelectedItemPosition = position;
            }
        });
        lvAssignedClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvAssignedClassesSelectedItemPosition = position;
            }
        });

        butAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lvStudentsSelectedItemPosition == -1)
                    Toast.makeText(getBaseContext(), "Please Select Student...", Toast.LENGTH_SHORT).show();
                else if (lvAllClassesSelectedItemPosition == -1)
                    Toast.makeText(getBaseContext(), "Please Select New Class...", Toast.LENGTH_SHORT).show();
                else {
                    Class aClass = allClassArrayList.get(lvAllClassesSelectedItemPosition);
                    Student aStudent = studentsArrayList.get(lvStudentsSelectedItemPosition);
                    if (!assignedClassArrayList.contains(aClass)) {
                        String key = DBReferenceClassVsStudent.push().getKey();
                        DBReferenceClassVsStudent.child(key).child("studentID").setValue(aStudent.getId());
                        DBReferenceClassVsStudent.child(key).child("classID").setValue(aClass.getId());
                        updateUI();
                    } else
                        Toast.makeText(getBaseContext(), "Class is already assigned...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        butRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lvStudentsSelectedItemPosition == -1)
                    Toast.makeText(getBaseContext(), "Please Select Student...", Toast.LENGTH_SHORT).show();
                else if (lvAssignedClassesSelectedItemPosition == -1)
                    Toast.makeText(getBaseContext(), "Please Select Assigned Class...", Toast.LENGTH_SHORT).show();
                else {
                    Log.d(TAG, "Deleting " + assignedClassStringArrayList.get(lvAssignedClassesSelectedItemPosition));
                    DBReferenceClassVsStudent.child(assignedClassStringArrayList.get(lvAssignedClassesSelectedItemPosition)).removeValue();
                    updateUI();
                }
            }
        });
    }

    ArrayList<Student> studentsArrayList = new ArrayList<>();

    ArrayList<Class> assignedClassArrayList = new ArrayList<>();
    ArrayList<String> assignedClassStringArrayList = new ArrayList<>();

    ArrayList<Class> allClassArrayList = new ArrayList<>();

    private void loadStudentsFromFireBase() {

        DBReferenceStudents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Student student = childSnapshot.getValue(Student.class);
                    studentsArrayList.add(student);
                }
                loadStudentsToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadStudentsToList() {
        Student_Adapter student_adapter = new Student_Adapter(studentsArrayList, getApplicationContext());
        lvStudentList.setAdapter(student_adapter);
    }

    private void loadAllClassesFromFireBase(String grade) {
        Log.d(TAG, "Start Loading Classes associate with grade : " + grade);
        Query query = DBReferenceClasses.orderByChild("class_grade").equalTo(grade);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allClassArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Class mClass = childSnapshot.getValue(Class.class);
                    allClassArrayList.add(mClass);
                }
                loadAllClassesToList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAssignedClassesFromFireBase(String studentID) {
        Log.d(TAG, "Start Loading Classes associate with student : " + studentID);
        Query query = DBReferenceClassVsStudent.orderByChild("studentID").equalTo(studentID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assignedClassArrayList.clear();
                assignedClassStringArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Log.d(TAG, childSnapshot.getKey());
                    String classID = childSnapshot.child("classID").getValue().toString();
                    Class mClass = findClassByID(classID);
                    assignedClassArrayList.add(mClass);
                    assignedClassStringArrayList.add(childSnapshot.getKey());
                }

                loadAssignedClassesToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAllClassesToList() {
        Class_Adapter class_adapter = new Class_Adapter(allClassArrayList, getApplicationContext());
        lvAllClassesList.setAdapter(class_adapter);
        lvAllClassesSelectedItemPosition = -1;
    }

    private void loadAssignedClassesToList() {
        Class_Adapter class_adapter = new Class_Adapter(assignedClassArrayList, getApplicationContext());
        lvAssignedClassList.setAdapter(class_adapter);
        lvAssignedClassesSelectedItemPosition = -1;
    }

    private Class findClassByID(String classID) {
        for (int i = 0; i < allClassArrayList.size(); i++) {
            if (allClassArrayList.get(i).getId().equals(classID))
                return allClassArrayList.get(i);
        }
        return null;
    }

    private void updateUI() {
        loadAllClassesFromFireBase(studentsArrayList.get(lvStudentsSelectedItemPosition).getGrade());
        loadAssignedClassesFromFireBase(studentsArrayList.get(lvStudentsSelectedItemPosition).getId());
    }

}
