package Controller.Request;

/**
 * An inaccessible request which overrides the need to have a client ID
 * to execute.
 *
 * @author Michael Kha
 */
public abstract class InaccessibleRequest implements Request {

    /**
     * An inaccessible request will not have a client ID.
     * @return This request will never have a client ID
     */
    @Override
    public boolean hasClientID() {
        return false;
    }

}
