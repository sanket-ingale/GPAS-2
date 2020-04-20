package com.app.gpas2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VisitCardDialogSecurity  extends AppCompatDialogFragment {
    private VisitorInfo visitorInfo;
    private VisitCardDialogSecurity.VisitorCardDialogListener listener;

    public void getObject(VisitorInfo v){
        this.visitorInfo=v;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        listener.applyTexts();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.visit_card_security_dialog, null);
        TextView tv=view.findViewById(R.id.cardinfo);
        Button startMeet= view.findViewById(R.id.startMeet);
        Button endMeet= view.findViewById(R.id.endMeet);
        if(!visitorInfo.getStartMeet().equals(null)){
            startMeet.setEnabled(false);
        }
        if(!visitorInfo.getCloseMeet().equals(null)){
            endMeet.setEnabled(false);
        }
        startMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "accept" +visitorInfo.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        endMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "reschedule" +visitorInfo.getName(), Toast.LENGTH_SHORT).show();

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
            listener = (VisitCardDialogSecurity.VisitorCardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement VisitorCardDialogListener");
        }
    }

    public interface VisitorCardDialogListener {
        void applyTexts();
    }
}
