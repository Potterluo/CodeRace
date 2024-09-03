package com.keriko.codesandbox.utils;

import com.keriko.codesandbox.model.ExecuteMessage;
import com.keriko.codesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class simpleInputOutputTest {
    private static final String CODE_FILE = "Main";
    private static final String INPUT_ARGS = "3\n 1 2 3";
    private static final String OUTPUT_ARGS = "Sum:6";
    private static final String DIRECTORY = "E:\\Program\\CodeRace\\CodeRace_sandbox\\src\\UserCode\\";

    @Test
    public void test() {
        try {
            // Change directory
            changeDirectory(DIRECTORY);

            // Compile the code
            compileCode(CODE_FILE);

            // Run the code
            String output = runCode(CODE_FILE);

            // Validate output
            validateOutput(output, OUTPUT_ARGS);

        } catch (IOException e) {
            log.error("IO exception occurred: ", e);
        } catch (InterruptedException e) {
            log.error("Process was interrupted: ", e);
        }
    }

    /**
     * 改变工作目录
     *
     * 通过启动外部命令行工具来改变当前工作目录此方法使用ProcessBuilder来构建并启动命令行进程，以便改变目录
     *
     * @param directory 要改变到的新目录路径
     * @throws IOException 如果构建或启动进程时发生错误
     */
    private void changeDirectory(String directory) throws IOException {
        // 记录要改变的目录路径
        log.info("Changing directory to: {}", directory);

        // 创建ProcessBuilder实例，参数为cmd.exe命令行工具，用于改变目录
        ProcessBuilder cdProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "cd", directory);

        // 设置ProcessBuilder的工作目录为要改变到的新目录
        cdProcessBuilder.directory(new File(directory));

        // 启动命令行进程以执行目录改变操作
        cdProcessBuilder.start();
    }

    /**
     * 编译给定代码文件的Java代码
     *
     * @param codeFile 代码文件名（不包括扩展名）, 用于构造编译命令
     * @throws IOException 如果创建或执行进程时发生IO错误
     * @throws InterruptedException 如果在等待进程执行时线程被中断
     */
    private void compileCode(String codeFile) throws IOException, InterruptedException {
        // 构造编译命令，使用utf-8编码来确保中文代码文件可以被正确编译
        String compileCmd = String.format("javac -encoding utf-8 %s.java", codeFile);
        log.info("Compiling code with command: {}", compileCmd);

        // 使用ProcessBuilder来构建编译命令的执行环境
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("cmd.exe", "/c", compileCmd);
        // 设置编译命令的执行目录为系统指定的临时目录
        compileProcessBuilder.directory(new File(DIRECTORY));
        // 启动编译进程
        Process compileProcess = compileProcessBuilder.start();

        // 运行编译进程并获取输出信息
        ExecuteMessage compileMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "Compilation");
        // 记录编译信息
        log.info(compileMessage.toString());
    }

    /**
     * 运行给定的Java代码文件
     *
     * 该方法使用指定的类路径和内存限制通过cmd.exe来执行Java代码它还记录了运行命令，
     * 并在执行过程中与进程交互，获取并记录输出信息
     *
     * @param codeFile 要运行的Java代码文件名
     * @return 运行后获取的消息字符串
     * @throws IOException 当创建或启动进程时发生IO错误
     * @throws InterruptedException 当线程被中断时
     */
    private String runCode(String codeFile) throws IOException, InterruptedException {
        // 格式化命令行命令，指定Java运行时的最大堆内存为256m，并指定类路径为当前目录，
        // 准备运行的类名为codeFile参数
        String runCmd = String.format("java -Xmx256m -cp . %s", codeFile);
        // 记录即将执行的命令
        log.info("Running code with command: {}", runCmd);

        // 创建ProcessBuilder实例，使用cmd.exe来执行格式化后的Java命令
        // 使用cmd.exe /c 来在Windows命令提示符下执行命令
        ProcessBuilder runProcessBuilder = new ProcessBuilder("cmd.exe", "/c", runCmd);
        // 设置进程的执行目录为代码中的DIRECTORY常量指定的目录
        runProcessBuilder.directory(new File(DIRECTORY));
        // 启动进程
        Process runProcess = runProcessBuilder.start();

        // 使用工具类ProcessUtils运行进程并与之交互，获取进程的输出信息
        // INPUT_ARGS为与进程交互的输入参数
        ExecuteMessage runMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, INPUT_ARGS);
        // 记录进程的输出信息
        log.info(runMessage.toString());
        // 返回进程的输出消息
        return runMessage.getMessage();
    }

    @Test
    private void validateOutput(String actualOutput, String expectedOutput) {
        if (!actualOutput.equals(expectedOutput)) {
            log.error("Output is incorrect. Expected: {} but got: {}", expectedOutput, actualOutput);
        } else {
            System.out.println("Output is correct: \n" + actualOutput);
            log.info("Output is correct: {}", actualOutput);
        }
        // 断言验证
        assertEquals(expectedOutput, actualOutput);
    }
}
