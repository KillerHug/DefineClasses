package com.defineclasses.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    Button btnMainActivity, submitData;
    EditText txtName, txtMobile;
    String name, mobile;
    TextView msg;
    ProgressDialog loading;
    Loading_Dialog loading_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        msg = (TextView) findViewById(R.id.errorMessage);
        btnMainActivity = (Button) findViewById(R.id.goto_login);
        txtName = (EditText) findViewById(R.id.signUpName);
        txtMobile = (EditText) findViewById(R.id.signUp_Mobile);
        submitData = (Button) findViewById(R.id.signup_data);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_left_to_right);
            }
        });
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkName() | !checkMobile()) {
                    return;
                }
                SubmitData();
            }
        });
        //loading = new ProgressDialog(SignUpActivity.this);

    }

    public boolean checkName() {
        String checkString = txtName.getText().toString().trim();
        String pattern = "[a-zA-z ]+";
        if (checkString.length() > 50) {
            Toast.makeText(this, "Text limit is 50 character", Toast.LENGTH_SHORT).show();
            txtName.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        if (checkString.isEmpty()) {
            txtName.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (!checkString.matches(pattern)) {
            Toast.makeText(this, "Please Enter Only Character", Toast.LENGTH_SHORT).show();
            txtName.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public boolean checkMobile() {
        String checkString = txtMobile.getText().toString().trim();
        String pattern = "[0-9]+";
        if (checkString.isEmpty()) {
            Toast.makeText(this, "Field can not be Empty", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);

            return false;
        } else if (!checkString.matches(pattern)) {
            Toast.makeText(this, "Please Enter Only Number", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (checkString.length() > 10) {
            Toast.makeText(this, "Mobile number is greater than 10 digit", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if (checkString.length() < 10) {
            Toast.makeText(this, "Mobile number is less than 10 digit", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }


    private void SubmitData() {
        /*loading.setTitle("Loading");
        loading.setCanceledOnTouchOutside(false);
        loading.show();*/
        loading_dialog=new Loading_Dialog(this);
        loading_dialog.startLoading();
        name = txtName.getText().toString().trim();
        mobile = txtMobile.getText().toString().trim();
        String url = "http://defineclasses.com/app/main_file.php";
        Log.e("Name", name);
        Log.e("Mobile", mobile);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("1")) {
                       // loading.dismiss();
                        loading_dialog.dismissDialog();
                        Log.e("Message", "Successfully Registered");
                        blankField();
                        Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        txtName.setBackgroundResource(R.drawable.border_design);
                        txtMobile.setBackgroundResource(R.drawable.border_design);
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                        SharedPreferences sharedPreferences = getSharedPreferences("MSG", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pass_success", "pass_success");
                        editor.commit();
                        startActivity(intent);
                        finish();
                    } else if (status.equals("2")) {
                        //loading.dismiss();
                        loading_dialog.dismissDialog();
                        Log.e("Message", "Mobile No. already registered");
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Mobile No. already registered");
                        txtMobile.setTextColor(Color.parseColor("#ff0000"));
                    } else {
                        blankField();
                        loading_dialog.dismissDialog();
                        //loading.dismiss();
                        Log.e("Message", "not Registered");
                    }
                } catch (JSONException e) {
                    Log.e("Sign Up JSON Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                loading_dialog.dismissDialog();
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(SignUpActivity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "register");
                param.put("name", name);
                param.put("mobile", mobile);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void blankField() {
        txtName.setText("");
        txtMobile.setText("");
    }
}