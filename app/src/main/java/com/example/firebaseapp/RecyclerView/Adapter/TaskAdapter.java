package com.example.firebaseapp.RecyclerView.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.R;
import com.example.firebaseapp.RecyclerView.Task.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class TaskAdapter  extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    List<Task> tasks;
    Context context;
    FirebaseFirestore database=FirebaseFirestore.getInstance();

    public TaskAdapter(Context context, List<Task> tasks)
    {
        this.context=context;
        this.tasks=tasks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.tasks,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  TaskAdapter.MyViewHolder holder, int position) {
        holder.title.setText(tasks.get(position).getTitle());
        holder.description.setText(tasks.get(position).getDescription());
        holder.publisher.setText(tasks.get(position).getPublisher());
        //holder.time.setText(tasks.get(position).getTime());
        holder.task.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
//               Vibrate for 100 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    popupBottDialog();
                } else {
                    //deprecated in API 26
                    v.vibrate(100);
                }
                return true;
            }
        });
    }

    private void popupBottDialog() {
        BottomSheetDialog bottDialog= new BottomSheetDialog(this.context);
        bottDialog.setContentView(R.layout.bottom_task_dialog);
        bottDialog.show();

        Button cancelDelete,deleteTask;

        if(bottDialog.isShowing())
        {
            cancelDelete=bottDialog.findViewById(R.id.id_cancel_delete);
            deleteTask=bottDialog.findViewById(R.id.id_delete_task);


            assert cancelDelete != null;
            cancelDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottDialog.dismiss();
                }
            });

            if (deleteTask != null) {
                deleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.collection("user").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot snapshots) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {

                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setList(List<Task> list) {
        this.tasks=list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,description,publisher,time;
        RelativeLayout task;
        MyViewHolder(View view)
        {
            super(view);
            title=view.findViewById(R.id.id_task_title);
            description=view.findViewById(R.id.id_task_description);
            publisher=view.findViewById(R.id.id_publisher_name);
            task=view.findViewById(R.id.id_task);
            //time=view.findViewById(R.id.id_task_time);
        }

    }

}
