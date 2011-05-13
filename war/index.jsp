<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>


<html>
    <head>
        <title>Upload Test</title>
    </head>
    <body>
    	<h1>Google App Engine datastore based Classloader example</h1>
    	
    	<p>
    	This page allows you to test running java code that has been uploaded to GAE using a HTTP file upload.  To run, create a JAR file which contains
    	compiled bytecode.  For the purposes of this test, the class must be named <em>com.eightbitcloud.agent.test.TestAgent</em>, and this class must 
    	implement the <em>com.eightbitcloud.appengine.test.Agent</em> interface.  This is only done for ease of demonstration however, and in practice you
    	could use whatever code you wished.  Then upload the JAR file using the form below (The "Agent Name" field is ingored).  Once the JAR file has been
    	uploaded, you will be redirected to a page which will execute the code in the JAR file, and show you the result.
    	</p> 
    	
    	<p>If you wish to create a sample agent jar, use the ANT buildfile that is included in the project</p>
    	
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            Agent Name: <input type="text" name="name"><br/>
            Agent Zip: <input type="file" name="agentZip"><br/>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
