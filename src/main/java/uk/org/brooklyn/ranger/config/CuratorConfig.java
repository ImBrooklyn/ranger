package uk.org.brooklyn.ranger.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.org.brooklyn.ranger.context.ZkContext;

import java.util.concurrent.TimeUnit;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@Configuration
public class CuratorConfig {
    @Value("${ranger.zookeeper.connect-string:127.0.0.1:2181}")
    private String connectString;

    private final ZkContext zkContext;

    @Autowired
    public CuratorConfig(ZkContext zkContext) {
        this.zkContext = zkContext;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();

        try {
            if (!curatorFramework.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Connecting to zookeeper timeout...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zkContext.setConnected(true);
        return curatorFramework;
    }
}
