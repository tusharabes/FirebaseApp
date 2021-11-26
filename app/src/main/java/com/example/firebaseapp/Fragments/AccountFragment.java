package com.example.firebaseapp.Fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firebaseapp.Pdf.PdfResume;
import com.example.firebaseapp.R;
import com.example.firebaseapp.RegisterLogin.MainActivity;
import com.example.firebaseapp.SqliteDatabase.AccountDbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


public class AccountFragment extends Fragment {

    private Button resume,logout;
    private FirebaseAuth auth;
    private  Context context;
    private ImageView profile_image;
    private final  static int RESULT_LOAD_IMG = 1;
    FirebaseStorage storage;
    ProgressDialog dialog;
    Uri selectedImage;
    FirebaseDatabase database;
    LinearLayout background;

    public AccountFragment()
    {

    }
    public AccountFragment(Context context) {
        // Required empty public constructor
        this.context=context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);


    }


    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resume=getActivity().findViewById(R.id.resume_button);
        logout=getActivity().findViewById(R.id.logout);
        auth=FirebaseAuth.getInstance();
        profile_image=getActivity().findViewById(R.id.profile_id);
        background=getActivity().findViewById(R.id.pImage_background);
        database=FirebaseDatabase.getInstance();
        profile_image.setImageResource(R.drawable.account_icon);


        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (getActivity())
                {
                    loadData();
                }

            }
        }).start();



//        Open Pdf Activity
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PdfResume.class);
                startActivity(intent);
            }
        });

//        Logout from the app

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent  intent =new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

//        Set profile Image
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new ProgressDialog(getActivity());

                loadImagefromGallery();


            }
        });
    }

    private void loadData() {
        AccountDbHelper dbHelper=new AccountDbHelper(getActivity());
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String query="SELECT * FROM PROFILE where id=1";
        Cursor cursor=db.rawQuery(query,null);


        if(cursor!=null)
        {
            if (cursor.moveToFirst())
            {
                byte[] bytes=cursor.getBlob(2);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                if(getActivity()!=null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //profile_image.setImageBitmap(bitmap);
                        profile_image.setImageBitmap(bitmap);
                    }
                });


            }
            cursor.close();
            db.close();
        }
    }


    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                 selectedImage = data.getData();

                // Set the Image in ImageView after decoding the String

                profile_image.setImageURI(selectedImage);
                //Toast.makeText(getActivity(),selectedImage.getPath(),Toast.LENGTH_LONG).show();
                saveImage(selectedImage);
                insertInSqlite();


            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void insertInSqlite() {
        AccountDbHelper dbHelper=new AccountDbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        BitmapDrawable drawable=(BitmapDrawable) profile_image.getDrawable();
        Bitmap bitMap =drawable.getBitmap();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[] bytes=outputStream.toByteArray();
        ContentValues values=new ContentValues();
        values.put("auth", FirebaseAuth.getInstance().getUid());
        values.put("image",bytes);
        sqLiteDatabase.insert("profile",null,values);


    }

    private void saveImage(Uri image)
    {

        dialog.show();
        dialog.setMessage("Storing Image");
        dialog.setCanceledOnTouchOutside(false);
        storage=FirebaseStorage.getInstance();
        StorageReference reference =storage.getReference("Users").child("Profile Images").child(auth.getUid());

        reference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference("Users").child(auth.getCurrentUser().getUid()).child("avatar").setValue(uri.toString());

                            //Toast.makeText(context, "Image Inserted", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else
                {
                    Toast.makeText(context, task.getResult()+"", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(getActivity(),"Error Occured while loading",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }
 }


