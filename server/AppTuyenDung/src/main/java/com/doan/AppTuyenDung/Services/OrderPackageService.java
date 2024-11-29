package com.doan.AppTuyenDung.Services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.DTO.Response.OrderPackageResponse;
import com.doan.AppTuyenDung.DTO.Response.PostResponse;
import com.doan.AppTuyenDung.Mapper.OrderPackageCvMapper;
import com.doan.AppTuyenDung.Mapper.OrderPackagePostMapper;
import com.doan.AppTuyenDung.Repositories.OrderPackageCvRepository;
import com.doan.AppTuyenDung.Repositories.OrderPackageRepository;
import com.doan.AppTuyenDung.Repositories.Specification.OrderPackageSpecification;
import com.doan.AppTuyenDung.Repositories.Specification.PostSpecification;
import com.doan.AppTuyenDung.entity.OrderPackage;
import com.doan.AppTuyenDung.entity.OrderPackageCv;
import com.doan.AppTuyenDung.entity.Post;

@Service
public class OrderPackageService {
	@Autowired
	OrderPackageCvRepository orderPackageCvRepository;
	@Autowired
	OrderPackageRepository orderPackageRepository;
	@Autowired 
	OrderPackageCvMapper cvMapper;
	@Autowired
	OrderPackagePostMapper postMapper;
	public Double sumPricePackageCV() {
		return orderPackageCvRepository.selectPrice();
	}
	public Double sumPricePackagePost() {
		return orderPackageRepository.selectPrice();
	}
	public Page<OrderPackageResponse> filterOrderPackageCv(Date fromDate, Date toDate, String companyId, Pageable pageable) {
            Specification<OrderPackageCv> spec = OrderPackageSpecification.filterOrderPackageCv( fromDate, toDate, companyId);
            Page<OrderPackageResponse> pageRs = mapOrderPackageCvResponsePage(orderPackageCvRepository.findAll(spec, pageable));
        return pageRs;
        }
    public Page<OrderPackageResponse> mapOrderPackageCvResponsePage(Page<OrderPackageCv> ordePage) {
        List<OrderPackageResponse> orderPackageResponses = ordePage.getContent().stream()
            .map(orderPackageCv -> mapToOrderPackageCvByMapper(orderPackageCv))
            .collect(Collectors.toList());

        return new PageImpl<>(orderPackageResponses, ordePage.getPageable(), ordePage.getTotalElements());
	}
    public OrderPackageResponse mapToOrderPackageCvByMapper(OrderPackageCv orderPackageCv) {
        if (orderPackageCv == null) {
            return null;
        }
        return cvMapper.toOrderPackageResponse(orderPackageCv );
    }
    
    
    
    public Page<OrderPackageResponse> filterOrderPackagePost(Date fromDate, Date toDate, String companyId, Pageable pageable) {
        Specification<OrderPackage> spec = OrderPackageSpecification.filterOrderPackagePost( fromDate, toDate, companyId);
        Page<OrderPackageResponse> pageRs = mapOrderPackagePostResponsePage(orderPackageRepository.findAll(spec, pageable));
    	System.out.println(pageRs.getContent().getClass().descriptorString());
        return pageRs;
    }
	public Page<OrderPackageResponse> mapOrderPackagePostResponsePage(Page<OrderPackage> ordePage) {
	    List<OrderPackageResponse> orderPackageResponses = ordePage.getContent().stream()
	        .map(orderPackagePost -> mapToOrderPackagePostByMapper(orderPackagePost))
	        .collect(Collectors.toList());
	
	    return new PageImpl<>(orderPackageResponses, ordePage.getPageable(), ordePage.getTotalElements());
	}
	public OrderPackageResponse mapToOrderPackagePostByMapper(OrderPackage orderPackagePost) {
	    if (orderPackagePost == null) {
        	System.out.println("Object null");
	    }
	    System.out.println(orderPackagePost.getCreatedAt());
	    return postMapper.toOrderPackageResponse(orderPackagePost );
	}
}
