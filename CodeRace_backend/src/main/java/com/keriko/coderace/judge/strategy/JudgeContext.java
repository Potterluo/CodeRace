package com.keriko.coderace.judge.strategy;

import com.keriko.coderace.model.dto.question.JudgeCase;
import com.keriko.coderace.model.entity.Question;
import com.keriko.coderace.model.entity.QuestionSubmit;
import com.keriko.coderace.judge.codesandbox.model.JudgeInfo;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
