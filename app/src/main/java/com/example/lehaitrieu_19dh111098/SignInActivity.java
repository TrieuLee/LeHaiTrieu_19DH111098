package com.example.lehaitrieu_19dh111098;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    EditText editName, editPass;
    AppCompatButton btnSignIn, btnSignUp;
    FirebaseAuth fAuth;

    ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    String email = intent.getStringExtra("email");

                    editName.setText(email);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editName = findViewById(R.id.editText);
        editPass = findViewById(R.id.editText1);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        fAuth = FirebaseAuth.getInstance();


        if (fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                mActivityLauncher.launch(intent);

            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String pass = editPass.getText().toString();
                if (editName.getText().toString().isEmpty()){
                    editName.setError("Khong duoc bo trong");
                    return;
                }
                if (editPass.getText().toString().isEmpty()){
                    editPass.setError("Khong duoc bo trong");
                    return;
                }
                if (editPass.length()<6){
                    editPass.setError("Mat khau khong duoc duoi 6 ki tu");
                    return;
                }
                fAuth.signInWithEmailAndPassword(name,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignInActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }
}