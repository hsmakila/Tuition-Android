package net.tution.thilini.tution.Activities.Viewers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.DataStructures.Teacher;
import net.tution.thilini.tution.ListAdapters.Class_Adapter;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Classes_By_Teacher extends AppCompatActivity {

    String TAG = "View_Classes_By_Teacher";

    ListView lvClassList, lvTeachersList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceClasses, databaseReferenceTeachers;

    String teacher_id;

    int listViewSelectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_classes_by_teacher);

        setTitle("Assign Class to Teacher");

        init_Fields();
        init_Listeners();

        loadTeachersFromFireBase();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            teacher_id = bundle.getString("teacher_id");
        }


    }

    private void init_Fields() {
        lvTeachersList = findViewById(R.id.lvTeachersList);
        lvClassList = findViewById(R.id.lvClassList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceClasses = firebaseDatabase.getReference().child("classes");
        databaseReferenceTeachers = firebaseDatabase.getReference().child("teachers");
    }

    private void init_Listeners() {
        lvTeachersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadClassesFromFireBase(position);
            }
        });
    }

    ArrayList<Teacher> teacherArrayList = new ArrayList<>();
    ArrayList<String> teachersStringList = new ArrayList<>();

    ArrayList<Class> classArrayList = new ArrayList<>();

    private void loadTeachersFromFireBase() {

        databaseReferenceTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Teacher teacher = childSnapshot.getValue(Teacher.class);
                    teacherArrayList.add(teacher);
                }
                loadTeachersToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadTeachersToList() {
        teachersStringList.clear();
        for (Teacher teacher : teacherArrayList) {
            teachersStringList.add(teacher.getFirst_name() + " " + teacher.getLast_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teachersStringList);
        lvTeachersList.setAdapter(adapter);
    }

    private void loadClassesFromFireBase(int id) {
        Query query;
        Log.d(TAG, "Start Loading Classes associate with " + teacherArrayList.get(id).getFirst_name());
        query = databaseReferenceClasses.orderByChild("teacher").equalTo(teacherArrayList.get(id).getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, childSnapshot.toString());
                    Class mClass = childSnapshot.getValue(Class.class);
                    classArrayList.add(mClass);
                }
                loadClassesToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadClassesToList() {
        Class_Adapter class_adapter = new Class_Adapter(classArrayList, getApplicationContext());
        lvClassList.setAdapter(class_adapter);
        listViewSelectedItemPosition = -1;
    }

}
