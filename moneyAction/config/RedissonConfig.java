package net.whir.hos.labunion.moneyAction.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "spring.redis")
@Configuration
@Data
public class RedissonConfig {

    // 读取配置文件里面的Redis信息
    private String host;
    private String port;
    private String password;
    private Cluster cluster;

    public static class Cluster {
        private List<String> nodes;
        public List<String> getNodes() {
            return nodes;
        }
        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

    /**
     * 配置redisson --集群方式
     * Redisson是RedissonClient的实现类
     * @return
     */
//    @Bean(destroyMethod = "shutdown")
//    public Redisson redisson() {
//        List<String> clusterNodes = new ArrayList<>();
//        for (int i = 0; i < this.getCluster().getNodes().size(); i++) {
//            clusterNodes.add("redis://" + this.getCluster().getNodes().get(i));
//        }
//        Config config = new Config();
//        ClusterServersConfig clusterServersConfig = config.useClusterServers()
//                .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
//        clusterServersConfig.setPassword(getPassword());
//        return (Redisson) Redisson.create(config);
//    }
    /**
     * 配置redisson --单节点
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        String address = "redis://" +host  + ":" + port;
        //使用json序列化方式
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer().setAddress(address);

        return Redisson.create(config);
    }

}
