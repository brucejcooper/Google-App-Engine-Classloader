package com.eightbitcloud.appengine.testing;

import com.eightbitcloud.appengine.classloader.DatastoreClassLoader;
import com.google.appengine.api.blobstore.BlobKey;

public class ClassloaderTest {
    
    public String test(String blobID) throws Exception {
        BlobKey zipKey = (BlobKey) new BlobKey(blobID);

        DatastoreClassLoader cl = new DatastoreClassLoader(this.getClass().getClassLoader(), zipKey);
        Class<? extends Agent> cz = Class.forName("com.eightbitcloud.agent.test.simple.TestAgentInOneJar", true, cl).asSubclass(Agent.class);
        
        Agent a = cz.newInstance();
        return a.execute(new String[] {});
    }
}
