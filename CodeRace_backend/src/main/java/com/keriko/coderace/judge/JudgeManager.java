package com.keriko.coderace.judge;

import com.keriko.coderace.judge.codesandbox.model.JudgeInfo;
import com.keriko.coderace.judge.strategy.DefaultJudgeStrategy;
import com.keriko.coderace.judge.strategy.JavaLanguageJudgeStrategy;
import com.keriko.coderace.judge.strategy.JudgeContext;
import com.keriko.coderace.judge.strategy.JudgeStrategy;
import com.keriko.coderace.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
