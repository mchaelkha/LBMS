package Model.Library;

import Controller.Request.RequestUtil;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A helper class for Library that tracks the current date. This information is used by the library to track
 * overdue books. The date can be pushed forward a number of days to simulate usage over a period of time for 
 * testing.
 * @author Hersh Nagpal
 */
public class TimeKeeper implements RequestUtil, Serializable {
    /**
     * Library's open hour
     */
    private static int OPEN_HOUR = 8;
    /**
     * Library's close hour
     */
    private static int CLOSE_HOUR = 19;
    /**
     * The object that holds the current time formatted nicely and is easy to manipulate.
     */
    private LocalDateTime clock;

    /**
     * The task that updates the time each minute.
     */
    private TimerTask timerTask;

    /**
     * The timer that tells the TimerTask to update each minute.
     */
    private Timer timer;

    /**
     * The LibrarySystem will be notified when its open or closing hour.
     */
    private LibrarySystem librarySystemObserver;

    /**
     * The delay in milliseconds before the task is performed the first time
     */
    private static long TIMER_DELAY = 1000;

    /**
     * The delay milliseconds between time updates for the clock.
     */
    private static long TIMER_INTERVAL = 1000;

    /**
     * Constructs a new TimeKeeper. 
     * A TimerTask is used in conjunction with a timer
     * to ensure the time is updated every minute.
     */
    public TimeKeeper() {
        timer = new Timer();
        clock = LocalDateTime.now();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        };
        timer.scheduleAtFixedRate(timerTask, TIMER_DELAY,TIMER_INTERVAL);
    }

    /**
     * Sets the librarySystemObserver
     * @param librarySystem observer to be notified when clock hits open or closed hours
     */
    public void setLibrarySystemObserver(LibrarySystem librarySystem){
        librarySystemObserver = librarySystem;
    }

    /**
     * Adds the TimerInterval in seconds to the clock. 
     * Used by the TimerTask to keep track of time.
     */
    public void updateTime() {
        this.clock = clock.plusSeconds(1);
        updateLibrary();
    }

    /**
     * Helper method to set the library state.
     */
    public void updateLibrary(){
        int hour = clock.getHour();
        if(hour >= OPEN_HOUR && hour < CLOSE_HOUR &&
                !librarySystemObserver.isOpen()){
            librarySystemObserver.openLibrary();
        }
        else if(hour < OPEN_HOUR && hour >= CLOSE_HOUR &&
                librarySystemObserver.isOpen()){
            librarySystemObserver.closeLibrary();
        }
    }

    /**
     * Helper method to set the library state when advance request is performed
     */
    public void setLibraryStateAdvance(){
        if(isLibraryOpen()){
            librarySystemObserver.openLibrary();
        }
        else{
            librarySystemObserver.closeLibrary();
        }
    }

    /**
     * Resets all time changes, resetting the clock to the current time.
     */
    public void resetTime() {
        clock = LocalDateTime.now();
    }

    /**
     * Returns a boolean stating whether or not the library is open.
     * @return true if the library is open, false otherwise.
     */
    public boolean isLibraryOpen() {
        int currentHour = clock.getHour();
        return (OPEN_HOUR <= currentHour) && (CLOSE_HOUR > currentHour);
    }

    /**
     * Returns a copy of the clock LocalDateTime object
     * @return the clock
     */
    public LocalDateTime getClock() {
        return LocalDateTime.of(clock.getYear(), clock.getMonth(),
                clock.getDayOfMonth(), clock.getHour(), clock.getMinute(), clock.getSecond());
    }

    /**
     * Returns the current time as a formatted String.
     * @return the current time.
     */
    public String readTime() {
        return readTime(clock);
    }

    /**
     * Returns the current date as a formatted String.
     * @return the current date, d/m/y.
     */
    public String readDate() {
        return readDate(clock);
    }

    /**
     * Returns the localDateTime time formatted string
     * @param localDateTime LocalDateTime used to get time string
     * @return time string of LocalDateTime object
     */
    public String readTime(LocalDateTime localDateTime) {
        int hour = clock.getHour();
        int minute = clock.getMinute();
        int second = clock.getSecond();
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Returns the localDateTime date formatted string
     * @param localDateTime LocalDateTime used to get date string
     * @return date string of LocalDateTime object
     */
    public String readDate(LocalDateTime localDateTime) {
        int day = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();
        return  String.format("%04d/%02d/%02d", year, month, day);
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
     * Discards the TimerTask and ends the Timer.
     */
    public void endTimeKeeping() {
        timerTask.cancel();
        timer.cancel();
    }

    /**
     * Moves the date forward by some number of days.
     * @param days The number of days to add to the current date.
     */
    public void addDays(int days) {
        clock = clock.plusDays(days);
    }

    /**
     * Moves the time forward by some number of hours.
     * @param hours the number of hours to add.
     */
    public void addHours(int hours){
        clock = clock.plusHours(hours);
    }

    /**
     * Calculates the duration between two LocalDayTime objects in hours:minutes:seconds
     * @return String representation of duration between LocalDayTime objects
     */
    public static String calculateDuration(LocalDateTime start, LocalDateTime end) {
        return calculateDurationString(calculateDurationMillis(start,end));
    }

    /**
     * Helper method for VisitorDB to store durations of visits in millis.
     * @param start starting time
     * @param end ending time
     * @return duration between start and end in millis
     */
    public static long calculateDurationMillis(LocalDateTime start, LocalDateTime end) {
        Duration dur = Duration.between(start, end);
        long millis = dur.toMillis();
        return millis;
    }

    /**
     * Helper method for VisitorDB to build response strings needing visit durations.
     * @param millis visit duration time
     * @return formatted string of visit duration
     */
    public static String calculateDurationString(long millis){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

}
