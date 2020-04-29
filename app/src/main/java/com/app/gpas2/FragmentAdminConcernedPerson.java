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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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


public class FragmentAdminConcernedPerson extends Fragment implements VisitorAdaptor.OnVisitorListener {
    private static final String URL_VISITORS = IPString.ip;
    List<VisitorInfo> visitorInfoList;
    List<String> personList;
    RecyclerView recyclerView;
    private Spinner spinner1;
    TextView emptyView;
    public FragmentAdminConcernedPerson() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_concerned_person, container, false);
        recyclerView = v.findViewById(R.id.recyclerView5);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        spinner1 = v.findViewById(R.id.spinner1);
        emptyView=v.findViewById(R.id.list_empty);
        loadPersons();
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadVisitors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

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

                                String conernP = String.valueOf(spinner1.getSelectedItem());
                                if (visitor.getString("conernP").equals(conernP)) {
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
                            VisitorAdaptor adapter = new VisitorAdaptor(visitorInfoList, FragmentAdminConcernedPerson.this);
                            if(adapter.getItemCount() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else {
                                emptyView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
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

    private void loadPersons() {
        personList= new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,IPString.loadPersonString ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject personJson = array.getJSONObject(i);
                                    personList.add(personJson.getString("name"));
                                }
                            populateSpinner();
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

        //adding our string request to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < personList.size(); i++) {
            lables.add(personList.get(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(spinnerAdapter);
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
