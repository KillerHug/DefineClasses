package com.defineclasses.app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class Contact_Us extends Fragment {
    EditText txtContactName,txtContactEmail, txtContactMobile,txtContactMessage;
    Button update_btn;
    TextView errorMessage;
    String txtName,txtEmail,txtMobile,txtMessage;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactus, null);
        txtContactName=view.findViewById(R.id.contactName);
        txtContactEmail=view.findViewById(R.id.contactEmail);
        txtContactMessage=view.findViewById(R.id.contactMessage);
        txtContactMobile=view.findViewById(R.id.contactMobile);
        errorMessage=view.findViewById(R.id.errorMessage);
        update_btn=view.findViewById(R.id.contactData);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkName() | !checkEmail()| !checkMobile()| !checkMessage()) {
                    //msgLayout.setVisibility(View.VISIBLE);
                    return;
                }
                ContactData();
            }
        });
        return view;
    }
    private boolean checkMessage() {
        String id = txtContactMessage.getText().toString().trim();
        if (id.isEmpty()) {
            txtContactMessage.setError("Field can not be Empty");
            return false;
        }
        return true;
    }
    private boolean checkMobile() {
        String id = txtContactMobile.getText().toString().trim();
        String checkspace = "[0-9]+";
        if (id.isEmpty()) {
            txtContactMobile.setError("Field can not be Empty");
            return false;
        } else if (id.length() > 10) {
            txtContactMobile.setError("Mobile Number is greater than 10");
        } else if (!id.matches(checkspace)) {
            txtContactMobile.setError("Invalid Mobile No.!");
            return false;
        } else {
            txtContactMobile.setError(null);
        }
        return true;
    }
    private boolean checkName() {
        String id = txtContactName.getText().toString().trim();
        String checkspace = "[a-zA-Z ]+";
        if (id.isEmpty()) {
            txtContactName.setError("Field can not be Empty");
            return false;
        } else if (!id.matches(checkspace)) {
            txtContactName.setError("Please Enter Only Character");
            return false;
        } else {
            txtContactName.setError(null);
        }
        return true;
    }
    public boolean checkEmail() {
        String id = txtContactEmail.getText().toString().trim();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (id.isEmpty()) {
            txtContactEmail.setError("Field can not be Empty");
            return false;
        } else if (!id.isEmpty()) {
            if (!id.matches(pattern)) {
                txtContactEmail.setError("Invalid Email!");
                txtContactEmail.setBackgroundResource(R.drawable.edit_error);
                return false;
            } else {
                txtContactEmail.setError(null);
            }
        }
        return true;
    }
    private void ContactData() {
        final Loading_Dialog loading_dialog=new Loading_Dialog(getActivity());
        txtName=txtContactName.getText().toString().trim();
        txtEmail=txtContactEmail.getText().toString().trim();
        txtMobile=txtContactMobile.getText().toString().trim();
        txtMessage=txtContactMessage.getText().toString().trim();
        String url = "http://defineclasses.com/app/main_file.php";
        loading_dialog.startLoading();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("success");
                    if(status.equals("verify"))
                    {
                        blankField();
                        loading_dialog.dismissDialog();
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Message Successfully Send...");
                        errorMessage.setTextColor(Color.parseColor("#55ba09"));
                    }
                    else if (status.equals("noMatch")) {
                        blankField();
                        loading_dialog.dismissDialog();
                        errorMessage.setVisibility(View.GONE);
                        errorMessage.setText("Message Successfully Send...");
                        errorMessage.setTextColor(Color.parseColor("#f53e09"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;

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
                Toast.makeText(getContext(), "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("action","contactData");
                param.put("name",txtName);
                param.put("email",txtEmail);
                param.put("mobile",txtMobile);
                param.put("message",txtMessage);

                return  param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

    private void blankField() {
        txtContactName.setText("");
        txtContactEmail.setText("");
        txtContactMessage.setText("");
        txtContactMobile.setText("");

    }
}
