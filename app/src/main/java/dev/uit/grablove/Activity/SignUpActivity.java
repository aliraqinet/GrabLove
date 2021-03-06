package dev.uit.grablove.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import dev.uit.grablove.Constants;
import dev.uit.grablove.R;

public class SignUpActivity extends AppCompatActivity {
    private MaterialEditText etUserName;
    private MaterialEditText etPassword;
    private MaterialEditText etFullName;
    private Button btnSignUp;

    private String strUsername;
    private String strPassword;
    private String strFullName;

    private FirebaseFirestore db;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        map();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIsEmpty())
                    retriveData();
            }
        });

    }

    private boolean checkIsEmpty() {
        strUsername = etUserName.getText().toString();
        strPassword = etPassword.getText().toString();
        strFullName = etFullName.getText().toString();
        boolean isEmpty = false;
        if(TextUtils.isEmpty(strUsername)) {
            etUserName.setError("You must enter user name");
            isEmpty = true;
        }
        if(TextUtils.isEmpty(strPassword)) {
            etPassword.setError("You must enter password");
            isEmpty = true;
        }
        if(TextUtils.isEmpty(strFullName)) {
            etFullName.setError("You must enter your full name");
            isEmpty = true;
        }
        return isEmpty;
    }

    private void map() {
        etUserName = (MaterialEditText) findViewById(R.id.edtUserNameSignUp);
        etPassword = (MaterialEditText) findViewById(R.id.edtPasswordSignUp);
        etFullName = (MaterialEditText) findViewById(R.id.edtNameSignUp);

        btnSignUp = (Button) findViewById(R.id.btnSignUpSignUp);
    }

    private void retriveData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("SignUp...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        checkUserName(strUsername);
    }

    private void checkUserName(String strUserName) {
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo(Constants.DB_USER_NAME, strUserName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                addDataToFirestore();
                            } else {
                                Toast.makeText(getApplicationContext(), "UserName has been used!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void addDataToFirestore() {
        Map<String, Object> user = new HashMap<>();
        user.put(Constants.DB_USER_NAME, strUsername);
        user.put(Constants.DB_USER_PASSWORD, strPassword);
        user.put(Constants.DB_USER_FULL_NAME, strFullName);
        user.put(Constants.DB_USER_IS_NEW, true);

        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        Log.d("wdadwd", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "SignUp thanh cong", Toast.LENGTH_LONG).show();
                        Intent Avatar = new Intent(SignUpActivity.this, SexActivity.class);
                        startActivity(Avatar);
                        finish();
                        WelcomeActivity.getInstance().finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.w("dasddw", "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "SignUp that bai", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            SharedPreferences pre= getSharedPreferences(Constants.REF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor edit= pre.edit();
                            edit.putString(Constants.USER_KEY, task.getResult().getId());
                            edit.putString(Constants.USER_NAME, strFullName);
                            edit.commit();
                        }
                    }
                });
    }
}
