package com.eightbitcloud.appengine.testing;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class AgentUploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String type = req.getParameter("type");
        
        
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("agentZip");
        
        if (type.equals("simple")) {
            res.sendRedirect("/testclassloader.jsp?agent=" + blobKey.getKeyString());
        } else {
            res.sendRedirect("/testagentloader.jsp?agent=" + blobKey.getKeyString());
            
        }
    }
}
