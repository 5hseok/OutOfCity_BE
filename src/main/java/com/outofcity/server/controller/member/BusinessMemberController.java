package com.outofcity.server.controller.member;

import com.outofcity.server.dto.member.business.request.BusinessMemberRequestDto;
import com.outofcity.server.dto.member.business.response.BusinessMemberResponseDto;
import com.outofcity.server.global.exception.dto.SuccessStatusResponse;
import com.outofcity.server.global.exception.message.SuccessMessage;
import com.outofcity.server.service.member.BusinessMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class BusinessMemberController {

    private final BusinessMemberService businessMemberService;

    @PostMapping("register/business")
    public ResponseEntity<SuccessStatusResponse<BusinessMemberResponseDto>> registerBusiness(@RequestBody BusinessMemberRequestDto businessMemberRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
                SuccessStatusResponse.of(
                        SuccessMessage.SIGNUP_SUCCESS, businessMemberService.registerBusiness(businessMemberRequestDto)
                )
        );
    }

}
