package Model.Library;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public interface TimeUtil {

    /**
     * Calculates the duration between two LocalDayTime objects in hours:minutes:seconds
     * @return String representation of duration between LocalDayTime objects
     */
    default String calculateDuration(LocalDateTime start, LocalDateTime end) {
        String hours = Long.toString(ChronoUnit.HOURS.between(start, end)%24);
        String minutes = Long.toString(ChronoUnit.MINUTES.between(start, end)%60);
        String seconds = Long.toString(ChronoUnit.SECONDS.between(start, end)%60);

        return hours+":"+minutes+":"+seconds;
    }
}
