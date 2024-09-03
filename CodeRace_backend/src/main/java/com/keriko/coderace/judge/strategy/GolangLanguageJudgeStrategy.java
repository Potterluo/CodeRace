package com.keriko.coderace.judge.strategy;

import com.keriko.coderace.judge.codesandbox.model.JudgeInfo;

public class GolangLanguageJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext)
    {
        return new DefaultJudgeStrategy().doJudge(judgeContext);
    }
}
