package com.eightbitcloud.appengine.testing;

import com.eightbitcloud.appengine.classloader.AgentLoader;
import com.eightbitcloud.appengine.classloader.DatastoreClassLoader;
import com.google.appengine.api.blobstore.BlobKey;

public class ClassloaderTest {
    
    public String testSimpleJar(String blobID) throws Exception {
        BlobKey zipKey = (BlobKey) new BlobKey(blobID);

        DatastoreClassLoader cl = new DatastoreClassLoader(this.getClass().getClassLoader(), zipKey);
        Class<? extends Agent> cz = Class.forName("com.eightbitcloud.agent.test.simple.TestAgentInOneJar", true, cl).asSubclass(Agent.class);
        
        Agent a = cz.newInstance();
        return a.execute(new String[] {});
    }


    public String testAgent(String blobID) throws Exception {
        BlobKey zipKey = (BlobKey) new BlobKey(blobID);

        AgentLoader l = new AgentLoader(this.getClass().getClassLoader(), zipKey);
        
        return l.createAgent().execute(new String[] {});        
    }

}
