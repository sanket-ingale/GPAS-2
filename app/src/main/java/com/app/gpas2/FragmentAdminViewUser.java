package com.app.gpas2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentAdminViewUser extends Fragment implements UserAdapter.OnUserListener{

    private OnFragmentInteractionListener mListener;

    private static final String URL_VISITORS = IPString.UrlUsers;
    private String server_url_insert=IPString.UrlButtonScripts;
    private ProgressDialog mdialog;
    List<UserDetails> userInfoList;
    RecyclerView recyclerView;
    Button deleteUser;
    TextView emptyView;

    public FragmentAdminViewUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_view_user, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mdialog = new ProgressDialog(getContext());

        emptyView=v.findViewById(R.id.list_empty);
        userInfoList = new ArrayList<>();
        loadUsers();

//        deleteUser = v.findViewById(R.id.userDelete);
//
//        deleteUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return v;
    }

    private void loadUsers() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_VISITORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject users = array.getJSONObject(i);

                                //adding the product to product list
                                userInfoList.add(new UserDetails(
                                        users.getInt("id"),
                                        users.getString("name"),
                                        users.getString("department"),
                                        users.getString("designation"),
                                        users.getString("email"),
                                        users.getString("password")
                                ));

                            }
                            //creating adapter object and setting it to recyclerview
                            UserAdapter adapter = new UserAdapter(userInfoList, FragmentAdminViewUser.this);
                            if(adapter.getItemCount() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else {
                                emptyView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onUserClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        final UserDetails userDetails = userInfoList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Are you sure you want to delete user - "+userDetails.getName()+"?")
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mdialog.setTitle("Deleting User");
                        mdialog.setMessage("Please wait ...");
                        mdialog.setCanceledOnTouchOutside(false);
                        mdialog.show();

                        String url=server_url_insert+ "?type=deleteUser"+"&id="+userDetails.getId()+"";
                        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if(jsonObject.getString("message").equals("success")) {
                                        mdialog.dismiss();
                                        Toast.makeText(getActivity(),"User deleted" , Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        mdialog.dismiss();
                                        Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mdialog.dismiss();
                                    Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mdialog.dismiss();
                                Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();                            }
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
                        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);


//                        Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
