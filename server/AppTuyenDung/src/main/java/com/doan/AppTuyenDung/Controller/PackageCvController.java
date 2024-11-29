package com.doan.AppTuyenDung.Controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.Services.PackageCvService;


@RestController
@RequestMapping("/package-cv")
public class PackageCvController {
    @Autowired
    private PackageCvService packageCvService;

    @GetMapping("/get-all-package-cv")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public Map<String, Object> getAllPackageCV() {
        return packageCvService.getAllPackageCV();
    }

    @GetMapping("/get-all-package-cv-filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> getAllPackages(@RequestParam(required = false, defaultValue = "10") int limit,@RequestParam(required = false, defaultValue = "0") int offset,@RequestParam(required = false) String search) {
        Map<String, Object> data = new HashMap<>();
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("search", search);
                                                             
        Map<String, Object> result = packageCvService.getAllPackages(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/set-active")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> setActiveTypePackage(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = packageCvService.setActiveTypePackage(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-by-id")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public ResponseEntity<Map<String, Object>> getPackageById(@RequestParam Integer id) {
        Map<String, Object> result = packageCvService.getPackageById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update-package-cv")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> updatePackageCv(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = packageCvService.updatePackageCv(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create-package-cv")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> createNewPackageCv(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = packageCvService.createNewPackageCv(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
