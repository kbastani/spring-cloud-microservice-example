package data.config;

import com.couchbase.client.CouchbaseClient;
import net.spy.memcached.FailureMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseFactoryBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kennybastani on 7/31/15.
 */
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {
    @Value("${couchbase.cluster.bucket:default}")
    private String bucketName;

    @Value("${couchbase.cluster.password:}")
    private String password;

    @Value("${couchbase.cluster.ip:couchbase}")
    private String ip;

    @Value("${couchbase.cluster.ttl:90}")
    private Integer timeout;

    @Override
    protected List<String> bootstrapHosts() {
        return Arrays.asList(ip);
    }

    @Override
    protected String getBucketName() {
        return "default";
    }

    @Override
    protected String getBucketPassword() {
        return password;
    }

    @Bean(name = "couchbaseCacheManager")
    public CouchbaseCacheManager couchbaseCacheManager() {
        HashMap<String, CouchbaseClient> instances = new HashMap<>();

        try {
            instances.put("cache", couchbaseFactoryBean().getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> ttl = new HashMap<>();
        ttl.put("cache", timeout);

        return new CouchbaseCacheManager(instances, ttl);
    }

    @Bean
    public CouchbaseFactoryBean couchbaseFactoryBean() {
        CouchbaseFactoryBean couchbaseFactoryBean = new CouchbaseFactoryBean();
        couchbaseFactoryBean.setOpTimeout(timeout);
        couchbaseFactoryBean.setBucket(bucketName);
        couchbaseFactoryBean.setPassword(password);

        couchbaseFactoryBean.setHost(ip);
        couchbaseFactoryBean.setFailureMode(String.valueOf(FailureMode.Cancel));

        return couchbaseFactoryBean;
    }
}
