package com.bese3.nauman.citizen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bese3.nauman.citizen.data.User;
import com.bese3.nauman.citizen.data.service.UserService;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity2 extends Activity {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox rememberMeCheckBox;
    private TextView forgotPasswordTextView;
    private Button loginButton;
    private Button signUpButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity2);

        if(UserService.getInstance(this).getCurrentUser()!=null)
        {
            goToMainMenuActivity();
            return;
        }

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.checkBox_Remember);
        forgotPasswordTextView =  (TextView) findViewById(R.id.forgotpassword);
        loginButton =  (Button) findViewById(R.id.loginbtn);
        signUpButton =  (Button) findViewById(R.id.signupbtn);

        loginButton.setOnClickListener(onClickLoginButtonListener);
        signUpButton.setOnClickListener(onClickSignUpButtonListener);
        forgotPasswordTextView.setOnClickListener(onClickForgotPasswordTextViewListener);


        SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
        boolean remembered = sharedPreferences.getBoolean("remembered",false);
        if(remembered)
        {
            rememberMeCheckBox.setChecked(true);
            String username = sharedPreferences.getString("username", null);
            String password = sharedPreferences.getString("password", null);
            login(username,password);
        }
    }

    private void login(String username,String password)
    {
        progressDialog.show();
        UserService.getInstance(LoginActivity2.this).login(username, password, loginListener);
    }

    private void goToSignupActivity()
    {
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }

    private void goToMainMenuActivity()
    {
        Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    View.OnClickListener onClickLoginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            login(username, password);
        }
    };

    View.OnClickListener onClickSignUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            goToSignupActivity();
        }
    };

    View.OnClickListener onClickForgotPasswordTextViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity2.this);
            builder.setTitle("Warning");
            builder.setIcon(R.drawable.error);
            builder.setMessage("Not implemented");
            builder.setPositiveButton("OK",null);
            builder.show();
        }
    };

    UserService.LoginListener loginListener = new UserService.LoginListener() {
        @Override
        public void onResponce(boolean loggedin, String message, User user) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity2.this, message, Toast.LENGTH_SHORT).show();
            if(loggedin)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(rememberMeCheckBox.isChecked())
                {
                    editor.putBoolean("remembered", true);
                    editor.putString("username", user.getUsername());
                    editor.putString("password", user.getPassword());
                }
                else
                {
                    editor.putBoolean("remembered",false);
                    editor.remove("username");
                    editor.remove("password");
                }
                editor.commit();
                goToMainMenuActivity();
            }
        }
    };
}



