package org.pursuit.ar_wrld.usermodel;

public class UserInformation {
    private String username;
    private long userscore;
    private String usertitle;

    public UserInformation(){}

    public UserInformation(String username, long userscore, String usertitle) {
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

    public long getUserscore() {
        return userscore;
    }

    public void setUserscore(long userscore) {
        this.userscore = userscore;
    }

    public String getUsertitle() {
        return usertitle;
    }

    public void setUsertitle(String usertitle) {
        this.usertitle = usertitle;
    }
}
