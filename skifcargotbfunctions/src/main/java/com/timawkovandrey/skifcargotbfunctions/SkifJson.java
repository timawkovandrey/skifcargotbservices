package com.timawkovandrey.skifcargotbfunctions;

import android.content.Context;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SkifJson {

    Context context;
    String usr;
    String pwd;
    SkifTbCallback skifTbCallback;

    public SkifJson(Context context, String usr, String pwd, SkifTbCallback skifTbCallback) {
        this.context = context;
        this.usr = usr;
        this.pwd = pwd;
        this.skifTbCallback = skifTbCallback;
    }

    public String runTbFunc(String... strings) {
        try {

            // POST Request
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("tbproc", "BCSUnloadListInfo");

            JSONArray args = new JSONArray();
            for (String arg : strings){
                JSONObject argJSON = new JSONObject();
                argJSON.put("value", arg);
                args.put(argJSON);
            }

            postDataParams.put("args", args);

            URL url = new URL("https://telega.skif-cargo.ru/UniversalSkifDriver/hs/v1/tbfunc");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            byte[] token = (usr + ":" + pwd).getBytes(StandardCharsets.UTF_8);
            String encoded = Base64.encodeToString(token, Base64.DEFAULT);
            conn.setRequestProperty("Authorization", "Basic "+encoded);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            new SkifTbAsync(conn,skifTbCallback,postDataParams).execute();
            return "";
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

}
