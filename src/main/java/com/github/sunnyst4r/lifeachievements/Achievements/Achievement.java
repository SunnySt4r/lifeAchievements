package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Date;

public class Achievement {
    private Date start;
    private Date end = null;
    private String name;
    private String description = "";

    private boolean isDone = false;

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

    public Date getStart(){
        return start;
    }

    public Date getEnd(){
        return end;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void done(){
        isDone = true;
    }

    @Override
    public String toString() {
        return name;
    }
}
