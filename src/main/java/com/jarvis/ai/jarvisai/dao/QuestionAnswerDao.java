package com.jarvis.ai.jarvisai.dao;

import com.jarvis.ai.jarvisai.model.QuestionAndAnswer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface QuestionAnswerDao {
    @Insert("insert into question_answer_record (user_id,question,answer,timestamp,date) values(#{userId},#{question},#{answer},unix_timestamp(now()),curdate())")
    int insertQuestionAndAnswerRecord(QuestionAndAnswer questionAndAnswer);

    @Select("select * from question_answer_record where user_id=#{userId} order by timestamp desc limit 1")
    QuestionAndAnswer getTheLastQuestionAnswerByUserId(@Param(value = "userId")long userId);

    @Select("select * from question_answer_record where user_id=#{userId} and question=#{question} order by timestamp desc limit 1")
    QuestionAndAnswer getTheLastAnswerByQuestion(@Param(value = "userId")long userId,@Param(value = "question")String question);

    @Update("update question_answer_record set answer=#{answer} where id=#{id}")
    int updateAnswerById(QuestionAndAnswer questionAndAnswer);





}
