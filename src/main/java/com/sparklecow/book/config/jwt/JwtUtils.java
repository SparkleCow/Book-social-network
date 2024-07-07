package com.sparklecow.book.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private String JWT_SECRET = "your-secret-key";
}
