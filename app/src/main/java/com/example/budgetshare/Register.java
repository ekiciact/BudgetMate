package com.example.budgetshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharebuddy.R;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class Register extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private RequestQueue requestQueue;
    private static final String SUBMIT_URL = "https://studev.groept.be/api/a18sdtest4/order/";
    private static final String QUEUE_URL = "https://studev.groept.be/api/a18sdtest4/queue";


    EditText etName, etMail, etPassword, etReEnter;
    Button btnRegister;
    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etReEnter = findViewById(R.id.etReEnter);
        btnRegister = findViewById(R.id.btnRegister);

        requestQueue = Volley.newRequestQueue(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {







            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty() || etMail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() ||
                        etReEnter.getText().toString().isEmpty())
                {
                    Toast.makeText(Register.this, "Please enter all details!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (etMail.getText().toString().toLowerCase().contains(".com") && etMail.getText().toString().toLowerCase().contains("@")){


                        if (etPassword.getText().toString().length() < 8){
                            Toast.makeText(Register.this, "Please enter at least 8 digits as password!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (etPassword.getText().toString().trim().equals(etReEnter.getText().toString().trim()))
                            {
                                String name = etName.getText().toString().trim();
                                String email = etMail.getText().toString().trim();
                                String password = etPassword.getText().toString().trim();



                                showProgress(true);
                                tvLoad.setText("Busy registering user...please wait...");

                                String requestURL = "https://studev.groept.be/api/a19sd312/register/"+name+"/"+email+"/"+password;

                                StringRequest submitRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Toast.makeText(Register.this, "User successfully registered!", Toast.LENGTH_SHORT).show();
                                        Register.this.finish();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(Register.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });
                                requestQueue.add(submitRequest);

                            }
                            else
                            {
                                Toast.makeText(Register.this, "Please make sure that your password and re-type password is the same!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(Register.this, "Please enter a valid e-mail!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }





    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
