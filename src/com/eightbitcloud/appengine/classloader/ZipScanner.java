package com.eightbitcloud.appengine.classloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.LockException;

public class ZipScanner {
    private static FileService fileService = FileServiceFactory.getFileService();

    private ZipScanner() {}
    
    public static ZipInputStream getZipStream(BlobKey key) throws LockException, IOException {
        AppEngineFile f = fileService.getBlobFile(key);
        InputStream in = Channels.newInputStream(fileService.openReadChannel(f, false));
        
        ZipInputStream zin = new ZipInputStream(in);
        return zin;
    }
    
    public static void scan(ZipInputStream zin, ZipEntryHandler h) throws IOException {
        ZipEntry entry;
        while((entry = zin.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                h.readZipEntry(entry, zin);
            }
        }
    }
    
    public static byte[] readZipBytes(ZipEntry entry, ZipInputStream zin) throws IOException {
        int count;
        int total = (int) entry.getSize();
        int pos = 0;
        byte[] data = new byte[total];
        while (pos < total && (count = zin.read(data, pos, total)) != -1) {
            pos += count;
        }
        return data;
    }
    
}
