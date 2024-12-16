package com.hwachang.hwachangapi.consultingRoom;

import com.hwachang.hwachangapi.utils.database.BaseMemberEntity;
import com.hwachang.hwachangapi.utils.database.BaseMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseMemberRepositoryTest {
    @Autowired
    BaseMemberRepository baseMemberRepository;
}
