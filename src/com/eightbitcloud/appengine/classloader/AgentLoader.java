package com.eightbitcloud.appengine.classloader;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public AgentLoader(ClassLoader parent, BlobKey key) throws EntityNotFoundException, IOException {
        loader = new DatastoreClassLoader(parent);
        
        ZipInputStream in = ZipScanner.getZipStream(key);
        ZipScanner.scan(in, new ZipEntryHandler() {
            @Override
            public void readZipEntry(ZipEntry entry, ZipInputStream zin) throws IOException {
                String name = entry.getName();
                
                if (name.startsWith("classes/")) {
                    loader.addClass(name.substring(8), ZipScanner.readZipBytes(entry, zin));

                } else if (name.startsWith("lib/") && name.endsWith(".jar")) {
                    ZipInputStream subZip = new ZipInputStream(zin);
                    loader.addClassJar(subZip);
                } else if (name.equals("META-INF/agent-inf.yaml")) {
                    // TODO deal with META-INF or app.yaml or whatever we end up using to deal with the application descriptor
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
        // TODO fetch this from the agent manifest
        return "com.eightbitcloud.agent.test.app.TestAgentInCompoundJar";
    }

    public Agent createAgent() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return getAgentClass().newInstance();
    }
    
    
}
