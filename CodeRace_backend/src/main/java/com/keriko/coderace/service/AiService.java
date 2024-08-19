package com.keriko.coderace.service;

import org.springframework.stereotype.Service;

@Service
public interface AiService {
    String doChat(String message);

    String problemAnalysis(String content);

    String answerAnalysis(String content, String answer);
}
