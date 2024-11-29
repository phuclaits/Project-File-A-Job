package com.doan.AppTuyenDung.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.Repositories.PackagePostRepository;
import com.doan.AppTuyenDung.entity.PackagePost;

@Service
public class PackagePostService {
    @Autowired
    private PackagePostRepository packagePostRepository;

    public Map<String, Object> getAllPackages(int limit, int offset, String search) {
        Map<String, Object> response = new HashMap<>();

        try {
            
            Pageable pageable = PageRequest.of(offset / limit, limit);


            Page<PackagePost> packagePosts = (search == null || search.isEmpty())
                ? packagePostRepository.findAll(pageable)
                : packagePostRepository.findByNameContaining(search, pageable);

            response.put("errCode", 0);
            response.put("data", packagePosts.getContent());
            response.put("count", packagePosts.getTotalElements());
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }
    public Map<String, Object> setActiveTypePackage(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra tham số
            Integer id = data.containsKey("id") ? Integer.valueOf(data.get("id").toString()) : null;
            Integer isActive = data.containsKey("isActive") ? Integer.valueOf(data.get("isActive").toString()) : null;
            
            if (id == null || isActive == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }
            boolean ConvertedActive;
            if(isActive == 1)
            {
                ConvertedActive = true;
            }
            else{
                ConvertedActive = false;
            }
            // Tìm gói sản phẩm theo ID
            Optional<PackagePost> packagePostOptional = packagePostRepository.findById(id);
            if (!packagePostOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Gói sản phẩm không tồn tại");
                return response;
            }

            PackagePost packagePost = packagePostOptional.get();
            packagePost.setIsActive(ConvertedActive);

            packagePostRepository.save(packagePost);

            response.put("errCode", 0);
            response.put("errMessage", isActive == 0 ? "Gói sản phẩm đã ngừng kích hoạt" : "Gói sản phẩm đã hoạt động");
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }


    public Map<String, Object> getPackageById(Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (id == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Tìm gói sản phẩm theo ID
            Optional<PackagePost> packagePostOptional = packagePostRepository.findById(id);
            if (packagePostOptional.isPresent()) {
                response.put("errCode", 0);
                response.put("data", packagePostOptional.get());
            } else {
                response.put("errCode", 0);
                response.put("errMessage", "Không tìm thấy dữ liệu gói sản phẩm");
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> updatePackagePost(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer id = data.containsKey("id") ? Integer.valueOf(data.get("id").toString()) : null;
            String name = (String) data.get("name");
            Double price = data.containsKey("price") ? Double.valueOf(data.get("price").toString()) : null;
            String value = (String) data.get("value");
            Boolean isHot = data.containsKey("isHot") ? Boolean.valueOf(data.get("isHot").toString()) : null;

            if (id == null || name == null || price == null || value == null || isHot == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            Optional<PackagePost> packagePostOptional = packagePostRepository.findById(id);
            if (!packagePostOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Cập nhật thất bại");
                return response;
            }
            PackagePost packagePost = packagePostOptional.get();
            if (packagePostRepository.existsByNameAndIdNot(name, id)) {
                response.put("errCode", 2);
                response.put("errMessage", "Tên gói sản phẩm đã tồn tại");
                return response;
            }

            // Cập nhật dữ liệu
            packagePost.setName(name);
            packagePost.setPrice(price);
            packagePost.setValue(value);
            packagePost.setIsHot(isHot);

            packagePostRepository.save(packagePost);

            response.put("errCode", 0);
            response.put("errMessage", "Cập nhật thành công");
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }
    public Map<String, Object> createNewPackagePost(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            String name = (String) data.get("name");
            Double price = data.containsKey("price") ? Double.valueOf(data.get("price").toString()) : null;
            String value = (String) data.get("value");
            Boolean isHot = data.containsKey("isHot") ? Boolean.valueOf(data.get("isHot").toString()) : null;

            if (name == null || price == null || value == null || isHot == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }
            if (packagePostRepository.existsByName(name)) {
                response.put("errCode", 2);
                response.put("errMessage", "Tên gói sản phẩm đã tồn tại");
                return response;
            }
            
            // Tạo mới gói sản phẩm
            PackagePost newPackagePost = new PackagePost();
            newPackagePost.setName(name);
            newPackagePost.setPrice(price);
            newPackagePost.setValue(value);
            newPackagePost.setIsHot(isHot);
            newPackagePost.setIsActive(true); 

            packagePostRepository.save(newPackagePost);

            response.put("errCode", 0);
            response.put("errMessage", "Tạo gói sản phẩm thành công");
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }
}
