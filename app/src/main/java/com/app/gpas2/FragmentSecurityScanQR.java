package com.app.gpas2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentSecurityScanQR extends Fragment {
SurfaceView surfaceView;
CameraSource cameraSource;
TextView textView;
BarcodeDetector barcodeDetector;
String QrData;

private static final String URL_VISITORS = IPString.ip;
List<VisitorInfo> visitorInfoList;


    public FragmentSecurityScanQR() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_security_scan_qr, container, false);
        surfaceView=v.findViewById(R.id.cameraView);
        textView=v.findViewById(R.id.TextViewQr);

        barcodeDetector=new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource=new CameraSource.Builder(getContext(),barcodeDetector)
                .setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes=detections.getDetectedItems();

                if(qrcodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            int a=0;
                            Vibrator vibrator=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            QrData=qrcodes.valueAt(0).displayValue;
                            String[] temp=QrData.split("=");

                            a =Integer.parseInt(temp[1]);

//                            textView.setText(qrcodes.valueAt(0).displayValue+" " + a);
                            if(a != 0){
                                loadVisitors(a);

                            }

                        }
                    });
                    cameraSource.stop();

                }

            }
        });

        return v;
    }
    private void loadVisitors(final int abc) {
        Log.e("jaaadu", "loadVisitors: "+abc );
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
                                int temp=visitor.getInt("id");
                                    if (temp==abc) {
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
                            VisitorInfo visitorInfo=visitorInfoList.get(0);
                            VisitCardDialogSecurity visitorCardDialog = new VisitCardDialogSecurity();
                            visitorCardDialog.getObject(visitorInfo);
                            visitorCardDialog.show(getFragmentManager(),"Visitor info dialog");

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
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
