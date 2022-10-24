package com.github.sunnyst4r.lifeachievements.Achievements;

import java.util.Date;

public class Challenge extends Achievement{

    int distance;
    int currentStreak = 0;
    int record = 0;
    boolean autoPass = true;
    int attempt = 1;

    public Challenge(Date start, Date end, String name, int distance) {
        super(start, end, name);
        this.distance = distance;
    }

    public Challenge(Date start, Date end, String name, String description, int distance) {
        super(start, end, name, description);
        this.distance = distance;
    }


}
