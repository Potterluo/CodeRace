package com.keriko.coderace.controller;

import com.keriko.coderace.common.BaseResponse;
import com.keriko.coderace.common.ErrorCode;
import com.keriko.coderace.common.ResultUtils;
import com.keriko.coderace.exception.BusinessException;
import com.keriko.coderace.exception.ThrowUtils;
import com.keriko.coderace.model.dto.analysis.ProblemAnalysisRequest;
import com.keriko.coderace.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/AiChat")
@Slf4j
public class AIController {
    @Autowired
    private AiService aiService;
/*
    @GetMapping("/chat")
    public String doChat(String message) {
        return aiService.doChat(message);
    }

    @GetMapping("/problemAnalysis")
    public String problemAnalysis(String content) {
        return aiService.problemAnalysis(content);
    }
    @GetMapping("/answerAnalysis")
    public String answerAnalysis(String content, String answer) {
        return aiService.answerAnalysis(content, answer);
    }
*/

    @PostMapping("/analysis")
    /**
     * 根据提供的问题或答案进行分析，并返回分析结果
     *
     * @param problemAnalysisRequest 包含问题内容和答案的请求体
     * @return 分析结果的字符串
     *
     * 当前方法主要处理两种情况：
     * 1. 如果请求体为空或问题内容为空，则抛出业务异常，错误码为PARAMS_ERROR
     * 2. 如果请求中不包含答案，则调用AI服务对问题进行分析
     * 3. 如果请求中包含答案，则调用AI服务对问题和答案进行分析
     * 在每种情况下，如果分析结果为空字符串，则抛出业务异常，错误码为OPERATION_ERROR
     */
    public BaseResponse<String> analysis(@RequestBody ProblemAnalysisRequest problemAnalysisRequest) {
        // 校验请求参数是否为空
        if (problemAnalysisRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验问题内容是否为空
        if (StringUtils.isBlank(problemAnalysisRequest.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否有答案，如果没有则只对问题进行分析
        String res;
        // 如果答案过短，则只对问题进行分析
        if(problemAnalysisRequest.getAnswer().length() < 20)
        {
            res = aiService.problemAnalysis(problemAnalysisRequest.getContent());
            // 校验分析结果是否为空
        } else{
            // 如果有答案，则对问题和答案进行分析
            res = aiService.answerAnalysis(problemAnalysisRequest.getContent(), problemAnalysisRequest.getAnswer());
            // 校验分析结果是否为空
        }
        ThrowUtils.throwIf(StringUtils.isBlank(res), ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(res);
    }

}
