package com.defineclasses.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile_Fragment extends Fragment {
    ImageButton back;
    User user;
    TextView username;
    Button imageChooser, edit_enable;
    Button changePass, updateData;
    CircleImageView imageView;
    private static final int PERMISSION_FILE = 23;
    private static final int ACCESS_FILE = 43;
    Bitmap bitmap;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    String encodeImage;
    String user_name;
    EditText updateUsername, updateName, updateMobile, updateEmail, updateAddress;
    SessionManager sessionManager;
    String txtUsername, txtname, txtEmail, txtAddress, txtMobile;
    ConstraintLayout successMSG;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        back = view.findViewById(R.id.backTo);
        updateUsername = view.findViewById(R.id.updateUser_Name);
        updateName = view.findViewById(R.id.updateName);
        updateAddress = view.findViewById(R.id.updateAddress);
        updateEmail = view.findViewById(R.id.updateEmailId);
        updateMobile = view.findViewById(R.id.updateMobile);
        updateData = view.findViewById(R.id.updateData_btn);
        changePass = view.findViewById(R.id.change_password);
        username = (TextView) view.findViewById(R.id.user_name);
        imageView = (CircleImageView) view.findViewById(R.id.user_logo);
        imageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        imageChooser = (Button) view.findViewById(R.id.imageChooser);
        edit_enable = view.findViewById(R.id.edit_data);
        successMSG = view.findViewById(R.id.successMSG);
        sessionManager = new SessionManager(getContext());
        updateUsername.setText(sessionManager.getUsername());
        updateName.setText(sessionManager.getName());
        edit_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName.setBackgroundResource(R.drawable.bottom_border);
                updateMobile.setBackgroundResource(R.drawable.bottom_border);
                updateEmail.setBackgroundResource(R.drawable.bottom_border);
                updateAddress.setBackgroundResource(R.drawable.bottom_border);
                updateName.setEnabled(true);
                updateMobile.setEnabled(true);
                updateEmail.setEnabled(true);
                updateAddress.setEnabled(true);
                updateData.setVisibility(View.VISIBLE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new Home_Fragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left)
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new Student_Change_Password();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right
                        )
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoose();
                Log.e("Hello", "Hi");
            }
        });
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEmail()) {
                    return;
                }
                updateStudentData();
            }
        });

        displayImage();
        showStudentData();
        return view;
    }

    public void updateStudentData() {
        txtUsername = sessionManager.getUsername();
        txtname = updateName.getText().toString();
        txtEmail = updateEmail.getText().toString();
        txtAddress = updateAddress.getText().toString();
        txtMobile = updateMobile.getText().toString();
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successMSG.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        successMSG.setVisibility(View.GONE);
                                    }
                                }, 3000);
                            }
                        }, 3000);
                        Toast.makeText(getContext(), "Your Data successfully updates", Toast.LENGTH_SHORT).show();
                    } else if (success.equals("0")) {
                        Toast.makeText(getContext(), "Data  not updates", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException error) {
                    Log.e("JSON Message", error.getMessage());
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "updateInfo");
                param.put("user_name", txtUsername);
                param.put("name", txtname);
                param.put("email", txtEmail);
                param.put("address", txtAddress);
                param.put("mobile", txtMobile);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showStudentData() {
        final SessionManager sessionManager = new SessionManager(getContext());
        final String user_name = sessionManager.getUsername();
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            updateEmail.setText(jsonObject1.getString("email"));
                            updateAddress.setText(jsonObject1.getString("address"));
                            updateMobile.setText(jsonObject1.getString("mobile"));
                            user= new User(jsonObject1.getString("email"));
                            user.setEmail(jsonObject1.getString("email"));
                            sessionManager.saveEmail_Address(user);
                        }
                    }
                } catch (JSONException error) {
                    Log.e("JSON Message", error.getMessage());
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "showStudentData");
                param.put("user_name", user_name);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public boolean checkEmail() {
        String id = updateEmail.getText().toString().trim();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!id.isEmpty()) {
            if (!id.matches(pattern)) {
                updateEmail.setError("Invalid Email!");
                updateEmail.setBackgroundResource(R.drawable.edit_error);
                return false;
            } else {
                updateEmail.setError(null);
            }
        }
        return true;
    }

    public void imageChoose() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Pilih gamber"), ACCESS_FILE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Crop IMage", "1");
        if (requestCode == ACCESS_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri FILE_URI = data.getData();
            Log.e("Crop URI", String.valueOf(FILE_URI));
            CropImage.activity(FILE_URI)
                    .setAspectRatio(1, 1)
                    .setActivityTitle("Crop Image")
                    .setCropMenuCropButtonTitle("Done")
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.e("Crop IMage", "3");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.e("Crop IMage", "4");
                Uri resultUri = result.getUri();
                Log.e("URI", String.valueOf(resultUri));
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(resultUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    imageStore(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("Crop Image", "Not Success");
        }
    }

    public void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        byte[] imageByte = stream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        final String imageAddress = encodeImage;
        Log.e("Image Address", encodeImage);
        SessionManager sessionManager = new SessionManager(getContext());
        user_name = sessionManager.getUsername();
        Log.e("Response", user_name);
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Image Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("noVerify")) {
                        Toast.makeText(getContext(), "Not Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Fragment Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "storeImage");
                param.put("imageAddress", imageAddress);
                param.put("user_name", user_name);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void displayImage() {
        String url = "http://defineclasses.com/app/main_file.php";
        SessionManager sessionManager = new SessionManager(getContext());
        final String user_name = sessionManager.getUsername();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String photoAddress = jsonObject.getString("photo");
                    if (photoAddress.isEmpty()) {
                        imageView.setBackgroundResource(R.drawable.profile_logo1);
                    }
                    Log.e("Photo", photoAddress);
                    String photoURL = "https://defineclasses.com/" + photoAddress;
                    Glide.with(getContext())
                            .asBitmap()
                            .placeholder(R.drawable.gradient)
                            .error(R.drawable.btn_bg)
                            .load(photoURL)
                            .into(imageView);
                    Log.e("Photo URL",photoURL);
                } catch (JSONException error) {
                    Toast.makeText(getContext(), "Message: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("JSON Error", error.getMessage());
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", user_name);
                params.put("action", "displayPhoto");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
