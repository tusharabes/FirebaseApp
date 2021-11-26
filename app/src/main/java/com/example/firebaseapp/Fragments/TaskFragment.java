package com.example.firebaseapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firebaseapp.R;
import com.example.firebaseapp.RecyclerView.Adapter.TaskAdapter;
import com.example.firebaseapp.RecyclerView.Task.Task;
import com.google.android.gms.common.internal.ICancelToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskFragment extends Fragment {


    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    ArrayList<Task> tasks;
    FirebaseFirestore firestore;
    FloatingActionButton add_task;
    Dialog nagDialog;
    Button dClose,dSubmit;
    TextView title_textCount,error_message;
    EditText add_title,description;
    SwipeRefreshLayout swipeTask;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasks=new ArrayList<>();
        initViews(view);
        loadTasks(view);
        swipeInstructions(view);

        add_task.setOnClickListener(view1 -> {

            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.task_popup);
            nagDialog.onWindowFocusChanged(true);
            nagDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            nagDialog.show();

           if(nagDialog.isShowing())
           {

               dClose=nagDialog.findViewById(R.id.id_task_close);
               dSubmit=nagDialog.findViewById(R.id.id_task_submit);
               title_textCount=nagDialog.findViewById(R.id.id_title_textCount);
               description=nagDialog.findViewById(R.id.id_desc);
               add_title=nagDialog.findViewById(R.id.id_add_title);
               dClose.setOnClickListener(view2 -> nagDialog.dismiss());

               dSubmit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       callSubmit(view);
                   }
               });
               title_textCount.setText("0 / 20");


               add_title.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                   }

                   @Override
                   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                       int count=add_title.length();
                       title_textCount.setText(count+ " / 20");
                   }

                   @Override
                   public void afterTextChanged(Editable editable) {

                   }
               });
           }
        });

//      Setting on click listener to delete task button


    }

//  Setting swipe task instructions

    private void swipeInstructions(View view) {
        swipeTask.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestore.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot=task.getResult();

                        if(snapshot.isEmpty());
                        else
                        {
                            List<Task> list=snapshot.toObjects(Task.class);
                            taskAdapter.setList(list);
                            taskAdapter.notifyDataSetChanged();
                            swipeTask.setRefreshing(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        swipeTask.setRefreshing(false);
                    }
                });
            }
        });
    }

//  Initializing the views

    private void initViews(View view) {
        add_task=view.findViewById(R.id.id_add_task);
        nagDialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        firestore=FirebaseFirestore.getInstance();
        swipeTask=view.findViewById(R.id.id_swipe_task);


    }

//  Submitting the task created to server

    private void callSubmit(View view)
    {
        Map<String ,String> task=new HashMap<>();
        task.put("title",add_title.getText().toString());
        task.put("description",description.getText().toString());
        task.put("publisher","Dummy");
        firestore.collection("user").add(task).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                nagDialog.dismiss();
                Snackbar.make(view.getContext(),view,"Task Added",Snackbar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
                Snackbar.make(view.getContext(),view,"Task Failed",Snackbar.LENGTH_SHORT).show();
                nagDialog.dismiss();
            }
        });
    }

//  Loading the data into the task fragment

    private void loadTasks(View view)
    {
        firestore.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                QuerySnapshot snapshot=task.getResult();

                if(snapshot.isEmpty());
                else
                {
                    List<Task> list=snapshot.toObjects(Task.class);
                    taskAdapter=new TaskAdapter(view.getContext(),list);
                    recyclerView=view.findViewById(R.id.id_recycler_task);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(taskAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull  Menu menu, @NonNull  MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}