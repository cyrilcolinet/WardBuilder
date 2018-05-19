package fr.mrlizzard.wardevil.builder.core;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.RedisConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisLoader {

    private WardBuilder             instance;

    protected JedisPool             main;

    public RedisLoader(WardBuilder instance) {
        this.instance = instance;

        try {
            this.initializeConnection();
        } catch (InterruptedException exception) {
            instance.getLog().error("Error during initialization of connection.");
            exception.printStackTrace();
        }
    }

    private void initializeConnection() throws InterruptedException {
        GenericObjectPoolConfig pool = new GenericObjectPoolConfig();
        RedisConfig conf = instance.getConfigManager().getConfig().getRedisConfig();

        instance.getLog().info("Initialize redis connection...");
        pool.setMaxTotal(1024);
        pool.setMaxWaitMillis(5000);

        if (conf.getAuth() == null || conf.getAuth().length() == 0) {
            this.main = new JedisPool(pool, conf.getHost(), conf.getPort(), 5000);
        } else {
            this.main = new JedisPool(pool, conf.getHost(), conf.getPort(), 5000, conf.getAuth());
        }

        instance.getLog().info("Connection initialized.");
    }

    public Jedis getRessource() {
        return main.getResource();
    }

    public Jedis getCacheRessource() {
        return main.getResource();
    }

    public void destroy() {
        instance.getLog().info("Destroying redis pool connection...");
        main.destroy();
    }

}
