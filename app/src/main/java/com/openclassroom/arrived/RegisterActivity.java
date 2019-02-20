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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.User;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = "RegisterActivity";

    private EditText mEditMail;
    private EditText mEditPwd;
    private EditText mEditName;
    private EditText mEditLName;
    private EditText mEditAge;

    private RadioButton mRadioF;
    private RadioButton mRadioM;

    private Button mButRegis;

    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mFUser;
    private FirebaseDatabase mFDatabase;
    private DatabaseReference mRefUsers;

    private ArrayList<User> mListUsers;
    private String[] mListMode = {"pieds", "voiture", "velo", "metro", "tram", "bus", "train"};
    private String[] mListMeteo = {"sunny", "cloudy", "rain", "snow"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        onClicButRegister();
    }


    private void init() {
        mEditMail = (EditText) findViewById(R.id.edit_email);
        mEditPwd = (EditText) findViewById(R.id.edit_pwd);
        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditLName = (EditText) findViewById(R.id.edit_l_name);
        mEditAge = (EditText) findViewById(R.id.edit_age);

        mRadioF = (RadioButton) findViewById(R.id.radio_f);
        mRadioM = (RadioButton) findViewById(R.id.radio_m);

        mButRegis = (Button) findViewById(R.id.but_regis);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mFUser = mAuth.getCurrentUser();
        mFDatabase = FirebaseDatabase.getInstance();
        mRefUsers = mFDatabase.getReference().child("utilisateurs");
        mListUsers = new ArrayList<>();

    }


    private void onClicButRegister() {
        mButRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logCreateUser();
            }
        });
    }

    private boolean validateCoord() {
        String email, pwd, name, lName, age, sex;
        email = mEditMail.getText().toString();
        pwd = mEditPwd.getText().toString();
        name = mEditName.getText().toString();
        lName = mEditLName.getText().toString();
        age = mEditAge.getText().toString();
        sex = mRadioF.isChecked() || mRadioM.isChecked() ? "select" : "non_select";

        if ((email.matches("")) || (pwd.matches("")) || (name.matches(""))
                || (lName.matches("")) || (age.matches("")) || (sex.matches(""))) {
            launchAlert();
            return false;
        } else {
            return true;
        }
    }

    private void launchAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
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

    private void logCreateUser() {
        if (validateCoord()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(mEditMail.getText().toString(), mEditPwd.getText().toString())
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "user creer registerActivity " + task.isSuccessful());
                            mProgressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                createFirebaseUser();
                                launchActivityHome();
                            } else {
                                Log.w(TAG, "user non creer registerActivity ", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void createFirebaseUser() {
        mRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFUser = mAuth.getCurrentUser();
                HashMap<String, String> mapMode = initMapMode();
                HashMap<String, String> mapMeteo = initMapMeteo();
                ArrayList<String> initList = new ArrayList<String>();
                User user = new User(mFUser.getUid(), mEditMail.getText().toString(), mEditName.getText().toString(),
                        mEditLName.getText().toString(),0, Integer.parseInt(mEditAge.getText().toString()),
                        initList, mapMode, mapMeteo);
                mRefUsers.child(user.getmUid()).setValue(user);
                Toast.makeText(RegisterActivity.this, " user " + user.getmName() + " add ", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private HashMap<String, String> initMapMode(){
        HashMap<String, String> mapMode = new HashMap<>();
        for(int i =0; i<mListMode.length;i++)
        {
            mapMode.put(mListMode[i],""+0);
        }
        return mapMode;
    }
    private HashMap<String, String> initMapMeteo(){
        HashMap<String, String> mapMeteo = new HashMap<>();
        for(int i =0; i<mListMeteo.length;i++)
        {
            mapMeteo.put(mListMeteo[i],""+0);
        }
        return mapMeteo;
    }


    private void launchActivityHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

}