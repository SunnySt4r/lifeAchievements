package com.github.sunnyst4r.lifeachievements.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity(name = "challenges")
public class ChallengeElement {

    @Id
    private Long challengeId;

    private Long userId;

    private String name;

    private Long parentId;

    private Date creationDate;

    private Date endingDate;

    private Date finish;

    private String description;

    private Boolean done;

    private Date start;

    private Integer distance;

    private Integer currentStreak;

    private Integer record;

    private Integer attempt;
}