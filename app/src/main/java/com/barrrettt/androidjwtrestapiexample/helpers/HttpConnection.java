package com.barrrettt.androidjwtrestapiexample.helpers;

import com.barrrettt.androidjwtrestapiexample.data.user.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {

    //private final String strUrl = "http://localhost:8080";
    private final String strUrl = "http://10.0.2.2:8080"; //por el emulador

    public String autenticarUsuario(String username, String password){
        String jwt = null;
        HttpURLConnection urlConn = null;

        try {
            URL url = new URL(strUrl+"/authenticate");
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("POST");

            //Headed
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setDoInput(true); urlConn.setDoOutput(false);

            //Body
            JSONObject json = new JSONObject();
            json.put("username",username);
            json.put("password",password);
            String sJson = json.toString();

            OutputStream os = urlConn.getOutputStream();
            byte[] input = sJson.getBytes("utf-8");
            os.write(input, 0, input.length);

            //Respuesta
            int responseCode = urlConn.getResponseCode();

            InputStream inputStream;
            if (responseCode == HttpURLConnection.HTTP_OK){
                inputStream = urlConn.getInputStream();

            }else{
                inputStream = urlConn.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            jwt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            jwt = null;

        }finally {
            urlConn.disconnect();
        }

        return jwt;
    }

    public String getHello(User user , String param){
        String response = null;
        HttpURLConnection urlConn = null;

        try {

            URL url;
            if (param.equalsIgnoreCase("")){
                url = new URL(strUrl+"/saludo");
            }else{
                url = new URL(strUrl+"/saludo/?nombre="+param);
            }

            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("GET");

            //Headed
            urlConn.setRequestProperty("Authorization", "Bearer "+ user.getJwt());

            //Respuesta
            int responseCode = urlConn.getResponseCode();

            InputStream inputStream;
            if (responseCode == HttpURLConnection.HTTP_OK){
                inputStream = urlConn.getInputStream();

            }else{
                inputStream = urlConn.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }

            response = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            response = null;

        }finally {
            urlConn.disconnect();
        }
        return response;
    }
}

