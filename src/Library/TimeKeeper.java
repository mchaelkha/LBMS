/**
 * A helper class for Library that tracks the current date. This information is used by the library to track
 * overdue books. The date can be pushed forward a number of days to simulate usage over a period of time for 
 * testing.
 * @author Hersh Nagpal
 */

package Library;

import java.time.LocalDateTime;

public class TimeKeeper {
    private LocalDateTime clock;

    public TimeKeeper() {
        this.clock = LocalDateTime.now();
    }

    /**
     * Continuously updates the current time of the library.
     */
    public void updateTime() {

    }

    /**
     * Returns a boolean stating whether or not the library is open.
     * @param openHour the hour that the library opens, 0-23
     * @param closeHour the hour that the library closes, 0-23
     * @return true if the library is open, false otherwise.
     */
    public boolean isLibraryOpen(int openHour, int closeHour) {
        int currentHour = clock.getHour();
        return (openHour <= currentHour) && (closeHour >= currentHour);
    }

    /**
     * Returns the current time as a formatted String.
     * @return the current time.
     */
    public String readTime() {
        Integer hour = clock.getHour();
        Integer minute = clock.getMinute();
        Integer second = clock.getSecond();
        return hour + ":" + minute + ":" + second;
    }

    /**
     * Returns the current date as a formatted String.
     * @return the current date, d/m/y.
     */
    public String readDate() {
        Integer day = clock.getDayOfMonth();
        Integer month = clock.getMonthValue();
        Integer year = clock.getYear();
        return day + "/" + month + "/" + year;
    }

    /**
     * Sets the library's date to a given month, day and year.
     * @param day a valid number day in the given month.
     * @param month 1-12, with each number representing a month.
     * @param year the number year.
     */
    public void setDate(int day, int month, int year) {
        clock = clock.withMonth(month);
        clock = clock.withDayOfMonth(day);
        clock = clock.withYear(year);
    }

    /**
     * Sets the library's time to the given hour, minute, and second.
     * @param hour the desired hour to be set to.
     * @param minute the desired minute to be set to.
     * @param second the desired second to be set to.
     */
    public void setTime(int hour, int minute, int second) {
        clock = clock.withHour(hour);
        clock = clock.withMinute(minute);
        clock = clock.withSecond(second);
    }

    /**
     * Sets the library's time to the given hour.
     * @param hour the desired hour to be set to.
     */
    public void setTime(int hour) {
        clock = clock.withHour(hour);
    }

    /**
     * Moves the date forward by some number of days.
     * @param days The number of days to add to the current date.
     */
    public void addDays(int days) {
        clock = clock.plusDays(days);
    }
}
