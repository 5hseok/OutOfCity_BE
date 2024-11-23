package com.outofcity.server.controller.admin;

import com.outofcity.server.dto.admin.request.ActivityRegisterRequestDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.admin.AdminActivityService;
import com.outofcity.server.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminActivityService adminActivityService;
    private final AdminService adminAService;

    //관리자 액티비티 등록
    @PostMapping("/activities")
    public ResponseEntity<SuccessStatusResponse<Void>> registerActivity(@RequestHeader("Authorization") String token, @RequestBody ActivityRegisterRequestDto requestDto) {
        adminActivityService.registerActivity(token, requestDto);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.ADMIN_ACTIVITY_REGISTER_SUCCESS));
    }

    //관리자 챌린지 인증
    @PatchMapping("/challenges/proof/{userChallengeId}")
    public ResponseEntity<SuccessStatusResponse<String>> proofChallenge(@RequestHeader("Authorization") String token, @PathVariable Long userChallengeId) {
        adminAService.proofChallenge(token, userChallengeId);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.ADMIN_CHALLENGE_PROOF_SUCCESS, "Certification Success"));
    }
}
