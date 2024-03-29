package com.app.gpas2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FragmentConcernedPersonForm extends Fragment {
    private static final String URL_VISITORS = IPString.ip;
    String server_url_insert = IPString.UrlAddVisit;
    private ProgressDialog dialog;

    private Spinner spinner1;
    List<String> personList;
    EditText visitDate, visitTime, intimDate;
    DatePickerDialog picker;
    Button submit, resetForm;
    EditText name, email, address, contact, vehicle, org, purpose;
//    final Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());

    public FragmentConcernedPersonForm() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_concerned_person_form, container, false);
        spinner1 = v.findViewById(R.id.spinner1);
        loadPersons();

        resetForm = v.findViewById(R.id.resetForm);

        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        address = v.findViewById(R.id.address);
        contact = v.findViewById(R.id.contact);
        vehicle = v.findViewById(R.id.vehicle);
        org = v.findViewById(R.id.org);
        purpose = v.findViewById(R.id.purpose);

        submit = v.findViewById(R.id.addVisit);
        dialog = new ProgressDialog(getContext());
        visitDate = v.findViewById(R.id.visitdate);
        visitDate.setInputType(InputType.TYPE_NULL);
        visitDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";
                                String day = "";
                                if (monthOfYear < 10) {
                                    month = "0" + (monthOfYear + 1);
                                } else {
                                    month = monthOfYear + "";
                                }
                                if (dayOfMonth < 10) {
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = dayOfMonth + "";
                                }
                                visitDate.setText(year + "-" + month + "-" + day);
                            }
                        }, year, month, day);
                picker.show();
                return true;
            }
        });

//        final DatePickerDialog.OnDateSetListener date = new
//                DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                          int dayOfMonth) {
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        updateLabel();
//                    }
//                };
//        visitDate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    new DatePickerDialog(getContext(), date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                }
//                return true;
//            }
//        });

        intimDate = v.findViewById(R.id.intimadte);
        intimDate.setInputType(InputType.TYPE_NULL);
        intimDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";
                                String day = "";
                                if (monthOfYear < 10) {

                                    month = "0" + (monthOfYear + 1);
                                } else {
                                    month = monthOfYear + "";
                                }
                                if (dayOfMonth < 10) {
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = dayOfMonth + "";
                                }
                                intimDate.setText(year + "-" + month + "-" + day);
                            }
                        }, year, month, day);
                picker.show();
                return true;
            }
        });

        visitTime = v.findViewById(R.id.visitTime);
        visitTime.setInputType(InputType.TYPE_NULL);
        visitTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                return true;
            }
        });


        resetForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sName = name.getText().toString();
                    String sEmail = email.getText().toString();
                    String sAddress = address.getText().toString();
                    String sContact = contact.getText().toString();
                    String sVehicle = vehicle.getText().toString();
                    String sOrg = org.getText().toString();
                    String sConcernPerson = spinner1.getSelectedItem().toString();
                    String sIntim = intimDate.getText().toString();
                    String sVisitDate = intimDate.getText().toString();
                    String sVisitTime = intimDate.getText().toString();
                    String sPurpose = intimDate.getText().toString();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    String vehiclePattern = "[a-zA-Z][a-zA-Z][0-9][0-9][a-zA-Z][a-zA-Z][0-9][0-9][0-9][0-9]";

                    if (checkInternetConnection()) {

                        if (TextUtils.isEmpty(sName)) {
                            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
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
                                    if (TextUtils.isEmpty(sAddress)) {
                                        Toast.makeText(getContext(), "Enter Address", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {

                                        if (TextUtils.isEmpty(sContact)) {
                                            Toast.makeText(getContext(), "Enter Contact", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            if (sContact.length() != 10) {
                                                Toast.makeText(getContext(), "Please provide a valid Contact", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                if (TextUtils.isEmpty(sVehicle)) {
                                                    Toast.makeText(getContext(), "Enter Vehicle No.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                } else {

                                                    if (!sVehicle.matches(vehiclePattern)) {
                                                        Toast.makeText(getContext(), "Please provide a vehicle number in MH20EK2019 format", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    } else {
                                                        if (TextUtils.isEmpty(sOrg)) {
                                                            Toast.makeText(getContext(), "Enter Organization", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        } else {
                                                            if (TextUtils.isEmpty(sConcernPerson)) {
                                                                Toast.makeText(getContext(), "Choose Concerned Person", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            } else {
                                                                if (TextUtils.isEmpty(sIntim)) {
                                                                    Toast.makeText(getContext(), "Enter Intimation Date", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                } else {
                                                                    if (TextUtils.isEmpty(sVisitDate)) {
                                                                        Toast.makeText(getContext(), "Enter Visit Date", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    } else {
                                                                        if (TextUtils.isEmpty(sVisitTime)) {
                                                                            Toast.makeText(getContext(), "Enter Visit Time", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        } else {
                                                                            if (TextUtils.isEmpty(sPurpose)) {
                                                                                Toast.makeText(getContext(), "Enter Purpose", Toast.LENGTH_SHORT).show();
                                                                                return;
                                                                            } else {
                                                                                submitVisitData();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
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

//    private void updateLabel() {
//
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        visitDate.setText(sdf.format(myCalendar.getTime()));
//    }


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

    private void submitVisitData() throws UnsupportedEncodingException {
        dialog.setTitle("Adding Visitor Entry");
        dialog.setMessage("Please wait ...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String sName = URLEncoder.encode(name.getText().toString(), "UTF8");
        String sEmail = URLEncoder.encode(email.getText().toString(), "UTF8");
        String sAddress = URLEncoder.encode(address.getText().toString(), "UTF8");
        String sContact = URLEncoder.encode(contact.getText().toString(), "UTF8");
        String sVehicle = URLEncoder.encode(vehicle.getText().toString(), "UTF8");
        String sOrg = URLEncoder.encode(org.getText().toString(), "UTF8");
        String sConcernPerson = URLEncoder.encode(spinner1.getSelectedItem().toString(), "UTF8");
        String sIntim = URLEncoder.encode(intimDate.getText().toString(), "UTF8");
        String sVisitDate = URLEncoder.encode(visitDate.getText().toString(), "UTF8");
        String sVisitTime = URLEncoder.encode(visitTime.getText().toString(), "UTF8");
        String sPurpose = URLEncoder.encode(purpose.getText().toString(), "UTF8");

        String url = server_url_insert + "?name=" + sName + "&email=" + sEmail + "&address=" + sAddress + "&contact=" + sContact
                + "&vehicle=" + sVehicle + "&org=" + sOrg + "&concernp=" + sConcernPerson + "&intimdate=" + sIntim
                + "&visitdate=" + sVisitDate + "&visittime=" + sVisitTime + "&purpose=" + sPurpose + "";
        Log.e("url : ", "" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Request Sent successfully!!", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "e" + e.toString(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "err" + error.toString(), Toast.LENGTH_LONG).show();
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
        personList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IPString.loadPersonString,
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
