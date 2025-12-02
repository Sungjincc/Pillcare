package com.pillcare.pillcare_server.service;

import com.pillcare.pillcare_server.dto.UpdateguardianRequest;
import com.pillcare.pillcare_server.entity.Userguardian;
import com.pillcare.pillcare_server.repository.UserguardianRepository;
import com.pillcare.pillcare_server.dto.PasswordResponse;
import com.pillcare.pillcare_server.dto.ResetPasswordRequest;
import com.pillcare.pillcare_server.dto.UpdateCareTargetRequest;
import com.pillcare.pillcare_server.dto.UpdateGuardianMemoRequest;
import com.pillcare.pillcare_server.entity.GuardianMemo;
import com.pillcare.pillcare_server.repository.GuardianMemoRepository;
import com.pillcare.pillcare_server.dto.GuardianMemoDto;

import java.util.List;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import com.pillcare.pillcare_server.security.AESUtil;

@Service
public class UserguardianService {

    private final UserguardianRepository userguardianRepository;
    private final GuardianMemoRepository guardianMemoRepository;

    public UserguardianService(UserguardianRepository userguardianRepository, GuardianMemoRepository guardianMemoRepository) {
        this.userguardianRepository = userguardianRepository;
        this.guardianMemoRepository = guardianMemoRepository;
    }

    // 유저 이름 및 전화번호 변겅
    public boolean updateUserInfo(int userId, UpdateguardianRequest request) {
        Optional<Userguardian> optionalUser = userguardianRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }

        Userguardian user = optionalUser.get();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());

        userguardianRepository.save(user);
        return true;
    }

    // 유저의 보호자 이름 및 전화번호 변경
    public boolean updateCareTarget(int userId, UpdateCareTargetRequest request) {
        Optional<Userguardian> optionalUser = userguardianRepository.findById(userId);
        if (optionalUser.isEmpty()) return false;

        Userguardian user = optionalUser.get();
        user.setCareTargetName(request.getCareTargetName());
        user.setCareTargetPhoneNumber(request.getCareTargetPhoneNumber());

        userguardianRepository.save(user);
        return true;
    }

    // 유저 비밀번호 갖고오기
    public PasswordResponse getPasswordByUserId(int userId) {
        Optional<Userguardian> optionalUser = userguardianRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        Userguardian user = optionalUser.get();
        try {
            String decrypted = AESUtil.decrypt(user.getPassword());
            return new PasswordResponse(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 복호화 실패", e);
        }
    }

    // 유저 비밀번호 초기화
    public boolean resetPassword(int userId, ResetPasswordRequest request) {
        Optional<Userguardian> optionalUser = userguardianRepository.findById(userId);
        if (optionalUser.isEmpty()) return false;

        Userguardian user = optionalUser.get();
        try {
            String encrypted = AESUtil.encrypt(request.getNewPassword());
            user.setPassword(encrypted);
            userguardianRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 암호화 실패", e);
        }
    }


    // 케어 대상자 메모 변경
    @Transactional
    public void updateGuardianMemos(int userId, List<UpdateGuardianMemoRequest> memos) {
        Userguardian user = userguardianRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        guardianMemoRepository.deleteByUserguardianUserId(userId);

        int index = 1;
        for (UpdateGuardianMemoRequest req : memos) {
            GuardianMemo memo = new GuardianMemo();
            memo.setMemoIndex(index++);
            memo.setContent(req.getContent());
            memo.setUserguardian(user);
            guardianMemoRepository.save(memo);
        }
    }

    // 암호화 된 비밀번호 비교
    public boolean isPasswordMatch(int userId, String inputPassword) {
        Optional<Userguardian> optionalUser = userguardianRepository.findById(userId);
        if (optionalUser.isEmpty()) return false;

        try {
            String encryptedInput = AESUtil.encrypt(inputPassword);
            return encryptedInput.equals(optionalUser.get().getPassword());
        } catch (Exception e) {
            return false;
        }
    }

}