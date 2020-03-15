package com.example.uploadfiletofirebasestorageandsaveurlfirebasedatabasewithdocumentation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_FILE = 1;
    Button uploadBtn;
    Uri FileUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadBtn=findViewById(R.id.uploadBtn);
        progressDialog=new ProgressDialog(this);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,PICK_FILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==PICK_FILE){
            if (resultCode==RESULT_OK){
                FileUri=data.getData();
                StorageReference Folder= FirebaseStorage.getInstance().getReference().child("Files");
                final StorageReference file_Name=Folder.child("File "+FileUri.getLastPathSegment());

                file_Name.putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("UserOne");
                        file_Name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("fileLink", String.valueOf(uri));

                                databaseReference.setValue(hashMap);
                                progressDialog.setTitle("Data uploading");
                                progressDialog.show();
                                Toast.makeText(MainActivity.this, "File uploaded successful", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                progressDialog.dismiss();


            }
        }
    }
}




/*

*
This project is under The below email
            email: mridulBikiran@gmail.com
            Note: Must make true the rules of Database then work with what you want to do

            Step 1: To upload a file to Cloud Storage, you first create a reference to the full path of the file, including the file name.
            Step 2:Need to add two dependency in firebase those are Firebase Storage and firebase Database
            Step 3: Take a Button make it initialize
            Step 4:


*/
