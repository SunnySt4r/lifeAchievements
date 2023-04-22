package com.github.sunnyst4r.lifeachievements.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "categories")
public class CategoryElement {

    @Id
    private Long categoryId;

    private Long user_id;

    private String name;

    private Long parentId;

}
