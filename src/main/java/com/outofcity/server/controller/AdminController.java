package com.outofcity.server.controller;

import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("{admin.url}")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/challenges/proof/{userChallengeId}")
    public ResponseEntity<SuccessStatusResponse<String>> proofChallenge(@RequestHeader("Authorization") String token, @PathVariable Long userChallengeId) {
        adminService.proofChallenge(token, userChallengeId);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.ADMIN_CHALLENGE_PROOF_SUCCESS, "Certification Success"));
    }
}
