package com.app.gpas2;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class VisitorCardDialog extends AppCompatDialogFragment {

    private VisitorInfo visitorInfo;
    private VisitorCardDialogListener listener;
    private String server_url_insert=IPString.UrlButtonScripts;
    private ProgressDialog dialog;


    public void getObject(VisitorInfo v){
        this.visitorInfo=v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        listener.applyTexts();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.visitor_card_dialog, null);
        TextView tv=view.findViewById(R.id.cardinfo);
        Button accept= view.findViewById(R.id.accept);
        Button reschedule= view.findViewById(R.id.reschedule);
        dialog = new ProgressDialog(getContext());

        if(!visitorInfo.getStatus().equals("Pending")){
            accept.setEnabled(false);
            reschedule.setEnabled(false);

        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Accepting Request");
                dialog.setMessage("Please wait ...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                SharedPreferences sp=getContext().getSharedPreferences("login",MODE_PRIVATE);
                String name=sp.getString("name","");
                String sName="";
                try {
                    sName= URLEncoder.encode(name,"UTF8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String url=server_url_insert+ "?type=accept"+"&n="+visitorInfo.getId()+"&aname="+sName+"";
                Log.e("sd", "onClick: "+url );
                StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("message").equals("success")) {
                                dialog.dismiss();

                                Toast.makeText(getActivity(),"Request accepted successfully!!" , Toast.LENGTH_LONG).show();
                            }
                            else{
                                dialog.dismiss();
                                Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Something went wrong!!" , Toast.LENGTH_LONG).show();
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
                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

//                Toast.makeText(getContext(), "accept" +visitorInfo.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Rejecting Request");
                dialog.setMessage("Please wait ...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                SharedPreferences sp=getContext().getSharedPreferences("login",MODE_PRIVATE);
                String name=sp.getString("name","");
                String sName="";
                try {
                    sName= URLEncoder.encode(name,"UTF8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url=server_url_insert+ "?type=reject"+"&n="+visitorInfo.getId()+"&aname="+sName+"";
                StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("message").equals("success")) {
                                dialog.dismiss();

                                Toast.makeText(getActivity(),"Request rejected successfully!!" , Toast.LENGTH_LONG).show();
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


//                Toast.makeText(getContext(), "reschedule" +visitorInfo.getName(), Toast.LENGTH_SHORT).show();

            }
        });
        tv.setText("Name - "+visitorInfo.getName()
                +"\nEmail Id - "+visitorInfo.getEmail()
                +"\nMobile Number - "+visitorInfo.getContact()
                +"\nVisit Date - "+visitorInfo.getVDate()
                +"\nVisit Time - "+visitorInfo.getVTime()
                +"\nConcerned Person - "+visitorInfo.getConcernPerson()
                +"\nVehicle Number - "+visitorInfo.getVehicle()
                +"\nOrganization Name - "+visitorInfo.getOrg()
                +"\nPurpose of Meeting - "+visitorInfo.getPurpose()
                +"\nStatus - "+visitorInfo.getStatus()
                +"\nApproving Authority - "+visitorInfo.getApprovingAuth()
                +"\nCampus Entry - "+visitorInfo.getStartMeet()
                +"\nCampus Exit - "+visitorInfo.getCloseMeet());
        builder.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (VisitorCardDialog.VisitorCardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement VisitorCardDialogListener");
        }
    }



    public interface VisitorCardDialogListener {
        void applyTexts();
    }

}


