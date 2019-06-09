package org.pursuit.ar_wrld.usermodel;

public class UserInformation {
    private String username;
    //private long gameNumber;
    private long userscore;

    public UserInformation(){}

    public UserInformation(String username, long userscore) {
        //this.gameNumber = gameNumber;
        this.username = username;
        this.userscore = userscore;
    }


    public long getUserscore() {
        return userscore;
    }

    public void setUserscore(long userscore) {
        this.userscore = userscore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
//    public long getGameNumber() {
//        return gameNumber;
//    }
//
//    public void setGameNumber(long gameNumber) {
//        this.gameNumber = gameNumber;
//    }
}
