package com.example.abm.Appointments.AppointmentCalender;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalenderUtils
{
    // https://www.youtube.com/watch?v=knpSbtbPz3o
    public static LocalDate selectedDate;

//    public static String monthYearFromDate(LocalDate date)
//    {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
//        return date.format(formatter);
//    }
//    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
//    {
//        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
//        YearMonth yearMonth = YearMonth.from(date);
//
//        int daysInMonth = yearMonth.lengthOfMonth();
//
//        LocalDate firstOfMonth = CalenderUtils.selectedDate.withDayOfMonth(1);
//        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();//return number between 0 to 7
//
//        for(int i = 1; i <= 42; i++)
//        {
//            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
//            {
//                daysInMonthArray.add(null);//we will add blank square
//            }
//            else
//            {
//                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
//            }
//        }
//        return  daysInMonthArray;
//    }
public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
{
    ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
    YearMonth yearMonth = YearMonth.from(date);

    int daysInMonth = yearMonth.lengthOfMonth();

    LocalDate firstOfMonth = CalenderUtils.selectedDate.withDayOfMonth(1);
    int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();//return number between 0 to 7

    for(int i = 1; i <= 42; i++)
    {
        if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
        {
            daysInMonthArray.add(null);//we will add blank square
        }
        else
        {
            daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
    }
    return  daysInMonthArray;
}

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }


    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {//stopped here - 3:39 https://www.youtube.com/watch?v=knpSbtbPz3o
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

    private static LocalDate sundayForDate(LocalDate current)
    {//look  a week into the past, go forward until you find sunday
        LocalDate oneWeekAgo= current.minusWeeks(1);
        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek()== DayOfWeek.SUNDAY)
                return current;//return the sunday
            current=current.minusDays(1);//if you cant find a sunday which will never happen we are going to return null
        }
        return null;
    }


}
