package net.tution.thilini.tution.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.tution.thilini.tution.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class Sign_In extends AppCompatActivity {

    Context context;
    Activity activity;

    private static final int RC_SIGN_IN = 1;

    private GoogleApiClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    String TAG = "Sign_In";

    SignInButton butSignIn;
    Button tvSignOut;
    TextView tvCurrentUser;

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        context = getApplicationContext();
        activity = this;

        Log.d(TAG, "On Create");

        init_Fields();
        init_Listeners();

        mAuth.addAuthStateListener(mAuthStateListener);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(Objects.requireNonNull(this), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (mAuth.getCurrentUser() != null) {
            gotoHome();
        }

        updateUI();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                fireBaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(context, "Sign in failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "fireBaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            updateUI();
                            Sign_In.this.finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "Sign in With Credential failed", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void init_Listeners() {

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Toast.makeText(context, "You are login to " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        butSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    Toast.makeText(context,"You are successfully logout",Toast.LENGTH_SHORT).show();
                }
                updateUI();
            }
        });


    }

    private void init_Fields() {
        butSignIn = findViewById(R.id.butSignIn);
        tvCurrentUser = findViewById(R.id.tvCurrentUser);
        tvSignOut = findViewById(R.id.butGrantPermissions);
        progressBar =findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    private void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            tvSignOut.setVisibility(View.INVISIBLE);
            tvCurrentUser.setText("");
        } else {
            tvSignOut.setVisibility(View.VISIBLE);
            tvCurrentUser.setText("You are login as\n" + mAuth.getCurrentUser().getEmail());
        }
    }

    private void gotoHome() {

    }

}
