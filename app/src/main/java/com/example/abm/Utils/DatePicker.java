package com.example.abm.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Class function code inspired by the tutorial and the accompanying github:
 * https://www.youtube.com/watch?v=qCoidM98zNk
 * https://github.com/codeWithCal/DatePickerTutorial/blob/master/app/src/main/java/codewithcal/au/datepickertutorial/MainActivity.java
 */
public class DatePicker {

    /**
     * Function to return the string representing today's date
     *
     * @return string representing a date
     */
    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH); // Jan = 0, so increment by 1 for the sake of understandability
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public static DatePickerDialog initDatePicker(TextView dateTextView, Activity activity) {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateTextView.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.R.style.Theme_DeviceDefault_Light_Dialog; //Can change style at will

        return new DatePickerDialog(activity, style, dateSetListener, year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());  // Can set max/min date with this line of code
    }

    /**
     * Construct a string to present to the user after choosing a date. Given the day, month and year, create and return an appropriate string
     *
     * @param day   day of month
     * @param month month of year
     * @param year  year
     * @return constructed string
     */
    public static String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    /**
     * Given a month as a number, returns the name of the month as a relevant string
     */
    public static String getMonthFormat(int month) {
        if (month == 1) return "January";
        if (month == 2) return "February";
        if (month == 3) return "March";
        if (month == 4) return "April";
        if (month == 5) return "May";
        if (month == 6) return "June";
        if (month == 7) return "July";
        if (month == 8) return "August";
        if (month == 9) return "September";
        if (month == 10) return "October";
        if (month == 11) return "November";
        if (month == 12) return "December";

        //default should never happen
        return "JAN";
    }
}
