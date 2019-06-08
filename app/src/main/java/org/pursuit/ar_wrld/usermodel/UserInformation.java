package org.pursuit.ar_wrld.usermodel;

public class UserInformation {
    //private long gameNumber;
    private long userscore;

    public UserInformation(){}

    public UserInformation(long userscore) {
        //this.gameNumber = gameNumber;
        this.userscore = userscore;
    }


    public long getUserscore() {
        return userscore;
    }

    public void setUserscore(long userscore) {
        this.userscore = userscore;
    }

//    public long getGameNumber() {
//        return gameNumber;
//    }
//
//    public void setGameNumber(long gameNumber) {
//        this.gameNumber = gameNumber;
//    }
}
