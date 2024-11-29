package com.doan.AppTuyenDung.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUploadUtil {
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    
    public static final String DATE_FORMAT = "yyyyMMddmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAlowedExtension(final String filename, String pattern)
    {
        final Matcher matcher = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE).matcher(filename);
        return matcher.matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern)
    {
        final long size = file.getSize();
        if(size > MAX_FILE_SIZE)
        {
            throw new IllegalArgumentException("File is too large. Maximum size is " + MAX_FILE_SIZE + " bytes.");
        }

        final String filename = file.getOriginalFilename();
        final String extension = FilenameUtils.getExtension(filename);

        if(!isAlowedExtension(filename, pattern)){
            throw new IllegalArgumentException("Only jpg, png,bmp file are allowed");

        }
    }


    public static String getFileName(final String name)
    {
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, name, date);
    }
}
