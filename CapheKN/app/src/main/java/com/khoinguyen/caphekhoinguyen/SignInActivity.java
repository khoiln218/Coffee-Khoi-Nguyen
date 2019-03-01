package com.khoinguyen.caphekhoinguyen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khoinguyen.caphekhoinguyen.event.LoadCompleteEvent;
import com.khoinguyen.caphekhoinguyen.model.User;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class SignInActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;

    private RelativeLayout mLayoutLoading;
    private RelativeLayout mLayoutSignIn;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // Views
        mLayoutLoading = findViewById(R.id.layoutLoading);
        mLayoutSignIn = findViewById(R.id.layoutSignIn);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        Button signInButton = findViewById(R.id.buttonSignIn);
        Button signUpButton = findViewById(R.id.buttonSignUp);

        // Click listeners
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        } else {
            showSignIn();
        }
    }

    private void showLoading() {
        mLayoutLoading.setVisibility(View.VISIBLE);
        mLayoutSignIn.setVisibility(View.GONE);
    }

    private void showSignIn() {
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutSignIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadCompleteEvent event) {
        LogUtils.d(TAG, "onMessageEvent: LoadCompleteEvent");
        // Go to MainActivity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void signIn() {
        LogUtils.d(TAG, "signIn");

        Utils.hideKeyBoard(this);
        if (validateForm()) {
            return;
        }

        showLoading();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    LogUtils.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                    showSignIn();

                    if (task.isSuccessful()) {
                        onAuthSuccess(Objects.requireNonNull(task.getResult()).getUser());
                    } else {
                        Utils.showToast(SignInActivity.this, "Đăng nhập không thành công");
                    }
                });
    }

    private void signUp() {
        LogUtils.d(TAG, "signUp");

        Utils.hideKeyBoard(this);
        if (validateForm()) {
            return;
        }

        showLoading();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    LogUtils.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                    showSignIn();

                    if (task.isSuccessful()) {
                        onAuthSuccess(Objects.requireNonNull(task.getResult()).getUser());
                    } else {
                        Utils.showToast(SignInActivity.this, "Đăng ký không thành công");
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser firebaseUser) {
        String username = usernameFromEmail(Objects.requireNonNull(firebaseUser.getEmail()));

        // Write new user
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), username);
        RealtimeDatabaseController.getInstance(this).themNguoiDung(user);
        RealtimeDatabaseController.getInstance(this).startListerner();
        showLoading();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        mEmailField.setError(null);
        mPasswordField.setError(null);
        if (!Utils.isValidEmail(mEmailField.getText().toString())) {
            mEmailField.setError("Email không hợp lệ");
            mEmailField.requestFocus();
            Utils.showSoftKeyboard(this);
            return true;
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Mật khẩu không hợp lệ");
            mPasswordField.requestFocus();
            Utils.showSoftKeyboard(this);
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonSignIn) {
            signIn();
        } else if (i == R.id.buttonSignUp) {
            signUp();
        }
    }
}
