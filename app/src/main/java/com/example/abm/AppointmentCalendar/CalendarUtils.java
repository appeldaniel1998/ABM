package com.example.abm.AppointmentCalendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils
{
    // https://www.youtube.com/watch?v=knpSbtbPz3o
    public static LocalDate selectedDate;

    public static String formatteDate(LocalDate date)
    {//Define day format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formatteTime(LocalTime time)
    {//Define time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");//hour,minutes,seconds, am/pm
        return time.format(formatter);
    }
    //function create arraylist of days in moth to represent it in monthly calendar
    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
{
    ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
    YearMonth yearMonth = YearMonth.from(date);

    int daysInMonth = yearMonth.lengthOfMonth();

    LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
    int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();//return number between 0 to 7

    for(int i = 1; i <= 42; i++)//len of 31 days in moth is 42
    {
        if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)//if day not in month
        {
            daysInMonthArray.add(null);//we will add blank square
        }
        else//if day is in month
        {
            daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
    }
    return  daysInMonthArray;
}
    //Define month format
    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

//function create arraylist of days in week
    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        //stopped here - 3:39 https://www.youtube.com/watch?v=knpSbtbPz3o
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current= sundayForDate(selectedDate);
        LocalDate endDate=current.plusWeeks(1);

        while(current.isBefore(endDate))
        {
            days.add(current);
            current=current.plusDays(1);

        }
        return days;
    }
    //look  a week into the past, go forward until you find sunday
    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo= current.minusWeeks(1);
        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek()== DayOfWeek.SUNDAY)
                return current;//return the sunday
            current=current.minusDays(1);
        }
        return null;//if you cant find a sunday which will never happen we are going to return null
    }



}
