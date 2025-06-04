// 꼭 이렇게 되어 있어야 함
package com.umeume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.umeume.umeumesweets")
public class UmeUmeSweetsApplication {
    public static void main(String[] args) {
        System.out.println("출발!");
        SpringApplication.run(UmeUmeSweetsApplication.class, args);
    }
}