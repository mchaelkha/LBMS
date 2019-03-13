package Model.Visitor;

import java.time.LocalDateTime;

/**
 * Represents a single visit with a starting time and end time once ended.
 *
 * @author Michael Kha
 */
public class Visit {

    /**
     * Time that the visit started
     */
    private LocalDateTime start;

    /**
     * Time that the visit ended
     */
    private LocalDateTime end;

    /**
     * Create a visit with a given start time.
     * @param start The start time
     */
    public Visit(LocalDateTime start) {
        this.start = start;
    }

    /**
     * End the visit at the given end time.
     * @param end The end time
     */
    public void end(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Get the start time.
     * @return The start time
     */
    public LocalDateTime getStart() {
        return start;
    }
}
