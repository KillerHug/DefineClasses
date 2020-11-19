package com.defineclasses.app;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import com.defineclasses.app.Model.Course_Model;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class All_Courses extends Fragment {
    RecyclerView recyclerView;
    Course_Model course_model;
    List<Course_Model> model_list;
    All_Course_Adapter course_adapter;
    ImageButton back;
    NavigationView navigationView;
    Boolean isScrolling = true;
    int currentItems, totalItems, scrollOutItems, previousTotal;
    LinearLayoutManager manager;
    ProgressBar progressBar;
    int page = 1;
    //Displaying Progressbar
    String url;
    TextView allCourse;
    int rating;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, null);
        allCourse=view.findViewById(R.id.topic_sub_name);
        recyclerView = view.findViewById(R.id.course_All_RecyclerView);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        /*RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(layoutManager);*/
        if (getArguments() != null) {
            if (getArguments().getString("page") == "page_initialize") {
                page = 1;
                Log.v("Page Initialize", "Page Pass");
            } else {
                Log.v("Page Initialize", "Page Fail");
            }
        }
        url = "http://defineclasses.com/app/course_file.php?page=" + page;
        recyclerView.setAdapter(course_adapter);

        model_list = new ArrayList<>();
        back = view.findViewById(R.id.backTo);
        navigationView = getActivity().findViewById(R.id.navigationView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(0).setChecked(true);
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
                            course_model = new Course_Model(course_id, course, banner, duration, lectures, course_fee, description);
                            model_list.add(course_model);
                            All_Course_Adapter course_adapter = new All_Course_Adapter(getContext(), model_list);
                            course_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(course_adapter);
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
                param.put("action", "course");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        pagination();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void pagination() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }*/
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstVisibleItemPosition();
                    if (isScrolling) {
                        if (totalItems > previousTotal) {
                            previousTotal = totalItems;
                            page++;
                            isScrolling = false;
                        }
                    }
                    if (!isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        progressBar.setVisibility(View.VISIBLE);
                        //page=page+1;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNext();
                                progressBar.setVisibility(View.GONE);
                            }
                        }, 3000);
                        isScrolling = true;
                        Log.v("All Courses", "Page Number: " + page);
                    }
                }
            }
        });
    }

    private void getNext() {

        url = "http://defineclasses.com/app/course_file.php?page=" + page;

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
                            course_model = new Course_Model(course_id, course, banner, duration, lectures, course_fee, description);
                            model_list.add(course_model);
                            All_Course_Adapter course_adapter = new All_Course_Adapter(getContext(), model_list);
                            course_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(course_adapter);
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
                param.put("action", "course");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
