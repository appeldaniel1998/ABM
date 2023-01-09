package com.example.abm.Utils;

import com.example.abm.Clients.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BackendHandling {
    public static boolean requestCompleted = false;
    public static ArrayList<Client> clients = new ArrayList<>();

    public static void handleServerResponses(JSONArray jsonArray) throws JSONException {
        handleGetAllClients(jsonArray);
        requestCompleted = true;
    }

    public static void handleGetAllClients(JSONArray jsonArray) throws JSONException {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String lastName = jsonObject.getString("lastName");
                String phoneNumber = jsonObject.getString("phoneNumber");
                String firstName = jsonObject.getString("firstName");
                String address = jsonObject.getString("address");
                String uid = jsonObject.getString("uid");
                boolean manager = jsonObject.getBoolean("manager");
                String email = jsonObject.getString("email");
                int birthdayDate = jsonObject.getInt("birthdayDate");
                clients.add(new Client(firstName, lastName, email, phoneNumber, address, birthdayDate, uid, manager));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
