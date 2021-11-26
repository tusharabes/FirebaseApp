package com.example.firebaseapp.Pdf;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebaseapp.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PdfResume extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);

        PDFView pdfView=findViewById(R.id.pdfView);
        pdfView.fromAsset("Tushar_Agarwal_Resume.pdf").load();

    }
}
