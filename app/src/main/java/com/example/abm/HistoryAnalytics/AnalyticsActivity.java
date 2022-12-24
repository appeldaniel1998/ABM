package com.example.abm.HistoryAnalytics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AnalyticsActivity extends BaseActivity {

    private final HashMap<String, ArrayList<ClientActivities>> clientActivitiesPerYear = new HashMap<>();

    private ArrayList<String> relevantYears;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        initMenuSideBar();

        Button historyButton = findViewById(R.id.analyticsButton); //set onclick listener to button "History"
        historyButton.setOnClickListener(v -> startActivity(new Intent(AnalyticsActivity.this, HistoryActivity.class)));

        //Identifying the different years for which to do the calculations and sorting the event to arraylists (arraylist per year)
        for (int i = 0; i < HistoryActivity.clientActivities.size(); i++) {
            ClientActivities currActivity = HistoryActivity.clientActivities.get(i);
            String currYear = (currActivity.getDate() / (100 * 100)) + "";
            if (!clientActivitiesPerYear.containsKey(currYear)) {
                clientActivitiesPerYear.put(currYear, new ArrayList<>());
            }
            clientActivitiesPerYear.get(currYear).add(currActivity);
        }

        relevantYears = new ArrayList<>();
        relevantYears.addAll(clientActivitiesPerYear.keySet());

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, relevantYears);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String year = parent.getItemAtPosition(position).toString();
            showYearData(year);
        });

        //set default value to the dropdown menu:
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        TextInputLayout textInputLayout = findViewById(R.id.autoCompleteWrapper);
        assert textInputLayout.getEditText() != null;
        textInputLayout.getEditText().setText(currYear + "");
        showYearData(currYear + "");
    }

    private void showYearData(String year) {
        //calculate monthly revenue
        double[] monthlyRevenue = new double[12];
        ArrayList<ClientActivities> currYear = clientActivitiesPerYear.get(year);
        if (currYear != null) {
            for (int i = 0; i < currYear.size(); i++) {
                ClientActivities currActivity = currYear.get(i);
                int month = (currActivity.getDate() / 100) % 100;
                monthlyRevenue[month - 1] += Double.parseDouble(currActivity.getPrice());
            }
        }
        //*******************************************//

        //Initialize text views to put data into them
        TextView[] monthlyTextViews = new TextView[12];
        monthlyTextViews[0] = findViewById(R.id.januaryTextView);
        monthlyTextViews[1] = findViewById(R.id.februaryTextView);
        monthlyTextViews[2] = findViewById(R.id.marchTextView);
        monthlyTextViews[3] = findViewById(R.id.aprilTextView);
        monthlyTextViews[4] = findViewById(R.id.mayTextView);
        monthlyTextViews[5] = findViewById(R.id.juneTextView);
        monthlyTextViews[6] = findViewById(R.id.julyTextView);
        monthlyTextViews[7] = findViewById(R.id.augustTextView);
        monthlyTextViews[8] = findViewById(R.id.septemberTextView);
        monthlyTextViews[9] = findViewById(R.id.octoberTextView);
        monthlyTextViews[10] = findViewById(R.id.novemberTextView);
        monthlyTextViews[11] = findViewById(R.id.decemberTextView);
        //*******************************************//

        //Set the new values for the placeholders
        for (int i = 0; i < 12; i++) {
            monthlyTextViews[i].setText((Math.round(monthlyRevenue[i] * 100) / 100.0) + "");
        }


        //Calculate and set the total revenue of the year
        TextView totalRevenue = findViewById(R.id.totalRevenueTextView);
        double revenue = 0;
        for(int i = 0; i < 12; i++) {
            revenue += monthlyRevenue[i];
        }
        totalRevenue.setText((Math.round(revenue * 100) / 100.0) + "");

    }
}