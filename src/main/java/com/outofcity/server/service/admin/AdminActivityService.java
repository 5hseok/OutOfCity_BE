package com.outofcity.server.service.admin;

import com.outofcity.server.dto.activity.response.ActivityResponseDto;
import com.outofcity.server.dto.admin.request.ActivityRegisterRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminActivityService {
    public void registerActivity(String token, ActivityRegisterRequestDto requestDto) {

    }
}
