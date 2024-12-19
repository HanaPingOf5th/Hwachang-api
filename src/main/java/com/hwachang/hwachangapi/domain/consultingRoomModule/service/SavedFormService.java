package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.SubjectedFormData;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.InsertFormDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.SavedFormEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.SavedFormRepository;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedFormService {
    private final SavedFormRepository savedFormRepository;
    private final CustomerRepository customerRepository;

    public void createSubjectedFormData(InsertFormDto insertFormDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        CustomerEntity customerEntity = this.customerRepository.findByUsername(username).orElseThrow();

        System.out.println(customerEntity.getName());

        try{
            System.out.println("11111111111111111111111");
            List<SubjectedFormData> result = insertFormDto.getSubjectedFormData();
            System.out.println(result.get(0).getLabel());
            System.out.println("22222222222222222222222222222222");

            InsertFormDto insertData = InsertFormDto.builder().subjectedFormData(result).build();

            SavedFormEntity savedFormEntity = SavedFormEntity.builder()
                    .customerId(customerEntity.getId())
                    .username(customerEntity.getUsername())
                    .insertFormDto(insertData)
                    .build();
            savedFormRepository.save(savedFormEntity);
            System.out.println("3333333333333333333333");
        }catch (Exception e){throw new RuntimeException();}
    }
}
