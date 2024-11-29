package com.doan.AppTuyenDung.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.doan.AppTuyenDung.DTO.Response.OrderPackageResponse;
import com.doan.AppTuyenDung.DTO.Response.PackageResponse;
import com.doan.AppTuyenDung.DTO.Response.UserResponse;
import com.doan.AppTuyenDung.entity.OrderPackage;
import com.doan.AppTuyenDung.entity.PackagePost;
import com.doan.AppTuyenDung.entity.User;

@Mapper(componentModel = "spring")
public interface OrderPackagePostMapper {
	@Mappings({
        @Mapping(target = "packageData", source = "packagePost"),
        @Mapping(target = "userData", source = "user")
    })
	OrderPackageResponse toOrderPackageResponse(OrderPackage postMapper);
	
	@Mappings({
		@Mapping(target = "genderCodeValue", source = "genderCode"),
		@Mapping(target = "addressUser", source = "address"),
		@Mapping(target = "dobUser", source = "dob")
	})
    UserResponse toUserResponse(User user);
	
	PackageResponse toPackageCvResponse(PackagePost packagePost);
}
