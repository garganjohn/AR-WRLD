package org.pursuit.ar_wrld.usermodel;

public class UserInformation {
    private String username;
    private long userscore;

    public UserInformation(){}

    public UserInformation(String username, long userscore) {
        this.username = username;
        this.userscore = userscore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserscore() {
        return userscore;
    }

    public void setUserscore(long userscore) {
        this.userscore = userscore;
    }

}
