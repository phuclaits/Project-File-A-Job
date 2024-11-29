package com.doan.AppTuyenDung.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.doan.AppTuyenDung.Services.PackageService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
@RestController
@RequestMapping()
public class PackageController {
    @Autowired
    private PackageService packageService;


    @GetMapping("/package/get-statistical-cv")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> getStatisticalPackage(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) {
        
        if (fromDate == null) {
            fromDate = LocalDate.now().withDayOfMonth(1).toString();
        }
        if (toDate == null) {
            toDate = LocalDate.now().toString(); 
        }
        Map<String, Object> result = packageService.getStatisticalPackage(fromDate, toDate, limit, offset);

        if (result.containsKey("errCode") && (int) result.get("errCode") == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.ok(result);
    }


    // role company
    @GetMapping("/package/get-package-by-Type")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public Map<String, Object> getPackageByType(@RequestParam(required = false) Integer isHot) {
        return packageService.getPackageByType(isHot);
    }

    @GetMapping("/package/get-package-by-Id")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public Map<String, Object> getPackageById(@RequestParam Integer id) {
        return packageService.getPackageById(id);
    }
}
