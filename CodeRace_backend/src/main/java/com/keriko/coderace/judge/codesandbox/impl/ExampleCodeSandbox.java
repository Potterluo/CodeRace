package com.keriko.coderace.judge.codesandbox.impl;

import com.keriko.coderace.judge.codesandbox.CodeSandbox;
import com.keriko.coderace.model.enums.QuestionSubmitStatusEnum;
import com.keriko.coderace.judge.codesandbox.model.ExecuteCodeRequest;
import com.keriko.coderace.judge.codesandbox.model.ExecuteCodeResponse;
import com.keriko.coderace.judge.codesandbox.model.JudgeInfo;
import com.keriko.coderace.model.enums.JudgeInfoMessageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱类，用于演示代码执行的流程。
 * 实现了 CodeSandbox 接口，其中包含了一个执行代码的方法。
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {

    /**
     * 执行代码请求，并返回执行结果。
     *
     * @param executeCodeRequest 包含待执行代码的请求对象。
     * @return ExecuteCodeResponse 包含执行结果的响应对象，例如输出列表、状态信息和判题详情等。
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("示例代码沙箱开始执行代码");
        // 从请求中获取输入列表
        List<String> inputList = executeCodeRequest.getInputList();

        // 初始化执行代码的响应对象
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 将输入列表设置为执行结果的输出列表
        executeCodeResponse.setOutputList(inputList);
        // 设置执行成功的消息和状态
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        // 设置判题信息，示例中为接受（Accepted）状态，使用模拟的内存和时间消耗
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        // 将判题信息设置到响应对象中
        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }
}

