package uk.org.brooklyn.ranger.exception;

/**
 * @author ImBrooklyn
 * @since 12/01/2024
 */
public class ZkNodeNotExistException extends RuntimeException {

    private final String path;


    public ZkNodeNotExistException(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
