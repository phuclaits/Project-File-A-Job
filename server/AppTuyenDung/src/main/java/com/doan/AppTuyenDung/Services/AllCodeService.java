package com.doan.AppTuyenDung.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.doan.AppTuyenDung.DTO.CloudinaryResponse;
import com.doan.AppTuyenDung.DTO.GetAllExpTypeAdmin.ExpTypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllExpTypeAdmin.ResponseAllExpType;
import com.doan.AppTuyenDung.DTO.GetAllJobLevelAdmin.JobLevelDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobLevelAdmin.ResponseAllJobLevel;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.JobtypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.ResponseAllJobType;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.ResponseDelete;
import com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin.ResponseAllSalaryType;
import com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin.SalaryTypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllUserAdmin.AccountDTO;
import com.doan.AppTuyenDung.DTO.GetAllWorkTypeAdmin.ResponseAllWorkType;
import com.doan.AppTuyenDung.DTO.GetAllWorkTypeAdmin.WorkTypeDTO;
import com.doan.AppTuyenDung.Repositories.CodeGenderRepository;
import com.doan.AppTuyenDung.Repositories.CodeRuleRepository;
import com.doan.AppTuyenDung.Repositories.SkillRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeExpTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeGenderPostRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeGendersRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeJobLevelRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeJobTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeProvinceRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeRulesRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeSalaryTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeWorkTypeRepository;
import com.doan.AppTuyenDung.entity.CodeExpType;
import com.doan.AppTuyenDung.entity.CodeGender;
import com.doan.AppTuyenDung.entity.CodeGenderPost;
import com.doan.AppTuyenDung.entity.CodeJobLevel;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.CodeProvince;
import com.doan.AppTuyenDung.entity.CodeRule;
import com.doan.AppTuyenDung.entity.CodeSalaryType;
import com.doan.AppTuyenDung.entity.CodeWorkType;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.Skill;
@Service
public class AllCodeService {
    @Autowired
    private CodeExpTypeRepository codeExpTypeRepository;

    @Autowired
    private CodeJobLevelRepository codeJobLevelRepository;

    @Autowired
    private CodeJobTypeRepository codeJobTypeRepository;

    @Autowired
    private CodeProvinceRepository codeProvinceRepository;

    @Autowired
    private CodeSalaryTypeRepository codeSalaryTypeRepository;

    @Autowired
    private CodeWorkTypeRepository codeWorkTypeRepository;

    @Autowired
    private CodeRulesRepository codeRulesRepository;
    
    @Autowired
    private CodeGendersRepository codeGendersRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private CodeGenderPostRepository codeGenderPostRepository;

    public List<CodeGenderPost> getAllGenderPost() {
        return codeGenderPostRepository.findAll();
    }
    // public Page<CodeGenderPost> getAllGenderPost(String search, Pageable pageable) {
    //     return codeGenderPostRepository.GetAllGenderPosts(search, pageable);
    // }
    public List<CodeExpType> getAllExpTypes() {
        return codeExpTypeRepository.findAll();
    }

    public List<CodeJobLevel> getAllJobLevels() {
        return codeJobLevelRepository.findAll();
    }

    public List<CodeJobType> getAllJobTypes() {
        return codeJobTypeRepository.findAll();
    }

    public List<CodeProvince> getAllProvinces() {
        return codeProvinceRepository.findAll();
    }

    public List<CodeSalaryType> getAllSalaryTypes() {
        return codeSalaryTypeRepository.findAll();
    }

    public List<CodeWorkType> getAllWorkTypes() {
        return codeWorkTypeRepository.findAll();
    }

    public List<CodeRule> getAllCodeRules(){
        return codeRulesRepository.findAll();
    }
    public List<CodeGender> getAllCodeGenders(){
        return codeGendersRepository.findAll();
    }
    public List<Skill> getListSkillsById(String id){
        return skillRepository.findByCategoryJobCode(id);
    }

