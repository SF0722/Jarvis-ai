package com.jarvis.ai.jarvisai.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Data
@Getter
@Setter
@ToString
public class Result {
    String resultType;
    Map<String,String> values;
    int groupType;
}
