package com.doan.AppTuyenDung.Mapper;

import org.mapstruct.*;

import com.doan.AppTuyenDung.DTO.Response.PostResponse;
import com.doan.AppTuyenDung.DTO.Response.UserResponse;
import com.doan.AppTuyenDung.DTO.Response.postDetailResponse;
import com.doan.AppTuyenDung.entity.CodePostStatus;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.DetailPost;
import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.User;
import com.doan.AppTuyenDung.Repositories.CompanyRepository;

@Mapper(componentModel = "spring")
public interface PostMapper {
    
    @Mappings({
        @Mapping(target = "thumbnail", expression = "java(getThumbnail(post, companyRepository))"),
        @Mapping(target = "postDetailData", source = "detailPost"),
        @Mapping(target = "userPostData", source = "user"),
        @Mapping(target = "statusCode", source = "statusCode"),
        @Mapping(target = "userId", source = "user.id")
    })
    PostResponse toPostResponse(Post post, @Context CompanyRepository companyRepository); 

    default String getThumbnail(Post post, @Context CompanyRepository companyRepository) {
        Company company = companyRepository.findById(post.getUser().getCompanyId()).get();
        return company != null && company.getThumbnail() != null ? company.getThumbnail() : "Chưa có thumbnail";
    }

    default String map(CodePostStatus value) {
        return value != null ? value.getCode() : null;
    }

    @Mappings({
		@Mapping(target = "genderCodeValue", source = "genderCode"),
		@Mapping(target = "addressUser", source = "address"),
		@Mapping(target = "dobUser", source = "dob")
	})
    UserResponse toUserResponse(User user);

    @Mapping(target = "jobTypePostData", source = "categoryJobCode")
    @Mapping(target = "workTypePostData", source = "categoryWorktypeCode")
    @Mapping(target = "salaryTypePostData", source = "salaryJobCode")
    @Mapping(target = "jobLevelPostData", source = "categoryJoblevelCode")
    @Mapping(target = "genderPostData", source = "genderPostCode")
    @Mapping(target = "provincePostData", source = "addressCode")
    @Mapping(target = "expTypePostData", source = "experienceJobCode")
    postDetailResponse toPostDetailResponse(DetailPost detailPost);
}

