package com.jarvis.ai.jarvisai.controller;

import com.alibaba.fastjson.JSON;
import com.jarvis.ai.jarvisai.model.Dialogue;
import com.jarvis.ai.jarvisai.model.DiseaseSession;
import com.jarvis.ai.jarvisai.model.SessionStatus;
import com.jarvis.ai.jarvisai.model.User;
import com.jarvis.ai.jarvisai.service.SayHelloService;
import com.jarvis.ai.jarvisai.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    SayHelloService sayHelloService;

    @Autowired
    UserService userService;



    @RequestMapping(value = "/say_hello_to_somebody")
    public Dialogue sayhello(@RequestParam(value = "userId")Long userId, @RequestParam(value = "content") String content){
//        String str = sayHelloService.sayhello(userId,content);
//        String str ="hello";
        String str=sayHelloService.queryNeo4j(new String[]{""},"MATCH (n:Disease) where n.first_category='I' RETURN distinct n.secondary_category order by n.secondary_category");
        return new Dialogue(0,str,new Date().toString());

    }

    @RequestMapping("/cookie")
    public Dialogue cookie(@RequestParam("content") String message, HttpServletRequest request, HttpSession session) {
        //取出session中的message
//        Object sessionMessage = session.getAttribute("message");


        String status=getStatus(getDiseaseSession(session));



        System.out.println(message);
        System.out.println("newStatus："+status);

        String login= (String) session.getAttribute("login");
        String s="";
        String pre="";
        String tryAgain="Illegal input, please follow the prompt!";

        switch (status){
            case "000000000":case "000111110":
                if(login ==null){
                    session.setAttribute("login","login");
                }else{
                    Set<String> set=new HashSet<>(Arrays.asList("I","E","D"));
                    if(set.contains(message)) {
                        session.setAttribute("firstCategory", message);
                    }else{
                        pre=tryAgain;
                    }
                }
                break;
            case "100000000":case "100111110":
                String[] cans1=sayHelloService.queryNeo4j(new String[]{"secondary_category"},
                        String.format("MATCH (n:Category) where n.first_category='%s' RETURN distinct n.secondary_category as secondary_category,n.secondary_category_name as secondary_category_name  order by n.secondary_category",getDiseaseSession(session).getFirstCategory()))
                        .split(",");
                Set<String> set1=new HashSet<>();
                for (String c:cans1) {
                    String cc=c.replace("[","").replace("]","").trim();
                    if(!cc.equals("NULL")){
                        set1.add(cc);
                    }
                }
                if(set1.contains(message)) {
                    session.setAttribute("secondaryCategory", message);
                }else{
                    pre=tryAgain;
                }
                break;
            case "110000000":case "110111110":
                String[] cans2=sayHelloService.queryNeo4j(new String[]{"disease_id","disease_name"},
                        String.format("MATCH (n:Disease)  where n.secondary_category=%s RETURN distinct n.disease_id as disease_id,n.disease_name as disease_name order by disease_id",getDiseaseSession(session).getSecondaryCategory()))
                        .split(",");
                Set<String> set2=new HashSet<>();
                for (String c:cans2) {
                    String diseaseId=c.replace("[","").replace("]","").trim().split(" ")[0];
                    System.out.println(diseaseId);
                    if(!diseaseId.equals("NULL")){
                        set2.add(diseaseId);
                    }
                }
                if(set2.contains(message)) {
                    session.setAttribute("diseaseId", message);
                    if(getStatus(getDiseaseSession(session)).equals("111111110")){
                        session.setAttribute("result",getSuggestion(session));
                    }
                }else{
                    pre=tryAgain;
                }
                break;
            case "111000000":
                try {
                    int year = Integer.parseInt(message.trim());
                    if (year >= 1 && year <= 100) {
                        session.setAttribute("ageYear", year + "");
                        session.setAttribute("ageMonth", "0");
                        session.setAttribute("ageWeek", year * 12 * 4 + "");
                    } else if (year < 1 && year>=0) {
                        session.setAttribute("ageYear", year + "");
                    } else {
                        pre = tryAgain;
                    }
                }catch (NumberFormatException nfe){
                    pre = tryAgain;
                }
                break;
            case "111100000":
                try{
                    int month=Integer.parseInt(message.trim());
                    if(month>=1 && month<=11){
                        session.setAttribute("ageMonth",month+"");
                        session.setAttribute("ageWeek",month*4+"");
                    }else if(month<1 && month>=0){
                        session.setAttribute("ageMonth",month+"" );
                    }else{
                        pre = tryAgain;
                    }
                }catch (NumberFormatException nfe){
                    pre = tryAgain;
                }
                break;
            case "111110000":
                try{
                    int week=Integer.parseInt(message.trim());
                    if(week>=0 && week<=3){
                        session.setAttribute("ageWeek",week+"");
                    }else{
                        pre = tryAgain;
                    }
                }catch (NumberFormatException nfe){
                    pre = tryAgain;
                }
                break;
            case "111111000":
                String g=message.trim();
                if(g.equals("1")){
                    session.setAttribute("gender",g);
                    session.setAttribute("isPregnancyBreastfeeding","0");
                    session.setAttribute("result",getSuggestion(session));
                }else if(g.equals("0")){
                    session.setAttribute("gender",g);
                }else{
                    pre = tryAgain;
                }
                break;
            case "111111100":
                String p=message.trim();
                if(p.equals("1") || p.equals("0")) {
                    session.setAttribute("isPregnancyBreastfeeding", p);
                    //111111110
                    session.setAttribute("result", getSuggestion(session));
                }else{
                    pre = tryAgain;
                }
                //111111111
                break;
            case "111111110":
                session.setAttribute("result",getSuggestion(session));
                break;
            case "111111111":
                int isSaveUser=Integer.parseInt(message);
                if(isSaveUser==1){
                    session.setAttribute("firstCategory",null);
                    session.setAttribute("secondaryCategory",null);
                    session.setAttribute("diseaseId",null);
                    session.setAttribute("result",null);
                    //000111110
                }else{
                    session.setAttribute("firstCategory",null);
                    session.setAttribute("secondaryCategory",null);
                    session.setAttribute("diseaseId",null);
                    session.setAttribute("result",null);
                    session.setAttribute("ageYear",null);
                    session.setAttribute("ageMonth",null);
                    session.setAttribute("ageWeek",null);
                    session.setAttribute("gender",null);
                    session.setAttribute("isPregnancyBreastfeeding",null);
                    //000000000
                }
                break;
        }
        //刷新status

        DiseaseSession diseaseSession=getDiseaseSession(session);
        System.out.println(JSON.toJSONString(diseaseSession));
        status=getStatus(diseaseSession);
        System.out.println("newStatus："+status);
        //文案
        switch (status){
            case "000000000":case "000111110":
                if(login==null){
                    pre="<br>Welcome to your personal healthcare assistant!<br/>";
                    s="<br>Which category do you want to query?(First category)<br/>\n" +
                            "<br>Please select the alphabet:<br/>\n" +
                            "<br>I:Internal disease - a disease that is not visible on the surface of the body.<br/>\n" +
                            "<br> E:External disease - a disease visible on the surface of the body.<br/>\n" +
                            "<br>D:Dentistry - dental and oral related diseases<br/>\n："+sayHelloService.queryNeo4j(new String[]{"first_category","first_category_name"},
                            "MATCH (n:Category)  RETURN distinct n.first_category as first_category,n.first_category_name as first_category_name  order by n.first_category");
                }else {
                    s = "<br>Which category do you want to query?(First category)<br/>\n" +
                            "<br>Please select the alphabet:<br/>\n" +
                            "<br>I:Internal disease - a disease that is not visible on the surface of the body.<br/>\n" +
                            "<br> E:External disease - a disease visible on the surface of the body.<br/>\n" +
                            "<br>D:Dentistry - dental and oral related diseases<br/>\n："+sayHelloService.queryNeo4j(new String[]{"first_category","first_category_name"},
                            "MATCH (n:Category)  RETURN distinct n.first_category as first_category,n.first_category_name as first_category_name  order by n.first_category");
                }
                break;
            case "100000000":case "100111110":
                s="<br>Which category do you want to query?(Secondary category)<br/>\n" +
                        "<br>Please select the number:<br/>"+sayHelloService.queryNeo4j(new String[]{"secondary_category","secondary_category_name"},
                        String.format("MATCH (n:Category) where n.first_category='%s' RETURN distinct n.secondary_category as secondary_category,n.secondary_category_name as secondary_category_name  order by n.secondary_category",diseaseSession.getFirstCategory()));
                break;
            case "110000000":case "110111110":
                s="<br>Please select the diseases number that you want to query :<br/>\n"+sayHelloService.queryNeo4j(new String[]{"disease_id","disease_name"},
                        String.format("MATCH (n:Disease)  where n.secondary_category=%s RETURN distinct n.disease_id as disease_id,n.disease_name as disease_name order by disease_id",diseaseSession.getSecondaryCategory()));
                break;
            case "111000000":
                s="<br>Your age is?(0-100, age under 1 please input 0)<br/>";
                break;
            case "111100000":
                int year= Integer.parseInt(diseaseSession.getAgeYear()) ;
                if(year>=1){
                    s="<br>What is your gender ?(1: male, 0: female)<br/>";
                }else{
                    s="<br>What is your age in month is?(0-11,under 1 month please input 0)<br/>";
                }
                break;
            case "111110000":
                int month=Integer.parseInt(diseaseSession.getAgeMonth()) ;
                if(month>=1){
                    s="<br>What is your gender ?(1: male, 0: female)<br/>\n";
                }else{
                    s="<br>What is your age in week is?(0-3)<br/>\n";
                }
                break;
            case "111111000":
                s="<br>What is your gender ?(1: male, 0: female)<br/>\n";
                break;
            case "111111100":
                int g=Integer.parseInt(diseaseSession.getGender()) ;
                if(g==1){
                    s="Return result:"+ diseaseSession.getResult()+"<br>Do you want to keep query as current patient or start as a new patient?(1: yes,0: no)<br/>\n";
                }else{
                    s="<br>Are you in pregnant or breastfeeding?(1: male, 0: female)<br/>\n";
                }
                break;
            case "111111110":case "111111111":
                s="\"Return result\n"+diseaseSession.getResult()+"\n<br>Do you want to keep query as current patient or start as a new patient?(1: yes,0: no)<br/>\n";
                break;


        }
//        return pre+s;
        return new Dialogue(0,pre+s,new Date().toString());
    }

    public DiseaseSession getDiseaseSession(HttpSession session){
        String firstCategory= (String) session.getAttribute("firstCategory");
        String secondaryCategory=(String) session.getAttribute("secondaryCategory");
        String diseaseId= (String) session.getAttribute("diseaseId");
        String ageYear= (String) session.getAttribute("ageYear");
        String ageMonth= (String) session.getAttribute("ageMonth");
        String ageWeek = (String) session.getAttribute("ageWeek");
        String gender  = (String) session.getAttribute("gender");
        String isPregnancyBreastfeeding= (String) session.getAttribute("isPregnancyBreastfeeding");
        String result=(String) session.getAttribute("result");
        DiseaseSession diseaseSession=new DiseaseSession(firstCategory,secondaryCategory,diseaseId,ageYear,ageMonth,ageWeek,gender,isPregnancyBreastfeeding,result);
        return diseaseSession;
    }

    public String getStatus(DiseaseSession diseaseSession){
          String status=new SessionStatus(diseaseSession).getStatus();
        return status;
    }

    public String getSuggestion(HttpSession session){
        String suggestion="Suggestion is ：\n"+
                sayHelloService.queryNeo4j(
                        new String[]{"suggestion"},
                        String.format(
                                "MATCH (n:Disease) where n.disease_id=%s RETURN distinct n.suggestion as suggestion",
                                getDiseaseSession(session).getDiseaseId(),
                                getDiseaseSession(session).getAgeWeek(),
                                getDiseaseSession(session).getAgeWeek(),
                                getDiseaseSession(session).getIsPregnancyBreastfeeding().equals("1")?"0":"1"
                        )
                )+
                "\n medication tips:" +
                sayHelloService.queryNeo4j(
                        new String[]{"common_brands","medication_detail"},
                        String.format(
                                "MATCH (n:Disease)-[r]->(m:Medical) where n.disease_id=%s and m.age_from_in_week<=%s and m.age_to_in_week>=%s and m.restrictions_pregnancy_breastfeeding='%s' RETURN distinct m.medication_detail as medication_detail,m.common_brands as common_brands",
                                getDiseaseSession(session).getDiseaseId(),
                                getDiseaseSession(session).getAgeWeek(),
                                getDiseaseSession(session).getAgeWeek(),
                                getDiseaseSession(session).getIsPregnancyBreastfeeding().equals("1")?"0":"1"
                        )
                );
        return suggestion;


    }





    @RequestMapping(value = "/add_user")
    public int addUser2table(@RequestParam( value = "userName")String userName,@RequestParam(value = "phone")String phone,@RequestParam(value = "iconUrl")String iconUrl,@RequestParam(value = "role")String role){
        User user=new User();
        user.setUserName(userName);
        user.setIconUrl(iconUrl);
        user.setPhone(phone);
        user.setRole(role);
        return userService.userRegister(user);
    }


    @RequestMapping(value = "/search_users_by_name")
    public List<User> getUsersByName(@RequestParam(value = "userName")String userName){
        List<User> users =new ArrayList<>();
        users.addAll(userService.getUsersByName(userName));
        return users;
    }


    @RequestMapping(value = "/update_username_by_phone")
    public int updateUserNameByPhone(@RequestParam(value = "newName")String newName, @Param(value = "phone")String phone){
        return userService.updateUserNameByPhone(phone,newName);
    }

    @RequestMapping(value = "/delete_user_by_phone")
    public int deleteUserByPhone(@Param(value = "phone")String phone){
        return userService.deleteUserByPhone(phone);
    }



}
