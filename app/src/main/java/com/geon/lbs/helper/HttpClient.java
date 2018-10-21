package com.geon.lbs.helper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Babu on 9/30/2016.
 */
public class HttpClient {

    static JSONObject jObj = null;
    static String json = "";
    public String Url = "";
    public String postParameters = "";

    public JSONObject downLoadUrl(){
        try{
            URL url = new URL(Url);
            //postParameters = "identity=ifad&password=Passw0rd";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParameters.getBytes().length);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postParameters);
            writer.flush();
            writer.close();
            os.close();

            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.e("statuscode",""+statusCode);
            }
            InputStream in = new BufferedInputStream(conn.getInputStream());

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                json = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            Log.e("downLoad data",json);

        }catch(Exception e){
            e.printStackTrace();
        }
        return jObj;
    }



    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.e("Excpn downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
