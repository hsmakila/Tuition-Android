package net.tution.thilini.tution.Activities.Editors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.tution.thilini.tution.R;

import java.util.Objects;

public class Edit_Student extends AppCompatActivity {

    EditText etFullName, etEmail, etTelephone, etAddress, etCity, etDOB, etComment, etParentName, etParentTelephone, etGrade, etSchool;
    Button butAdd;
    Spinner etGender;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceStudent;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        setTitle("Add Home_Student");

        init_Fields();
        init_Listeners();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            loadValuesFromFireBase();
            butAdd.setText("Update");
        }

    }

    private void init_Fields() {
        etFullName = findViewById(R.id.etTeacher);
        etEmail = findViewById(R.id.etClassGrade);
        etTelephone = findViewById(R.id.etClassType);
        etAddress = findViewById(R.id.etSubject);
        etCity = findViewById(R.id.etFeeAmount);
        etGender = findViewById(R.id.etClassDay);
        etDOB = findViewById(R.id.etStartingTime);
        etComment = findViewById(R.id.etEndTime);
        etParentName = findViewById(R.id.etParentName);
        etParentTelephone = findViewById(R.id.etParentTelephone);
        etGrade = findViewById(R.id.etGrade);
        etSchool = findViewById(R.id.etSchool);

        butAdd = findViewById(R.id.butADD);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceStudent = firebaseDatabase.getReference().child("students");
    }

    private void init_Listeners() {
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFullName.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Home_Student Name", Toast.LENGTH_SHORT).show();
                else if (etEmail.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                else if (etTelephone.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Telephone Number", Toast.LENGTH_SHORT).show();
                /*else if (etAddress.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Address", Toast.LENGTH_SHORT).show();
                else if (etCity.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid City Name", Toast.LENGTH_SHORT).show();
                else if (etGender.getSelectedItem().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Gender", Toast.LENGTH_SHORT).show();
                else if (etDOB.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Date Of Birth", Toast.LENGTH_SHORT).show();
                else if (etParentName.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Parent Name", Toast.LENGTH_SHORT).show();
                else if (etParentTelephone.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Parent Telephone Number", Toast.LENGTH_SHORT).show();
                else if (etGrade.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Parent Grade", Toast.LENGTH_SHORT).show();
                else if (etSchool.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Parent School Name", Toast.LENGTH_SHORT).show();*/
                else
                    saveToFireBase();



            }
        });
    }

    private void saveToFireBase() {

        if (id == null)
            id = databaseReferenceStudent.push().getKey();

        Student student = new Student(id);
        student.setFull_name(etFullName.getText().toString());
        student.setEmail(etEmail.getText().toString());
        student.setTelephone(etTelephone.getText().toString());
        student.setAddress(etAddress.getText().toString());
        student.setCity(etCity.getText().toString());
        student.setGender(etGender.getSelectedItem().toString());
        student.setDob(etDOB.getText().toString());
        student.setComment(etComment.getText().toString());
        student.setParent_name(etParentName.getText().toString());
        student.setParent_telephone(etParentTelephone.getText().toString());
        student.setGrade(etGrade.getText().toString());
        student.setSchool(etSchool.getText().toString());

        databaseReferenceStudent.child(student.getId()).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getBaseContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadValuesFromFireBase()  {
        databaseReferenceStudent.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    etFullName.setText(student.getFull_name());
                    etEmail.setText(student.getEmail());
                    etTelephone.setText(student.getTelephone());
                    etAddress.setText(student.getAddress());
                    etCity.setText(student.getCity());
                    if (student.getGender().equals("Male"))
                        etGender.setSelection(0);
                    else
                        etGender.setSelection(1);
                    etDOB.setText(student.getDob());
                    etComment.setText(student.getComment());
                    etParentName.setText(student.getParent_name());
                    etParentTelephone.setText(student.getParent_telephone());
                    etGrade.setText(student.getGrade());
                    etSchool.setText(student.getSchool());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
