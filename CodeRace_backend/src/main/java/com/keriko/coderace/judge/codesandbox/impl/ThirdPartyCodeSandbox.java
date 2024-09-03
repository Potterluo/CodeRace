package com.keriko.coderace.judge.codesandbox.impl;

import com.keriko.coderace.judge.codesandbox.CodeSandbox;
import com.keriko.coderace.judge.codesandbox.model.ExecuteCodeRequest;
import com.keriko.coderace.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
@Slf4j
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("第三方代码沙箱执行代码");
        return null;
    }
}
