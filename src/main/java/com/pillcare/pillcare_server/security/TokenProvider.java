package com.pillcare.pillcare_server.security;

import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    public String createToken(int userId, String username) {
        // 테스트용: 실제 JWT 대신 하드코딩된 FCM 토큰 반환
        return "e-BNNqGbQV2BI6X2ElpAMB:APA91bHxHNrK2Js1hfKMdA1jlzst2aOWaPRZMSiztMn1kpG2U_l0OaMPw2vyUPXsLPB93gYxtQ3oY4OdtWT140vgfKUrpwdCJCMcSe0CEjIhK1dE-qJ8S_s";
    }
}
