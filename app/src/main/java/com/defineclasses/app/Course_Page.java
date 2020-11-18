package com.defineclasses.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.defineclasses.app.Adapter.Subject_Adapter;
import com.defineclasses.app.Adapter.Topic_Adapter;
import com.defineclasses.app.Model.Subject_Model;
import com.defineclasses.app.Model.Topic_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Course_Page extends Fragment {
    ImageButton back;
    Button moreInfo;
    LinearLayout information_layout;
    public String course_id, course_name, course_banner, course_duration, course_lectures, course_fee, course_description;
    ImageView banner;
    TextView c_name, c_duration, c_fee;
    Subject_Model subject_model;
    List<Subject_Model> subjectList;
    Subject_Adapter subject_adapter;
    Topic_Model topic_model;
    List<Topic_Model> topic_modelList;
    Topic_Adapter topic_adapter;
    RecyclerView recyclerView;
    String subject, topic_name;
    String topic_type, subject_id, topic_subject_id, topic_course_id;
    TextView txtCourseName, txtCourseDuration, txtCourseLecture, txtDescription;
    String topic_id, topic_video;
    ImageButton downArrow;
    RatingBar personal_Rating, reviewRating;
    CircleImageView personalRatingImage;
    TextView personalRatingName, personalRatingDate;
    TextView personalRatingMessage;
    LinearLayout  reviewSubmitLayout, personalReviewLayout;
    Button submitReview, editReview, seeAllReview;
    EditText reviewMessage;
    TextView txtReviewMessage;
    TextView txtMessage;
    private Object All_Courses;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_page, container, false);
        back = view.findViewById(R.id.backTo);
        txtMessage = view.findViewById(R.id.review_message);
        seeAllReview = view.findViewById(R.id.see_all_review);
        reviewSubmitLayout = view.findViewById(R.id.review_submit_layout);
        editReview = view.findViewById(R.id.edit_personal_review);
        downArrow = view.findViewById(R.id.more_info_arrow);
        moreInfo = view.findViewById(R.id.more_info);
        banner = view.findViewById(R.id.course_page_banner);
        c_name = view.findViewById(R.id.course_page_name);
        c_duration = view.findViewById(R.id.course_page_duration);
        c_fee = view.findViewById(R.id.course_page_fee);
        txtCourseDuration = view.findViewById(R.id.moreInfo_course_duration);
        txtCourseLecture = view.findViewById(R.id.moreInfo_course_lecture);
        txtCourseName = view.findViewById(R.id.moreInfo_course_name);
        txtDescription = view.findViewById(R.id.moreInfo_course_description);
        information_layout = view.findViewById(R.id.information_layout);
        personalRatingImage = view.findViewById(R.id.personal_review_image);
        personalRatingMessage = view.findViewById(R.id.personal_review_message);
        personalRatingName = view.findViewById(R.id.personal_review_name);
        personalRatingDate = view.findViewById(R.id.personal_review_date);
        personal_Rating = view.findViewById(R.id.personal_RatingView);
        personalReviewLayout = view.findViewById(R.id.personal_review_layout);
        reviewMessage = view.findViewById(R.id.comment_message);
        reviewRating = view.findViewById(R.id.reviewRating);
        submitReview = view.findViewById(R.id.comment_submit);
        txtReviewMessage = view.findViewById(R.id.review_message);
        course_id = getArguments().getString("course_id");
        course_name = getArguments().getString("course_name");
        course_banner = getArguments().getString("course_banner");
        course_duration = getArguments().getString("course_duration");
        course_lectures = getArguments().getString("course_lectures");
        course_fee = getArguments().getString("course_fee");
        course_description = getArguments().getString("course_description");
        editReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewSubmitLayout.setVisibility(View.VISIBLE);
                personalReviewLayout.setVisibility(View.GONE);
                personalReviewLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_right_to_left));
                reviewSubmitLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_right_to_left));
                reviewMessage.setText(personalRatingMessage.getText());
                reviewRating.setRating(personal_Rating.getRating());
            }
        });
        seeAllReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new Review_Fragment();
                Bundle args = new Bundle();
                args.putString("course_name", getArguments().getString("course_name"));
                args.putString("course_id", course_id);
                args.putString("course_description", getArguments().getString("course_description"));
                args.putString("course_banner", getArguments().getString("course_banner"));
                args.putString("course_duration", getArguments().getString("course_duration"));
                args.putString("course_fee", getArguments().getString("course_fee"));
                args.putString("course_lectures", getArguments().getString("course_lectures"));
                myFragment.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                getActivity().getFragmentManager().popBackStackImmediate();
            }
        });
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    submitReviewMethod();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Go to Login Screen");
                    alertDialog.setMessage("You want to submit your review! Login Please");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                                }
                            });
                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
        if(checkLogin()) {
            checkReview();
        }
        else {
            reviewSubmitLayout.setVisibility(View.VISIBLE);
        }
        if (getArguments() != null) {
            course_id = getArguments().getString("course_id");
            txtCourseName.setText(getArguments().getString("course_name"));
            txtCourseDuration.setText(getArguments().getString("course_duration"));
            txtCourseLecture.setText(getArguments().getString("course_lectures") + " Lectures");
            txtDescription.setText("Description " + getArguments().getString("course_description"));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( getArguments()!=null)
                {
                    if(getArguments().getString("TAG")=="HomeFragment")
                    {
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
                    else {
                        Fragment myFragment = new All_Courses();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.enter_left_to_right,
                                        R.anim.exit_left_to_right,
                                        R.anim.enter_right_to_left,
                                        R.anim.exit_right_to_left)
                                .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                    }
                }
            }
        });
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (information_layout.getVisibility() == View.GONE) {
                    information_layout.setVisibility(View.VISIBLE);
                    information_layout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_right_to_left));
                    downArrow.setBackgroundResource(R.drawable.ic_arrow_drop_up);
                }
                else {
                    information_layout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_right_to_left));
                    information_layout.setVisibility(View.GONE);
                    downArrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);
                }
            }
        });
        recyclerView = view.findViewById(R.id.recyclerViewInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(subject_adapter);
        subjectList = new ArrayList<>();
        showSubject();
        displayContent();
        return view;
    }
    public void checkReview() {

        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response for Review", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        personalReviewLayout.setVisibility(View.VISIBLE);
                        //personalReviewLayout.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_left_to_right));
                        int personalRating = jsonObject.getInt("rating");
                        String name = jsonObject.getString("full_name");
                        String message = jsonObject.getString("message");
                        String postDate = jsonObject.getString("registered_date");
                        String student_photo = jsonObject.getString("student_photo");
                        personalRatingMessage.setText(message);
                        personal_Rating.setRating(personalRating);
                        personalRatingDate.setText(postDate);
                        personalRatingName.setText(name);
                        String photoURL = "https://defineclasses.com/" + student_photo;

                        Glide.with(getContext())
                                .asBitmap()
                                .placeholder(R.drawable.gradient)
                                .error(R.drawable.btn_bg)
                                .load(photoURL)
                                .into(personalRatingImage);
                    } else if (status.equals("notVerify")) {
                        reviewSubmitLayout.setVisibility(View.VISIBLE);
                        reviewSubmitLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_right_to_left));
                    } else {
                    }
                } catch (JSONException e) {
                    Log.e("Sign Up JSON Error", e.getMessage());
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
                param.put("action", "showPersonalReview");
                param.put("user_name", new SessionManager(getContext()).getUsername());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public boolean checkLogin() {
        SessionManager sessionManager = new SessionManager(getContext());
        boolean id = sessionManager.getLogin();
        if (id) {
            return true;
        }
        return false;
    }
    private void displayContent() {
        c_name.setText(course_name);
        c_fee.setText("\u20B9 " + course_fee);
        c_duration.setText(course_duration + " Month");
        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.drawable.gradient)
                .error(R.drawable.btn_bg)
                .load(course_banner)
                .into(banner);
    }
    private void showSubject() {
        /*loading.setTitle("Loading");
        loading.setCanceledOnTouchOutside(false);
        loading.show();*/
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response Subject", response);
                try {
                    //loading.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("subject");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            topic_modelList = new ArrayList<>();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            subject = jsonObject1.getString("sub_name");
                            subject_id = jsonObject1.getString("subject_id");
                            Log.d("subject Name", subject);
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("topic");
                            if (jsonArray1.length() > 0) {
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray1.get(j);
                                    topic_id = jsonObject2.getString("topic_id");
                                    topic_type = jsonObject2.getString("topic_type").trim();
                                    topic_subject_id = jsonObject2.getString("topic_subject_id");
                                    topic_course_id = jsonObject2.getString("topic_course_id");
                                    topic_video = jsonObject2.getString("topic_video");
                                    topic_name = jsonObject2.getString("topic_name");
                                    Log.d("Topic Name", topic_name);
                                    topic_model = new Topic_Model(course_id,course_name,course_banner,course_duration,course_lectures,course_fee,course_description,topic_id, topic_name, topic_type, topic_video);
                                    //topic_model = new Topic_Model(topic_id, topic_name, topic_type, topic_video);
                                    topic_modelList.add(topic_model);
                                }
                            }
                            subject_model = new Subject_Model(subject, topic_modelList);
                            subjectList.add(subject_model);
                            Subject_Adapter subject_adapter = new Subject_Adapter(getContext(), subjectList);
                            subject_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(subject_adapter);
                        }
                    }
                } catch (
                        JSONException error) {
                    Log.e("Error Subject json", error.getMessage());
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
                param.put("action", "showSubject");
                //param.put("course_id", "8je8pqadrec28g1eugjpqqra914kwx");
                param.put("course_id", course_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void submitReviewMethod() {
        final String message, countRating;
        message = reviewMessage.getText().toString().trim();
        countRating = String.valueOf(reviewRating.getRating());
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("1")) {
                        blankField();
                        reviewSubmitLayout.setVisibility(View.GONE);
                        txtReviewMessage.setVisibility(View.VISIBLE);
                        txtReviewMessage.setText(" Your Review successfully submitted.");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtReviewMessage.setVisibility(View.GONE);
                            }
                        }, 3000);
                        checkReview();
                        Log.e("Message", "Successfully Submitted");
                    } else if (status.equals("0")) {
                        blankField();
                        txtReviewMessage.setVisibility(View.VISIBLE);
                        txtReviewMessage.setText("Review Not Submitted");
                        txtReviewMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtMessage.setVisibility(View.GONE);
                            }
                        }, 3000);
                        Log.e("Message", "Not Submitted");
                    } else if (status.equals("3")) {
                        reviewSubmitLayout.setVisibility(View.GONE);
                        reviewSubmitLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_left_to_right));
                        personalReviewLayout.setVisibility(View.VISIBLE);
                        personalReviewLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_left_to_right));
                        txtReviewMessage.setVisibility(View.VISIBLE);
                        txtReviewMessage.setText(" Your Review successfully Updated.");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtReviewMessage.setVisibility(View.GONE);
                            }
                        }, 3000);
                        checkReview();
                    }
                } catch (JSONException e) {
                    Log.e("Sign Up JSON Error", e.getMessage());
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
                else {
                    message = "Null Value not required";
                }
                Toast.makeText(getContext(), "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "submitReview");
                param.put("rating", countRating);
                param.put("message", message);
                param.put("user_name", new SessionManager(getContext()).getUsername());
                param.put("course_id", getArguments().getString("course_id"));
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void blankField() {
        reviewMessage.setText("");
    }
    @Override
    public void onResume() {

        super.onResume();

        /*getView().setFocusableInTouchMode(true);
        getView().requestFocus();*/
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    Fragment myFragment = new All_Courses();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.anim.enter_left_to_right,
                                    R.anim.exit_left_to_right,
                                    R.anim.enter_right_to_left,
                                    R.anim.exit_right_to_left)
                            .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

                    return true;

                }

                return false;
            }
        });
    }


}
