package net.tution.thilini.tution.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.tution.thilini.tution.Activities.Home_Pages.Home;
import net.tution.thilini.tution.MainActivity;
import net.tution.thilini.tution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button butLogin;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceAdmins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initiate_Fields();
        initiate_Listeners();
    }

    private void initiate_Listeners() {
        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReferenceAdmins.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isAdmin = false;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (etUsername.getText().toString().equals(child.getKey()) && etPassword.getText().toString().equals(child.getValue().toString())) {
                                isAdmin = true;
                                Toast.makeText(Login.this, "Login as " + etUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                                gotoHome();
                                break;
                            }
                        }
                        if (!isAdmin)
                            Toast.makeText(Login.this, "Incorrect Login Credentials", Toast.LENGTH_SHORT).show();

                        etUsername.setText("");
                        etPassword.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void initiate_Fields() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        butLogin = findViewById(R.id.butLogin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAdmins = firebaseDatabase.getReference().child("admins");
    }

    private void gotoHome() {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            MainActivity.appStatus = MainActivity.AppStatus.SIGN_IN_FAILED;
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
