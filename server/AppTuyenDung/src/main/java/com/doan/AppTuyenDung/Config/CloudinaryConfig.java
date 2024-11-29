package com.doan.AppTuyenDung.Config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config =  new HashMap<String, String>();
        config.put("cloud_name","di9chfwic");
        config.put("api_key","197928566545859");
        config.put("api_secret","VbBN5ksFg9EDCPkK66vqX9P4gPo");
        return new Cloudinary(config);
    }
}
