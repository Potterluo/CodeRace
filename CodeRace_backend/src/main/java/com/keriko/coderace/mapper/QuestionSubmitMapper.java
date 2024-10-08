package com.keriko.coderace.mapper;

import com.keriko.coderace.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author keriko
* @description 针对表【question_submit(题目提交)】的数据库操作Mapper
* @createDate 2023-08-07 20:58:53
* @Entity com.yupi.yuoj.model.entity.QuestionSubmit
*/
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {
    QuestionSubmit getSubmitByUserAndQuestion(Long userId,Long questionId) ;
}


