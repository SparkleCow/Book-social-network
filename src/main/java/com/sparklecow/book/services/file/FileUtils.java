package com.sparklecow.book.services.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class FileUtils {
    private FileUtils(){}

    public static byte[] readFileFromLocation(String location){
        if(StringUtils.isBlank(location)) return new byte[0];
        try{
            return Files.readAllBytes(new File(location).toPath());
        }catch(IOException e){
            log.warn("File does not exist in path: "+location);
        }
        return new byte[0];
    }
}
