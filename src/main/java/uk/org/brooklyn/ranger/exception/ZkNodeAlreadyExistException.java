package uk.org.brooklyn.ranger.exception;

import lombok.Getter;

/**
 * @author ImBrooklyn
 * @since 05/04/2024
 */
@Getter
public class ZkNodeAlreadyExistException extends RuntimeException {
    private final String path;

    public ZkNodeAlreadyExistException(String path) {
        this.path = path;
    }

}
