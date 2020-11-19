package com.defineclasses.app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.defineclasses.app.Adapter.Review_Adapter;
import com.defineclasses.app.Model.Review_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Review_Fragment extends Fragment {
    String photoURL;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    List<Review_Model> review_modelList;
    Review_Model review_model;
    Review_Adapter review_adapter;
    EditText reviewName, reviewMessage, reviewEmail;
    Button submitReview;
    TextView txtReviewMessage;
    LinearLayout addLayout,allReviewLayout,noDataLayout;
    TextView txtfull_name, txtemail, txtmessage, txtregistered_date;
    RatingBar txtrating;
    ImageView txtstudent_photo;
    RatingBar  reviewRating, averageRating;
    ProgressBar progressBar1, progressBar2, progressBar3, progressBar4, progressBar5;
    int averageCount = 0, txtprogressCount1 = 0, txtprogressCount2 = 0, txtprogressCount3 = 0, txtprogressCount4 = 0, txtprogressCount5 = 0;
    TextView  txtAverageRating, txtProgress1, txtProgress2, txtProgress3, txtProgress4, txtProgress5;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        recyclerView = view.findViewById(R.id.rating_recyclerView);
        allReviewLayout=view.findViewById(R.id.allReviewLayout);
        noDataLayout=view.findViewById(R.id.noData_Layout);
        manager = new LinearLayoutManager(getContext());
        addLayout = view.findViewById(R.id.reviewAddLayout);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(review_adapter);
        averageRating = view.findViewById(R.id.averageRatingBar);
        txtAverageRating = view.findViewById(R.id.txtAverageRating);
        review_modelList = new ArrayList<>();
        reviewMessage = view.findViewById(R.id.comment_message);
        submitReview = view.findViewById(R.id.comment_submit);
        txtReviewMessage = view.findViewById(R.id.review_message);
        txtProgress1 = view.findViewById(R.id.progressCount1);
        txtProgress2 = view.findViewById(R.id.progressCount2);
        txtProgress3 = view.findViewById(R.id.progressCount3);
        txtProgress4 = view.findViewById(R.id.progressCount4);
        txtProgress5 = view.findViewById(R.id.progressCount5);
        progressBar1 = view.findViewById(R.id.progress1);
        progressBar2 = view.findViewById(R.id.progress2);
        progressBar3 = view.findViewById(R.id.progress3);
        progressBar4 = view.findViewById(R.id.progress4);
        progressBar5 = view.findViewById(R.id.progress5);
        displayReview();
        return view;
    }

    public void addCustomLayout() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_review, null);
        txtfull_name = view.findViewById(R.id.review_name);
        txtemail = view.findViewById(R.id.review_email);
        txtmessage = view.findViewById(R.id.review_message);
        txtrating = view.findViewById(R.id.showRatingView);
        txtregistered_date = view.findViewById(R.id.review_date);
        txtstudent_photo = view.findViewById(R.id.review_image);
        addLayout.addView(view);
    }

    void displayReview() {
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response Full Review", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        allReviewLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String review_id = jsonObject1.getString("review_id");
                            String full_name = jsonObject1.getString("full_name");
                            String email = jsonObject1.getString("email");
                            String message = jsonObject1.getString("message");
                            String student_photo = jsonObject1.getString("student_photo");
                            String registered_date = jsonObject1.getString("registered_date");
                            int rating = jsonObject1.getInt("rating");
                            /*addCustomLayout();
                            txtfull_name.setText(full_name);
                            txtemail.setText(email);
                            txtmessage.setText(message);
                            txtregistered_date.setText("Posted Date: " + registered_date);
                            txtrating.setRating(rating);

                            photoURL = "https://defineclasses.com/" + student_photo;

                            Glide.with(getContext())
                                    .asBitmap()
                                    .placeholder(R.drawable.gradient)
                                    .error(R.drawable.btn_bg)
                                    .load(R.drawable.course)
                                    .into(txtstudent_photo);*/
                            review_model = new Review_Model(review_id, full_name, email, message, student_photo, registered_date, rating);
                            review_modelList.add(review_model);
                            Review_Adapter review_adapter = new Review_Adapter(getContext(), review_modelList);
                            review_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(review_adapter);
                            reviewCount(rating);
                            averageCount = averageCount + rating;
                        }
                        txtAverageRating.setText(String.valueOf(averageCount / jsonArray.length()));
                        averageRating.setRating(averageCount / jsonArray.length());
                        Log.e("Length", String.valueOf(jsonArray.length()));
                    }
                    else {
                        noDataLayout.setVisibility(View.VISIBLE);
                        allReviewLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException error) {
                    Log.e("Error Course json", error.getMessage());
                }
                catch (ArithmeticException e)
                {
                    Toast.makeText(getContext(), "No Review Found", Toast.LENGTH_SHORT).show();
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
                Map<String, String> param = new HashMap<>();
                param.put("action", "showReview");
                param.put("course_id", getArguments().getString("course_id"));
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void reviewCount(int rating) {
        int ratingCount = rating;
        switch (ratingCount) {
            case 1:
                txtprogressCount1 = txtprogressCount1 + 1;
                break;
            case 2:
                txtprogressCount2 = txtprogressCount2 + 1;
                break;
            case 3:
                txtprogressCount3 = txtprogressCount3 + 1;
                break;
            case 4:
                txtprogressCount4 = txtprogressCount4 + 1;
                break;
            case 5:
                txtprogressCount5 = txtprogressCount5 + 1;
                break;
        }
        txtProgress1.setText(String.valueOf(txtprogressCount1));
        txtProgress2.setText(String.valueOf(txtprogressCount2));
        txtProgress3.setText(String.valueOf(txtprogressCount3));
        txtProgress4.setText(String.valueOf(txtprogressCount4));
        txtProgress5.setText(String.valueOf(txtprogressCount5));
        progressBar1.setProgress(txtprogressCount1 * 10);
        progressBar2.setProgress(txtprogressCount2 * 10);
        progressBar3.setProgress(txtprogressCount3 * 10);
        progressBar4.setProgress(txtprogressCount4 * 10);
        progressBar5.setProgress(txtprogressCount5 * 10);
    }

    private void blankField() {
        reviewMessage.setText("");
        reviewEmail.setText("");
        reviewName.setText("");
    }
}

