package com.github.sunnyst4r.lifeachievements.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity(name = "achievements")
public class AchievementElement {

    @Id
    private Long achievementId;

    private Long userId;

    private String name;

    private Long parentId;

    private Date creationDate;

    private Date endingDate;

    private Date finish;

    private String description;

    private Boolean done;
}