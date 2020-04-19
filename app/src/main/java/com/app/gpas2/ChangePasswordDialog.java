package com.app.gpas2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordDialog extends AppCompatDialogFragment {
    private EditText editCurrentPassword;
    private EditText editNewPassword;
    private EditText editReenterPassword;
    private ChangePasswordDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.change_password_dialogue, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String currentPassword = editCurrentPassword.getText().toString();
                        String newPassword = editNewPassword.getText().toString();
                        String reenterPassword = editReenterPassword.getText().toString();
                        if (currentPassword.isEmpty()) {
                            Toast.makeText(view.getContext(), "Enter current password", Toast.LENGTH_SHORT).show();
                        } else if (newPassword.isEmpty()) {
                            Toast.makeText(view.getContext(), "Enter new password", Toast.LENGTH_SHORT).show();
                        } else if (newPassword.length() < 8) {
                            Toast.makeText(view.getContext(), "New password should contain at least 8 characters", Toast.LENGTH_SHORT).show();
                        } else if (reenterPassword.isEmpty()) {
                            Toast.makeText(view.getContext(), "Re-enter new password", Toast.LENGTH_SHORT).show();
                        } else if (!newPassword.matches(reenterPassword)) {
                            Toast.makeText(view.getContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.applyTexts(currentPassword, newPassword);
                        }
                    }
                });

        editCurrentPassword = view.findViewById(R.id.current_password);
        editNewPassword = view.findViewById(R.id.new_password);
        editReenterPassword = view.findViewById(R.id.re_new_password);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ChangePasswordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ChangePasswordDialogListener");
        }
    }

    public interface ChangePasswordDialogListener {
        void applyTexts(String currentPassword, String newPassword);
    }

}
