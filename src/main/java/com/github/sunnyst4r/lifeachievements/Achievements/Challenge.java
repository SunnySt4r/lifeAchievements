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

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public void fail(){
        attempt++;
        setRecord();
        resetStartingSetting();
        sendNotification();
    }

    private void setRecord() {
        if (record < currentStreak) {
            record = currentStreak;
        }
    }

    private void resetStartingSetting(){
        currentStreak = 0;
        start = new Date();
    }

    private void sendNotification(){
        if(getEndingDate() != null && canDoItOnTime()){
            System.out.println("Вы не сможете уложиться до конечного дня.\n Переставить конечный день?");
        }
    }
    private boolean canDoItOnTime(){
        Calendar calendarWithExpectedEndingDate = createCalendarWithExpectedEndingDate(start);
        return calendarWithExpectedEndingDate.getTime().after(getEndingDate());
    }

    private Calendar createCalendarWithExpectedEndingDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, distance);
        return calendar;
    }

    public void addPoint(int step){
        currentStreak += step;
        checkPositiveStep(step);
    }

    private void checkPositiveStep(int step){
        if(step > 0){
            tryToFinish();
        }else{
            tryToStepDown();
        }
    }

    private void tryToFinish(){
        if(currentStreak == distance){
            this.iAmDone();
        }
    }

    private void tryToStepDown(){
        if(distance-currentStreak==1){
            setFinish(null);
            setDone(false);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}