    public Page<JobtypeDTO> GetAllJobType(String search, Pageable pageable) {
        return codeJobTypeRepository.GetAllJobType(search, pageable);
    }
    public Page<JobLevelDTO> GetAllJobLevel(String search, Pageable pageable) {
        return codeJobLevelRepository.GetAllJobLevel(search, pageable);
    }
    public Page<WorkTypeDTO> GetAllWorkType(String search, Pageable pageable) {
        return codeWorkTypeRepository.GetAllWorkType(search, pageable);
    }

    public Page<SalaryTypeDTO> GetAllSalaryType(String search, Pageable pageable) {
        return codeSalaryTypeRepository.GetAllSalaryType(search, pageable);
    }
    public Page<ExpTypeDTO> GetAllExpType(String search, Pageable pageable) {
        return codeExpTypeRepository.GetAllExpType(search, pageable);
    }

    public ResponseEntity<ResponseDelete> handleDeleteAllCode(String code) {
        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(new ResponseDelete(1, "Missing required parameters!"), HttpStatus.BAD_REQUEST);
        }
        Optional<CodeJobType> foundAllCode = codeJobTypeRepository.findById(code);
        if (!foundAllCode.isPresent()) {
            return new ResponseEntity<>(new ResponseDelete(2, "Không tồn tại code"), HttpStatus.NOT_FOUND);
        } else {
            try {
                codeJobTypeRepository.deleteById(code);
                return new ResponseEntity<>(new ResponseDelete(0, "Đã xóa thành công"), HttpStatus.OK);
            } catch (Exception e) {
                if (e.getMessage().contains("foreign key constraint fails")) {
                    return new ResponseEntity<>(new ResponseDelete(3, "Bạn không thể xóa thông tin này vì các dữ liệu khác liên quan"), HttpStatus.CONFLICT);
                }
                throw e; 
            }
        }
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<ResponseAllJobType> getDetailJobTypeByCode(String code){
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseAllJobType(1, "Missing required parameters"));
        }

        Optional<CodeJobType> foundCode = codeJobTypeRepository.findById(code);
        if (foundCode.isPresent()) {
            return ResponseEntity.ok(new ResponseAllJobType(0, "Đã tìm thấy Loại công việc", foundCode.get()));
        } else {
            return ResponseEntity.ok(new ResponseAllJobType(-1, "Không tìm thấy code"));
        }
    }


    public Map<String, Object> handleCreateNewJobType(CodeJobType data, MultipartFile Imagefile) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeJobType> foundCode = codeJobTypeRepository.findById(data.getCode());
        // Optional<CodeJobType> foundCode = codeJobTypeRepository.findByCode(data.getCode());
        if (foundCode.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên loại công việc đã tồn tại");
            return response;
        }

        String imageUrl = "";
        String imageJobType = data.getCode()+generateRandomNumbers(10)+"Images";
        try{
            if (Imagefile != null) {
                try {
                    CloudinaryResponse thumbnailResponse = cloudinaryService.uploadFile(Imagefile,imageJobType);
                    imageUrl = thumbnailResponse.getUrl();
                } catch (Exception e) {
                    response.put("errCode", -1);
                    response.put("errMessage", "Lỗi đường truyền lên Cloud!");
                }
            }
            data.setType(data.getType());
            data.setValue(data.getValue());
            data.setCode(data.getCode());
            data.setImage(imageUrl);
            CodeJobType savedJobType = codeJobTypeRepository.save(data);
            response.put("errCode", 0);
            response.put("errMessage", "Đã tạo Loại công việc thành công");
            response.put("JobType", savedJobType);  
        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "An error occurred");
        }
        return response;
    }


    public Map<String, Object> handleUpdateJobType(CodeJobType data, MultipartFile filethumb) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        Optional<CodeJobType> foundCode = codeJobTypeRepository.findById(data.getCode());
        String imageUrl = "";
        String imageJobType = data.getCode()+generateRandomNumbers(10)+"Images";
        if (foundCode.isPresent()) {
            CodeJobType jType = foundCode.get();
            if (filethumb != null) {
                try {
                    CloudinaryResponse thumbnailResponse = cloudinaryService.uploadFile(filethumb,imageJobType);
                    imageUrl = thumbnailResponse.getUrl();
                } catch (Exception e) {
                    response.put("errCode", -1);
                    response.put("errMessage", "Lỗi đường truyền lên Cloud!");
                }
            }
            data.setType(data.getType());
            data.setValue(data.getValue());
            data.setCode(data.getCode());
            data.setImage(imageUrl);
            codeJobTypeRepository.save(data);
            response.put("errCode", 0);
            response.put("errMessage", "Đã cập nhật thành công!");
            response.put("JobType", data); 
        }else{
            response.put("errCode", 2);
            response.put("errMessage", "Không tìm thấy Loại công việc để cập nhật!");
        }

        return response;
    }
   
    public Map<String, Object> handleCreateNewJobLevel(CodeJobLevel data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeJobLevel> foundCode = codeJobLevelRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên cấp bậc công việc đã tồn tại");
            return response;
        }
        data.setType(data.getType());
        data.setValue(data.getValue());
        data.setCode(data.getCode());
        CodeJobLevel savedJobType = codeJobLevelRepository.save(data);
        response.put("errCode", 0);
        response.put("errMessage", "Đã tạo Loại công việc thành công");
        response.put("JobType", savedJobType);  
        return response;
    }

    public ResponseEntity<ResponseDelete> handleDeleteJobLevel(String code) {
        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(new ResponseDelete(1, "Missing required parameters!"), HttpStatus.BAD_REQUEST);
        }
        Optional<CodeJobLevel> foundAllCode = codeJobLevelRepository.findById(code);
        if (!foundAllCode.isPresent()) {
            return new ResponseEntity<>(new ResponseDelete(2, "Không tồn tại code"), HttpStatus.NOT_FOUND);
        } else {
            try {
                codeJobLevelRepository.deleteById(code);
                return new ResponseEntity<>(new ResponseDelete(0, "Đã xóa thành công"), HttpStatus.OK);
            } catch (Exception e) {
                if (e.getMessage().contains("foreign key constraint fails")) {
                    return new ResponseEntity<>(new ResponseDelete(3, "Bạn không thể xóa thông tin này vì các dữ liệu khác liên quan"), HttpStatus.CONFLICT);
                }
                throw e; 
            }
        }
    }
    public Map<String, Object> handleUpdateJobLevel(CodeJobLevel data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeJobLevel> foundCode = codeJobLevelRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            data.setType(data.getType());
            data.setValue(data.getValue());
            data.setCode(data.getCode());
            data.setImage("");
            codeJobLevelRepository.save(data);
            response.put("errCode", 0);
            response.put("errMessage", "Đã cập nhật thành công!");
            response.put("JobType", data); 
        }else{
            response.put("errCode", 2);
            response.put("errMessage", "Không tìm thấy Cấp Bậc Công Việc để cập nhật!");
        }

        return response;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<ResponseAllJobLevel> getDetailJobLevelByCode(String code){
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseAllJobLevel(1, "Missing required parameters"));
        }

        Optional<CodeJobLevel> foundCode = codeJobLevelRepository.findById(code);
        if (foundCode.isPresent()) {
            return ResponseEntity.ok(new ResponseAllJobLevel(0, "Đã tìm thấy Cấp bậc công việc", foundCode.get()));
        } else {
            return ResponseEntity.ok(new ResponseAllJobLevel(-1, "Không tìm thấy code"));
        }
    }

    // work type
    public Map<String, Object> handleCreateNewWorkType(CodeWorkType data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeWorkType> foundCode = codeWorkTypeRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên hình thức làm việc đã tồn tại");
            return response;
        }
        data.setType(data.getType());
        data.setValue(data.getValue());
        data.setCode(data.getCode());
        CodeWorkType savedWorkType = codeWorkTypeRepository.save(data);
        response.put("errCode", 0);
        response.put("errMessage", "Đã tạo hình thức làm việc thành công");
        response.put("WorkType", savedWorkType);  
        return response;
    }

    public ResponseEntity<ResponseDelete> handleDeleteWorkType(String code) {
        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(new ResponseDelete(1, "Missing required parameters!"), HttpStatus.BAD_REQUEST);
        }
        Optional<CodeWorkType> foundAllCode = codeWorkTypeRepository.findById(code);
        if (!foundAllCode.isPresent()) {
            return new ResponseEntity<>(new ResponseDelete(2, "Không tồn tại code"), HttpStatus.NOT_FOUND);
        } else {
            try {
                codeWorkTypeRepository.deleteById(code);
                return new ResponseEntity<>(new ResponseDelete(0, "Đã xóa thành công"), HttpStatus.OK);
            } catch (Exception e) {
                if (e.getMessage().contains("foreign key constraint fails")) {
                    return new ResponseEntity<>(new ResponseDelete(3, "Bạn không thể xóa thông tin này vì các dữ liệu khác liên quan"), HttpStatus.CONFLICT);
                }
                throw e; 
            }
        }
    }

    public Map<String, Object> handleUpdateWorkType(CodeWorkType data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeWorkType> foundCode = codeWorkTypeRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            data.setType(data.getType());
            data.setValue(data.getValue());
            data.setCode(data.getCode());
            data.setImage("");
            codeWorkTypeRepository.save(data);
            response.put("errCode", 0);
            response.put("errMessage", "Đã cập nhật thành công!");
            response.put("WorkType", data); 
        }else{
            response.put("errCode", 2);
            response.put("errMessage", "Không tìm thấy Cấp Bậc Công Việc để cập nhật!");
        }

        return response;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<ResponseAllWorkType> getDetailWorkTypeByCode(String code){
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseAllWorkType(1, "Missing required parameters"));
        }

        Optional<CodeWorkType> foundCode = codeWorkTypeRepository.findById(code);
        if (foundCode.isPresent()) {
            return ResponseEntity.ok(new ResponseAllWorkType(0, "Đã tìm thấy Hình thức công việc", foundCode.get()));
        } else {
            return ResponseEntity.ok(new ResponseAllWorkType(-1, "Không tìm thấy code"));
        }
    }

    // salary type
    public Map<String, Object> handleCreateNewSalaryType(CodeSalaryType data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeSalaryType> foundCode = codeSalaryTypeRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên khoảng lương đã tồn tại");
            return response;
        }
        data.setType(data.getType());
        data.setValue(data.getValue());
        data.setCode(data.getCode());
        CodeSalaryType savedSalaryType = codeSalaryTypeRepository.save(data);
        response.put("errCode", 0);
        response.put("errMessage", "Đã tạo khoảng lương thành công");
        response.put("SalaryType", savedSalaryType);  
        return response;
    }

    public ResponseEntity<ResponseDelete> handleDeleteSalaryType(String code) {
        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(new ResponseDelete(1, "Missing required parameters!"), HttpStatus.BAD_REQUEST);
        }
        Optional<CodeSalaryType> foundAllCode = codeSalaryTypeRepository.findById(code);
        if (!foundAllCode.isPresent()) {
            return new ResponseEntity<>(new ResponseDelete(2, "Không tồn tại code"), HttpStatus.NOT_FOUND);
        } else {
            try {
                codeSalaryTypeRepository.deleteById(code);
                return new ResponseEntity<>(new ResponseDelete(0, "Đã xóa thành công"), HttpStatus.OK);
            } catch (Exception e) {
                if (e.getMessage().contains("foreign key constraint fails")) {
                    return new ResponseEntity<>(new ResponseDelete(3, "Bạn không thể xóa thông tin này vì các dữ liệu khác liên quan"), HttpStatus.CONFLICT);
                }
                throw e; 
            }
        }
    }

    public Map<String, Object> handleUpdateSalaryType(CodeSalaryType data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeSalaryType> foundCode = codeSalaryTypeRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            data.setType(data.getType());
            data.setValue(data.getValue());
            data.setCode(data.getCode());
            data.setImage("");
            codeSalaryTypeRepository.save(data);
            response.put("errCode", 0);
            response.put("errMessage", "Đã cập nhật thành công!");
            response.put("SalaryType", data); 
        }else{
            response.put("errCode", 2);
            response.put("errMessage", "Không tìm thấy Khoảng lương để cập nhật!");
        }

        return response;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<ResponseAllSalaryType> getDetailSalaryTypeByCode(String code){
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseAllSalaryType(1, "Missing required parameters"));
        }

        Optional<CodeSalaryType> foundCode = codeSalaryTypeRepository.findById(code);
        if (foundCode.isPresent()) {
            return ResponseEntity.ok(new ResponseAllSalaryType(0, "Đã tìm thấy khoảng lương ", foundCode.get()));
        } else {
            return ResponseEntity.ok(new ResponseAllSalaryType(-1, "Không tìm thấy code"));
        }
    }

    // exp
    public Map<String, Object> handleCreateNewExpType(CodeExpType data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeExpType> foundCode = codeExpTypeRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên Kinh Nghiệm đã tồn tại");
            return response;
        }
        data.setType(data.getType());
        data.setValue(data.getValue());
        data.setCode(data.getCode());
        CodeExpType savedExpType = codeExpTypeRepository.save(data);
        response.put("errCode", 0);
        response.put("errMessage", "Đã tạo Kinh Nghiệm thành công");
        response.put("ExpType", savedExpType);  
        return response;
    }

    public Map<String, Object> handleUpdateExpType(CodeExpType data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getType() == null || data.getValue() == null || data.getCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
        Optional<CodeExpType> foundCode = codeExpTypeRepository.findById(data.getCode());
        if (foundCode.isPresent()) {
            data.setType(data.getType());
            data.setValue(data.getValue());
            data.setCode(data.getCode());
            data.setImage("");
            codeExpTypeRepository.save(data);
            response.put("errCode", 0);
            response.put("errMessage", "Đã cập nhật thành công!");
            response.put("ExpType", data); 
        }else{
            response.put("errCode", 2);
            response.put("errMessage", "Không tìm thấy Khoảng lương để cập nhật!");
        }

        return response;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<ResponseAllExpType> getDetailExpTypeByCode(String code){
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseAllExpType(1, "Missing required parameters"));
        }

        Optional<CodeExpType> foundCode = codeExpTypeRepository.findById(code);
        if (foundCode.isPresent()) {
            return ResponseEntity.ok(new ResponseAllExpType(0, "Đã tìm thấy Exp Type ", foundCode.get()));
        } else {
            return ResponseEntity.ok(new ResponseAllExpType(-1, "Không tìm thấy code"));
        }
    }
    public ResponseEntity<ResponseDelete> handleDeleteExpType(String code) {
        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(new ResponseDelete(1, "Missing required parameters!"), HttpStatus.BAD_REQUEST);
        }
        Optional<CodeExpType> foundAllCode = codeExpTypeRepository.findById(code);
        if (!foundAllCode.isPresent()) {
            return new ResponseEntity<>(new ResponseDelete(2, "Không tồn tại code"), HttpStatus.NOT_FOUND);
        } else {
            try {
                codeExpTypeRepository.deleteById(code);
                return new ResponseEntity<>(new ResponseDelete(0, "Đã xóa thành công"), HttpStatus.OK);
            } catch (Exception e) {
                if (e.getMessage().contains("foreign key constraint fails")) {
                    return new ResponseEntity<>(new ResponseDelete(3, "Bạn không thể xóa thông tin này vì các dữ liệu khác liên quan"), HttpStatus.CONFLICT);
                }
                throw e; 
            }
        }
    }


    public static String generateRandomNumbers(int count) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(100); // Số ngẫu nhiên từ 0 đến 99
            stringBuilder.append(randomNumber);
        }

        return stringBuilder.toString();
    }



}
