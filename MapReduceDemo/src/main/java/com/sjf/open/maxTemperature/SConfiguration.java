package com.sjf.open.maxTemperature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xiaosi on 16-11-11.
 */

public class SConfiguration {

    private static final Log LOG = LogFactory.getLog(SConfiguration.class);

    private boolean loadDefaults = true;

    private Properties properties;
    private Properties overlay;

    private ClassLoader classLoader;
    {
        classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Configuration.class.getClassLoader();
        }
    }

    /**
     * 资源对象
     */
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

    private ArrayList<Resource> resources = new ArrayList<Resource>();

    /**
     * List of configuration parameters marked <b>final</b>.
     */
    private Set<String> finalParameters = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    /**
     * Stores the mapping of key to the resource which modifies or loads the key most recently
     */
    private Map<String, String[]> updatingResource;

    /**
     * Configuration objects
     */
    private static final WeakHashMap<SConfiguration, Object> REGISTRY = new WeakHashMap<SConfiguration, Object>();

    /**
     * List of default Resources. Resources are loaded in the order of the list entries
     */
    private static final CopyOnWriteArrayList<String> defaultResources = new CopyOnWriteArrayList<String>();

    // ------------------------------------------------------------------------------------------------------------------
    /** A new configuration. */
    public SConfiguration() {
        this(true);
    }

    /**
     * A new configuration where the behavior of reading from the default resources can be turned off.
     *
     * If the parameter {@code loadDefaults} is false, the new instance will not load resources from the default files.
     * 
     * @param loadDefaults specifies whether to load from the default files
     */
    public SConfiguration(boolean loadDefaults) {
        this.loadDefaults = loadDefaults;
        updatingResource = new ConcurrentHashMap<String, String[]>();
        synchronized (SConfiguration.class) {
            REGISTRY.put(this, null);
        }
    }

    public SConfiguration(SConfiguration other) {
        this.resources = (ArrayList<SConfiguration.Resource>) other.resources.clone();
        synchronized(other) {
            // list
            if (other.properties != null) {
                this.properties = (Properties)other.properties.clone();
            }

            if (other.overlay!=null) {
                this.overlay = (Properties)other.overlay.clone();
            }
            // map
            this.updatingResource = new ConcurrentHashMap<String, String[]>(
                    other.updatingResource);
            this.finalParameters = Collections.newSetFromMap(
                    new ConcurrentHashMap<String, Boolean>());
            this.finalParameters.addAll(other.finalParameters);
        }

        synchronized(Configuration.class) {
            REGISTRY.put(this, null);
        }
        this.classLoader = other.classLoader;
        this.loadDefaults = other.loadDefaults;
        //setQuietMode(other.getQuietMode());
    }
    // ------------------------------------------------------------------------------------------------------------------

    /**
     * Add a default resource. Resources are loaded in the order of the resources added.
     * 
     * @param name file name. File should be present in the classpath.
     */
    public static synchronized void addDefaultResource(String name) {
        if (!defaultResources.contains(name)) {
            defaultResources.add(name);
            for (SConfiguration conf : REGISTRY.keySet()) {
                if (conf.loadDefaults) {
                    //conf.reloadConfiguration();
                }
            }
        }
    }

}
