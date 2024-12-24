package net.tution.thilini.tution.Activities.Editors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Teacher;
import net.tution.thilini.tution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Edit_Teacher extends AppCompatActivity {

    EditText etFirstName, etLastName, etEmail, etTelephone, etAddress, etCity, etComment;
    Button butAdd;
    Spinner etTitle;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceTeacher;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);

        setTitle("Add Teacher");

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
        etTitle = findViewById(R.id.etTeacher);
        etFirstName = findViewById(R.id.etClassGrade);
        etLastName = findViewById(R.id.etClassType);
        etEmail = findViewById(R.id.etSubject);
        etTelephone = findViewById(R.id.etFeeAmount);
        etAddress = findViewById(R.id.etClassDay);
        etCity = findViewById(R.id.etStartingTime);
        etComment = findViewById(R.id.etEndTime);

        butAdd = findViewById(R.id.butADD);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceTeacher = firebaseDatabase.getReference().child("teachers");
    }

    private void init_Listeners() {
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getSelectedItem().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Title", Toast.LENGTH_SHORT).show();
                else if (etFirstName.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid First Name", Toast.LENGTH_SHORT).show();
                else if (etLastName.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Last Name", Toast.LENGTH_SHORT).show();
                else if (etEmail.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                else if (etTelephone.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Telephone Number", Toast.LENGTH_SHORT).show();
                else if (etAddress.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid Address", Toast.LENGTH_SHORT).show();
                else if (etCity.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(), "Enter Valid City", Toast.LENGTH_SHORT).show();
                else
                    saveToFireBase();

            }
        });
    }

    private void saveToFireBase() {

        if (id == null)
            id = databaseReferenceTeacher.push().getKey();

        Teacher teacher = new Teacher(id);
        teacher.setTitle(etTitle.getSelectedItem().toString());
        teacher.setFirst_name(etFirstName.getText().toString());
        teacher.setLast_name(etLastName.getText().toString());
        teacher.setEmail(etEmail.getText().toString());
        teacher.setTelephone(etTelephone.getText().toString());
        teacher.setAddress(etAddress.getText().toString());
        teacher.setCity(etCity.getText().toString());
        teacher.setComment(etComment.getText().toString());

        databaseReferenceTeacher.child(teacher.getId()).setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void loadValuesFromFireBase() {
        databaseReferenceTeacher.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Teacher teacher = dataSnapshot.getValue(Teacher.class);
                if (teacher != null) {
                    switch (teacher.getTitle()) {
                        case "Mr":
                            etTitle.setSelection(0);
                            break;
                        case "Miss":
                            etTitle.setSelection(1);
                            break;
                        default:
                            etTitle.setSelection(2);
                            break;
                    }

                    etFirstName.setText(teacher.getFirst_name());
                    etLastName.setText(teacher.getLast_name());
                    etEmail.setText(teacher.getEmail());
                    etTelephone.setText(teacher.getTelephone());
                    etAddress.setText(teacher.getAddress());
                    etCity.setText(teacher.getCity());
                    etComment.setText(teacher.getComment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
