package com.example.budgetshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class BudgetList extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private RequestQueue requestQueue;
    ListView lvList;
    CustomAdapter customAdapter;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        requestQueue = Volley.newRequestQueue(this);

        lvList = (ListView) findViewById(R.id.lvList);


        customAdapter = new CustomAdapter(this,R.layout.row_layout);
        lvList.setAdapter(customAdapter);
        final ArrayList<String> schemaId = new ArrayList<String>();



        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userId = pref.getString("userId", null);         // getting String


        showProgress(true);
        tvLoad.setText("Preparing the list....please wait...");






        String requestURL = "https://studev.groept.be/api/a19sd312/getmybudget/"+userId;

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



                        String schema_name, schema_description, schema_date;




                        // create a new array of size n+1

                        for (int i = 0; i <= response.length(); i++)
                        {
                            try {
                                JSONObject JO = response.getJSONObject(response.length()-i);
                                schema_name = JO.getString("schema_name");
                                schema_description = JO.getString("schema_description");
                                schema_date = JO.getString("schema_date");
                                schemaId.add(JO.getString("schema_id"));
                                CustomBudgetList customBudgetList = new CustomBudgetList(schema_name, schema_description, schema_date);

                                customAdapter.add(customBudgetList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                showProgress(false);
                            }
                        }
                        showProgress(false);


                        /*
                        List<String> liste = new ArrayList<String>();
                        for (int i=0; i<response.length(); i++) {
                            try {
                                liste.add( response.getString(i) );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
*/



/*
                           adapter = new CustomAdapter(BudgetList.this, response);
                           lvList.setAdapter(adapter);
                           showProgress(false);
*/

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(BudgetList.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
        requestQueue.add(jsonArrayRequest);


        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(BudgetList.this, BudgetItem.class);
                intent.putExtra("schemaId", schemaId.get(position));



                startActivity(intent);



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




