package com.eightbitcloud.appengine.testing;

import com.eightbitcloud.appengine.classloader.DatastoreClassloader;
import com.google.appengine.api.blobstore.BlobKey;

public class ClassloaderTest {
    
    public String test(String blobID) throws Exception {
        BlobKey zipKey = (BlobKey) new BlobKey(blobID);

        DatastoreClassloader cl = new DatastoreClassloader(this.getClass().getClassLoader(), zipKey);
        Class<? extends Agent> cz = Class.forName("com.eightbitcloud.agent.test.TestAgent", true, cl).asSubclass(Agent.class);
        
        Agent a = cz.newInstance();
        return a.execute(new String[] {});
    }
}
