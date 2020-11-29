package com.jarvis.ai.jarvisai.service;

import com.jarvis.ai.jarvisai.dao.QuestionAnswerDao;
import com.jarvis.ai.jarvisai.model.QuestionAndAnswer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionAnswerService {
    @Autowired
    QuestionAnswerDao questionAnswerDao;

    int insertQuestionAndAnswerRecord(QuestionAndAnswer questionAndAnswer){
        return questionAnswerDao.insertQuestionAndAnswerRecord(questionAndAnswer);
    }

    QuestionAndAnswer getTheLastQuestionAnswerByUserId(long userId){
        return questionAnswerDao.getTheLastQuestionAnswerByUserId(userId);
    }

    QuestionAndAnswer getTheLastAnswerByQuestion(long userId,String question) {
        return questionAnswerDao.getTheLastAnswerByQuestion(userId,question);
    }


    int updateAnswerById(QuestionAndAnswer questionAndAnswer){
        return questionAnswerDao.updateAnswerById(questionAndAnswer);
    }

}
