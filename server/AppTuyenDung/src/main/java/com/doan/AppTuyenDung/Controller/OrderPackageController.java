package com.doan.AppTuyenDung.Controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.DTO.Response.OrderPackageResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Services.OrderPackageService;

@RestController
@RequestMapping()
// @PreAuthorize("hasAnyAuthority('ADMIN')")
public class OrderPackageController {
	@Autowired
	OrderPackageService orderPackageService;
	
	@GetMapping("/order-package-cv/sum-price")
	public ApiResponse<Double> sumPriceOrderPackageCv() {
		ApiResponse<Double> apiRs = new ApiResponse<Double>();
		apiRs.setMessage("Tổng giá của đơn hàng xem CV");
		apiRs.setResult(orderPackageService.sumPricePackageCV());
		return apiRs;
	}
	@GetMapping("/order-package-post/sum-price")
	public ApiResponse<Double> sumPriceOrderPackagePost() {
		ApiResponse<Double> apiRs = new ApiResponse<Double>();
		apiRs.setMessage("Tổng giá của đơn hàng đăng bài");
		apiRs.setResult(orderPackageService.sumPricePackagePost());
		return apiRs;
	}
	
	@GetMapping("/order-package-cv/get-history-trade-cv")
	public ApiResponse<Page<OrderPackageResponse>> filterOrderPackageCv(@RequestParam(value = "fromDate", required = false) 
													    @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
													    @RequestParam(value = "toDate", required = false)  
													    @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                        @RequestParam(required = false) String companyId,
                                                        @RequestParam(defaultValue = "0") int offset,
                                                        @RequestParam(defaultValue = "10") int limit) {
		try {
			ApiResponse<Page<OrderPackageResponse>> apiRP = new ApiResponse<Page<OrderPackageResponse>>();
    		Pageable pageable = PageRequest.of(offset, limit);
            Page<OrderPackageResponse> cvRS = orderPackageService.filterOrderPackageCv(fromDate, toDate, companyId, pageable);
            apiRP.setResult(cvRS);
            return apiRP;
    	}
    	catch (Exception e) {
    		System.out.println(e);
    		throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
	}
	@GetMapping("/order-package-post/get-history-trade-post")
	public ApiResponse<Page<OrderPackageResponse>> filterOrderPackagePost(@RequestParam(value = "fromDate", required = false) 
													    @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
													    @RequestParam(value = "toDate", required = false)  
													    @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                        @RequestParam(required = false) String companyId,
                                                        @RequestParam(defaultValue = "0") int offset,
                                                        @RequestParam(defaultValue = "10") int limit) {
		try {
			ApiResponse<Page<OrderPackageResponse>> apiRP = new ApiResponse<Page<OrderPackageResponse>>();
    		Pageable pageable = PageRequest.of(offset, limit);
    		System.out.println(fromDate +" vs " + toDate);
            Page<OrderPackageResponse> cvRS = orderPackageService.filterOrderPackagePost(fromDate, toDate, companyId, pageable);
            apiRP.setResult(cvRS);
            return apiRP;
    	}
    	catch (Exception e) {
    		System.out.println(e);
    		throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
	}
}
