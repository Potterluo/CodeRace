package com.keriko.codesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.keriko.codesandbox.model.ExecuteMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 进程工具类
 */
@Slf4j
public class ProcessUtils {

    /**
     * 执行进程并获取信息
     *
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待程序执行，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            // 正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));
            } else {
                // 异常退出
                System.out.println(opName + "失败，错误码： " + exitValue);
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));

                // 分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                // 逐行读取
                List<String> errorOutputStrList = new ArrayList<>();
                // 逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutputStrList.add(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(StringUtils.join(errorOutputStrList, "\n"));
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (Exception e) {
            log.error("runInteractProcessAndGetMessage error", e);
        }
        return executeMessage;
    }



    /**
     * 执行交互式进程并获取消息
     * 本函数用于启动一个进程，向其输入指定参数args，然后获取该进程的输出
     *
     * @param runProcess 要运行的进程
     * @param args 输入到进程的参数，包含空格和换行，符合特定格式要求
     * @return 返回一个ExecuteMessage对象，包含进程的输出信息
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        // 初始化ExecuteMessage对象，用于存储执行结果
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 向进程的标准输入流写入参数args
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(args);
            outputStreamWriter.write("\n"); // 写入换行符，模拟用户输入结束
            outputStreamWriter.flush(); // 刷新输出流，确保数据被写入

            // 读取进程的正常输出
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取进程输出，并追加到StringBuilder中
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            // 将进程的全部输出设置到ExecuteMessage对象的消息字段中
            executeMessage.setMessage(compileOutputStringBuilder.toString());

            // 关闭所有打开的资源
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            // 销毁进程，防止资源泄漏
            runProcess.destroy();
        } catch (Exception e) {
            // 异常处理：打印异常堆栈信息
            log.error("runInteractProcessAndGetMessage error", e);
        }
        // 返回包含进程输出信息的ExecuteMessage对象
        return executeMessage;
    }

}
