package com.pillcare.pillcare_server.controller;

import com.pillcare.pillcare_server.dto.CheckIdResponse;
import com.pillcare.pillcare_server.dto.GuardianMemoDto;
import com.pillcare.pillcare_server.dto.UpdateguardianRequest;
import com.pillcare.pillcare_server.dto.UserguardianResponse;
import com.pillcare.pillcare_server.entity.GuardianMemo;
import com.pillcare.pillcare_server.entity.Userguardian;
import com.pillcare.pillcare_server.repository.UserguardianRepository;
import com.pillcare.pillcare_server.service.UserguardianService;
import com.pillcare.pillcare_server.dto.PasswordResponse;
import com.pillcare.pillcare_server.dto.ResetPasswordRequest;
import com.pillcare.pillcare_server.repository.GuardianMemoRepository;
import com.pillcare.pillcare_server.dto.UpdateGuardianMemoRequest;
import com.pillcare.pillcare_server.dto.PasswordCheckRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import com.pillcare.pillcare_server.security.AESUtil;
import java.util.Optional;
import java.util.Collections;

@RestController
@RequestMapping("/api/guardian")
public class UserguardianController {

    private final UserguardianRepository userguardianRepository;
    private final UserguardianService userguardianService;
    private final GuardianMemoRepository guardianMemoRepository;

    public UserguardianController(UserguardianRepository userguardianRepository, UserguardianService userguardianService, GuardianMemoRepository guardianMemoRepository) {
        this.userguardianRepository = userguardianRepository;
        this.userguardianService = userguardianService;
        this.guardianMemoRepository = guardianMemoRepository;
    }


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerGuardian(@RequestBody Userguardian user) {
        List<GuardianMemo> guardianMemos = user.getGuardianMemos();
        int index = 1;
        for (GuardianMemo memo : guardianMemos) {
            memo.setUserguardian(user);
            memo.setMemoIndex(index++);  // ✅ memoIndex 지정
        }

        try {
            // 비밀번호 암호화
            String encrypted = AESUtil.encrypt(user.getPassword());
            user.setPassword(encrypted);

            userguardianRepository.save(user);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("비밀번호 암호화 실패");
        }
    }

    // 아이디 중복 확인
    @GetMapping("/check-id")
    public ResponseEntity<CheckIdResponse> checkDuplicateId(@RequestParam("ID") String id) {
        boolean exists = userguardianRepository.existsByID(id);
        return ResponseEntity.ok(new CheckIdResponse(exists));
    }

    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserguardianResponse> getUserInfo(@PathVariable int userId) {
        Userguardian user = userguardianRepository.findById(userId).orElse(null);
        if (user == null) {
        	System.out.println("있긴 해요");
            return ResponseEntity.notFound().build();
        }

        List<GuardianMemoDto> memoDtos = new ArrayList<>();
        List<GuardianMemo> guardianMemos = user.getGuardianMemos();
        for (GuardianMemo memo : guardianMemos) {
            GuardianMemoDto dto = new GuardianMemoDto(
                memo.getMemoId(),
                user.getUserId(),
                memo.getContent()
            );
            memoDtos.add(dto);
        }

        UserguardianResponse response = new UserguardianResponse(
            user.getUserId(),
            user.getName(),
            user.getPhoneNumber(),
            user.getID(),
            user.getCareTargetName(),
            user.getCareTargetPhoneNumber(),
            memoDtos
        );

        return ResponseEntity.ok(response);
    }

    // 사용자 정보 변경
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserInfo(
            @PathVariable int userId,
            @RequestBody UpdateguardianRequest request
    ) {
        boolean success = userguardianService.updateUserInfo(userId, request);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // 케어 대상자 정보 변경
    @PutMapping("/care-target/{userId}")
    public ResponseEntity<Void> updateCareTarget(
            @PathVariable int userId,
            @RequestBody com.pillcare.pillcare_server.dto.UpdateCareTargetRequest request
    ) {
        boolean success = userguardianService.updateCareTarget(userId, request);
        if (!success) {
            return ResponseEntity.notFound().build();
        }

        if (request.getGuardianMemos() != null) {
            userguardianService.updateGuardianMemos(userId, Optional.ofNullable(request.getGuardianMemos()).orElse(Collections.emptyList()));
        }
        return ResponseEntity.ok().build();
    }

    // 유저 비밀번호 동일한지 체크
    @PostMapping("/password/{userId}")
    public ResponseEntity<Boolean> checkPassword(@PathVariable int userId,@RequestBody PasswordCheckRequest request) {
        boolean matches = userguardianService.isPasswordMatch(userId, request.getPassword());
        return ResponseEntity.ok(matches);
    }

    // 유저 비밀번호 변경
    @PutMapping("/password/{userId}")
    public ResponseEntity<Void> resetPassword(@PathVariable int userId, @RequestBody ResetPasswordRequest request) {
        boolean success = userguardianService.resetPassword(userId, request);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

//    // 케어 대상자 메모 변경 확인
//    @PutMapping("/memos/{userId}")
//    public ResponseEntity<Void> updateMemos(
//            @PathVariable int userId,
//            @RequestBody List<UpdateGuardianMemoRequest> memos
//    ) {
//        userguardianService.updateGuardianMemos(userId, memos);
//        return ResponseEntity.ok().build();
//    }



}
