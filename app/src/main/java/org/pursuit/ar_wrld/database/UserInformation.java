package org.pursuit.ar_wrld.database;

public class UserInformation {
    private String username;
    private int userscore;
    private String usertitle;

    public UserInformation(){}

    public UserInformation(String username, int userscore, String usertitle) {
        this.username = username;
        this.userscore = userscore;
        this.usertitle = usertitle;
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

    public String getUsertitle() {
        return usertitle;
    }

    public void setUsertitle(String usertitle) {
        this.usertitle = usertitle;
    }
}
