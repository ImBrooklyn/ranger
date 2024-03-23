package uk.org.brooklyn.ranger.convertor;

import org.apache.zookeeper.data.Stat;
import uk.org.brooklyn.ranger.model.NodeStat;

import java.util.Date;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
public final class CuratorBeanConvertor {
    private CuratorBeanConvertor() {
    }

    public static NodeStat toNodeStat(Stat stat) {
        return NodeStat.builder()
                .cZxid(stat.getCzxid())
                .ctime(new Date(stat.getCtime()))
                .mZxid(stat.getMzxid())
                .mtime(new Date(stat.getMtime()))
                .pZxid(stat.getPzxid())
                .cversion(stat.getCversion())
                .dataVersion(stat.getVersion())
                .aclVersion(stat.getAversion())
                .ephemeralOwner(stat.getEphemeralOwner())
                .dataLength(stat.getDataLength())
                .numChildren(stat.getNumChildren())
                .build();
    }
}
