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
    	compiled bytecode.  There are two forms of code that can be uploaded here.  Both are intended to demonstrate using the classloader, rather than 
    	doing anything useful with it.  You should look at the code to see what is being if you wish to use it yourself.
    	</p>
    	
    	<p>Sample jars can be created using the ANT buildfile in the project.  The default target will create two jar files, called <em>simple.jar</em> and <em>app.jar</em>.
    	The first form is a simple JAR.  Once uploaded, the application will create a classloader from it, load and execute the class <em>com.eightbitcloud.agent.test.simple.TestAgentInOneJar</em>.  
    	The second form expects a JAR file in a similar form to a WAR file.  That is, it has a <em>classes</em> directory that contains classes and a <em>lib</em> directory which contains additional
    	JAR files to add to the classpath.  The application form also has a manifest file in <em>META-INF/agent-inf.yaml</em> which specifies which class should be loaded etc...  
    	</p> 

		<h2>Simple JAR upload and execution</h2>    	
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            Agent Zip: <input type="file" name="agentZip"><br/>
            <input type="hidden" name="type" value="simple">
            <input type="submit" value="Submit">
        </form>

		<h2>Complete Application upload and execution</h2>    	
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            Agent Zip: <input type="file" name="agentZip"><br/>
            <input type="hidden" name="type" value="agent">
            <input type="submit" value="Submit">
        </form>

    </body>
</html>
