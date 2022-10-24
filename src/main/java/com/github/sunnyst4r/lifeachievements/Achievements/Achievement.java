package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Date;

public class Achievement {
    Date start;
    Date end = null;
    String name;
    String description = "";

    public Achievement(Date start, String name){
        this.start = start;
        this.name = name;
    }

    public Achievement(Date start, String name, String description){
        this(start, name);
        this.description = description;
    }

    public  Achievement(Date start, Date end, String name){
        this(start, name);
        this.end = end;
    }

    public Achievement(Date start, Date end, String name, String description){
        this(start, name, description);
        this.end = end;
    }
}
