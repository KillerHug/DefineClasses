package com.defineclasses.app;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.defineclasses.app.Adapter.Course_Adapter;
import com.defineclasses.app.Adapter.Mock_Adapter;
import com.defineclasses.app.Adapter.Review_Adapter;
import com.defineclasses.app.Model.Course_Model;
import com.defineclasses.app.Model.Mock_Model;
import com.defineclasses.app.Model.Review_Model;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home_Fragment extends Fragment {
    DrawerLayout drawerLayout;
    ImageButton drawerOpen;
    Button allCourse;
    RecyclerView recyclerView,recyclerView_Mock;
    Course_Model course_model;
    List<Course_Model> model_list;
    Course_Adapter course_adapter;
    NavigationView navigationView;
    SessionManager sessionManager;
    Mock_Model mock_model;
    List<Mock_Model> mock_List;
    Mock_Adapter mock_adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        drawerLayout = getActivity().findViewById(R.id.drawerLayout);
        drawerOpen = view.findViewById(R.id.drawerOpen);
        allCourse = view.findViewById(R.id.all_course);
        recyclerView_Mock=view.findViewById(R.id.mockRecyclerView);
        navigationView = getActivity().findViewById(R.id.navigationView);
        allCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(1).setChecked(true);
                Fragment myFragment = new All_Courses();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
        drawerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        recyclerView = view.findViewById(R.id.courseRecyclerView);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setAdapter(course_adapter);
        model_list = new ArrayList<>();
        mock_List = new ArrayList<>();
        recyclerView_Mock = view.findViewById(R.id.mockRecyclerView);
        recyclerView_Mock.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //recyclerView_Mock.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView_Mock.setAdapter(mock_adapter);
        showCourse();
        showMockTest();
        return view;
    }

    private void showMockTest() {
        String url = "http://defineclasses.com/app/main_file.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Mock Response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mock_test");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String moc_test_id = jsonObject1.getString("moc_test_id");
                            String moc_test_name = jsonObject1.getString("moc_test_name");
                            String create_date = jsonObject1.getString("create_date");
                            String moc_banner = jsonObject1.getString("moc_banner");
                            Log.e("Banner",moc_banner);
                            String status = jsonObject1.getString("status");
                            mock_model=new Mock_Model(moc_test_id,moc_test_name,create_date,moc_banner,status);
                            mock_List.add(mock_model);
                            Mock_Adapter mock_adapter = new Mock_Adapter(getContext(), mock_List);
                            mock_adapter.notifyDataSetChanged();
                            recyclerView_Mock.setAdapter(mock_adapter);
                        }
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
                Map<String, String> param = new HashMap<>();
                param.put("action", "mock_test");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showCourse() {
        int page = 1;
        String url = "http://defineclasses.com/app/course_file.php?page=" + page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
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
                            Course_Adapter subject_adapter = new Course_Adapter(getContext(), model_list);
                            subject_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(subject_adapter);
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
                param.put("page", "1");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
