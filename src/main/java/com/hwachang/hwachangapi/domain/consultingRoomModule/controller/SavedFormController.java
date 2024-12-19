package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.InsertFormDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.SavedFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/save-form")
@RequiredArgsConstructor
public class SavedFormController {
    private final SavedFormService savedFormService;
    @PostMapping()
    public void saveFormData(@RequestBody InsertFormDto insertFormDto) {
        System.out.println(Arrays.toString(insertFormDto.getSubjectedFormData().toArray()));
        this.savedFormService.createSubjectedFormData(insertFormDto);
    }
}
