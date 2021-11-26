package com.example.firebaseapp.RegisterLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.firebaseapp.HomePage;
import com.example.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private Button login;
    private final MainActivity context=this;
    private FirebaseAuth mAuth;
    private TextView register_user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //// Initializing the views
        initializeViews();
        mAuth = FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.login_color));

        /// Login button onclick listener
        login.setOnClickListener(v -> {


            if(TextUtils.isEmpty(email.getText().toString()))
            {
                email.setError("Email can not be empty");
            }

            if(TextUtils.isEmpty(password.getText().toString()))
            {
                password.setError("Password can not be empty");
            }

            else
            {
                progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Please wait....");
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Intent intent=new Intent(MainActivity.this, HomePage.class);
                                    startActivity(intent);

                                    finish();
                                }

                                else
                                {
                                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });

        /// Register user


        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(MainActivity.this,HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    private void initializeViews()
    {
        email=findViewById(R.id.user_email);
        password=findViewById(R.id.user_password);
        login=findViewById(R.id.user_login);
        register_user=findViewById(R.id.register_user);
    }

}
