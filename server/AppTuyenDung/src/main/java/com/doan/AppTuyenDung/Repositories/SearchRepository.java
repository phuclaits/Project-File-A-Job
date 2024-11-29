package com.doan.AppTuyenDung.Repositories;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.doan.AppTuyenDung.DTO.DetailPostDTO;
import com.doan.AppTuyenDung.DTO.Response.PageResponse;
import com.doan.AppTuyenDung.DTO.Response.PostJobTypeCountDTO;
import com.doan.AppTuyenDung.Repositories.criteria.PostSearchQueryCriteriaConsumer;
import com.doan.AppTuyenDung.Repositories.criteria.SearchCriteria;

import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.DetailPost;
import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.User;

import org.springframework.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import static com.doan.AppTuyenDung.utils.AppConst.*;
@Component
@Slf4j
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String LIKE_FORMAT = "%%%s%%";

    List<SearchCriteria> criteriaList = new ArrayList<>();
    List<PostJobTypeCountDTO> criteriaListPostJob = new ArrayList<>(); 

    // public PageResponse advanceSearchDetailPost(int pageNo,int pageSize,String sortBy, Integer isHot,String... search)
    // {

    //     List<SearchCriteria> criteriaList = new ArrayList<>();
    //     if(search != null)
    //     {
    //         for (String s : search) {
    //             Pattern pattern = Pattern.compile(SEARCH_OPERATOR);
    //             Matcher matcher = pattern.matcher(s);
    //             if (matcher.find()) {
    //                 criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
    //             }
    //         }
    //     }

    //     List<DetailPost> detailPosts = getDetailPosts(pageNo,pageSize,criteriaList,sortBy,isHot);
    //     Long totalElements = 1l;

        

    //     return PageResponse.builder()
    //             .page(pageNo)
    //             .size(pageSize)
    //             .total(0)
    //             .items(detailPosts)
    //             .build();
    // }


    // private List<DetailPost> getDetailPosts(int pageNo, int pageSize,List<SearchCriteria> criteriaList, String sortBy, Integer isHot) {
    //     CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    //     CriteriaQuery<DetailPost> query = criteriaBuilder.createQuery(DetailPost.class);
    //     Root<DetailPost> root = query.from(DetailPost.class);

    //     // xử lý dk tìm
    //     Predicate  predicate = criteriaBuilder.conjunction();
    //     PostSearchQueryCriteriaConsumer queryConsumer = new PostSearchQueryCriteriaConsumer(criteriaBuilder,predicate,root);


    //     if(isHot!=null)
    //     {
    //         Join<DetailPost,Post> djoin = root.join("posts");
    //         // Predicate postPredicate = criteriaBuilder.like(djoin.get("isHot"), isHot);
    //         Predicate postPredicate = criteriaBuilder.equal(djoin.get("isHot"), isHot);
    //         query.where(predicate, postPredicate);
    //     }
    //     else{
    //         criteriaList.forEach(queryConsumer);
    //         predicate = queryConsumer.getPredicate();
    //         query.where(predicate);
    //     }
    //     if(StringUtils.hasLength(sortBy))
    //     {
    //         Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
    //             Matcher matcher = pattern.matcher(sortBy);
    //             if (matcher.find()) {
    //                 String columnName = matcher.group(1);
    //                 if(matcher.group(3).equalsIgnoreCase("desc")){
    //                     query.orderBy(criteriaBuilder.desc(root.get(columnName)));
    //                 }
    //                 else{
    //                 query.orderBy(criteriaBuilder.asc(root.get(columnName)));
    //                 }
    //             }
    //     }        
    //     //excute
    //     return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();

    // }
    // public PageResponse searchPostJobTypePost(int pageNo , int pageSize)
    // {   
    //     List<PostJobTypeCountDTO> postJobCounts = getPostJobTypeAndCountPost(pageNo, pageSize );
        
    //     return PageResponse.builder()
    //             .page(pageNo)
    //             .size(pageSize)
    //             .total(0)
    //             .items(postJobCounts)
    //             .build();
    // }

    // private List<PostJobTypeCountDTO> getPostJobTypeAndCountPost(int pageNo, int pageSize) {
    //             CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    //             CriteriaQuery<PostJobTypeCountDTO> query = criteriaBuilder.createQuery(PostJobTypeCountDTO.class);
    //             Root<Post> root = query.from(Post.class);
    //             Root<DetailPost> mapping = query.from(DetailPost.class);
    //             Predicate  predicate = criteriaBuilder.conjunction();
    //                 Join<Post,Allcodes> djoin = root.join("statusCode");
    //                 Join<Post, DetailPost> allcodesJoin = djoin.join("detailPost");
    //                 // Predicate postPredicate = criteriaBuilder.like(djoin.get("isHot"), isHot);
    //                 Predicate postPredicate = criteriaBuilder.equal(allcodesJoin.get("code"), "PS1");
    //                 query.where(predicate, postPredicate);  
    //             //excute
    //             // List<PostJobTypeCountDTO> rs = entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList()
    //             return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    // }


    // public Page<PostJobTypeCountDTO> getPostJobTypeAndCountPost(Pageable pageable) {
    //     CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    //     CriteriaQuery<PostJobTypeCountDTO> criteriaQuery = criteriaBuilder.createQuery(PostJobTypeCountDTO.class);
    //     Root<Post> postRoot = criteriaQuery.from(Post.class);
    //     Join<Post, DetailPost> detailPostJoin = postRoot.join("detailPost");
    //     Join<Post, Allcodes> allcodesJoin = detailPostJoin.join("categoryJobCode");

    //     // Xây dựng query
    //     criteriaQuery.select(criteriaBuilder.construct(
    //             PostJobTypeCountDTO.class,
    //             detailPostJoin.get("categoryJobCode"),
    //             criteriaBuilder.count(detailPostJoin.get("categoryJobCode")),
    //             allcodesJoin.get("value"),
    //             allcodesJoin.get("code"),
    //             allcodesJoin.get("image")
    //     ))
    //     .where(criteriaBuilder.equal(postRoot.get("statusCode"), "PS1"))
    //     .groupBy(
    //             detailPostJoin.get("categoryJobCode"),
    //             allcodesJoin.get("value"),
    //             allcodesJoin.get("code"),
    //             allcodesJoin.get("image")
    //     )
    //     .orderBy(criteriaBuilder.desc(criteriaBuilder.count(detailPostJoin.get("categoryJobCode"))));

    //     TypedQuery<PostJobTypeCountDTO> query = entityManager.createQuery(criteriaQuery);

    //     // Phân trang
    //     query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
    //     query.setMaxResults(pageable.getPageSize());

    //     List<PostJobTypeCountDTO> results = query.getResultList();

    //     // Đếm tổng số kết quả (cần thiết cho phân trang)
    //     CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    //     Root<Post> countRoot = countQuery.from(Post.class);
    //     countQuery.select(criteriaBuilder.count(countRoot));
    //     Long count = entityManager.createQuery(countQuery).getSingleResult();

    //     return new PageImpl<>(results, pageable, count);
    // }
    
    // public Page<PostJobTypeCountDTO> searchPostJobTypeAndCountPost(Pageable pageable, List<SearchCriteria> criteriaList) {
    //     CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    //     CriteriaQuery<PostJobTypeCountDTO> criteriaQuery = criteriaBuilder.createQuery(PostJobTypeCountDTO.class);
    //     Root<Post> postRoot = criteriaQuery.from(Post.class);
    //     Join<Post, DetailPost> detailPostJoin = postRoot.join("detailPost");
    //     Join<Post, Allcodes> allcodesJoin = detailPostJoin.join("statusCode");

    //     // Xây dựng điều kiện tìm kiếm
    //     Predicate predicate = criteriaBuilder.conjunction();
    //     for (SearchCriteria criteria : criteriaList) {
    //         if ("categoryJobCode".equals(criteria.getKey())) {
    //             predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(detailPostJoin.get("categoryJobCode"), criteria.getValue()));
    //         }
    //         // Thêm các điều kiện tìm kiếm khác nếu cần
    //     }

    //     criteriaQuery.select(criteriaBuilder.construct(
    //             PostJobTypeCountDTO.class,
    //             detailPostJoin.get("categoryJobCode"),
    //             criteriaBuilder.count(detailPostJoin.get("categoryJobCode")),
    //             allcodesJoin.get("value"),
    //             allcodesJoin.get("code"),
    //             allcodesJoin.get("image")
    //     ))
    //     .where(predicate, criteriaBuilder.equal(postRoot.get("statusCode"), "PS1"))
    //     .groupBy(
    //             detailPostJoin.get("category_Job_Code"),
    //             allcodesJoin.get("value"),
    //             allcodesJoin.get("code"),
    //             allcodesJoin.get("image")
    //     )
    //     .orderBy(criteriaBuilder.desc(criteriaBuilder.count(detailPostJoin.get("categoryJobCode"))));

    //     TypedQuery<PostJobTypeCountDTO> query = entityManager.createQuery(criteriaQuery);

    //     // Phân trang
    //     query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
    //     query.setMaxResults(pageable.getPageSize());

    //     List<PostJobTypeCountDTO> results = query.getResultList();

    //     // Đếm tổng số kết quả (cần thiết cho phân trang)
    //     CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
    //     Root<Post> countRoot = countQuery.from(Post.class);
    //     countQuery.select(criteriaBuilder.count(countRoot));
    //     Long count = entityManager.createQuery(countQuery).getSingleResult();

    //     return new PageImpl<>(results, pageable, count);
    // }


    // public Page<DetailPostDTO> findFilteredPosts(String categoryJobCode, String addressCode, String search, 
    //                                               List<String> experienceJobCodes, List<String> categoryWorktypeCodes,
    //                                               List<String> salaryJobCodes, List<String> categoryJoblevelCodes,
    //                                               Integer isHot, Pageable pageable) {

    //     CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    //     CriteriaQuery<DetailPostDTO> cq = cb.createQuery(DetailPostDTO.class);
    //     Root<Post> post = cq.from(Post.class);
    //     Join<Post, User> user = post.join("user");
    //     Join<User, Company> company = user.join("company");
    //     Join<Post, DetailPost> detailPost = post.join("detailPost");

    //     Join<DetailPost, Allcodes> ac1 = detailPost.join("categoryJobCode", JoinType.LEFT);
    //     Join<DetailPost, Allcodes> ac2 = detailPost.join("categoryJoblevelCode", JoinType.LEFT);
    //     Join<DetailPost, Allcodes> ac3 = detailPost.join("categoryWorktypeCode", JoinType.LEFT);
    //     Join<DetailPost, Allcodes> ac4 = detailPost.join("experienceJobCode", JoinType.LEFT);
    //     Join<DetailPost, Allcodes> ac5 = detailPost.join("genderPostCode", JoinType.LEFT);

    //     cq.multiselect(
    //             detailPost.get("id").alias("id"),
    //             ac1.get("value").alias("CategoryJobCodeValue"),
    //             ac2.get("value").alias("CategoryJobLevelCodeValue"),
    //             ac3.get("value").alias("CategoryWorktypeCodeValue"),
    //             ac4.get("value").alias("ExperienceJobCodeValue"),
    //             ac5.get("value").alias("GenderPostCodeValue"),
    //             company.get("thumbnail").alias("CompanyThmbnailValue"),
    //             detailPost.get("addressCode").alias("AddressCodeValue")
    //     );

    //     Predicate predicate = cb.conjunction();

    //     if (categoryJobCode != null) {
    //         predicate = cb.and(predicate, cb.equal(detailPost.get("categoryJobCode"), categoryJobCode));
    //     }

    //     if (addressCode != null) {
    //         predicate = cb.and(predicate, cb.equal(detailPost.get("addressCode"), addressCode));
    //     }

    //     if (search != null) {
    //         predicate = cb.and(predicate, cb.like(detailPost.get("name"), "%" + search + "%"));
    //     }

    //     if (experienceJobCodes != null && !experienceJobCodes.isEmpty()) {
    //         predicate = cb.and(predicate, detailPost.get("experienceJobCode").in(experienceJobCodes));
    //     }

    //     if (categoryWorktypeCodes != null && !categoryWorktypeCodes.isEmpty()) {
    //         predicate = cb.and(predicate, detailPost.get("categoryWorktypeCode").in(categoryWorktypeCodes));
    //     }

    //     if (salaryJobCodes != null && !salaryJobCodes.isEmpty()) {
    //         predicate = cb.and(predicate, detailPost.get("salaryJobCode").in(salaryJobCodes));
    //     }

    //     if (categoryJoblevelCodes != null && !categoryJoblevelCodes.isEmpty()) {
    //         predicate = cb.and(predicate, detailPost.get("categoryJoblevelCode").in(categoryJoblevelCodes));
    //     }

    //     if (isHot != null) {
    //         predicate = cb.and(predicate, cb.equal(post.get("isHot"), isHot));
    //     }

    //     cq.where(predicate);
    //     cq.orderBy(cb.desc(post.get("timePost")));

    //     TypedQuery<DetailPostDTO> query = entityManager.createQuery(cq);
    //     int totalRows = query.getResultList().size(); // For pagination, you need to get the total number of rows.
    //     query.setFirstResult((int)pageable.getOffset());
    //     query.setMaxResults(pageable.getPageSize());

    //     List<DetailPostDTO> result = query.getResultList();
    //     return new PageImpl<>(result, pageable, totalRows);
    // }


}



    // public PageResponse getFilterPost(Integer limit, Integer offset,String sortBy, String... search)
    // {   
    //     if(search != null)
    //     {
    //         for(String s : search)
    //         {
    //             Pattern pattern = Pattern.compile(SEARCH_OPERATOR);
    //             Matcher matcher = pattern.matcher(s);

    //             if(matcher.find())
    //             {
    //                 criteriaList.add(new SearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3)));
    //             }
    //         }
    //     }

    //     List<Post> posts =getPosts(limit, offset,criteriaList,sortBy);
        

    //     return PageResponse.builder().page(offset).size(limit).total(0).items(posts).build();
    // }

    // private List<Post> getPosts(int offset, int pageSize, List<SearchCriteria> criteriaList, String detailPost, String sortBy) {
        
    //      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    //     CriteriaQuery<Post> query = criteriaBuilder.createQuery(Post.class);
    //     Root<Post> PostRoot = query.from(Post.class);

    //     Predicate PostPredicate = criteriaBuilder.conjunction();
    //     PostSearchQueryCriteriaConsumer searchConsumer = new PostSearchQueryCriteriaConsumer(PostPredicate, criteriaBuilder, PostRoot);

    //     if (StringUtils.hasLength(detailPost)) {
    //         Join<DetailPost, Post> userAddressJoin = PostRoot.join("detailPost");
    //         Predicate DetailPostPredicate = criteriaBuilder.equal(userAddressJoin.get("id"), id);
    //         query.where(PostPredicate, DetailPostPredicate);
    //     } else {
    //         criteriaList.forEach(searchConsumer);
    //         userPredicate = searchConsumer.getPredicate();
    //         query.where(userPredicate);
    //     }

    //     Pattern pattern = Pattern.compile(SORT_BY);
    //     if (StringUtils.hasLength(sortBy)) {
    //         Matcher matcher = pattern.matcher(sortBy);
    //         if (matcher.find()) {
    //             String fieldName = matcher.group(1);
    //             String direction = matcher.group(3);
    //             if (direction.equalsIgnoreCase("asc")) {
    //                 query.orderBy(criteriaBuilder.asc(userRoot.get(fieldName)));
    //             } else {
    //                 query.orderBy(criteriaBuilder.desc(userRoot.get(fieldName)));
    //             }
    //         }
    //     }

    //     return entityManager.createQuery(query)
    //             .setFirstResult(offset)
    //             .setMaxResults(pageSize)
    //             .getResultList();

    // }

    // public PageResponse<?> searchPostByCriteria(int offset, int pageSize, String sortBy, String detailPost, String... search) {
        
    //     List<SearchCriteria> criteriaList = new ArrayList<>();

    //     if (search.length > 0) {
    //         Pattern pattern = Pattern.compile(SEARCH_OPERATOR);
    //         for (String s : search) {
    //             Matcher matcher = pattern.matcher(s);
    //             if (matcher.find()) {
    //                 criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
    //             }
    //         }
    //     }

    //     if (StringUtils.hasLength(sortBy)) {
    //         Pattern pattern = Pattern.compile(SORT_BY);
    //         for (String s : search) {
    //             Matcher matcher = pattern.matcher(s);
    //             if (matcher.find()) {
    //                 criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
    //             }
    //         }
    //     }

    //     List<Post> users = getPosts(offset, pageSize, criteriaList, detailPost, sortBy);

    //     Long totalElements = getTotalElements(criteriaList);

    //     Page<User> page = new PageImpl<>(users, PageRequest.of(offset, pageSize), totalElements);

    //     return PageResponse.builder()
    //             .page(offset)
    //             .size(pageSize)
    //             .total(page.getTotalPages())
    //             .items(users)
    //             .build();
    // }

// }
