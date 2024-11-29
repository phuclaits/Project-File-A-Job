package com.doan.AppTuyenDung.DTO;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cloudinary.utils.ObjectUtils;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudinaryResponse {

    @Autowired
    private CloudinaryResponse cloudinaryResponse;

    private Cloudinary cloudinary;
    private String publicId;

    private String url;
    
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName) {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "public_id", "di9chfwic/image/" + fileName
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return CloudinaryResponse.builder()
                                     .publicId(publicId)
                                     .url(url)
                                     .build();
        } catch (Exception e) {
            throw new com.doan.AppTuyenDung.Exception.ResourceNotFoundException("Error uploading file");
        }
    }


}
