package com.caesar.ken.coralfits.Models;

public class User {
    public String myemail, userid, token;

    public User(String myemail, String userid, String token) {
        this.myemail = myemail;
        this.userid = userid;
        this.token = token;
    }

    public String getMyemail() {
        return myemail;
    }

    public void setMyemail(String myemail) {
        this.myemail = myemail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
