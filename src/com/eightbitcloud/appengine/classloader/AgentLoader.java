package com.eightbitcloud.appengine.classloader;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.yaml.snakeyaml.Yaml;

import com.eightbitcloud.appengine.testing.Agent;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.EntityNotFoundException;



/**
 * Same thing as a DatastoreClassloader, but also reads things like the application manifest.  I could have just put this into the ClassLoader 
 * but its not really the ClassLoader's responsibility
 * @author bruce
 *
 */
public class AgentLoader {
    private DatastoreClassLoader loader;
    Map<String, Object> manifest;


    public AgentLoader(ClassLoader parent, BlobKey key) throws EntityNotFoundException, IOException {
        loader = new DatastoreClassLoader(parent);
        
        ZipInputStream in = ZipScanner.getZipStream(key);
        ZipScanner.scan(in, new ZipEntryHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void readZipEntry(ZipEntry entry, ZipInputStream zin) throws IOException {
                String name = entry.getName();
                
                if (name.startsWith("classes/")) {
                    loader.addClass(name.substring(8), ZipScanner.readZipBytes(entry, zin));

                } else if (name.startsWith("lib/") && name.endsWith(".jar")) {
                    ZipInputStream subZip = new ZipInputStream(zin);
                    loader.addClassJar(subZip);
                } else if (name.equals("META-INF/agent-inf.yaml")) {
                    manifest = (Map<String, Object>) new Yaml().load(zin);
                }
            }
        });
        in.close();

    }
    
    public ClassLoader getClassLoader() {
        return loader;
    }
    
    public Class<? extends Agent> getAgentClass() throws ClassNotFoundException {
        return Class.forName(getAgentClassName(), true, loader).asSubclass(Agent.class);
    }
    
    private String getAgentClassName() {
        return (String) manifest.get("class");
    }

    public Agent createAgent() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return getAgentClass().newInstance();
    }
    
    
}
