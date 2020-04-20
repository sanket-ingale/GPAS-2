package com.app.gpas2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdminStatus extends Fragment implements VisitorAdaptor.OnVisitorListener {
    private static final String URL_VISITORS = IPString.ip;
    List<VisitorInfo> visitorInfoList;
    RecyclerView recyclerView;
    private Spinner spinner1;
    public FragmentAdminStatus() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_status, container, false);
        recyclerView = v.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        spinner1 = v.findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadVisitors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;    }

    private void loadVisitors() {
        visitorInfoList = new ArrayList<>();

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
                                JSONObject visitor = array.getJSONObject(i);

                                String status = String.valueOf(spinner1.getSelectedItem());
                                if (visitor.getString("status").equals(status)) {
                                    //adding the product to product list
                                    visitorInfoList.add(new VisitorInfo(
                                            visitor.getInt("id"),
                                            visitor.getString("name"),
                                            visitor.getString("address"),
                                            visitor.getString("email"),
                                            visitor.getString("contact"),
                                            visitor.getString("Vehicle"),
                                            visitor.getString("org"),
                                            visitor.getString("intimatedate"),
                                            visitor.getString("vD"),
                                            visitor.getString("vT"),
                                            visitor.getString("conernP"),
                                            visitor.getString("purpose"),
                                            visitor.getString("status"),
                                            visitor.getString("approvingauth"),
                                            visitor.getString("startmeet"),
                                            visitor.getString("closemeet")
                                    ));
                                }
                            }
                            //creating adapter object and setting it to recyclerview
                            VisitorAdaptor adapter = new VisitorAdaptor(visitorInfoList, FragmentAdminStatus.this);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onVisitorClick(int position) {
        VisitorInfo visitorInfo= visitorInfoList.get(position);
        VisitorCardDialog visitorCardDialog = new VisitorCardDialog();
        visitorCardDialog.getObject(visitorInfo);
        visitorCardDialog.show(getFragmentManager(),"Visitor info dialog");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
