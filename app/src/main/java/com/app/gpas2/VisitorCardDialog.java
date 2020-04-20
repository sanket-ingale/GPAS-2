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

public class VisitorCardDialog extends AppCompatDialogFragment {

    private VisitorInfo visitorInfo;
    private VisitorCardDialogListener listener;

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
        if(visitorInfo.getStatus().equals("Approved")){
            accept.setEnabled(false);
            reschedule.setEnabled(false);

        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "accept" +visitorInfo.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        reschedule.setOnClickListener(new View.OnClickListener() {
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


