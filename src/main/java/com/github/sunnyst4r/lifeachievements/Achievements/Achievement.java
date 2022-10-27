package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Calendar;
import java.util.Date;

public class Achievement {
    private Date creatingDate;
    private Date end = null;
    private Date finished = null;
    private String name;
    private String description = "";
    private boolean isDone = false;

    public Achievement(Date creatingDate, String name){
        this.creatingDate = creatingDate;
        this.name = name;
    }

    public Achievement(Date creatingDate, String name, String description){
        this(creatingDate, name);
        this.description = description;
    }

    public  Achievement(Date creatingDate, Date end, String name){
        this(creatingDate, name);
        this.end = end;
    }

    public Achievement(Date creatingDate, Date end, String name, String description){
        this(creatingDate, name, description);
        this.end = end;
    }

    public Date getCreatingDate(){
        return creatingDate;
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
        finished = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return name;
    }
}
