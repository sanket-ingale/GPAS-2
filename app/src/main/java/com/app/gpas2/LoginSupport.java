package com.app.gpas2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;


public class LoginSupport extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    SharedPreferences sp;

    LoginSupport(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = IPString.loginString;
        if (type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        sp = context.getSharedPreferences("login", MODE_PRIVATE);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        String[] temp = result.split("#");
        Log.e("result", result);
        if (temp[0].equals("Authority")) {
            Intent i = new Intent(context, AdminHome.class);
            context.startActivity(i);
            sp.edit().putBoolean("logged", true).apply();
            sp.edit().putString("user", "Authority").apply();
            sp.edit().putString("name", temp[1]).apply();
        } else if (temp[0].equals("Security")) {
            Intent i = new Intent(context, SecurityPanel.class);
            context.startActivity(i);
            sp.edit().putBoolean("logged", true).apply();
            sp.edit().putString("user", "Security").apply();
            sp.edit().putString("name", temp[1]).apply();

        } else if (temp[0].equals("Employee")) {
            Intent i = new Intent(context, ConcernedPerson.class);
//            i.putExtra("name",temp[1]);
            context.startActivity(i);
            sp.edit().putBoolean("logged", true).apply();
            sp.edit().putString("user", "Employee").apply();
            sp.edit().putString("name", temp[1]).apply();
        } else {
            alertDialog.setMessage("Invalid User");
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
