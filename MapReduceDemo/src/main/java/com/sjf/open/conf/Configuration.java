package com.sjf.open.conf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xiaosi on 16-12-12.
 */
public class Configuration {

    private static final Log LOG = LogFactory.getLog(Configuration.class);

    // 是否加载默认配置文件 hdfs-default.xml mapred-default.xml core-default.xml
    private boolean loadDefaults = true;

    /**
     * List of default Resources. Resources are loaded in the order of the list entries
     */
    private static final CopyOnWriteArrayList<String> defaultResources = new CopyOnWriteArrayList<String>();


    private static class Resource {

        private final Object resource;
        private final String name;

        public Resource(Object resource) {
            this(resource, resource.toString());
        }

        public Resource(Object resource, String name) {
            this.resource = resource;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Object getResource() {
            return resource;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    static{
        //print deprecation warning if hadoop-site.xml is found in classpath
        ClassLoader cL = Thread.currentThread().getContextClassLoader();
        if (cL == null) {
            cL = org.apache.hadoop.conf.Configuration.class.getClassLoader();
        }
        if(cL.getResource("hadoop-site.xml")!=null) {
            LOG.warn("DEPRECATED: hadoop-site.xml found in the classpath. " +
                    "Usage of hadoop-site.xml is deprecated. Instead use core-site.xml, "
                    + "mapred-site.xml and hdfs-site.xml to override properties of " +
                    "core-default.xml, mapred-default.xml and hdfs-default.xml " +
                    "respectively");
        }
        addDefaultResource("core-default.xml");
        addDefaultResource("core-site.xml");
    }

    /**
     * A new configuration where the behavior of reading from the default resources can be turned off.
     *
     * If the parameter loadDefaults is false, the new instance will not load resources from the default files.
     * 
     * @param loadDefaults
     */
    public Configuration(boolean loadDefaults) {
        this.loadDefaults = loadDefaults;
    }

    private static void addDefaultResource(String name) {

        if(!defaultResources.contains(name)) {
            defaultResources.add(name);
            /*for(Configuration conf : REGISTRY.keySet()) {
                if(conf.loadDefaults) {
                    conf.reloadConfiguration();
                }
            }*/
        }

    }

}
