package net.tution.thilini.tution.Activities.Editors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.DataStructures.Teacher;
import net.tution.thilini.tution.ListAdapters.Class_Adapter;
import net.tution.thilini.tution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Assign_Class_To_Teacher extends AppCompatActivity {

    String TAG = "Assign_Class_To_Teacher";

    EditText etClassGrade, etSubject, etFeeAmount, etStartingTime, etEndingTime;
    Button butAdd, butDelete, butUpdate;
    Spinner etTeacher, etClassType, etClassDay;
    ListView lvClassList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceClasses, databaseReferenceTeachers;

    String teacher_id;

    int listViewSelectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_class_to_teacher);

        setTitle("Assign Class to Teacher");

        init_Fields();
        init_Listeners();

        loadTeachersFromFireBase();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            teacher_id = bundle.getString("teacher_id");
            loadClassesFromFireBase();
        }


    }

    private void init_Fields() {
        etTeacher = findViewById(R.id.etTeacher);
        etClassGrade = findViewById(R.id.etClassGrade);
        etClassType = findViewById(R.id.etClassType);
        etSubject = findViewById(R.id.etSubject);
        etFeeAmount = findViewById(R.id.etFeeAmount);
        etClassDay = findViewById(R.id.etClassDay);
        etStartingTime = findViewById(R.id.etStartingTime);
        etEndingTime = findViewById(R.id.etEndingTime);

        butAdd = findViewById(R.id.butADD);
        butDelete = findViewById(R.id.butDelete);
        butUpdate = findViewById(R.id.butUpdate);

        lvClassList = findViewById(R.id.lvClassList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceClasses = firebaseDatabase.getReference().child("classes");
        databaseReferenceTeachers = firebaseDatabase.getReference().child("teachers");
    }

    private void init_Listeners() {
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTeacher.getSelectedItem() == null)
                    Toast.makeText(getBaseContext(), "Enter Valid Teacher", Toast.LENGTH_SHORT).show();
                else if (etClassGrade.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Class or Grade", Toast.LENGTH_SHORT).show();
                else if (etClassType.getSelectedItem().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Class Type", Toast.LENGTH_SHORT).show();
                else if (etSubject.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Subject Name", Toast.LENGTH_SHORT).show();
                else if (etFeeAmount.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Fee Amount", Toast.LENGTH_SHORT).show();
                else if (etClassDay.getSelectedItem().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Class Day", Toast.LENGTH_SHORT).show();
                else if (etStartingTime.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Starting Time", Toast.LENGTH_SHORT).show();
                else if (etEndingTime.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Ending Time", Toast.LENGTH_SHORT).show();
                else
                    saveToFireBase();
            }
        });

        etTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadClassesFromFireBase();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "On Class Item Click At : " + String.valueOf(position));
                Class mClass = classArrayList.get(position);
                etClassGrade.setText(mClass.getClass_grade());
                etClassType.setSelection(getSpinnerIndex(etClassType, mClass.getType()));
                etSubject.setText(mClass.getSubject());
                etFeeAmount.setText(mClass.getFee());
                etClassDay.setSelection(getSpinnerIndex(etClassDay, mClass.getClass_day()));
                etStartingTime.setText(mClass.getStart_time());
                etEndingTime.setText(mClass.getEnd_time());
                listViewSelectedItemPosition = position;
                butUpdate.setEnabled(true);
                butDelete.setEnabled(true);
            }
        });

        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToFireBase();
            }
        });

        butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromFireBase();
            }
        });

    }

    private void saveToFireBase() {

        String id = databaseReferenceClasses.push().getKey();

        Class mClass = new Class(id);
        mClass.setTeacher(teacherArrayList.get(etTeacher.getSelectedItemPosition()).getId());
        mClass.setClass_grade(etClassGrade.getText().toString());
        mClass.setType(etClassType.getSelectedItem().toString());
        mClass.setSubject(etSubject.getText().toString());
        mClass.setFee(etFeeAmount.getText().toString());
        mClass.setClass_day(etClassDay.getSelectedItem().toString());
        mClass.setStart_time(etStartingTime.getText().toString());
        mClass.setEnd_time(etEndingTime.getText().toString());

        databaseReferenceClasses.child(mClass.getId()).setValue(mClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getBaseContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                loadClassesFromFireBase();
            }
        });
    }

    private void deleteFromFireBase() {
        String id = classArrayList.get(listViewSelectedItemPosition).getId();
        databaseReferenceClasses.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getBaseContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Unsuccessful Operation", Toast.LENGTH_SHORT).show();
                loadClassesFromFireBase();
            }
        });
    }

    private void updateToFireBase() {

        String id = classArrayList.get(listViewSelectedItemPosition).getId();

        Class mClass = new Class(id);
        mClass.setTeacher(teacherArrayList.get(etTeacher.getSelectedItemPosition()).getId());
        mClass.setClass_grade(etClassGrade.getText().toString());
        mClass.setType(etClassType.getSelectedItem().toString());
        mClass.setSubject(etSubject.getText().toString());
        mClass.setFee(etFeeAmount.getText().toString());
        mClass.setClass_day(etClassDay.getSelectedItem().toString());
        mClass.setStart_time(etStartingTime.getText().toString());
        mClass.setEnd_time(etEndingTime.getText().toString());

        databaseReferenceClasses.child(mClass.getId()).setValue(mClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getBaseContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                loadClassesFromFireBase();
            }
        });
    }

    ArrayList<Teacher> teacherArrayList = new ArrayList<>();
    ArrayList<String> teachersStringList = new ArrayList<>();

    ArrayList<Class> classArrayList = new ArrayList<>();
    ArrayList<String> classStringList = new ArrayList<>();

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
                loadTeachersToSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadTeachersToSpinner() {
        teachersStringList.clear();
        for (Teacher teacher : teacherArrayList) {
            teachersStringList.add(teacher.getFirst_name() + " " + teacher.getLast_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teachersStringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etTeacher.setAdapter(adapter);
    }

    private void loadClassesFromFireBase() {
        Query query;
        if (etTeacher.getSelectedItem() != null && teachersStringList.size() != 0) {
            Log.d(TAG, "Start Loading Classes associate with " + etTeacher.getSelectedItem().toString());
            query = databaseReferenceClasses.orderByChild("teacher").equalTo(teacherArrayList.get(etTeacher.getSelectedItemPosition()).getId());
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
    }

    private void loadClassesToList() {
        Class_Adapter class_adapter = new Class_Adapter(classArrayList, getApplicationContext());
        lvClassList.setAdapter(class_adapter);

        listViewSelectedItemPosition = -1;
        butUpdate.setEnabled(false);
        butDelete.setEnabled(false);
    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

}
