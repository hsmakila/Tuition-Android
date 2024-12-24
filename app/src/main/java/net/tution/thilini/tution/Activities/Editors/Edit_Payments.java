package net.tution.thilini.tution.Activities.Editors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.Dialogs.Payment_Slip;
import net.tution.thilini.tution.ListAdapters.Student_Adapter;
import net.tution.thilini.tution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Edit_Payments extends AppCompatActivity {

    EditText etYear, etMonth, etAmountPay;
    Button butUpdateInfo, butPay;
    ListView lvSelectStudent;
    TextView tvTotalToPay, tvAlreadyPaid, tvRemainingAmount;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRefStudent, dbRefPayments, dbRefClassStudent, dbRefClasses;

    int posStudent = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment);

        setTitle("Payments");

        init_Fields();
        init_Listeners();

        loadStudentsFromFireBase();

        updateUI();

    }

    private void init_Fields() {
        etYear = findViewById(R.id.etYear);
        etMonth = findViewById(R.id.etMonth);
        etAmountPay = findViewById(R.id.etAmountPay);

        butUpdateInfo = findViewById(R.id.butUpdateInfo);
        butPay = findViewById(R.id.butPay);

        lvSelectStudent = findViewById(R.id.lvSelectStudent);

        tvTotalToPay = findViewById(R.id.tvTotalToPay);
        tvAlreadyPaid = findViewById(R.id.tvAlreadyPaid);
        tvRemainingAmount = findViewById(R.id.tvRemainingAmount);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRefStudent = firebaseDatabase.getReference().child("students");
        dbRefPayments = firebaseDatabase.getReference().child("payments");
        dbRefClassStudent = firebaseDatabase.getReference().child("class_student");
        dbRefClasses = firebaseDatabase.getReference().child("classes");
    }

    private void init_Listeners() {
        lvSelectStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posStudent = position;
                loadValuesFromFireBase();
            }
        });
        butUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (posStudent == -1)
                    Toast.makeText(Edit_Payments.this, "Select Student", Toast.LENGTH_SHORT).show();
                else if (etYear.getText().toString().equals("") || Integer.valueOf(etYear.getText().toString()) < 0)
                    Toast.makeText(Edit_Payments.this, "Enter Valid Year", Toast.LENGTH_SHORT).show();
                else if (etMonth.getText().toString().equals("") || Integer.valueOf(etMonth.getText().toString()) > 12 || Integer.valueOf(etMonth.getText().toString()) < 1)
                    Toast.makeText(Edit_Payments.this, "Enter Valid Month", Toast.LENGTH_SHORT).show();
                else {
                    loadValuesFromFireBase();
                }
            }
        });
        butPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAmountPay.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                else if (Integer.valueOf(etAmountPay.getText().toString()) > Integer.valueOf(tvRemainingAmount.getText().toString()))
                    Toast.makeText(getBaseContext(), "Amount exceed remaining amount", Toast.LENGTH_SHORT).show();
                else
                    saveValueToFireBase();

            }
        });
    }

    private void saveValueToFireBase() {
        Calendar calendar = calendarWithSelectedMonthAndYear();
        int value = Integer.valueOf(tvAlreadyPaid.getText().toString()) + Integer.valueOf(etAmountPay.getText().toString());
        dbRefPayments.child(studentArrayList.get(posStudent).getId()).child(String.valueOf(calendar.getTimeInMillis())).setValue(String.valueOf(value)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                    loadValuesFromFireBase();

                    String name = studentArrayList.get(posStudent).getFull_name();
                    String year = etYear.getText().toString();
                    int month = Integer.valueOf(etMonth.getText().toString());
                    String amountPaid = etAmountPay.getText().toString();

                    Payment_Slip payment_slip = new Payment_Slip(Edit_Payments.this, name, year, month, amountPaid);
                    payment_slip.show();
                } else
                    Toast.makeText(getBaseContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                etAmountPay.setText("");
            }
        });
    }

    ArrayList<Student> studentArrayList = new ArrayList<>();

    private void loadStudentsFromFireBase() {
        dbRefStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentArrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Student student = child.getValue(Student.class);
                    studentArrayList.add(student);
                }
                Student_Adapter student_adapter = new Student_Adapter(studentArrayList, getBaseContext());
                lvSelectStudent.setAdapter(student_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadValuesFromFireBase() {
        tvTotalToPay.setText("0");
        tvAlreadyPaid.setText("0");
        tvRemainingAmount.setText("0");

        final Student student = studentArrayList.get(posStudent);
        Calendar calendar = calendarWithSelectedMonthAndYear();
        dbRefPayments.child(student.getId()).child(String.valueOf(calendar.getTimeInMillis())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value == null || value.equals(""))
                    tvAlreadyPaid.setText("0");
                else
                    tvAlreadyPaid.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbRefClassStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> classStringArrayList = new ArrayList<>();
                for (DataSnapshot childClassStudent : dataSnapshot.getChildren()) {
                    if (childClassStudent.child("studentID").getValue(String.class).equals(student.getId()))
                        classStringArrayList.add(childClassStudent.child("classID").getValue(String.class));
                }
                dbRefClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int totalAmount = 0;
                        for (DataSnapshot childClasses : dataSnapshot.getChildren()) {
                            Class aClass = childClasses.getValue(Class.class);
                            for (String classTempID : classStringArrayList) {
                                if (classTempID.equals(aClass.getId()))
                                    totalAmount += Integer.valueOf(aClass.getFee());
                            }
                        }
                        tvTotalToPay.setText(String.valueOf(totalAmount));
                        int paidAmount = Integer.valueOf(tvAlreadyPaid.getText().toString());
                        tvRemainingAmount.setText(String.valueOf(totalAmount - paidAmount));
                        updateUI();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        int remainingAmount = Integer.valueOf(tvRemainingAmount.getText().toString());
        if (remainingAmount > 0)
            butPay.setEnabled(true);
        else
            butPay.setEnabled(false);
    }

    private Calendar calendarWithSelectedMonthAndYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Integer.valueOf(etMonth.getText().toString()) - 1);
        calendar.set(Calendar.YEAR, Integer.valueOf(etYear.getText().toString()));
        return calendar;
    }

}
