package uk.org.brooklyn.ranger.client.impl;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.org.brooklyn.ranger.client.ZookeeperClient;
import uk.org.brooklyn.ranger.convertor.CuratorBeanConvertor;
import uk.org.brooklyn.ranger.model.NodeStat;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Component
public class CuratorZookeeperClientImpl implements ZookeeperClient, InitializingBean {

    @Value("${ranger.zookeeper.connect-string:127.0.0.1:2181}")
    private String connectString;

    @Value("${ranger.zookeeper.connect-on-boot:false}")
    private Boolean connectOnBoot;

    private CuratorFramework zkClient;

    @Override
    public void afterPropertiesSet() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.zkClient = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .build();

        if (connectOnBoot) {
            this.connect();
        }
    }

    @Override
    public boolean connect() {
        if (this.connected()) {
            return true;
        }
        this.zkClient.start();
        try {
            if (!this.zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Connecting to zookeeper timeout...");
            }
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    @Override
    public boolean connected() {
        return zkClient.getState() == CuratorFrameworkState.STARTED;
    }

    @SneakyThrows
    @Override
    public boolean exists(String path) {
        Stat stat = zkClient.checkExists().forPath(path);
        return stat != null;
    }

    @SneakyThrows
    @Override
    public List<String> children(String path) {
        return zkClient.getChildren().forPath(path);
    }

    @Override
    @SneakyThrows
    public NodeStat stat(String path) {
        Stat stat = new Stat();
        zkClient.getData().storingStatIn(stat).forPath(path);
        return CuratorBeanConvertor.toNodeStat(stat);
    }

    @Override
    @SneakyThrows
    public void create(String path, boolean ephemeral, boolean sequential) {
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(NodeModeEnum.withOption(ephemeral, sequential).getMode())
                .forPath(path);
    }

    private enum NodeModeEnum {
        PERSISTENT(0, CreateMode.PERSISTENT),
        PERSISTENT_SEQUENTIAL(1, CreateMode.PERSISTENT_SEQUENTIAL),
        EPHEMERAL(2, CreateMode.EPHEMERAL),
        EPHEMERAL_SEQUENTIAL(3, CreateMode.EPHEMERAL_SEQUENTIAL),

        ;

        private final int code;

        @Getter
        private final CreateMode mode;


        NodeModeEnum(int code, CreateMode mode) {
            this.code = code;
            this.mode = mode;
        }

        static NodeModeEnum withOption(boolean ephemeral, boolean sequential) {
            int code = 0;
            if (ephemeral) {
                code |= 2;  // 10
            }

            if (sequential) {
                code |= 1; // 01
            }
            for (NodeModeEnum value : values()) {
                if (code == value.code) {
                    return value;
                }
            }
            return PERSISTENT;
        }

    }

}
