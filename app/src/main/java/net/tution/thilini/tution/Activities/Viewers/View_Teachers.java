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

import net.tution.thilini.tution.Activities.Editors.Edit_Teacher;
import net.tution.thilini.tution.DataStructures.Teacher;
import net.tution.thilini.tution.ListAdapters.Teacher_Adapter;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Teachers extends Activity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceTeachers;

    EditText etSearch;
    Button butSearch;
    ListView lvTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_teachers);

        setTitle("Teachers");

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
        lvTeachers = findViewById(R.id.lvTeachers);
        etSearch = findViewById(R.id.etSearch);
        butSearch = findViewById(R.id.butSearch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceTeachers = firebaseDatabase.getReference().child("teachers");
    }

    private void init_Listeners() {
        lvTeachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("id", teachersArrayList.get(position).getId());

                Intent intent = new Intent(View_Teachers.this, Edit_Teacher.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        lvTeachers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(View_Teachers.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseReferenceTeachers.child(teachersArrayList.get(position).getId()).removeValue();
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

    ArrayList<Teacher> teachersArrayList = new ArrayList<>();

    private void loadValuesFromFireBase(final String keyword) {
        databaseReferenceTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teachersArrayList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Teacher teacher = childSnapshot.getValue(Teacher.class);

                    if (keyword.equals(""))
                        teachersArrayList.add(teacher);
                    else if (teacher.getFirst_name().toLowerCase().contains(keyword.toLowerCase()) || teacher.getLast_name().toLowerCase().contains(keyword.toLowerCase()))
                        teachersArrayList.add(teacher);

                }
                updateList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateList() {
        Teacher_Adapter teacher_adapter = new Teacher_Adapter(teachersArrayList, getApplicationContext());
        lvTeachers.setAdapter(teacher_adapter);
    }

}
