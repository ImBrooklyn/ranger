package uk.org.brooklyn.ranger.exception;

import lombok.Getter;

/**
 * @author ImBrooklyn
 * @since 12/01/2024
 */
@Getter
public class ZkNodeNotExistException extends RuntimeException {

    private final String path;

    public ZkNodeNotExistException(String path) {
        this.path = path;
    }

}
