package com.jarvis.ai.jarvisai.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class QuestionAndAnswer {
    int id;
    long userId;
    String question;
    String answer;
    long timestamp;
    String date;
}
