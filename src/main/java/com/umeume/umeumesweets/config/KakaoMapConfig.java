package com.umeume.umeumesweets.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao.map")
public class KakaoMapConfig {
    private String apiKey;
}
