package com.example.lehaitrieu_19dh111098;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserNamePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserNamePasswordFragment extends Fragment {
    TextInputEditText tvEmail, tvPassword, tvConfirm;
    //NavController navController;
    Button btnRegister;
    FirebaseAuth mAuth;
    String userID;
    FirebaseDatabase mDB;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserNamePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserNamePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserNamePasswordFragment newInstance(String param1, String param2) {
        UserNamePasswordFragment fragment = new UserNamePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_name_password, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        //   navController = Navigation.findNavController(view);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvConfirm = view.findViewById(R.id.tvConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Firebase
                String address = getArguments().getString("address");
                String firstname = getArguments().getString("firstname");
                String lastname = getArguments().getString("lastname");
                double latitude = getArguments().getDouble("latitude");
                double longitude = getArguments().getDouble("longitude");
                String mobile = getArguments().getString("mobile");
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();
                if (!isValid(email)) {
                    tvEmail.setError("Sai phương thức nhập Email");
                    return;
                }
                if (tvPassword.getText().toString().isEmpty()) {
                    tvPassword.setError("Password required");
                    return;
                }
                if (tvConfirm.getText().toString().isEmpty()) {
                    tvConfirm.setError("Password required");
                }
                if (!tvPassword.getText().toString().equals(tvConfirm.getText().toString())) {

                    tvPassword.setError("Password and confirm password does not match");
                    tvConfirm.setText("");
                    return;
                }

                //tạp user lên firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),"Đăng kí thành công", Toast.LENGTH_LONG).show();
                                    userID = mAuth.getCurrentUser().getUid();
                                    DatabaseReference databaseReference = mDB.getReference();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("firstname", firstname);
                                    user.put("lastname", lastname);
                                    user.put("address", address);
                                    user.put("email", email);
                                    user.put("mobile", mobile);
                                    user.put("latitude", latitude);
                                    user.put("longitude", longitude);
                                    databaseReference.child("users").child(userID)
                                            .setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> aVoid) {
                                                    if(aVoid.isSuccessful()){
                                                        Intent intent = new Intent(getContext(), SignInActivity.class);
                                                        intent.putExtra("email", email);

                                                        getActivity().setResult(Activity.RESULT_OK, intent);
                                                        getActivity().finish();
                                                    }

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
    }






    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}