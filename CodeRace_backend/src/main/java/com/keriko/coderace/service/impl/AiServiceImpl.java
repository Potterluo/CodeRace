package com.keriko.coderace.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keriko.coderace.model.enums.AiPrams;
import com.keriko.coderace.service.AiService;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    @Value("${ai.zhiPuApiKey}")
    private String zhiPuApiKey;

    @Override
    /**
     * 实现与AI模型的聊天功能
     *
     * @param message 用户输入的聊天信息
     * @return AI模型的回复信息，如果获取失败则返回null
     */
    public String doChat(String message) {
        // 创建并初始化聊天消息列表
        List<ChatMessage> messages = new ArrayList<>();
        // 构造用户角色的聊天消息，并添加到列表中
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(chatMessage);
        return sendMessage(messages);
    }


    @Override
    public String problemAnalysis(String content) {
        // 创建并初始化聊天消息列表
        List<ChatMessage> messages = new ArrayList<>();
        String prompt = "你是一位 AI 编程助手，任务是根据用户给出的编程题目提供以下信息：\n" +
                "\n" +
                "难度评级：对题目的难度进行合理的分类（简单、中等、困难），并说明原因。\n" +
                "题目分类：将题目归类到相应的算法或数据结构领域（如动态规划、贪心算法、图论、树、二叉树等）。\n" +
                "解题思路：清晰地描述解决该问题的思路和步骤，包括关键的算法思想或策略。\n" +
                "伪代码：提供简洁易懂的伪代码，帮助用户理解如何实现该算法。\n" +
                "题目笔记：记录该题目中的重要知识点、常见的错误以及可能的优化点。\n" +
                "输入示例：\n" +
                "\n" +
                "题目：“给定一个数组，找出其中的最大子数组和。”\n" +
                "输出格式：\n" +
                "\n" +
                "难度评级：\n" +
                "\n" +
                "难度：中等\n" +
                "原因：需要理解和应用动态规划思想。\n" +
                "题目分类：\n" +
                "\n" +
                "分类：动态规划\n" +
                "解题思路：\n" +
                "\n" +
                "本题属于经典的动态规划问题。可以使用一个辅助变量记录当前子数组的和，并不断更新最大值。在遍历数组时，如果当前的和为负数，则将其重置为当前元素，否则累加当前元素。\n" +
                "伪代码：\n" +
                "\n" +
                "lua\n" +
                "复制代码\n" +
                "function maxSubArray(arr):\n" +
                "    max_sum = arr[0]\n" +
                "    current_sum = arr[0]\n" +
                "    \n" +
                "    for i from 1 to len(arr)-1:\n" +
                "        current_sum = max(arr[i], current_sum + arr[i])\n" +
                "        max_sum = max(max_sum, current_sum)\n" +
                "    \n" +
                "    return max_sum\n" +
                "题目笔记：\n" +
                "\n" +
                "动态规划的核心思想是记录子问题的最优解。\n" +
                "常见错误包括忘记处理负数情况下的重置操作。\n" +
                "可以进一步优化为“分治法”来解决该问题，但动态规划的解法已经足够高效。";
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), prompt);
        messages.add(chatMessage);
        chatMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        messages.add(chatMessage);
        return sendMessage(messages);
    }

    @Override
    public String answerAnalysis(String content, String answer) {
        List<ChatMessage> messages = new ArrayList<>();
        String prompt = "你是一位 AI 编程助手，任务是根据用户给出的编程题目和用户自己的作答，提供以下信息：\n" +
                "\n" +
                "题目分析：简要分析题目的关键点和考查的知识领域（如数据结构、算法思想等），并描述解决该问题的一般思路。\n" +
                "用户答案分析：重点分析用户的作答，判断是否正确，并指出代码中的问题或不足之处。\n" +
                "修改建议：提供修改思路，针对用户代码中的问题，给出相应的优化或修正建议，并说明原因。\n" +
                "学习建议：针对用户在解题过程中暴露出的薄弱点，提供学习建议，帮助用户提高相关技能。\n" +
                "输入示例：\n" +
                "\n" +
                "题目：“给定一个数组，找出其中的最大子数组和。”\n" +
                "用户答案：\n" +
                "python\n" +
                "复制代码\n" +
                "def max_sub_array(arr):\n" +
                "    max_sum = 0\n" +
                "    for i in range(len(arr)):\n" +
                "        for j in range(i, len(arr)):\n" +
                "            sum = 0\n" +
                "            for k in range(i, j+1):\n" +
                "                sum += arr[k]\n" +
                "            if sum > max_sum:\n" +
                "                max_sum = sum\n" +
                "    return max_sum\n" +
                "输出格式：\n" +
                "\n" +
                "题目分析：\n" +
                "\n" +
                "本题是经典的动态规划问题，要求找出数组的最大子数组和。解决方案通常通过记录当前子数组的和，并在每步更新最大值。\n" +
                "用户答案分析：\n" +
                "\n" +
                "用户代码通过三层嵌套循环遍历所有可能的子数组，并计算每个子数组的和。这种解法虽然能找到正确结果，但时间复杂度为 O(n^3)，效率很低。\n" +
                "问题点：\n" +
                "使用了三层循环，导致性能过低，无法处理较大规模的数据。\n" +
                "未考虑初始数组中可能全为负数的情况，max_sum 的初值设为 0 是不正确的。\n" +
                "修改建议：\n" +
                "\n" +
                "可以采用动态规划的解法，将时间复杂度优化到 O(n)。通过记录当前的子数组和，并在遍历过程中更新最大值。这样可以避免三层嵌套循环的低效问题。\n" +
                "伪代码修改如下：\n" +
                "python\n" +
                "复制代码\n" +
                "def max_sub_array(arr):\n" +
                "    current_sum = arr[0]\n" +
                "    max_sum = arr[0]\n" +
                "    \n" +
                "    for i in range(1, len(arr)):\n" +
                "        current_sum = max(arr[i], current_sum + arr[i])\n" +
                "        max_sum = max(max_sum, current_sum)\n" +
                "    \n" +
                "    return max_sum\n" +
                "学习建议：\n" +
                "\n" +
                "建议用户学习动态规划的基本思想，特别是如何利用子问题的最优解来构建整体问题的解法。可以从“最大子数组和”这样的经典问题入手。\n" +
                "此外，用户可以关注算法优化的基本方法，如通过减少嵌套循环和利用空间换时间来提高效率。\n";
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), prompt);
        messages.add(chatMessage);
        chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "题目："+content);
        messages.add(chatMessage);
        chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "用户答案：\n"+answer);
        messages.add(chatMessage);
        return sendMessage(messages);
    }

    private String sendMessage(List<ChatMessage> messages){
        // 初始化ClientV4客户端，用于与AI模型交互
        ClientV4 client = new ClientV4.Builder(zhiPuApiKey).build();
        // 定义业务请求ID的模板，方便后续识别和管理请求
        String requestIdTemplate = "myCompany-%d";

        // 根据当前时间生成唯一的业务请求ID
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

        // 构建聊天完成请求对象
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4) // 指定聊天模型
                .stream(Boolean.FALSE) // 非流式回复
                .invokeMethod(Constants.invokeMethod) // 调用方法
                .messages(messages) // 聊天消息列表
                .requestId(requestId) // 业务请求ID
                .build();
        // 调用AI模型API，获取回复
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        // 如果获取回复成功，则返回AI模型的回复内容
        if(invokeModelApiResp.getCode()==200){
            return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        }
        // 如果获取回复失败，则返回null
        return null;



    }
}
