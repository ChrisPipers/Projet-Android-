package com.openclassroom.arrived;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import fragment.ChoiceFragment;

public class HomeActivity extends AppCompatActivity {

    //TODO faire en sorte de pouvoir changer de mdp

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ChoiceFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    private Menu m = null;


    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.home:
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);

                return true;
            case R.id.deconnection:
                FirebaseAuth mFAuth = FirebaseAuth.getInstance();
                mFAuth.signOut();
                Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent2);

                return true;
/*
            case R.id.pers_stat:
                FragmentManager fragmentManager2;
                fragmentManager2 = getSupportFragmentManager();
                Fragment fragment = fragmentManager2.findFragmentById(R.id.fragment_container);
                if (fragment == null) {
                    fragment = new ChoiceFragment();
                    fragmentManager2.beginTransaction()
                            .add(R.id.fragment_container, fragment)
                            .commit();
                }
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

}
