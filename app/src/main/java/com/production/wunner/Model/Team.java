package com.production.wunner.Model;

import com.google.gson.annotations.SerializedName;

public class Team {
    @SerializedName("TeamID")
    private String teamID;
    @SerializedName("TeamName")
    private String teamPoint;
    @SerializedName("EventID")
    private String eventID;

    public Team(String teamID, String teamPoint,String eventID) {
        this.teamID = teamID;
        this.teamPoint = teamPoint;
        this.eventID= eventID;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getTeamPoint() {
        return teamPoint;
    }

    public void setTeamPoint(String teamPoint) {
        this.teamPoint = teamPoint;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
