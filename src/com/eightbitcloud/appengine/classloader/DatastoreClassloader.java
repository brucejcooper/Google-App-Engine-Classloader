package com.eightbitcloud.appengine.classloader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

/**
 * Classloader that fetches its class definitions from zip files stored in the GAE DataStore.
 * 
 * @author bruce
 */
public class DatastoreClassLoader extends ClassLoader {
    private FileService fileService = FileServiceFactory.getFileService();
    /**
     * Cached copy of the byte streams of every class in the zip files.  Not efficient, but good for demonstration purposes
     */
    private Map<String, byte[]> byteStreams = new HashMap<String, byte[]>();
    
    Logger logger = Logger.getLogger(DatastoreClassLoader.class.getName());


    /**
     * 
     * @param parent Parent class loader.  
     * @param keys varargs array of the Blob keys that specify the zips that will be scanned.
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public DatastoreClassLoader(ClassLoader parent, BlobKey... keys) throws EntityNotFoundException, IOException {
        super(parent);
        
        for (BlobKey key: keys) {
            AppEngineFile f = fileService.getBlobFile(key);
            InputStream in = Channels.newInputStream(fileService.openReadChannel(f, false));
            
            ZipInputStream zin = new ZipInputStream(in);
            addClassJar(zin);
            in.close();
        }
    }
    


    /**
     * Provides the ability to add another ZIP (Blob) in after the classloader has been constructed.  This
     * may be useful if you have a core WAR looking thing, but want to add additional JARs based upon
     * configuration etc... 
     * 
     * @param key
     * @throws IOException
     * @throws EntityNotFoundException
     */
    public void addClassJar(ZipInputStream zin) throws IOException {
        ZipScanner.scan(zin, new ZipEntryHandler() {
            @Override
            public void readZipEntry(ZipEntry entry, ZipInputStream in) throws IOException {
                String name = entry.getName();
                if (byteStreams.containsKey(name)) {
                    logger.warning("duplicate defintion of class/resource " + name + ". It will be ignored");
                } else {
                    addClass(name, ZipScanner.readZipBytes(entry, in));
                }
            }
        });
    }
    
    
    void addClass(String className, byte[] data) throws IOException {
        byteStreams.put(className, data);
    }
    
    @Override
    protected Class<?> loadClass (String name, boolean resolve) 
      throws ClassNotFoundException {
       if (name == null) {
               throw new NullPointerException();
           }
           if(name.indexOf("/") != -1) {
               throw new ClassNotFoundException(name);
           }

      // Since all support classes of loaded class use same class loader
      // must check subclass cache of classes for things like Object

      // Class loaded yet?
      Class<?> c = findLoadedClass (name);
      if (c == null) {
          try {
              c = getParent().loadClass(name);
          } catch (ClassNotFoundException ex) {
              // Load class data from file and save in byte array
              String fileName = name.replace('.','/') + ".class";
              byte data[] = byteStreams.get(fileName);
              
              if (data == null)
                  throw new ClassNotFoundException(name);

              // Convert byte array to Class
              c = defineClass (name, data, 0, data.length);

              // If failed, throw exception
              if (c == null)
                  throw new ClassNotFoundException (name);
          }

      }

      // Resolve class definition if approrpriate
      if (resolve)
          resolveClass (c);

      // Return class just created

      return c;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        byte data[] = byteStreams.get(name);
        if (data == null)
            return null;
        return new ByteArrayInputStream(data);
    }

}
