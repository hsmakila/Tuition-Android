package net.tution.thilini.tution.Activities.Viewers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.tution.thilini.tution.Activities.Editors.Edit_Student;
import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.ListAdapters.Student_Adapter;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Students extends Activity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceStudents;

    EditText etSearch;
    Button butSearch;
    ListView lvStudents;

    Boolean qrRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_teachers);

        setTitle("Students");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
            qrRequest = bundle.getBoolean("qrrequest");

        init_Fields();
        init_Listeners();

        loadValuesFromFireBase(etSearch.getText().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadValuesFromFireBase(etSearch.getText().toString());
    }

    private void init_Fields() {
        lvStudents = findViewById(R.id.lvTeachers);
        etSearch = findViewById(R.id.etSearch);
        butSearch = findViewById(R.id.butSearch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceStudents = firebaseDatabase.getReference().child("students");
    }

    private void init_Listeners() {
        lvStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (qrRequest) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", studentArrayList.get(position).getId());

                    Intent intent = new Intent(View_Students.this, QR_Viewer.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", studentArrayList.get(position).getId());

                    Intent intent = new Intent(View_Students.this, Edit_Student.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            }
        });


        lvStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(View_Students.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseReferenceStudents.child(studentArrayList.get(position).getId()).removeValue();
                                dialog.dismiss();
                            }

                        })

                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();


                return false;
            }
        });

        butSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadValuesFromFireBase(etSearch.getText().toString());
            }
        });
    }

    ArrayList<Student> studentArrayList = new ArrayList<>();

    private void loadValuesFromFireBase(final String keyword) {
        databaseReferenceStudents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Student student = childSnapshot.getValue(Student.class);
                    if (keyword.equals(""))
                        studentArrayList.add(student);
                    else if (student.getFull_name().toLowerCase().contains(keyword.toLowerCase()))
                        studentArrayList.add(student);
                }
                updateList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateList() {
        Student_Adapter student_adapter = new Student_Adapter(studentArrayList, getApplicationContext());
        lvStudents.setAdapter(student_adapter);
    }

}
