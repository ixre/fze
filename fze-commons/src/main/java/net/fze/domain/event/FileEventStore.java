/**
 * Copyright (C) 2007-2024 56X.NET,All rights reserved.
 * <p>
 * name : FileEventStore.java
 * author : jarrysix (jarrysix#gmail.com)
 * date : 2024-03-31 15:17
 * description :
 * history :
 */
package net.fze.domain.event;

import net.fze.util.Times;
import net.fze.util.Types;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * 文件事件存储
 * @author jarrysix
 */
public class FileEventStore implements IEventBusStore{
    private final boolean splitByDate;
    private final String basePath;

    public FileEventStore(String basePath, boolean splitByDate) {
        this.basePath = basePath;
        this.splitByDate = splitByDate;
    }
    @Override
    public void storeEvent(String eventId, String eventName, Object event) {
        if(event == null){
            throw new IllegalArgumentException("event data is null");
        }
        this.writeLog("ID:" +eventId + " "+eventName+ " >  " + Types.toJson(event) + "\n");
    }

    private String getStoreFile() {
        File fi = new File(basePath);
        if(fi.isFile()){
            if(!fi.delete()){
                throw new RuntimeException("delete file failed, "+ basePath);
            }
        }
        if(!fi.exists()){
            if(!fi.mkdirs()){
                throw new RuntimeException("create dir failed, "+ basePath);
            }
        }
        if(this.splitByDate){
            return "event-bus_"+  Times.format(new Date(),"yyyy-MM-dd") +".log";
        }
        return "event-bus.log";
    }

    @Override
    public void captureException(String eventId, String eventName, Throwable ex) {
        if( ex == null){
            return;
        }
        this.writeLog("ID:"+eventId + " "+eventName+" # " + ex.getMessage() + "\n");
    }

    public void writeLog(String line) {
        line = String.format("%s : %s",Times.format(new Date(),"yyyy-MM-dd HH:mm:ss"),line);
        try {
            Path path = Paths.get(this.basePath, this.getStoreFile());
            Files.write(path, line.getBytes(), StandardOpenOption.CREATE ,StandardOpenOption.APPEND);
        }catch(Throwable ex){
           ex.printStackTrace();
        }
    }
}