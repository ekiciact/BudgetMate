package com.example.budgetshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Item extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private RequestQueue requestQueue;
    private RequestQueue requestQueue1;


    Button btnEdit, btnDelete;
    EditText etAmount, etName, etDesc;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);


        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        etAmount = findViewById(R.id.etAmount);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);


        Intent intent = getIntent();
        final String itemId= intent.getStringExtra("itemId");

        Toast.makeText(getApplicationContext(), itemId,
                              Toast.LENGTH_SHORT).show();


        showProgress(true);
        tvLoad.setText("Preparing the list....please wait...");


        String requestURL = "https://studev.groept.be/api/a19sd312/getitem/"+itemId;

        //////
        JsonArrayRequest
                jsonArrayRequest
                = new JsonArrayRequest(
                Request.Method.GET,
                requestURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {


                            try {
                                JSONObject JO = response.getJSONObject(0);
                                etAmount.setHint("Amount: "+JO.getString("item_amount"));
                                etName.setHint("Name: "+JO.getString("item_name"));
                                etDesc.setHint("Description: "+JO.getString("item_description"));





                            } catch (JSONException e) {
                                e.printStackTrace();
                                showProgress(false);
                            }


                        showProgress(false);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(Item.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
        requestQueue.add(jsonArrayRequest);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty() || etDesc.getText().toString().isEmpty() || etAmount.getText().toString().isEmpty())
                {
                    Toast.makeText(Item.this, "Please enter all fields!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name = etName.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();
                    String amount = etAmount.getText().toString().trim();

                    showProgress(true);
                    tvLoad.setText("Adding new budget....please wait...");

                    String requestURL = "https://studev.groept.be/api/a19sd312/updatemyitem/"+name+"/"+desc+"/"+amount+"/"+itemId;

                    StringRequest submitRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(Item.this, "Item successful added to the list!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            Item.this.finish();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(Item.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                    requestQueue1.add(submitRequest);
                }


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    showProgress(true);
                    tvLoad.setText("Adding new budget....please wait...");

                    String requestURL = "https://studev.groept.be/api/a19sd312/deletemyitem/"+itemId;

                    StringRequest submitRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(Item.this, "Item is successful deleted from the list!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            Item.this.finish();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Item.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                    requestQueue1.add(submitRequest);

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

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}




