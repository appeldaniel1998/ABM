package com.example.abm.Cart;

import com.example.abm.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.Clients.Client;
import com.example.abm.HistoryAnalytics.ClientActivities;

public class Order implements ClientActivities{

    //date, price, time
    private String clientId;
    private String totalPrice;
    private int date;
    private String time;


    public Order(String clientId, String totalPrice, int date, String time) {
        this.clientId = clientId;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
    }


    public String toString() {
        return "Orders{" +
                "clientId='" + clientId + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                '}';
    }



    @Override
    public String getActivityName() {
        return "Order";
    }

    @Override
    public int getDate() {
        return this.date;
    }

    @Override
    public String getTime() {
        //4 digits
        return this.time;
    }

    @Override
    public String getPrice() {
        return this.totalPrice;
    }

    @Override
    public String getClientName() {
        if (this.clientId.contains(" ")) {
            return this.clientId;
        }
        else {
            Client currClient = CalendarMainActivity.clients.get(this.clientId);
            assert currClient != null;
            return currClient.getFirstName() + " " + currClient.getLastName();
        }
    }
}
