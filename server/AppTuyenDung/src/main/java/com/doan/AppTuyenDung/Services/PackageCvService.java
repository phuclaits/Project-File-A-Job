package com.doan.AppTuyenDung.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.Repositories.PackageCvRepository;
import com.doan.AppTuyenDung.entity.PackageCv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PackageCvService {
    @Autowired
    private PackageCvRepository packageCvRepository;

    public Map<String, Object> getAllPackageCV() {
        Map<String, Object> response = new HashMap<>();
        List<PackageCv> packageCvs = packageCvRepository.findByIsActiveTrue();

        response.put("errCode", 0);
        response.put("data", packageCvs);
        return response;
    }

    public Map<String, Object> getAllPackages(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer limit = (Integer) data.get("limit");
            Integer offset = (Integer) data.get("offset");
            String search = (String) data.get("search");

            if (limit == null || offset == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            Pageable pageable = PageRequest.of(offset / limit, limit);

            Page<PackageCv> packageCvs = (search == null || search.isEmpty())
                ? packageCvRepository.findAll(pageable)
                : packageCvRepository.findByNameContaining(search, pageable);

            response.put("errCode", 0);
            response.put("data", packageCvs.getContent());
            response.put("count", packageCvs.getTotalElements());
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> setActiveTypePackage(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra các tham số
            Integer id = data.containsKey("id") ? Integer.valueOf(data.get("id").toString()) : null;
            Integer isActive = data.containsKey("isActive") ? Integer.valueOf(data.get("isActive").toString()) : null;

            if (id == null || isActive == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Tìm kiếm gói sản phẩm
            Optional<PackageCv> packageCvOptional = packageCvRepository.findById(id);
            if (!packageCvOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Gói sản phẩm không tồn tại");
                return response;
            }

            // Cập nhật trạng thái isActive
            PackageCv packageCv = packageCvOptional.get();
            packageCv.setIsActive(isActive);
            packageCvRepository.save(packageCv);

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

            // Tìm PackageCv theo ID
            Optional<PackageCv> packageCvOptional = packageCvRepository.findById(id);

            if (packageCvOptional.isPresent()) {
                response.put("errCode", 0);
                response.put("data", packageCvOptional.get());
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


    public Map<String, Object> updatePackageCv(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra tham số
            Integer id = data.containsKey("id") ? Integer.valueOf(data.get("id").toString()) : null;
            String name = (String) data.get("name");
            Double price = data.containsKey("price") ? Double.valueOf(data.get("price").toString()) : null;
            String value = (String) data.get("value");

            if (id == null || name == null || price == null || value == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Tìm gói sản phẩm theo ID
            Optional<PackageCv> packageCvOptional = packageCvRepository.findById(id);
            if (!packageCvOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Cập nhật thất bại");
                return response;
            }

            PackageCv packageCv = packageCvOptional.get();

            // Kiểm tra nếu tên gói đã tồn tại
            if (packageCvRepository.existsByNameAndIdNot(name, id)) {
                response.put("errCode", 2);
                response.put("errMessage", "Tên gói sản phẩm đã tồn tại");
                return response;
            }

            // Cập nhật dữ liệu
            packageCv.setName(name);
            packageCv.setPrice(price);
            packageCv.setValue(value);

            packageCvRepository.save(packageCv);

            response.put("errCode", 0);
            response.put("errMessage", "Cập nhật thành công");
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }
    public Map<String, Object> createNewPackageCv(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra tham số
            String name = (String) data.get("name");
            Double price = data.containsKey("price") ? Double.valueOf(data.get("price").toString()) : null;
            String value =  (String) data.get("value");

            if (name == null || price == null || value == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Kiểm tra nếu tên gói đã tồn tại
            if (packageCvRepository.existsByName(name)) {
                response.put("errCode", 2);
                response.put("errMessage", "Tên gói sản phẩm đã tồn tại");
                return response;
            }

            // Tạo mới gói sản phẩm
            PackageCv newPackage = new PackageCv();
            newPackage.setName(name);
            newPackage.setPrice(price);
            newPackage.setValue(value);
            newPackage.setIsActive(1); 

            packageCvRepository.save(newPackage);

            response.put("errCode", 0);
            response.put("errMessage", "Tạo gói sản phẩm thành công");
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "An error occurred: " + e.getMessage());
        }

        return response;
    }
}
