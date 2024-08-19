package com.keriko.coderace.model.dto.analysis;

import lombok.Data;

@Data
public class ProblemAnalysisRequest {
    private String content;
    private String answer;
}
