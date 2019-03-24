package Model.Book;

/**
 * Enumeration of the book information services that exist.
 *
 * @author Michael Kha
 */
public enum Service {
    LOCAL("local"),
    GOOGLE("google");

    /**
     * The service
     */
    private String service;

    /**
     * Enum constructor to set value to a string
     * @param service The service name
     */
    Service(String service) {
        this.service = service;
    }

    /**
     * Determine if the given service is a valid service.
     * @param service The service to check
     * @return If the service is local, google or not
     */
    public static boolean isService(String service) {
        return LOCAL.service.equals(service) || GOOGLE.service.equals(service);
    }

    /**
     * Get the service provided
     * @return The service string
     */
    public String getService() {
        return service;
    }
}
