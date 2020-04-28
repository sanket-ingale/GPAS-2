package com.app.gpas2;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentConcernedPersonForm extends Fragment {
    private static final String URL_VISITORS = IPString.ip;
    String server_url_insert=IPString.UrlAddVisit;
    private ProgressDialog dialog;

    private Spinner spinner1;
    List<String> personList;
    EditText visitDate,visitTime,intimDate;
    DatePickerDialog picker;
    Button submit;
    EditText name,email,address,contact,vehicle,org,purpose;

    public FragmentConcernedPersonForm() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_concerned_person_form, container, false);
        spinner1 = v.findViewById(R.id.spinner1);
        loadPersons();
        name=v.findViewById(R.id.name);
        email=v.findViewById(R.id.email);
        address=v.findViewById(R.id.address);
        contact=v.findViewById(R.id.contact);
        vehicle=v.findViewById(R.id.vehicle);
        org=v.findViewById(R.id.org);
        name=v.findViewById(R.id.name);
        purpose=v.findViewById(R.id.purpose);
        submit=v.findViewById(R.id.addVisit);
        dialog = new ProgressDialog(getContext());
        visitDate=v.findViewById(R.id.visitdate);
        visitDate.setInputType(InputType.TYPE_NULL);
        visitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month="";
                                String day="";
                                if(monthOfYear < 10){
                                    month = "0" + (monthOfYear+1);
                                }
                                else {
                                    month=monthOfYear+"";
                                }
                                if(dayOfMonth < 10){
                                    day  = "0" + dayOfMonth ;
                                }
                                else{
                                    day=dayOfMonth+"";
                                }
                                visitDate.setText(year + "-" + month + "-" + day);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        intimDate=v.findViewById(R.id.intimadte);
        intimDate.setInputType(InputType.TYPE_NULL);
        intimDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month="";
                        String day="";
                        if(monthOfYear < 10){

                            month = "0" + (monthOfYear+1);
                        }
                        else {
                            month=monthOfYear+"";
                        }
                        if(dayOfMonth < 10){
                            day  = "0" + dayOfMonth ;
                        }
                        else{
                            day=dayOfMonth+"";
                        }
                        intimDate.setText(year + "-" + month + "-" + day);                    }
                }, year, month, day);
        picker.show();
    }
});
        visitTime=v.findViewById(R.id.visitTime);
        visitTime.setInputType(InputType.TYPE_NULL);
        visitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                visitTime.setText(String.format("%02d:%02d", hourOfDay, minute));
//                                visitTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitVisitData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

    private void submitVisitData()  throws UnsupportedEncodingException {
        dialog.setTitle("Adding Visitor Entry");
        dialog.setMessage("Please wait ...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String sName= URLEncoder.encode(name.getText().toString(),"UTF8");
        String sEmail= URLEncoder.encode(email.getText().toString(),"UTF8");
        String sAddress= URLEncoder.encode(address.getText().toString(),"UTF8");
        String sContact= URLEncoder.encode(contact.getText().toString(),"UTF8");
        String sVehicle= URLEncoder.encode(vehicle.getText().toString(),"UTF8");
        String sOrg= URLEncoder.encode(org.getText().toString(),"UTF8");
        String sConcernPerson= URLEncoder.encode(spinner1.getSelectedItem().toString(),"UTF8");
        String sIntim= URLEncoder.encode(intimDate.getText().toString(),"UTF8");
        String sVisitDate= URLEncoder.encode(intimDate.getText().toString(),"UTF8");
        String sVisitTime= URLEncoder.encode(intimDate.getText().toString(),"UTF8");
        String sPurpose= URLEncoder.encode(intimDate.getText().toString(),"UTF8");

        String url=server_url_insert+ "?name="+sName+"&email="+sEmail+"&address="+sAddress+"&contact="+sContact
                +"&vehicle="+sVehicle+"&org="+sOrg+"&concernp="+sConcernPerson+"&intimdate="+sIntim
                +"&visitdate="+sVisitDate+"&visittime="+sVisitTime+"&purpose="+sPurpose+"";
        Log.e("url : ", ""+url );
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("message").equals("success")) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Request Sent successfully!!" , Toast.LENGTH_LONG).show();
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getActivity(),"e"+e.toString(),Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"err"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        name.setText("");
        email.setText("");
        address.setText("");
        contact.setText("");
        vehicle.setText("");
        org.setText("");
        purpose.setText("");
        visitDate.setText("");
        visitTime.setText("");
        intimDate.setText("");
        name.setText("");
        name.requestFocus();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
