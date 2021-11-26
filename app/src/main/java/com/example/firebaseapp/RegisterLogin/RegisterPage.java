package com.example.firebaseapp.RegisterLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.firebaseapp.R;
import com.example.firebaseapp.User.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    Button verify;
    EditText register_email, register_password,user_name;
    ProgressDialog progressDialog;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        initialize_Views();
        auth = FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.login_color));

        verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(user_name.getText().toString().trim()))
                {
                    user_name.setError("Name can not be Empty");
                }
                if(TextUtils.isEmpty(register_email.getText().toString().trim()))
                {
                    register_email.setError("Email can not be Empty");
                }
                if(TextUtils.isEmpty(register_password.getText().toString()))
                {
                    register_password.setError("Password can not be Empty");
                }
                if(register_password.getText().toString().trim().length()<=5)
                {
                    register_password.setError("Password should be greater than 5");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(register_email.getText().toString()).matches())
                {
                    register_email.setError("Invalid Email");
                }
                else {
                    registerUser();
                }
            }
        });

    }

//    Registering User

    private void registerUser() {
        progressDialog=new ProgressDialog(RegisterPage.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait....");
        auth.createUserWithEmailAndPassword(register_email.getText().toString(),register_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseDatabase database=FirebaseDatabase.getInstance();
                String id = authResult.getUser().getUid();
                User user=new User(user_name.getText().toString(),register_email.getText().toString(),register_password.getText().toString(),null,id);
                database.getReference("Users").child(id).setValue(user);
                Toast.makeText(RegisterPage.this,"DataInserted",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent=new Intent(RegisterPage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void initialize_Views() {
        verify=findViewById(R.id.register_verify);
        register_email=findViewById(R.id.register_mail);
        register_password=findViewById(R.id.register_password);
        user_name=findViewById(R.id.user_name);

    }


}