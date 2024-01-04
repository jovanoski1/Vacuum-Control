package rs.raf.demo.utils;

import java.time.LocalDateTime;

public class Util {

    public static String convertTimeToCron(LocalDateTime dateTime){
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHour();
        int min = dateTime.getMinute();
        int sec = dateTime.getSecond();
        return String.format("%d %d %d %d %d *", sec, min, hour, day, month);
    }

}
