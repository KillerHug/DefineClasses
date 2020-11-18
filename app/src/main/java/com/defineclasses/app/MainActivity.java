package com.defineclasses.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SessionManager sessionManager;
    String user_name;
    Button closeDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headerView = navigationView.getHeaderView(0);
        closeDrawer = (Button) headerView.findViewById(R.id.drawerClose);
        sessionManager = new SessionManager(this);
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        boolean id = sessionManager.getLogin();
        Log.e("id", String.valueOf(id));

        if (id) {
            navigationView.getMenu().getItem(7).setVisible(false);
            navigationView.getMenu().getItem(8).setVisible(false);
            navigationView.getMenu().getItem(9).setVisible(true);
            navigationView.getMenu().getItem(2).setVisible(true);
        } else {
            navigationView.getMenu().getItem(9).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Home_Fragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new Home_Fragment()).addToBackStack(null).commit();
                break;
            case R.id.course:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new All_Courses()).addToBackStack(null).commit();
                break;
            case R.id.profile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new Profile_Fragment()).addToBackStack(null).commit();
                break;
            case R.id.latest_news:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new News_Fragment()).addToBackStack(null).commit();
                break;
            case R.id.contact_us:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new Contact_Us()).addToBackStack(null).commit();
                break;
            case R.id.register:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                Intent signIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signIntent);
                finish();
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                break;
            case R.id.login:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                break;
            case R.id.logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "logout");
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.setLogin(false);
                sessionManager.removeSession();
                Intent logoutIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(logoutIntent);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment_byID = fm.findFragmentById(R.id.fragment_container);
        /*if (count == 0) {
            //super.onBackPressed();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Leave application?");
            alertDialog.setMessage("Are you sure you want to leave the application?");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();

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
        else */if (fragment_byID instanceof Home_Fragment){
            Log.e("HOme Fragment","Yes");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Leave application?");
            alertDialog.setMessage("Are you sure you want to leave the application?");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();

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
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}