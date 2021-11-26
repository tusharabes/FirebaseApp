package com.example.firebaseapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.firebaseapp.Fragments.AccountFragment;
import com.example.firebaseapp.Fragments.HomeFragment;
import com.example.firebaseapp.Fragments.MessageFragment;
import com.example.firebaseapp.Fragments.TaskFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    NavigationBarView bottomNav;
    Button resume;
    ImageView profile;
    Uri image;
    HomeFragment hFragment;
    MessageFragment mFragment;
    AccountFragment aFragment;
    TaskFragment tFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


//       Setting Statusbar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.dark_login));
        auth=FirebaseAuth.getInstance();
        bottomNav=findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().getItem(2).setChecked(true);
        loadFragment(new HomeFragment());
        profile=findViewById(R.id.profile_id);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homes:
                    {
                        if(hFragment==null)
                        {
                            hFragment=new HomeFragment();
                        }

                            loadFragment(hFragment);


                        break;
                    }

                    case R.id.messages:
                    {

                            mFragment=new MessageFragment();

                        loadFragment(mFragment);

                        break;
                    }
                    case R.id.account:
                    {
                        if(aFragment==null) {
                            aFragment = new AccountFragment(HomePage.this);
                        }
                        loadFragment(aFragment);
                        break;
                    }

                    case R.id.id_task_menu:
                        if(tFragment==null)
                        {
                            tFragment=new TaskFragment();
                        }
                        loadFragment(tFragment);
                        break;

                }

                //Toast.makeText(HomePage.this,item.getItemId()+"",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        bottomNav.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull  MenuItem item) {

            }
        });

//

    }

    public void setImageUri(Uri selectedImage)
    {
        this.image=selectedImage;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }


    public Uri getImageUri() {
        return image;
    }

//    Search bar Operations

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}