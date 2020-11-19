package com.defineclasses.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnSignUp, btnMainActivity, forgetBtn, openForget;
    EditText txtId, txtPass, forgetNumber;
    TextView msg, forgetMessage;
    String strUser, strPass, forgetMobile;
    SessionManager sessionManager;
    User user;
    LinearLayout forgetLayout;
    ConstraintLayout msgLayout;
    ProgressDialog loading;
    Loading_Dialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgetNumber = (EditText) findViewById(R.id.forgetNumber);
        btnSignUp = (Button) findViewById(R.id.goto_signup);
        btnMainActivity = (Button) findViewById(R.id.login_data);
        txtId = (EditText) findViewById(R.id.signin_ID);
        txtPass = (EditText) findViewById(R.id.signPass);
        msg = (TextView) findViewById(R.id.errorMessage);
        forgetMessage = (TextView) findViewById(R.id.forgetPassMSG);
        msgLayout = (ConstraintLayout) findViewById(R.id.successMSG);
        forgetBtn = (Button) findViewById(R.id.forget_btn);
        openForget = (Button) findViewById(R.id.open_forget);
        forgetLayout = (LinearLayout) findViewById(R.id.forgetSection);
        loading = new ProgressDialog(LoginActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("MSG", MODE_PRIVATE);
        /*String msg = sharedPreferences.getString("pass_success", null);
        if (msg != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            msgLayout.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    msgLayout.setVisibility(View.GONE);
                }
            }, 3000);

        }*/
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                finish();
            }
        });
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkId() | !checkPass()) {
                    //msgLayout.setVisibility(View.VISIBLE);
                    return;
                }
                LoginData();
            }
        });
        sessionManager = new SessionManager(this);
        boolean id = sessionManager.getLogin();
        if (id) {
            moveToFragmentContainer();
        }
        openForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetLayout.setVisibility(forgetLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                forgetMessage.setVisibility(View.GONE);
            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPassword();
            }
        });
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    private void LoginData() {

        strUser = txtId.getText().toString().trim();
        strPass = txtPass.getText().toString().trim();
        loading_dialog = new Loading_Dialog(this);
        loading_dialog.startLoading();
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    String user_name, name, student_id;
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        loading_dialog.dismissDialog();
                        user_name = jsonObject.getString("user_name");
                        name = jsonObject.getString("name");
                        student_id = jsonObject.getString("student_id");
                        sessionManager = new SessionManager(LoginActivity.this);
                        user = new User(user_name, name);
                        user.setUser_name(user_name);
                        sessionManager.saveSession(user);
                        sessionManager.setLogin(true);
                        moveToFragmentContainer();
                    } else if (status.equals("noMatch")) {
                        loading_dialog.dismissDialog();
                        Log.e("Message", "Username or Password not matched");
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Username or Password not matched");
                    } else {
                        blankField();
                        loading_dialog.dismissDialog();
                        Log.e("Message", "Not Login");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Toast.makeText(LoginActivity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("action", "signin");
                param.put("user_name", strUser);
                param.put("password", strPass);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    private void sendPassword() {
        forgetMobile = forgetNumber.getText().toString().trim();
        String url = "http://defineclasses.com/app/main_file.php";
        loading_dialog = new Loading_Dialog(this);
        loading_dialog.startLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    String mobile;
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        loading_dialog.dismissDialog();
                        forgetMessage.setVisibility(View.VISIBLE);
                        mobile = jsonObject.getString("mobile");
                        forgetMessage.setVisibility(View.VISIBLE);
                        forgetMessage.setText("Your password seccessfully send " + mobile);
                        forgetLayout.setVisibility(View.GONE);
                    } else if (status.equals("noMatch")) {
                        loading_dialog.dismissDialog();
                        forgetMessage.setVisibility(View.VISIBLE);
                        forgetMessage.setText("Your User Id or Mobile no is not found");
                        forgetLayout.setVisibility(View.GONE);
                    } else {
                        loading_dialog.dismissDialog();
                    }
                } catch (JSONException e) {
                    Log.e("JSON SMS Error", e.getMessage());
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
                Toast.makeText(LoginActivity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "sendPassword");
                param.put("mobile", forgetMobile);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean checkId() {
        String checkString = txtId.getText().toString().trim();
        if (checkString.isEmpty()) {
            Toast.makeText(this, "Field can not be Empty", Toast.LENGTH_SHORT).show();
            txtId.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (checkString.length() > 30) {
            Toast.makeText(this, "Your Username is greater than 30 digit", Toast.LENGTH_SHORT).show();
            txtId.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public boolean checkPass() {
        String checkString = txtPass.getText().toString().trim();

        if (checkString.isEmpty()) {
            Toast.makeText(this, "Field can not be Empty", Toast.LENGTH_SHORT).show();
            txtPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (checkString.length() > 20) {
            Toast.makeText(this, "Your Password is greater than 20 digit", Toast.LENGTH_SHORT).show();
            txtPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public void blankField() {
        txtId.setText("");
        txtPass.setText("");
    }

    private void moveToFragmentContainer() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
        finish();

    }
}
