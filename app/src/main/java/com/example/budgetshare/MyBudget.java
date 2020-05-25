package com.example.budgetshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharebuddy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyBudget extends AppCompatActivity {

    Button btnNewBudget, btnBudgetList;
    EditText etName, etDesc;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_budget2);

        requestQueue = Volley.newRequestQueue(this);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnNewBudget = findViewById(R.id.btnNewBudget);
        btnBudgetList = findViewById(R.id.btnBudgetList);


        btnNewBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty() || etName.getText().toString().isEmpty())
                {
                    Toast.makeText(MyBudget.this, "Please enter all fields!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name = etName.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();


                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    String userId = pref.getString("userId", null);         // getting String


                    showProgress(true);
                    tvLoad.setText("Adding new budget....please wait...");

                    String requestURL = "https://studev.groept.be/api/a19sd312/newmybudget/"+userId+"/"+name+"/"+desc;



                    StringRequest submitRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(MyBudget.this, "Budget successful added to the list!", Toast.LENGTH_SHORT).show();

                            showProgress(false);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(MyBudget.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                    requestQueue.add(submitRequest);
                }


            }
        });

        btnBudgetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(MyBudget.this, BudgetList.class));


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


/*
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String email = pref.getString("userId", null);         // getting String

        Toast.makeText(MyBudget.this, email, Toast.LENGTH_SHORT).show();

*/

