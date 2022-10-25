package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Calendar;
import java.util.Date;

public class Challenge extends Achievement{

    private Date start;
    private int distance;
    private int currentStreak = 0;
    private int record = 0;
    private boolean autoPass = true;
    private int attempt = 1;

    public Challenge(Date creationDate, String name, int distance) {
        super(creationDate, name);
        this.distance = distance;
    }

    public Challenge(Date creationDate, String name, String description, int distance) {
        super(creationDate, name, description);
        this.distance = distance;
    }

    public Challenge(Date creationDate, String name, String description, int distance, boolean autoPass) {
        this(creationDate, name, description, distance);
        this.autoPass = autoPass;
    }

    public Challenge(Date creationDate, Date end, String name, int distance) {
        super(creationDate, end, name);
        this.distance = distance;
    }

    public Challenge(Date creationDate, Date end, String name, String description, int distance) {
        super(creationDate, end, name, description);
        this.distance = distance;
    }

    public Challenge(Date creationDate, Date end, String name, String description, int distance, boolean autoPass) {
        this(creationDate, end, name, description, distance);
        this.autoPass = autoPass;
    }

    public int getDistance() {
        return distance;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getRecord() {
        return record;
    }

    public boolean isAutoPass() {
        return autoPass;
    }

    public int getAttempt() {
        return attempt;
    }

    public void fail(){
        attempt++;
        if(record < currentStreak){
            record = currentStreak;
        }
        currentStreak = 0;
        start = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, distance);
        if(c.getTime().after(getEnd())){
            System.out.println("Вы не сможете уложиться до конечного дня.\n Переставить конечный день?");
        }
    }

    public void pass(){
        currentStreak++;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}