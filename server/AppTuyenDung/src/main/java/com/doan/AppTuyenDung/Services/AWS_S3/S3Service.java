package com.doan.AppTuyenDung.Services.AWS_S3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {
    private String bucketName = "imagecoms3";
    Region regions = Region.AP_SOUTHEAST_2;

    public String putS3Object(S3Client s3,String bucketName, String objectKey, String objectPath) { 
        // S3Client s3 = S3Client.builder()
        //         .region(region)
        //         .build();
        try{
            Map<String, String> metadata = new HashMap();
            metadata.put("Content-Type", "image/jpeg");

        PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .metadata(metadata)
                    .build();
        PutObjectResponse response = s3.putObject(putOb,
                    RequestBody.fromBytes(getObjectFile(objectPath)));
        return response.eTag();
        }
        catch(S3Exception e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
    private static byte[] getObjectFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }
}
