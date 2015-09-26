package login.fam.sl.simplelogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private String TAG="RegisterActivity";
    private EditText edt_reg_username, edt_reg_password,edt_reg_email,edt_reg_contact;
    private Button btn_reg_submit;
    private TextView tv_response_reg;

    // Progress Dialog
    private ProgressDialog pDialog;
   // RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

    //JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);
        edt_reg_username =(EditText)findViewById(R.id.edt_reg_username);
        edt_reg_password =(EditText)findViewById(R.id.edt_reg_password);
        edt_reg_email =(EditText)findViewById(R.id.edt_reg_email);
        edt_reg_contact =(EditText)findViewById(R.id.edt_reg_contact);
        btn_reg_submit =(Button)findViewById(R.id.btn_reg_submit);
        tv_response_reg =(TextView)findViewById(R.id.tv_response_reg);
        pDialog=new ProgressDialog(this);

        btn_reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edt_reg_username.getText().toString().trim();
                String email = edt_reg_email.getText().toString().trim();
                String password = edt_reg_password.getText().toString().trim();
                String contact = edt_reg_contact.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password,contact);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }




    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password,final String contact) {
        RequestQueue queue = Volley.newRequestQueue(this);
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConstants.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d(TAG, "***** Register Response: " + response.toString());
              //  String tags="//</?[^>]+>//gi";
              //  String responseStrip= response.replaceAll(tags, "");
              //  Log.d(TAG, "***FFF** Register responseStrip: " + responseStrip.toString());

              //  try {
                  //  JSONObject jObj = new JSONObject(responseStrip);
                    boolean success = response.contains("true");
                    if (success) {

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                    //    String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                "Error in updating data", Toast.LENGTH_LONG).show();
                    }
               /* } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("contact", contact);
                params.put("password", password);

                return params;
            }

        };
        queue.add(strReq);

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
