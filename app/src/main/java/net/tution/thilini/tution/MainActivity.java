package net.tution.thilini.tution;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import net.tution.thilini.tution.Activities.Home_Pages.Home;
import net.tution.thilini.tution.Activities.Login;
import net.tution.thilini.tution.Activities.Sign_In;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public enum AppStatus {SIGN_IN_FAILED, EXIT_FROM_HOME}

    public static AppStatus appStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("TUTION.NET");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appStatus == null)
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Intent intent = new Intent(MainActivity.this, Sign_In.class);
                startActivity(intent);
            } else if (!checkPermissions()) {
                requestPermissions();
            } else {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        else if (appStatus == AppStatus.EXIT_FROM_HOME || appStatus == AppStatus.SIGN_IN_FAILED) {
            appStatus = null;
            finish();
        }


    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        else
            return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

}
