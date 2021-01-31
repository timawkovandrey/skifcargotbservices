package com.timawkovandrey.skifcargotbfunctions;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

public class SkifTbAsync extends  AsyncTask<String,String,String>{

    private SkifTbCallback skifTbCallback;
    HttpURLConnection conn;
    JSONObject postDataParams;

    public SkifTbAsync(HttpURLConnection conn, SkifTbCallback skifTbCallback, JSONObject postDataParams) {
        this.skifTbCallback = skifTbCallback;
        this.conn = conn;
        this.postDataParams = postDataParams;
    }

    @Override
    protected String doInBackground(String... voids) {
        try {

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode(); // To Check for 200
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                return sb.toString();
            }
            return  "[{\"error\": \"" + "Bad request: "+String.valueOf(responseCode) + "\"}]";
        }
        catch(Exception e){
            return  "[{\"error\": \"" + e.getMessage() + "\"}]";
        }
    }

    protected void onPostExecute(String s) {
        if(s!=null){
            try {
                skifTbCallback.skifTbCallback(new JSONArray(s));
            } catch (JSONException e) {
                try {
                    skifTbCallback.skifTbCallback(new JSONArray("[{\"error\": \"" + e.getMessage() + "\"}]"));
                } catch (JSONException e1) {
                    skifTbCallback.skifTbCallback(null);
                }
            }
        } else {
            skifTbCallback.skifTbCallback(null);
        }
    }
}
