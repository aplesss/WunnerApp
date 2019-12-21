package com.production.wunner.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    @SerializedName("UserID")
    private String userID;
    @SerializedName("UserPassword")
    private String userPassword;
    @SerializedName("UserFullname")
    private String userFullname;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("UserDoB")
    private String userDoB;
    @SerializedName("UserAddress")
    private String userAddress;
    @SerializedName("UserType")
    private String userType;
    @SerializedName("TeamID")
    ArrayList<String> team = new ArrayList<>();




    // Getter Methods

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDoB() {
        return userDoB;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserType() {
        return userType;
    }

    // Setter Methods

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserDoB(String userDoB) {
        this.userDoB = userDoB;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(String userID, String userPassword, String userFullname, String userName, String userDoB, String userAddress, String userType, ArrayList<String> team) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userFullname = userFullname;
        this.userName = userName;
        this.userDoB = userDoB;
        this.userAddress = userAddress;
        this.userType = userType;
        this.team = team;
    }
    public User(String userID, String userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
    }
    public ArrayList<String> getTeam() {
        return team;
    }

    public void setTeam(ArrayList<String> team) {
        this.team = team;
    }
}