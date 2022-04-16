package com.jarvis.ai.jarvisai.model;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SessionStatus {

    DiseaseSession diseaseSession;

    public String getStatus(){
        String status="";
        int[] s=new int[9];

        if(diseaseSession.firstCategory!=null){
            s[0]=1;
        }
        if(diseaseSession.secondaryCategory!=null){
            s[1]=1;
        }
        if(diseaseSession.diseaseId !=null){
            s[2]=1;
        }
        if(diseaseSession.ageYear!=null){
            s[3]=1;
        }
        if(diseaseSession.ageMonth!=null){
            s[4]=1;
        }
        if(diseaseSession.ageWeek!=null){
            s[5]=1;
        }
        if(diseaseSession.gender!=null){
            s[6]=1;
        }
        if(diseaseSession.isPregnancyBreastfeeding!=null){
            s[7]=1;
        }
        if(diseaseSession.getResult()!=null){
            s[8]=1;
        }
        for (int i:s) {
            status=status+i;
        }
        return status;
    }

    public static void main(String[] args) {
        DiseaseSession diseaseSession= new DiseaseSession("E",null,null,null,null,null,null,null,null);
        System.out.println(new SessionStatus(diseaseSession).getStatus());
    }
}
