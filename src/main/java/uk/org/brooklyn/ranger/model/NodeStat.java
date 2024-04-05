package uk.org.brooklyn.ranger.model;

import lombok.Builder;
import uk.org.brooklyn.ranger.utils.Printable;

import java.util.Date;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
@Builder
public record NodeStat(long cZxid,
                       Date ctime,
                       long mZxid,
                       Date mtime,
                       long pZxid,
                       int cversion,
                       int dataVersion,
                       int aclVersion,
                       long ephemeralOwner,
                       int dataLength,
                       int numChildren) implements Printable {
}
