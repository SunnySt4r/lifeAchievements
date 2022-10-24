package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Date;

public class Challenge extends Achievement{

    private int distance;
    private int currentStreak = 0;
    private int record = 0;
    private boolean autoPass = true;
    private int attempt = 1;

    public Challenge(Date start, Date end, String name, int distance) {
        super(start, end, name);
        this.distance = distance;
    }

    public Challenge(Date start, Date end, String name, String description, int distance) {
        super(start, end, name, description);
        this.distance = distance;
    }

    public Challenge(Date start, Date end, String name, String description, int distance, boolean autoPass) {
        this(start, end, name, description, distance);
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
        //setEnd(today + distance)
    }

    public void pass(){
        currentStreak++;
    }
}