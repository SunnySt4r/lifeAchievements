package com.github.sunnyst4r.lifeachievements.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "usersLifeAchievements")
public class User {

    @Id
    private Long userId;

    private Long chatId;

    private String login;

    private String password;
}
