package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Calendar;
import java.util.Date;

public class Achievement extends Category{
    private Date creatingDate;
    private Date end = null;
    private Date finish = null;
    private String description = "";
    private boolean done = false;

    public Achievement(Date creatingDate, String name){
        super(name);
        this.creatingDate = creatingDate;
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

    public String getDescription(){
        return description;
    }

    public void setDone(){
        done = true;
        finish = Calendar.getInstance().getTime();
    }

    public boolean isDone(){
        return done;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
