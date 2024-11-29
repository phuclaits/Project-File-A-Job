package com.doan.AppTuyenDung.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.doan.AppTuyenDung.DTO.Response.OrderPackageResponse;
import com.doan.AppTuyenDung.DTO.Response.PackageCvResponse;
import com.doan.AppTuyenDung.DTO.Response.UserResponse;
import com.doan.AppTuyenDung.entity.OrderPackageCv;
import com.doan.AppTuyenDung.entity.PackageCv;
import com.doan.AppTuyenDung.entity.User;

@Mapper(componentModel = "spring")
public interface OrderPackageCvMapper {
	@Mappings({
        @Mapping(target = "packageCvData", source = "packageCv"),
        @Mapping(target = "userData", source = "user")
    })
	OrderPackageResponse toOrderPackageResponse(OrderPackageCv cvMapper);
	
	@Mappings({
		@Mapping(target = "genderCodeValue", source = "genderCode"),
		@Mapping(target = "addressUser", source = "address"),
		@Mapping(target = "dobUser", source = "dob")
	})
    UserResponse toUserResponse(User user);
	
	PackageCvResponse toPackageCvResponse(PackageCv cv);
}
