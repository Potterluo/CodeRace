package com.keriko.coderace.model.enums;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AiPrams {
    @Value("${ai.type}")
    private String type;

    @Value("${ai.zhiPuApiKey}")
    private String zhiPuApiKey;

    public String getZhiPuApiKey() {
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println("zhiPuApiKey:"+zhiPuApiKey);
        return zhiPuApiKey;
    }

}
