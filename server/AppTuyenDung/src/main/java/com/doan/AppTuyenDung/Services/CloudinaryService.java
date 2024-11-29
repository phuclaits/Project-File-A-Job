package com.doan.AppTuyenDung.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.doan.AppTuyenDung.DTO.CloudinaryResponse;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    
    public CloudinaryResponse uploadFile (MultipartFile file, String filename) {
        try {
            final Map result = cloudinary.uploader().upload(file.getBytes(),Map.of("public_id","lhp03/images"+filename));
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url).build();
        } catch (Exception e) {
            throw new com.doan.AppTuyenDung.Exception.ResourceNotFoundException("Error uploading file");
        }
    }
}
