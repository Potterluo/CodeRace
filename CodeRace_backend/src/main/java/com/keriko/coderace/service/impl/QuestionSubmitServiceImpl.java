package com.keriko.coderace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keriko.coderace.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.keriko.coderace.common.ErrorCode;
import com.keriko.coderace.constant.CommonConstant;
import com.keriko.coderace.exception.BusinessException;
import com.keriko.coderace.judge.JudgeService;
import com.keriko.coderace.mapper.QuestionSubmitMapper;
import com.keriko.coderace.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.keriko.coderace.model.entity.Question;
import com.keriko.coderace.model.entity.QuestionSubmit;
import com.keriko.coderace.model.entity.User;
import com.keriko.coderace.model.enums.QuestionSubmitLanguageEnum;
import com.keriko.coderace.model.enums.QuestionSubmitStatusEnum;
import com.keriko.coderace.model.vo.QuestionSubmitVO;
import com.keriko.coderace.model.vo.QuestionVO;
import com.keriko.coderace.model.vo.UserVO;
import com.keriko.coderace.service.QuestionService;
import com.keriko.coderace.service.QuestionSubmitService;
import com.keriko.coderace.service.UserService;
import com.keriko.coderace.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 针对表【question_submit(题目提交)】的数据库操作Service实现
 *
 * @author keriko
 * @description
 * @createDate 2023-08-07 20:58:53
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    @Autowired
    private QuestionSubmitMapper questionSubmitMapper;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 包含提交请求的数据，如编程语言、题目ID和代码等
     * @param loginUser 当前登录的用户信息
     * @return 返回提交的题目的ID
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验所选编程语言是否在系统支持的范围内
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        long questionId = questionSubmitAddRequest.getQuestionId();
        // 根据题目ID查询题目信息，判断该题目是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 检查当前用户是否已经提交过该题目
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);

        // 设置提交状态为等待判题，并初始化判题信息
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");

        // 保存提交的题目信息到数据库
        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }

        Long questionSubmitId = questionSubmit.getId();
        // 异步调用判题服务，对提交的代码进行判题
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });

        return questionSubmitId;
    }


    /**
     * 根据前端请求构建查询条件
     *
     * @param questionSubmitQueryRequest 前端传来的查询请求对象
     * @return mybatis plus 的查询包装类 QueryWrapper
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        // 构建查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
/*        queryWrapper.eq("questionVO",questionVO);
        queryWrapper.eq("userVO",userVO);*/
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 根据提交的题目和登录用户，返回脱敏后的视图对象
     *
     * @param questionSubmit 提交的题目实体
     * @param loginUser 当前登录用户
     * @return 脱敏后的视图对象
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏处理：非本人和管理员不能查看代码
        long userId = loginUser.getId();
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            //questionSubmitVO.setCode(null);
            return null;
        }
        long userId1 = questionSubmit.getUserId();
        long questionId = questionSubmit.getQuestionId();
        UserVO userVO = userService.getUserVO(userService.getById(userId1));
        QuestionVO questionVO = questionService.getQuestionVO(questionService.getById(questionId));
        questionSubmitVO.setUserVO(userVO);
        questionSubmitVO.setQuestionVO(questionVO);
        questionSubmitVO.setUserId(null);
        return questionSubmitVO;
    }

    /**
     * 获取问题提交的视图对象分页信息。
     *
     * @param questionSubmitPage 问题提交的实体分页信息，包含当前页、每页大小和总记录数。
     * @param loginUser 当前登录的用户信息。
     * @return 返回问题提交的视图对象分页信息，包含当前页、每页大小、总记录数和转换后的视图对象列表。
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords(); // 从实体分页信息中获取当前页的记录列表
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal()); // 创建视图对象的分页信息，保持与实体分页信息的页码、每页大小和总记录数一致
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage; // 如果记录列表为空，直接返回空的视图对象分页信息
        }
        // 将实体列表转换为视图对象列表
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .filter(Objects::nonNull) // 过滤掉null值
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList); // 将转换后的视图对象列表设置到视图对象分页信息中
        return questionSubmitVOPage; // 返回视图对象分页信息
    }

    @Override
    public QueryWrapper<QuestionSubmit> getCurrentUserQuestionSubmitQueryWrapper(Long questionId, User loginUser){
        questionSubmitMapper.getSubmitByUserAndQuestion(loginUser.getId(), questionId);
        return null;
    }

}
