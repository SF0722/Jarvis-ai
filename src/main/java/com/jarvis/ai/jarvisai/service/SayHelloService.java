package com.jarvis.ai.jarvisai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jarvis.ai.jarvisai.model.QuestionAndAnswer;
import com.jarvis.ai.jarvisai.model.Result;
import com.jarvis.ai.jarvisai.utils.HttpClientUtil;

import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Service
public class SayHelloService {

    @Autowired
    QuestionAnswerService questionAnswerService;

    public String sayhello(Long userId,String questionContent){

        String answerContent="";

        //教育模式
        if(questionContent.trim().startsWith("错，") || questionContent.trim().startsWith("不对，")){
            QuestionAndAnswer questionAndAnswer = questionAnswerService.getTheLastQuestionAnswerByUserId(userId);
            if(questionAndAnswer!=null){
                System.out.println(questionAndAnswer);
                String correctAnswer=questionContent.substring(questionContent.indexOf("，")).replaceFirst("，","");
                System.out.println(correctAnswer);
                questionAndAnswer.setAnswer(correctAnswer);
                questionAnswerService.updateAnswerById(questionAndAnswer);
                answerContent="好的，我记住了！";
            }else{
                answerContent="我说错了什么了吗？";
            }
        }else{
            QuestionAndAnswer historyAnswer = questionAnswerService.getTheLastAnswerByQuestion(userId,questionContent);
            if(historyAnswer!=null){
                answerContent= historyAnswer.getAnswer();
            }else{
                JSONObject answer=HttpClientUtil.doPost("http://openapi.tuling123.com/openapi/api/v2","{\"reqType\":0,\"perception\":{\"inputText\":{\"text\":\""+questionContent+"\"},\"inputImage\":{\"url\":\"imageUrl\"},\"selfInfo\":{\"location\":{\"city\":\"南京\",\"province\":\"江苏\"}}},\"userInfo\":{\"apiKey\":\"f5c37b86ae4441faa6f2aeb792f074e2\",\"userId\":\"1\"}}");
                JSONArray resultArray = answer.getJSONArray("results");


                Object[] objects=resultArray.stream().toArray();
                StringBuffer stringBuffer=new StringBuffer();

                Arrays.stream(objects).forEach(x-> {
                    Result result = JSON.parseObject(x.toString(),Result.class);
                    String v = String.join(",",result.getValues().values());
                    stringBuffer.append(v);
                });

                 answerContent =stringBuffer.toString();

                QuestionAndAnswer questionAndAnswer=new QuestionAndAnswer();
                questionAndAnswer.setUserId(userId);
                questionAndAnswer.setQuestion(questionContent);
                questionAndAnswer.setAnswer(answerContent);

                questionAnswerService.insertQuestionAndAnswerRecord(questionAndAnswer);

            }

        }
        return answerContent;
    }



    public String getResultBykey(String[] keys,org.neo4j.driver.Result result){
        StringBuffer sb=new StringBuffer();
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.toString());
            sb.append("[");
            for (String key:keys) {
                sb.append(record.get(key)+" ");
            }
            sb.append("]");
            sb.append(",");
        }
        return sb.toString();
    }

    public String queryNeo4j(String[] keys,String cypherStr){
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic("neo4j", "Stephanie0722"));
        Session session = driver.session();
        String resultstr="";
        ArrayList<Record> records=new ArrayList<>();
        // 查询
        System.out.println("执行cypher语句"+cypherStr);
        org.neo4j.driver.Result result = session.beginTransaction().run(cypherStr);
        resultstr=getResultBykey(keys,result);
        session.close();
        driver.close();
        return resultstr;
    }

}
