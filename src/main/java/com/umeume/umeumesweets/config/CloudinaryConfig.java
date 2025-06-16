package com.umeume.umeumesweets.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxwogrkds",
                "api_key", "199128324939241",
                "api_secret", "N3QOzrn5hZDsNCCMW8YSjZSKXoM"
        ));
    }
}
