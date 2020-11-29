package com.jarvis.ai.jarvisai.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class User {
    long id;
    String userName;//用户名
    String phone;//电话
    String iconUrl;//头像图片链接
    String role;//角色



}
