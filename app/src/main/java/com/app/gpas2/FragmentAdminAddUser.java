package com.app.gpas2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class FragmentAdminAddUser extends Fragment {
    EditText nameET, departmentET, emailET, passwordET;
    Spinner designationSP;
    Button addUser, userReset;
    String server_url_insert = IPString.UrlInsert;
    private ProgressDialog dialog;

    public FragmentAdminAddUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_add_user, container, false);
        nameET = v.findViewById(R.id.name);
        departmentET = v.findViewById(R.id.department);
        emailET = v.findViewById(R.id.email);
        passwordET = v.findViewById(R.id.password);
        designationSP = v.findViewById(R.id.designation);
        dialog = new ProgressDialog(getContext());

        addUser = v.findViewById(R.id.AddUser);

        userReset = v.findViewById(R.id.resetUser);

        userReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameET.setText("");
                departmentET.setText("");
                emailET.setText("");
                passwordET.setText("");

            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sName = nameET.getText().toString();
                    String sDepartment = departmentET.getText().toString();
                    String sDesignation = String.valueOf(designationSP.getSelectedItem());
                    String sEmail = emailET.getText().toString();
                    String sPassword = passwordET.getText().toString();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (checkInternetConnection()) {

                        if (TextUtils.isEmpty(sName)) {
                            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            if (TextUtils.isEmpty(sDepartment)) {
                                Toast.makeText(getContext(), "Enter Department", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                if (TextUtils.isEmpty(sDesignation)) {
                                    Toast.makeText(getContext(), "Enter Designation", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    if (TextUtils.isEmpty(sEmail)) {
                                        Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {

                                        if (!sEmail.matches(emailPattern)) {
                                            Toast.makeText(getContext(), "Please provide a valid Email address", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {

                                            if (TextUtils.isEmpty(sPassword)) {
                                                Toast.makeText(getContext(), "Enter a Password", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                if (sPassword.length() < 8) {
                                                    Toast.makeText(getContext(), "Password should contain at least 8 characters", Toast.LENGTH_SHORT).show();
                                                    return;
                                                } else {
                                                    submitData();
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    } else {
                        Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }


    public boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    private void submitData() throws UnsupportedEncodingException {

        dialog.setTitle("Adding User");
        dialog.setMessage("Please wait ...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String sName = URLEncoder.encode(nameET.getText().toString(), "UTF8");
        String sDepartment = URLEncoder.encode(departmentET.getText().toString(), "UTF8");
        String sDesignation = URLEncoder.encode(String.valueOf(designationSP.getSelectedItem()), "UTF8");
        String sEmail = URLEncoder.encode(emailET.getText().toString(), "UTF8");
        String sPassword = URLEncoder.encode(passwordET.getText().toString(), "UTF8");

        String url = server_url_insert + "?name=" + sName + "&department=" + sDepartment + "&designation=" + sDesignation + "&email=" + sEmail + "&password=" + sPassword + "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        dialog.dismiss();
                        nameET.setText("");
                        emailET.setText("");
                        passwordET.setText("");
                        departmentET.setText("");
                        designationSP.setSelection(0);
                        Toast.makeText(getActivity(), "User added successfully!!", Toast.LENGTH_LONG).show();
                        nameET.requestFocus();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
