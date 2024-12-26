package com.outofcity.server.service.scheduler;

import com.outofcity.server.domain.Reserve;
import com.outofcity.server.repository.ReserveRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SchedulerService {

    private final ReserveRepository reserveRepository;

    public SchedulerService(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    public void updateUserReserveStatus(){
        // 여행 등급이 사용자가 소비한 가격으로 결정이 되는데,
        // 소비라는건 결국 사용자가 체험을 완료했다는 것을 확인해주어야 한다.
        // 그래서 30분마다 사용자가 체험을 완료했는지 확인하는 작업을 해준다.
        // 이 작업은 사용자가 예약한 체험 목록을 가져와 현재 시간과 비교하여 진행된다.

        // 사용자가 예약한 체험 목록을 가져온다.
        List<Reserve> userReserveList = reserveRepository.findAllNotCompleted();

        // 현재 시간을 가져온다.
        LocalDateTime currentDateTime = LocalDateTime.now();

        //userReserveList 각 항목에서 reserveTime을 가져와 현재 시간과 비교한다.
        for(Reserve reservedItem : userReserveList){

            if(currentDateTime.isAfter(reservedItem.getReservedAt())){
                // 현재 시간이 예약 시간을 지났다면 예약 상태를 completed로 변경한다.
                reserveRepository.updateReserveStateToCompleted(reservedItem);
            }
        }

    }

}