package org.pursuit.ar_wrld.database;

public class UserInformation {
    public String username;
    public int userscore;

    public UserInformation(){}

    public UserInformation(String username, int userscore) {
        this.username = username;
        this.userscore = userscore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserscore() {
        return userscore;
    }

    public void setUserscore(int userscore) {
        this.userscore = userscore;
    }
}
