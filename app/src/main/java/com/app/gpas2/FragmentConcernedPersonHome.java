package com.app.gpas2;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
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

import static android.content.Context.MODE_PRIVATE;


public class FragmentConcernedPersonHome extends Fragment implements VisitorAdaptor.OnVisitorListener{
    private static final String URL_VISITORS = IPString.ip;
    List<VisitorInfo> visitorInfoList;
    RecyclerView recyclerView;
    String name;

    TextView emptyView;

    public FragmentConcernedPersonHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=inflater.inflate(R.layout.fragment_concerned_person_home, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewConcern);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        visitorInfoList = new ArrayList<>();
        emptyView=v.findViewById(R.id.list_empty);

//        Intent i=getActivity().getIntent();
//        name=i.getExtras().getString("name");
        SharedPreferences sp=getContext().getSharedPreferences("login",MODE_PRIVATE);
        name=sp.getString("name","");
        loadVisitors();
        return v;
    }
    private void loadVisitors() {

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
                                String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());

                                //adding the product to product list
                                if(name.equals(visitor.getString("conernP"))&& visitor.getString("vD").equals(date))
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
                            //creating adapter object and setting it to recyclerview
                            VisitorAdaptor adapter = new VisitorAdaptor(visitorInfoList, FragmentConcernedPersonHome.this);
                            if(adapter.getItemCount() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else {
                                emptyView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }                        } catch (JSONException e) {
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
        VisitCardDialogEmployee visitorCardDialog = new VisitCardDialogEmployee();
        visitorCardDialog.getObject(visitorInfo);
        visitorCardDialog.show(getFragmentManager(),"Visitor info dialog");

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
