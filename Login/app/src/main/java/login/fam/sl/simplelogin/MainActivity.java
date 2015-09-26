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
//import org.apache.http.NameValuePair;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private AsyncTask<String, String, String> asyncTask;
    private Button mLoginBtn;
    private EditText mUserName, mPwd;
    private TextView mTvResponse;
    private String response;
    private Button mBtnRegister;
    // Progress Dialog
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginBtn= (Button) findViewById(R.id.btn_login);
        mUserName = (EditText)findViewById(R.id.edt_email);
        mPwd = (EditText)findViewById(R.id.edt_password);
        mTvResponse = (TextView)findViewById(R.id.tv_response);
        mBtnRegister= (Button) findViewById(R.id.btn_register);
        pDialog=new ProgressDialog(this);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();
                String  email=mUserName.getText().toString().trim();
                String pwd=mPwd.getText().toString().trim();

                makeHTTPCall(email,pwd);

            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void makeHTTPCall(final String email , final String password)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        // Tag used to cancel the request
        pDialog.setMessage("Signing in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConstants.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mTvResponse.setText(response);
                hideDialog();
                Log.d("LOGIN", "***** LOGin Response: " + response.toString());

                boolean success = response.contains("true");
                if (success) {

                    Toast.makeText(getApplicationContext(), "Signed in !", Toast.LENGTH_LONG).show();
                    //Launch welcome activity
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();
                } else {

                    Toast.makeText(getApplicationContext(),
                            "User not found", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOGIN", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };
        queue.add(strReq);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

