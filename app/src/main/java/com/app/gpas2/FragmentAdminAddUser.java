package com.app.gpas2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class FragmentAdminAddUser extends Fragment {
EditText nameET,departmentET,emailET,passwordET;
Spinner designationSP;
Button addUser;
String server_url_insert=IPString.UrlInsert;
    public FragmentAdminAddUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_add_user, container, false);
        nameET=v.findViewById(R.id.name);
        departmentET=v.findViewById(R.id.department);
        emailET=v.findViewById(R.id.email);
        passwordET=v.findViewById(R.id.password);
        designationSP=v.findViewById(R.id.designation);
        addUser=v.findViewById(R.id.AddUser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }
    private void submitData() throws UnsupportedEncodingException {
        String sName= URLEncoder.encode(nameET.getText().toString(),"UTF8");
        String sDepartment= URLEncoder.encode(departmentET.getText().toString(),"UTF8");
        String sDesignation= URLEncoder.encode(String.valueOf(designationSP.getSelectedItem()),"UTF8");
        String sEmail= URLEncoder.encode(emailET.getText().toString(),"UTF8");
        String sPassword= URLEncoder.encode(passwordET.getText().toString(),"UTF8");

        String url=server_url_insert+ "?name="+sName+"&department="+sDepartment+"&designation="+sDesignation+"&email="+sEmail+"&password="+sPassword+"";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("message").equals("success")) {
                        Toast.makeText(getActivity(),"User added successfully!!" , Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"e"+e.toString(),Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"err"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        nameET.setText("");
        emailET.setText("");
        passwordET.setText("");
        departmentET.setText("");
        designationSP.setSelection(0);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
