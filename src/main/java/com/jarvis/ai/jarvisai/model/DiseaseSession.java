package com.jarvis.ai.jarvisai.model;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseSession {
    String firstCategory;
    String secondaryCategory;
    String diseaseId;
    String ageYear,ageMonth,ageWeek;
    String gender;
    String isPregnancyBreastfeeding;
    String result;




}
