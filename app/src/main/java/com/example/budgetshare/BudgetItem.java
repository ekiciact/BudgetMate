package com.example.budgetshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
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

public class BudgetItem extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private RequestQueue requestQueue;
    private RequestQueue requestQueue1;
    ListView lvItem;
    CustomItemAdapter customItemAdapter;
    Button btnNewItem;
    EditText etAmount, etName, etDesc;
    TextView result;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_item);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);

        lvItem = (ListView) findViewById(R.id.lvItem);
        btnNewItem = findViewById(R.id.btnNewItem);
        etAmount = findViewById(R.id.etAmount);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        result = findViewById(R.id.result);


        customItemAdapter = new CustomItemAdapter(this,R.layout.item_row_layout);
        lvItem.setAdapter(customItemAdapter);
        final ArrayList<String> itemId = new ArrayList<String>();


        Intent intent = getIntent();
        final String schemaId= intent.getStringExtra("schemaId");

        showProgress(true);
        tvLoad.setText("Preparing the list....please wait...");


        String requestURL = "https://studev.groept.be/api/a19sd312/getmyitem/"+schemaId;

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

                        String item_name, item_description, item_date, item_amount;
                        double total = 0;

                        for (int i = 0; i <= response.length(); i++)
                        {
                            try {
                                JSONObject JO = response.getJSONObject(response.length()-i);
                                item_name = JO.getString("item_name");
                                item_description = JO.getString("item_description");
                                item_date = JO.getString("item_date");
                                item_amount = JO.getString("item_amount");

                                total+= Double.parseDouble(item_amount);

                                itemId.add(JO.getString("item_id"));
                                CustomItemList customItemList = new CustomItemList(item_name, item_description, item_date, item_amount);


                                customItemAdapter.add(customItemList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                showProgress(false);
                            }
                        }
                        result.setText("Total expense/income = "+total);
                        if (total > 0)
                        {
                            result.setBackgroundColor(Color.GREEN);
                            result.setTextColor(Color.WHITE);
                        }
                        else if (total < 0)
                        {
                            result.setBackgroundColor(Color.RED);
                            result.setTextColor(Color.WHITE);
                        }
                        showProgress(false);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(BudgetItem.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
        requestQueue.add(jsonArrayRequest);


        btnNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty() || etDesc.getText().toString().isEmpty() || etAmount.getText().toString().isEmpty())
                {
                    Toast.makeText(BudgetItem.this, "Please enter all fields!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name = etName.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();
                    String amount = etAmount.getText().toString().trim();

                    Intent intent = getIntent();
                    String schemaId= intent.getStringExtra("schemaId");


                    showProgress(true);
                    tvLoad.setText("Adding new budget....please wait...");

                    String requestURL = "https://studev.groept.be/api/a19sd312/newmyitem/"+schemaId+"/"+name+"/"+desc+"/"+amount;

                    StringRequest submitRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            Toast.makeText(BudgetItem.this, "Item successful added to the list!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(BudgetItem.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                    requestQueue1.add(submitRequest);
                }


            }
        });

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(BudgetItem.this, Item.class);
                intent.putExtra("itemId", itemId.get(position));

                startActivityForResult(intent, 1);

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




