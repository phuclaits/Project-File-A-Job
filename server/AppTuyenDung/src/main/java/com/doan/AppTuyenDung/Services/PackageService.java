package com.doan.AppTuyenDung.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.DTO.Response.PackageCvResponse;
import com.doan.AppTuyenDung.Repositories.OrderPackageCvRepository;
import com.doan.AppTuyenDung.Repositories.PackageCvRepository;
import com.doan.AppTuyenDung.Repositories.PackagePostRepository;
import com.doan.AppTuyenDung.entity.PackageCv;
import com.doan.AppTuyenDung.entity.PackagePost;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class PackageService {
     @Autowired
    private PackageCvRepository packageCvRepository;

    @Autowired
    private OrderPackageCvRepository orderPackageCvRepository;

    @Autowired
    private PackagePostRepository packagePostRepository;

    public Map<String, Object> getStatisticalPackage(String fromDate, String toDate, Integer limit, Integer offset) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Kiểm tra các tham số từ ngày và đến ngày
            if (fromDate == null || toDate == null) {
                result.put("errCode", 1);
                result.put("errMessage", "Missing required parameters!");
                return result;
            }

            // Phân trang
            Pageable pageable = PageRequest.of(offset != null ? offset : 0, limit != null ? limit : Integer.MAX_VALUE);

            // Lấy danh sách PackageCv với phân trang
            Page<PackageCv> listPackage = packageCvRepository.findAll(pageable);

            // Lấy danh sách OrderPackageCV với điều kiện thời gian và nhóm
            List<Object[]> listOrderPackage = orderPackageCvRepository.findOrderStatistics(fromDate, toDate);

            // Tính tổng số tiền
            double sum = listOrderPackage.stream()
                .mapToDouble(row -> (Double) row[2])
                .sum();

            // Cập nhật thông tin số lượng và tổng cho từng PackageCv
            List<PackageCvResponse> responseList = listPackage.getContent().stream()
                .map(packageCv -> {
                    PackageCvResponse response = new PackageCvResponse();
                    response.setId(packageCv.getId());
                    response.setName(packageCv.getClass().getName());
                    response.setValue(packageCv.getValue());
                    response.setPrice(packageCv.getPrice());
                    // response.setIsHot(packageCv.getIsHot());
                    response.setIsActive(packageCv.getIsActive());
                    response.setCount(0);
                    response.setTotal(0.0);

                    for (Object[] order : listOrderPackage) {
                        Long packageId = ((Number) order[0]).longValue();
                        if (packageId.equals(packageCv.getId())) {
                            response.setCount(((Number) order[1]).intValue());
                            response.setTotal(((Double) order[2]));
                            break;
                        }
                    }

                    return response;
                })
                .collect(Collectors.toList());

            result.put("errCode", 0);
            result.put("data", responseList);
            result.put("count", listPackage.getTotalElements());
            result.put("sum", sum);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("errCode", 1);
            result.put("errMessage", "Error occurred while processing data.");
        }
        return result;
    }

    public Map<String, Object> getPackageByType(Integer isHot) {
        Map<String, Object> response = new HashMap<>();
    
        if (isHot == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        List<PackagePost> packagePosts = packagePostRepository.findByIsHotAndIsActive(isHot != 0, true);
        
        response.put("errCode", 0);
        response.put("data", packagePosts);
        return response;
    }

    public Map<String, Object> getPackageById(Integer id) {
        Map<String, Object> response = new HashMap<>();
        if (id == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters !");
            return response;
        }
        Optional<PackagePost> packagePost = packagePostRepository.findById(id);
        if (packagePost.isPresent()) {
            response.put("errCode", 0);
            response.put("data", packagePost.get());
        } else {
            response.put("errCode", 0);
            response.put("errMessage", "Không tìm thấy dữ liệu gói sản phẩm");
        }
        return response;
    }
}
