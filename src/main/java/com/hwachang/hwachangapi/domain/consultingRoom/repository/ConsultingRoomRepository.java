package com.hwachang.hwachangapi.domain.consultingRoom.repository;

import com.hwachang.hwachangapi.domain.consultingRoom.entity.ConsultingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultingRoomRepository extends JpaRepository<ConsultingRoom, String> {

}
