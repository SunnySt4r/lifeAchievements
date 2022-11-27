package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Calendar;
import java.util.Date;

public class Achievement extends Category{
    private Date creatingDate;
    private Date endingDate = null;
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

    public  Achievement(Date creatingDate, Date endingDate, String name){
        this(creatingDate, name);
        this.endingDate = endingDate;
    }

    public Achievement(Date creatingDate, Date endingDate, String name, String description){
        this(creatingDate, name, description);
        this.endingDate = endingDate;
    }

    public Date getCreatingDate(){
        return creatingDate;
    }

    public Date getEndingDate(){
        return endingDate;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void iAmDone(){
        setDone(true);
        finish = Calendar.getInstance().getTime();
    }

    public boolean isDone(){
        return done;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }
}
