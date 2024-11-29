package com.doan.AppTuyenDung.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.entity.*;
import com.doan.AppTuyenDung.Services.AllCodeService;

@RestController
@RequestMapping("/get-all-code")
public class AllcodeController {
    @Autowired
    private AllCodeService codeService;    


    @GetMapping("/exp-types")
    public ResponseEntity<List<CodeExpType>> getExpTypes() {
        return ResponseEntity.ok(codeService.getAllExpTypes());
    }

    @GetMapping("/job-levels")
    public ResponseEntity<List<CodeJobLevel>> getJobLevels() {
        return ResponseEntity.ok(codeService.getAllJobLevels());
    }

    @GetMapping("/job-types")
    public ResponseEntity<List<CodeJobType>> getJobTypes() {
        return ResponseEntity.ok(codeService.getAllJobTypes());
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<CodeProvince>> getProvinces() {
        return ResponseEntity.ok(codeService.getAllProvinces());
    }

    @GetMapping("/salary-types")
    public ResponseEntity<List<CodeSalaryType>> getSalaryTypes() {
        return ResponseEntity.ok(codeService.getAllSalaryTypes());
    }

    @GetMapping("/work-types")
    public ResponseEntity<List<CodeWorkType>> getWorkTypes() {
        return ResponseEntity.ok(codeService.getAllWorkTypes());
    }
    @GetMapping("/rules-user")
    public ResponseEntity<List<CodeRule>> getCodeRules() {
        return ResponseEntity.ok(codeService.getAllCodeRules());
    }
    @GetMapping("/genders-user")
    public ResponseEntity<List<CodeGender>> getCodeGenders() {
        return ResponseEntity.ok(codeService.getAllCodeGenders());
    }

    @GetMapping("/genders-post")
    public ResponseEntity<List<CodeGenderPost>> getCodeGenderPosts() {
        return ResponseEntity.ok(codeService.getAllGenderPost());
    }
    

}
