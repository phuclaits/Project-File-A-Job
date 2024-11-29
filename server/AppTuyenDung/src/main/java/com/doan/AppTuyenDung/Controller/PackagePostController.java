package com.doan.AppTuyenDung.Controller;

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

import com.doan.AppTuyenDung.Services.PackagePostService;

@RestController
@RequestMapping("/package-post")
public class PackagePostController {
    @Autowired
    private PackagePostService packagePostService;

    @GetMapping("/get-all-package")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public ResponseEntity<Map<String, Object>> getAllPackages(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false) String search) {

        Map<String, Object> result = packagePostService.getAllPackages(limit, offset, search);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/set-active-package-post")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> setActiveTypePackage(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = packagePostService.setActiveTypePackage(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/get-packagePost-by-id")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public ResponseEntity<Map<String, Object>> getPackageById(@RequestParam Integer id) {
        Map<String, Object> result = packagePostService.getPackageById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/update-package-post")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> updatePackagePost(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = packagePostService.updatePackagePost(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/create-package-post")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> createNewPackagePost(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = packagePostService.createNewPackagePost(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
