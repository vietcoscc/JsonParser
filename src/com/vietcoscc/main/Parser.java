package com.vietcoscc.main;

import com.sun.jndi.toolkit.url.Uri;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by vaio on 18/04/2017.
 */
public class Parser {
    private String link;
    private String pageToken = "";
    private String data = "";

    private String[] s = {"coffee", "cafe", "quán+cà+phê", "quán+cafe", "quán+coffee", "café", "cà+phê"}; // search list

    public Parser(String link) {
        this.link = link;
    }

    public void parse() throws Exception {
        File file = new File("D:/KDL/data.txt");
        createFile(file);

        //
        String format = "json";
        String location = "9.179533,105.150190"; // location
        String radius = "49000";
        String type = "";
        String nameSearch;
        String key = "AIzaSyAjCxDFbqC9uy11X5SKpmLzVqFXy6KKJP4";
        //

        JSONObject prevObject;
        for (int i = 0; i < s.length; i++) {
            nameSearch = s[i];
            link = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + format + "?" +
                    "type" + "&" +
                    "location=" + location + "&" +
                    "radius=" + radius + "&" +
                    "name=" + nameSearch + "&" +
                    "key=" + key;

            data = getData(link);
            JSONObject jsonObjectRoot = new JSONObject(data);

            System.out.println(link);
            do {
                Thread.sleep(2000);
                JSONArray jsonArrayResults = jsonObjectRoot.getJSONArray("results");

                for (int j = 0; j < jsonArrayResults.length(); j++) {

                    JSONObject jsonObjectResult = jsonArrayResults.getJSONObject(j);

                    String name = jsonObjectResult.getString("name");
                    String address = jsonObjectResult.getString("vicinity");

                    JSONObject jsonObjectGeometry = jsonObjectResult.getJSONObject("geometry");
                    JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");

                    double lat = jsonObjectLocation.getDouble("lat");
                    double lng = jsonObjectLocation.getDouble("lng");

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/KDL/data.txt", true)));
                    String dataLine = name + "_" + address + "_" + "_" + lat + "_" + lng;
                    bufferedWriter.write(new String(dataLine.getBytes("ISO-8859-1"), "UTF-8"));
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                    System.out.println(new String(dataLine.getBytes("ISO-8859-1"), "UTF-8"));
                }


                prevObject = jsonObjectRoot;
                if (jsonObjectRoot.has("next_page_token")) {
                    pageToken = jsonObjectRoot.getString("next_page_token");
                    data = getData(link + "&" + "pagetoken=" + pageToken);
                    System.out.println(link + "&" + "pagetoken=" + pageToken);
                    jsonObjectRoot = new JSONObject(data);
                }


            } while (prevObject.has("next_page_token"));

        }

    }

    private void createFile(File file) throws Exception {
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        parent.mkdirs();
        file.createNewFile();
    }

    private String getData(String link) {
        try {
//            Thread.sleep(1000);
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            StringBuilder builder = new StringBuilder();
            int c = stream.read();
            while (c != -1) {
                builder.append((char) c);
                c = stream.read();
            }
//            System.out.println(builder.toString());
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
