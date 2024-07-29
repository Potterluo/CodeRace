package com.keriko.coderace.judge.codesandbox;

import com.keriko.coderace.judge.codesandbox.model.ExecuteCodeRequest;
import com.keriko.coderace.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
