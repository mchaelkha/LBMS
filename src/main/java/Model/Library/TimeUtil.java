package Model.Library;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public interface TimeUtil {

    /**
     * Calculates the duration between two LocalDayTime objects in hours:minutes:seconds
     * @return String representation of duration between LocalDayTime objects
     */
    default String calculateDuration(LocalDateTime start, LocalDateTime end) {
        Duration dur = Duration.between(start, end);
        long millis = dur.toMillis();

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
