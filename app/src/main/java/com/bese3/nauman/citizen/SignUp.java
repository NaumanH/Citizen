package com.bese3.nauman.citizen;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD
=======
import android.widget.CompoundButton;
>>>>>>> origin/master
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import com.bese3.nauman.citizen.data.User;
<<<<<<< HEAD
import android.data.service.UserService;
=======
import com.bese3.nauman.citizen.data.service.UserService;
>>>>>>> origin/master



public class SignUp extends ActionBarActivity {

    private ImageView profileImageView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText cnicEditText;
    private EditText cellnoEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button signupButton;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;

    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(UserService.getInstance(this).getCurrentUser()!=null)
        {
            goToMainMenuActivity();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
        usernameEditText = (EditText) findViewById(R.id.edittext_username);
        passwordEditText = (EditText) findViewById(R.id.edittext_password);
        emailEditText = (EditText) findViewById(R.id.edittext_email);
        cnicEditText = (EditText) findViewById(R.id.edittext_cnic);
        cellnoEditText = (EditText) findViewById(R.id.edittext_cellno);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);
        maleRadioButton = (RadioButton) findViewById(R.id.radiobutton_male);
        femaleRadioButton = (RadioButton) findViewById(R.id.radiobutton_female);
        signupButton = (Button) findViewById(R.id.btn_signup);

        maleRadioButton.setChecked(true);
        signupButton.setOnClickListener(onClickSignUpButtonListener);

        profileImageView.setOnClickListener(onClickProfileImageViewListener);
    }

    View.OnClickListener onClickProfileImageViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            chooseImage();
        }
    };

    private void chooseImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(data.getData());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=2;
                bitmap = BitmapFactory.decodeStream(stream, null, options);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double scale = 100.0/height;
                height = (int)(height*scale);
                width = (int)(width*scale);
                bitmap = Bitmap.createScaledBitmap(bitmap, width,height, false);
                stream.close();
                profileImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    View.OnClickListener onClickSignUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String cnic = cnicEditText.getText().toString();
                String cellno = cellnoEditText.getText().toString();

                User.Gender gender;
                if (genderRadioGroup.getCheckedRadioButtonId() == R.id.radiobutton_male) {
                    gender = User.Gender.MALE;
                } else {
                    gender = User.Gender.FEMALE;
                }
                register(username, password, email, cnic, cellno, gender);
        }
    };

    private void register(String username,String password, String email, String cnic, String cellno, User.Gender gender)
    {
        progressDialog.show();
        UserService.getInstance(SignUp.this).register(username,password,email,cnic,cellno,gender,registerListener,bitmap);
    }

    UserService.RegisterListener registerListener = new UserService.RegisterListener() {
        @Override
        public void onResponce(boolean registered, String message, User user) {
            progressDialog.dismiss();
            Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
            if(registered)
            {
                goToMainMenuActivity();
            }
        }
    };

    private void goToMainMenuActivity()
    {
        Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
