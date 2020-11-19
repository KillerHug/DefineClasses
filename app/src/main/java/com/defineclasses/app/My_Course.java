package com.defineclasses.app;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.defineclasses.app.Adapter.All_Course_Adapter;
import com.defineclasses.app.Adapter.My_Course_Adapter;
import com.defineclasses.app.Model.Course_Model;
import com.defineclasses.app.Model.MyCourse_Model;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Course extends Fragment {

    MyCourse_Model myCourse_model;
    List<MyCourse_Model> myCourse_modelList;
    My_Course_Adapter my_course_adapter;
    RecyclerView mycourseRecycler;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course, null);
        myCourse_modelList=new ArrayList<>();
        mycourseRecycler=view.findViewById(R.id.my_course_RecyclerView);
        mycourseRecycler.setAdapter(my_course_adapter);
        mycourseRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        showMyCourse();
    return view;
    }

    private void showMyCourse() {
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response for all Course", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("course");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String course_id = jsonObject1.getString("course_id");
                            String course = jsonObject1.getString("course");
                            String image = jsonObject1.getString("course_banner");
                            String banner = "http://defineclasses.com/" + image;
                            String duration = jsonObject1.getString("course_duration");
                            String lectures = jsonObject1.getString("no_of_lectures");
                            String course_fee = jsonObject1.getString("course_fee");
                            String description = jsonObject1.getString("description");
                            myCourse_model = new MyCourse_Model(course_id, course, banner, duration, lectures, course_fee, description);
                            myCourse_modelList.add(myCourse_model);
                            My_Course_Adapter course_adapter = new My_Course_Adapter(getContext(), myCourse_modelList);
                            course_adapter.notifyDataSetChanged();
                            mycourseRecycler.setAdapter(course_adapter);
                        }
                    }
                } catch (JSONException error) {
                    Log.e("Error Course json", error.getMessage());
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
                param.put("action", "mycourse");
                param.put("user_name", new SessionManager(getContext()).getUsername());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
