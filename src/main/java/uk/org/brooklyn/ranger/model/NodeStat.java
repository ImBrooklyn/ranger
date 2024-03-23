package uk.org.brooklyn.ranger.model;

import lombok.Builder;
import uk.org.brooklyn.ranger.utils.Printable;

import java.util.Date;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
@Builder
public class NodeStat implements Printable {
    private final long cZxid;
    private final Date ctime;
    private final long mZxid;
    private final Date mtime;
    private final long pZxid;
    private final int cversion;
    private final int dataVersion;
    private final int aclVersion;
    private final long ephemeralOwner;
    private final int dataLength;
    private final int numChildren;
}
