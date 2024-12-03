package com.hwachang.hwachangapi.utils.randomGenerate;

import java.util.Random;

public class RandomCodeGenerator {
    Random random = new Random();

    public String generateRandomCode(){
        String result = "";
        for (int i = 0; i < 6; i++) {
            result = result + random.nextInt(9);
        }
        return result;
    }
}
