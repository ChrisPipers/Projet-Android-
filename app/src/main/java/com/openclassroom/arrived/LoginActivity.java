package com.openclassroom.arrived;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 3;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean mNotAdd = true;
    private Button mButLogin;
    private Button mButRegis;
    private ImageView mImVLogo;
    private EditText mEditMail;
    private EditText mEditPwd;

    FirebaseAuth mFAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        registButtonListener();
        loginButtonListener();
    }

    private void init() {
        mImVLogo = (ImageView) findViewById(R.id.image_view_logo);
        mButLogin = (Button) findViewById(R.id.login_button);
        mButRegis = (Button) findViewById(R.id.signin_button);
        mEditMail = (EditText) findViewById(R.id.edit_addr);
        mEditPwd = (EditText) findViewById(R.id.edit_pwd);
        mEditMail.setText("vasiaccepte@gmail.com");
        mEditPwd.setText("123456");
    }

    private void registButtonListener() {
        this.mButRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginButtonListener()
    {
        this.mButLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateCoord())
                {
                    mFAuth = FirebaseAuth.getInstance();
                    mFAuth.signInWithEmailAndPassword(mEditMail.getText().toString(), mEditPwd.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "sign in faillure ", task.getException());
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });
    }

    private boolean validateCoord() {
        String email, pwd;
        email = mEditMail.getText().toString();
        pwd = mEditPwd.getText().toString();
        //TODO pourrait modif attendre maj et min et chiffre
        if ((email.matches("")) || (pwd.matches("")) || pwd.length()<6 ) {
            launchAlert();
            return false;
        } else {
            return true;
        }
    }
    private void launchAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage("Certains champs ne sont pas remplis");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public void onBackPressed() {
        return;
    }

}