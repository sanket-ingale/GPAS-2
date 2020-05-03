package com.app.gpas2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SecurityPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentAdminHome.OnFragmentInteractionListener,
        FragmentSecurityHome.OnFragmentInteractionListener,
        FragmentSecurityScanQR.OnFragmentInteractionListener,
        FragmentHelp.OnFragmentInteractionListener,
        ChangePasswordDialog.ChangePasswordDialogListener,
        VisitCardDialogSecurity.VisitorCardDialogListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView toolbarTitle;
    private FirebaseUser user;
    private SharedPreferences sharedPreferences;
    private String server_url_insert=IPString.UrlPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_panel);

        Toolbar toolbar = findViewById(R.id.security_toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        drawer = findViewById(R.id.security_drawer_layout);

        toolbarTitle = findViewById(R.id.security_toolbar_title);

        navigationView = findViewById(R.id.security_drawer_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getMenu().findItem(R.id.drawer_security_home).setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_security_layout, new FragmentSecurityHome()).commit();

        toolbarTitle.setText(navigationView.getMenu().findItem(R.id.drawer_security_home).getTitle());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        Fragment selectedFragment = new FragmentSecurityHome();

        switch (item.getItemId()) {
            case R.id.drawer_security_home:
                selectedFragment = new FragmentSecurityHome();
                break;

            case R.id.drawer_security_scan_qr:
                selectedFragment = new FragmentSecurityScanQR();
                break;

            case R.id.drawer_security_help:
                selectedFragment = new FragmentHelp();
                break;

        }

        item.setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_security_layout, selectedFragment).commit();
        drawer.closeDrawer(GravityCompat.START);


        toolbarTitle.setText(item.getTitle());


        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_logout, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()== R.id.admin_logout_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Do you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            FirebaseAuth.getInstance().signOut();

                            sharedPreferences.edit().putBoolean("logged",false).apply();
                            sharedPreferences.edit().putString("user","").apply();
                            sharedPreferences.edit().putString("name","").apply();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SecurityPanel.this, "Logged out", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId()== R.id.admin_change_pass_btn) {
            ChangePasswordDialog changePasswordDialog =  new ChangePasswordDialog();
            changePasswordDialog.show(getSupportFragmentManager(), "Change Password Dialog");
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void applyTexts(String currentPassword, final String newPassword) {
        try {
            submitData(currentPassword,newPassword);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void submitData(String s1,String s2) throws UnsupportedEncodingException {
        String sOld= URLEncoder.encode(s1,"UTF8");
        String sNew= URLEncoder.encode(s2,"UTF8");
        String sName= URLEncoder.encode(sharedPreferences.getString("name",""),"UTF8");

        String url=server_url_insert+ "?old="+sOld+"&new="+sNew+"&name="+sName+"";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("message").equals("success")) {
                        Toast.makeText(getApplicationContext(),"Password changed successfully!!" , Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Incorrect old password!!" , Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(SecurityPanel.this);
        requestQueue.add(stringRequest);
    }


//    public void applyTexts(String currentPassword, final String newPassword) {
//
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        final String email = user.getEmail();
//        AuthCredential credential = EmailAuthProvider.getCredential(email,currentPassword);
//
//        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(!task.isSuccessful()){
//                                Toast.makeText(SecurityPanel.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
//
//                            }else {
//                                Toast.makeText(SecurityPanel.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
//                }else {
//                    Toast.makeText(SecurityPanel.this, "Current password incorrect", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }

    @Override
    public void applyTexts() {

    }
}
