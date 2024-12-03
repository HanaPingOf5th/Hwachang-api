package com.hwachang.hwachangapi.domain.member.mapper;

import com.hwachang.hwachangapi.domain.member.service.TellerService;
import com.hwachang.hwachangapi.domain.member.service.impl.TellerServiceImplV1;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TellerServiceMapper {
    private Map<Long, TellerService> tellerServiceMap = new HashMap<>();

    public TellerServiceMapper(TellerServiceImplV1 tellerServiceImplV1){
        tellerServiceMap.put(1L, tellerServiceImplV1);
    }

    public TellerService get(Long version){return tellerServiceMap.get(version);}
}
