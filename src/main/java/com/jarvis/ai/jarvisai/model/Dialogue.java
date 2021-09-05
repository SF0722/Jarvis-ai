package com.jarvis.ai.jarvisai.model;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Dialogue {
    long userId;
    String contentStr;
    String time;
}
